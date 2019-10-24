variable "vpc-cluster-name" {
  description = "This name will be used for all the resources created in AWS"
  type    = "string"
}

variable "VPC_ciderBlock"{
  description = "Cider block to be used for VPC creation"
  type    = "string"
  default = "10.0.0.0/16"

}

