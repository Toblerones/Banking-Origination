terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 2.70"
    }
  }
}

provider "aws" {
  profile = "default"
  region  = "ap-southeast-2"
}

resource "aws_dynamodb_table" "banking-origination-dynamodb" {
  name           = "digital_form"
  billing_mode   = "PROVISIONED"
  read_capacity  = 5
  write_capacity = 5
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
    write_capacity  = 5
    read_capacity   = 5
    projection_type = "ALL"
  }

  global_secondary_index {
    name            = "productsAndCreatedAt"
    hash_key        = "PK"
    range_key       = "productsCreatedAt"
    write_capacity  = 5
    read_capacity   = 5
    projection_type = "ALL"
  }

  stream_enabled   = true
  stream_view_type = "NEW_AND_OLD_IMAGES"

  tags = {
    Name        = "origination-digital-form"
    Environment = "testing"
  }
}