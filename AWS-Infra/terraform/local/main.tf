terraform {
  backend "local" {}
}

provider "aws" {
  access_key = "mock_access_key"
  secret_key = "mock_secret_key"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true
  region  = "ap-southeast-2"
  s3_force_path_style         = true

  endpoints {
    iam       = "http://0.0.0.0:4593"
    s3        = "http://0.0.0.0:4572"
    lambda    = "http://0.0.0.0:4574"
    dynamodb  = "http://0.0.0.0:4569"
  }
}

resource "aws_s3_bucket" "origination-bucket" {
  bucket = "lambda-bucket"
  acl    = "private"
}

resource "aws_lambda_function" "origination_lambda" {
  function_name = "ddb_stream_handler"
  description   = "Handling DynamoDB Stream"
  runtime       = "python3.6"
  handler       = "app.lambda_handler"

  filename = "lambda.zip"
  role     = aws_iam_role.lambda_exec.arn

  environment {
    variables = {
      DATA_BUCKET = aws_s3_bucket.origination-bucket.bucket,
    }
  }
}

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

resource "aws_dynamodb_table" "banking-origination-dynamodb" {
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

  stream_enabled   = true
  stream_view_type = "NEW_AND_OLD_IMAGES"

  tags = {
    Name        = "origination-digital-form"
    Environment = "testing"
  }
}