data "aws_availability_zones" "available" {}

resource "aws_vpc" "vpc_tf" {
	cidr_block = "${var.VPC_ciderBlock}"
	instance_tenancy     = "default"
	enable_dns_support   = true
	enable_dns_hostnames = true
	tags = "${
    map(
		"Name", "${var.vpc-cluster-name}",
    )
  }"
}

resource "aws_subnet" "subnet_tf" {
	count = 1
	availability_zone = "${data.aws_availability_zones.available.names[count.index]}"
	cidr_block = "10.0.${count.index}.0/24"
	vpc_id = "${aws_vpc.vpc_tf.id}"
	tags = "${
	map(
		"Name","${var.vpc-cluster-name}",
    )
  }"
}

resource "aws_internet_gateway" "ig_tf" {
	vpc_id = "${aws_vpc.vpc_tf.id}"
	tags = "${
	map(
		"Name","${var.vpc-cluster-name}IG",
	)
	}"
}

resource "aws_route_table" "rt_tf" {
	vpc_id = "${aws_vpc.vpc_tf.id}"
	route {
		cidr_block = "0.0.0.0/0"
		gateway_id = "${aws_internet_gateway.ig_tf.id}"
  }
}

resource "aws_route_table_association" "rtAsso_tf" {
	count = 1
	subnet_id = "${aws_subnet.subnet_tf.*.id[count.index]}"
	route_table_id = "${aws_route_table.rt_tf.id}"
}

 # * * * * * * * * * Security group creation * * * * * * * * * *

#EC2 instance creation
resource "aws_instance" "ec2-instance" {
	count=1
	ami = "${var.ami_id}"
	instance_type = "${var.instance_type}"
	key_name = "${var.ami_key_pair_name}"
	security_groups = ["${aws_security_group.application.id}"]
	subnet_id = "${aws_subnet.subnet_tf.*.id[count.index]}"
	disable_api_termination = "false"
	root_block_device {
		volume_size = "${var.volume_size}"
		volume_type = "${var.volume_type}"
	}
	depends_on = [
    		aws_db_instance.RDS,
  	]
}
#Security group for EC2 instance created
resource "aws_security_group" "application" {
	name = "Application security group"
	description = "Allow traffic for Webapp"
	vpc_id = "${aws_vpc.vpc_tf.id}"
	ingress {
		cidr_blocks = ["0.0.0.0/0"]
		from_port = 22
		to_port = 22
		protocol = "tcp"
	}
	ingress {
		cidr_blocks = ["0.0.0.0/0"]
		from_port = 80
		to_port = 80
		protocol = "tcp"
	}
	ingress {
		cidr_blocks = ["0.0.0.0/0"]
		from_port = 443
		to_port = 443
		protocol = "tcp"
	}
	ingress {
		cidr_blocks = ["0.0.0.0/0"]
		from_port = 8080
		to_port = 8080
		protocol = "tcp"
	}
	egress {
		cidr_blocks = ["0.0.0.0/0"]
	    	from_port = 0
	   	to_port = 0
	    	protocol = "-1"
	}
}
#Creating RDS instances
resource "aws_db_instance" "RDS"{
	count = 1
	name = "csye6225"
	allocated_storage = 20
	engine = "mysql"
	storage_type = "gp2"
	instance_class = "db.t2.medium"
	multi_az = "false"
	identifier_prefix = "csye6225-fall2019-"
	port = "3306"
	username = "root"
	password = "Admit$18"
	#db_subnet_group_name = "${aws_subnet.subnet_tf.*.id[count.index]}"
	publicly_accessible = "true"
	skip_final_snapshot = "true"
	#final_snapshot_identifier = "${aws_db_instance.RDS.*.id[count.index]}-final-snapshot"
}

#creating database security group
resource "aws_security_group" "database" {
  name        = "Database Security Group"
  description = "Allow TLS inbound traffic"
  vpc_id = "${aws_vpc.vpc_tf.id}"
}
resource "aws_security_group_rule" "ingress-database-rule" {
    type = "ingress" 
    # TLS (change to whatever ports you need)
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    security_group_id = "${aws_security_group.database.id}"
  }
resource "aws_security_group_rule" "egress-database-rule" {
    type            ="egress"
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
   # cidr_blocks     = ["0.0.0.0/0"]
    security_group_id = "${aws_security_group.database.id}"
    source_security_group_id  = "${aws_security_group.application.id}"
}

resource "aws_eip" "ip-test-env" {
	count = 1
	instance = "${aws_instance.ec2-instance.*.id[count.index]}"
	vpc = true
}
#Creating key for S# encryption
resource "aws_kms_key" "key" {
	description = "This key is used to encrypt bucket objects"
	deletion_window_in_days = 10
}
#Creating S3 Bucket
resource "aws_s3_bucket" "bucket" {
	bucket = "webapp.${var.domain-name}"
	acl    = "private"
	force_destroy = "true"
	tags = "${
      		map(
     		"Name", "${var.domain-name}",
    		)
  	}"
	server_side_encryption_configuration {
    		rule {
			apply_server_side_encryption_by_default {
				kms_master_key_id = "${aws_kms_key.key.arn}"
				sse_algorithm = "aws:kms" 
			}
      		}
    	}
	lifecycle_rule {
	    id      = "log/"
	    enabled = true
		transition{
			days = 30
			storage_class = "STANDARD_IA"
		}
	}
 
}
#Creating DynamoDB table
resource "aws_dynamodb_table" "basic-dynamodb-table" {
	 name           = "csye6225"
	 hash_key       = "id"
	 read_capacity = "20"
	 write_capacity = "20"
	 attribute {
		name = "id"
		type = "S"
  	}
  
}
