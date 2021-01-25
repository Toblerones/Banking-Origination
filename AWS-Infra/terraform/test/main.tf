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
//  s3_force_path_style         = true

  endpoints {
    iam       = "http://0.0.0.0:4593"
    s3        = "http://0.0.0.0:4572"
    lambda    = "http://0.0.0.0:4574"
    dynamodb  = "http://0.0.0.0:4569"
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

resource "aws_iam_role_policy_attachment" "attach_basic_execution_to_lambda" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}
