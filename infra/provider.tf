terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.56.0"
    }
  }

  backend "s3" {
    bucket = "pgr301-2020-terraform"
    key    = "tf/terraform.tfstate"
    region = "eu-west-1"
  }
}
