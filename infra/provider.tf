terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.56.0"
    }
  }

  backend "s3" {
    bucket = var.s3_bucket_name
    key    = "tf/terraform.tfstate"
    region = "eu-west-1"
  }
}
