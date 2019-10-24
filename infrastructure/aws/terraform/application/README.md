# CSYE 6225 - Fall 2019

## File information and Steps to run the code

1: csye6225-networking.tf : Contains the script for creation of VPC, Subnets, Internet gateways, route tables, EC2 and RDS Instances, their security groups with rules for inbound/outbound traffic, S3 bucket with default encryption and a MySQL Dynamo Table for RDS instance.

2:varibles.tf : Contains all the variable declaration. These will be asked while running the script if no default value is set.

Steps: for creation -- 
- 1: terraform init
- 2: export AWS_PROFILE="your_value"
- 3: terraform plan
- 4: terraform apply

Wait for the process, it will create the instance in the cloud. Check for instance under EC2 and RDS modules on AWS.

Termination of all instances -- 
- 1: terraform destroy

Wait for the process. 

Note: You should have the Modification credentials to create or terminate an instance.
