provider "aws" {
        region     = "${var.region}"
} # end provider

# create the VPC
resource "aws_vpc" "VPC_TF" {
  cidr_block           = "${var.vpcCIDRblock}"
  instance_tenancy     = "default"
  enable_dns_support   = true
  enable_dns_hostnames = "${var.dnsHostNames}"
  tags = {
    Name = "VPC_TF"
  }
} # end resource

# create the Subnet1
resource "aws_subnet" "PublicSubnetTF1" {
  vpc_id                  = "${aws_vpc.VPC_TF.id}"
  cidr_block              = "${var.subnetCIDRblock1}"
  map_public_ip_on_launch =  true
  availability_zone       = "${var.availabilityZone1}"
tags = {
     Name = "My VPC SubnetTF1"
  }
} # end resource

# create the Subnet2
resource "aws_subnet" "PublicSubnetTF2" {
  vpc_id                  = "${aws_vpc.VPC_TF.id}"
  cidr_block              = "${var.subnetCIDRblock2}"
  map_public_ip_on_launch =  true 
  availability_zone       = "${var.availabilityZone2}"
tags = {
     Name = "My VPC SubnetTF2"
  }
} # end resource

# create the Subnet3
resource "aws_subnet" "PublicSubnetTF3" {
  vpc_id                  = "${aws_vpc.VPC_TF.id}"
  cidr_block              = "${var.subnetCIDRblock3}"
  map_public_ip_on_launch =  true 
  availability_zone       = "${var.availabilityZone3}"
tags = {
     Name = "My VPC SubnetTF3"
  }
} # end resource

# create the Internet Gateway
resource "aws_internet_gateway" "internetGatewayTF" {
  vpc_id = "${aws_vpc.VPC_TF.id}"
  #id     = "${aws_internet_gateway.internetGatewayTF.id}"

  tags = {
    Name = "My InternetGatewayTF"
  }
}

# Create the Route Table
resource "aws_route_table" "routeTableTF" {
    vpc_id = "${aws_vpc.VPC_TF.id}"
tags = {
        Name = "My VPC Route TableTF"
    }
} 


#Associating subnets to created RT
resource "aws_route_table_association" "a" {
  subnet_id      = "${aws_subnet.PublicSubnetTF1.id}"
  route_table_id = "${aws_route_table.routeTableTF.id}"
}
resource "aws_route_table_association" "b" {
  subnet_id      = "${aws_subnet.PublicSubnetTF2.id}"
  route_table_id = "${aws_route_table.routeTableTF.id}"
}
resource "aws_route_table_association" "c" {
  subnet_id      = "${aws_subnet.PublicSubnetTF3.id}"
  route_table_id = "${aws_route_table.routeTableTF.id}"
}  # end resource


#Creating public route for above route table
resource "aws_route" "publicRouteTF" {
  route_table_id            = "${aws_route_table.routeTableTF.id}"
  destination_cidr_block    = "${var.destinationCIDRblock}"
  gateway_id                = "${aws_internet_gateway.internetGatewayTF.id}"
  }