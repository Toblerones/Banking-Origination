terraform {
  backend "local" {}
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.0"
    }
  }
}

provider "aws" {
  access_key                  = "mock_access_key"
  secret_key                  = "mock_secret_key"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true
  region                      = "ap-southeast-2"
  s3_force_path_style         = true

  endpoints {
    iam      = "http://0.0.0.0:4566"
    s3       = "http://0.0.0.0:4566"
    lambda   = "http://0.0.0.0:4566"
    dynamodb = "http://0.0.0.0:4566"
    sns      = "http://0.0.0.0:4566"
    sqs      = "http://0.0.0.0:4566"
  }
}

resource "aws_s3_bucket" "origination-bucket" {
  bucket = "lambda-bucket"
  acl    = "private"
}

data "archive_file" "dummy_lambda" {

  output_path = "./dummy_lambda.zip"
  type        = "zip"

  source {
    content  = "dummy"
    filename = "app.py"
  }

}

// provision a empty shell
// Use aws cli to update function code - see guideline_lambda.md
resource "aws_lambda_function" "origination_lambda" {
  function_name = "ddb_stream_handler"
  description   = "Handling DynamoDB Stream"
  runtime       = "python3.7"
  handler       = "app.lambda_handler"

  filename = "dummy_lambda.zip"
  role     = aws_iam_role.lambda_exec.arn

  //  environment {
  //    variables = {
  //      DATA_BUCKET = aws_s3_bucket.origination-bucket.bucket,
  //    }
  //  }
}

//data "archive_file" "dummy_lambda" {
//
//  output_path = "./dummy_lambda.zip"
//  type        = "zip"
//
//  source {
//    content = "dummy"
//    filename = "ddb.js"
//  }
//
//}
//resource "aws_lambda_function" "origination_lambda" {
//  function_name = "ddb_stream_handler"
//  description   = "Handling DynamoDB Stream"
//  runtime       = "nodejs12.x"
//  handler       = "ddb.handler"
//
//  filename = "dummy_lambda.zip"
//  role     = aws_iam_role.lambda_exec.arn
//
//}

resource "aws_iam_role" "lambda_exec" {
  name = "example_lambda"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_policy" "s3_read_policy" {
  name = "example_s3_read"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
          "s3:Get*",
          "s3:List*"
      ],
      "Resource": "${aws_s3_bucket.origination-bucket.arn}"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "attach_s3_read_to_lambda" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = aws_iam_policy.s3_read_policy.arn
}

resource "aws_iam_role_policy_attachment" "attach_basic_execution_to_lambda" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_dynamodb_table" "banking_origination_dynamodb" {
  name           = "digital_form"
  billing_mode   = "PROVISIONED"
  read_capacity  = 1
  write_capacity = 1
  hash_key       = "PK"
  range_key      = "SK"

  attribute {
    name = "PK"
    type = "S"
  }

  attribute {
    name = "SK"
    type = "S"
  }

  attribute {
    name = "productsCreatedAt"
    type = "S"
  }

  global_secondary_index {
    name            = "invertedIndex"
    hash_key        = "SK"
    range_key       = "PK"
    write_capacity  = 1
    read_capacity   = 1
    projection_type = "ALL"
  }

  global_secondary_index {
    name            = "productsAndCreatedAt"
    hash_key        = "PK"
    range_key       = "productsCreatedAt"
    write_capacity  = 1
    read_capacity   = 1
    projection_type = "ALL"
  }

  // BUG in local - hanging.
  // manual enable
  //aws lambda create-event-source-mapping --function-name ProcessDynamoDBRecords \
  // --batch-size 100 --starting-position LATEST --event-source DynamoDB-stream-arn
  //  - see https://docs.aws.amazon.com/lambda/latest/dg/with-ddb-example.html
  stream_enabled   = true
  stream_view_type = "NEW_AND_OLD_IMAGES"

  tags = {
    Name        = "origination_digital_form"
    Environment = "testing"
  }
}

resource "aws_sns_topic" "ddb_stream_cdc" {
  name            = "ddb_stream_cdc"
  delivery_policy = <<JSON
{
  "http": {
    "defaultHealthyRetryPolicy": {
      "minDelayTarget"    : 20,
      "maxDelayTarget"    : 600,
      "numRetries"        : 5,
      "backoffFunction"   : "exponential"
    },
    "disableSubscriptionOverrides": false
  }
}
JSON
}

resource "aws_sqs_queue" "ddbstream_placed_queue_1" {
  name                       = "ddbstream_placed_queue_1"
  redrive_policy             = "{\"deadLetterTargetArn\":\"${aws_sqs_queue.ddbstream_placed_dlq_1.arn}\",\"maxReceiveCount\":5}"
  visibility_timeout_seconds = 300
}

resource "aws_sqs_queue" "ddbstream_placed_dlq_1" {
  name = "ddbstream_placed_dlq_1"
}

//Subscribe SQS to the SNS event message
resource "aws_sns_topic_subscription" "ddbstream_placed_subscription" {
  topic_arn = "ddb_stream_cdc"
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.ddbstream_placed_queue_1.arn
}

//resource "aws_sqs_queue_policy" "ddbstream_placed_queue_1_policy" {
//  queue_url = aws_sqs_queue.ddbstream_placed_queue_1.id
//  policy    = data.aws_iam_policy_document.ddbstream_placed_queue_1_iam_policy.json
//}
//
//data "aws_iam_policy_document" "ddbstream_placed_queue_1_iam_policy" {
//  policy_id = "SQSSendAccess"
//  statement {
//    sid       = "SQSSendAccessStatement"
//    effect    = "Allow"
//    actions   = ["SQS:SendMessage"]
//    resources = [aws_sqs_queue.ddbstream_placed_queue_1.arn,]
//    principals {
//      identifiers = ["*"]
//      type        = "*"
//    }
//    //    condition {
//    //      test     = "ArnEquals"
//    //      values   = ["${aws_sns_topic.ddb_stream_cdc.arn}"]
//    //      variable = "aws:SourceArn"
//    //    }
//  }
//}

resource "aws_sns_topic_policy" "default" {
  arn = aws_sns_topic.ddb_stream_cdc.arn

  policy = data.aws_iam_policy_document.sns_topic_policy.json
}

data "aws_iam_policy_document" "sns_topic_policy" {
  policy_id = "__default_policy_ID"

  statement {
    actions = [
      "SNS:Subscribe",
      "SNS:SetTopicAttributes",
      "SNS:RemovePermission",
      "SNS:Receive",
      "SNS:Publish",
      "SNS:ListSubscriptionsByTopic",
      "SNS:GetTopicAttributes",
      "SNS:DeleteTopic",
      "SNS:AddPermission",
    ]

    effect = "Allow"

    principals {
      type        = "AWS"
      identifiers = ["*"]
    }

    resources = [
      aws_sns_topic.ddb_stream_cdc.arn,
    ]

    sid = "__default_statement_ID"
  }
}