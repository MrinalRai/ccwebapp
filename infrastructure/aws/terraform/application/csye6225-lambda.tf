# =================== EC2 NAT Gateway ===========================
resource "aws_eip" "nat_1" {
  vpc                       = true
  associate_with_private_ip = "${var.subnet_ids}"
}
resource "aws_nat_gateway" "gw_1" {
  allocation_id = "${aws_eip.nat_1.id}"
  subnet_id     = "${aws_subnet.subnet_ids[0].id}"

  tags = {
    Name = "gw NAT 1"
  }
}
# ===================== EC2 Launch Configuration ===========================
resource "aws_launch_configuration" "ec2_lc" {
  name_prefix   = "ec2-lc"
  image_id      = "${var.ami_id}"
  instance_type = "${var.instance_type}"
  key_name      = "${var.ami_key_pair_name}"
 # security_groups = ["${aws_security_group.terra_bastion_sg.id}"]
  user_data = "${templatefile("userdata.sh",
		{
			s3_bucket_name = "${aws_s3_bucket.bucket.bucket}",
			aws_db_endpoint = "${aws_db_instance.RDS.endpoint}",
			aws_db_name = "${aws_db_instance.RDS.name}",
			aws_db_username = "${aws_db_instance.RDS.username}",
			aws_db_password = "${aws_db_instance.RDS.password}",
			aws_region = "${var.region}",
			aws_profile = "${var.profile}"
		})}"

  root_block_device {
		volume_size = "${var.volume_size}"
		volume_type = "${var.volume_type}"
	}

  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    "aws_security_group.terra_bastion_sg"
  ]
}

# ============================ Autoscaling group =========================
resource "aws_autoscaling_group" "ec2_asg" {
  name                 = "ec2_asg"
  launch_configuration = "${aws_launch_configuration.ec2_lc.name}"
  min_size             = 3
  max_size             = 10
  health_check_type    = "EC2"
  vpc_zone_identifier       = ["${aws_subnet.subnet_tf[0].id}"]
  launch_configuration = "${aws_launch_configuration.ec2_lc.name}"
  lifecycle {
    create_before_destroy = true
  }

  depends_on = [
    "aws_launch_configuration.ec2_lc",
    "aws_subnet.subnet_tf[0]",
  ]
  tag {
    key = "Name"
    value = "myEC2Instance"}
}
#---------------------------- Autoscaling Policies ---------------------------
# SCALE - UP Policy
resource "aws_autoscaling_policy" "asg_scaleUp" {
  name                   = "WebServerScaleUpPolicy"
  scaling_adjustment     = "1"
  adjustment_type        = "ChangeInCapacity"
  cooldown               = "60"
  autoscaling_group_name = "${aws_autoscaling_group.ec2_asg.name}"
  policy_type = "SimpleScaling"
}
resource "aws_cloudwatch_metric_alarm" "up-cpu-alarm" {
  alarm_name = "up-cpu-alarm"
  alarm_description = "Scale-up if CPU > 90% for 10 minutes"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods = "2"
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = "300"
  statistic = "Average"
  threshold = "90"
  dimensions = {
  "AutoScalingGroupName" = "${aws_autoscaling_group.ec2_asg.name}"}
  alarm_actions = [
        "${aws_autoscaling_policy.asg_scaleUp.arn}"
    ]
}

# SCALE - DOWN Policy
resource "aws_autoscaling_policy" "asg_scaleDwn" {
  name                   = "WebServerScaleDownPolicy"
  scaling_adjustment     = "-1"
  adjustment_type        = "ChangeInCapacity"
  cooldown               = "60"
  autoscaling_group_name = "${aws_autoscaling_group.ec2_asg.name}"
  policy_type = "SimpleScaling"
}
resource "aws_cloudwatch_metric_alarm" "down-cpu-alarm" {
  alarm_name = "down-cpu-alarm"
  alarm_description = "Scale-down if CPU < 70% for 10 minutes"
  comparison_operator = "LessThanThreshold"
  evaluation_periods = "2"
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = "300"
  statistic = "Average"
  threshold = "90"
  dimensions = {
  "AutoScalingGroupName" = "${aws_autoscaling_group.ec2_asg.name}"}
  alarm_actions = [
        "${aws_autoscaling_policy.asg_scaleDwn.arn}"
    ]
}

# ============================== SNS Topic ===================================

resource "aws_sns_topic" "email_request" {
  name = "email_request"
  delivery_policy = <<EOF
{
  "http": {
    "defaultHealthyRetryPolicy": {
      "minDelayTarget": 20,
      "maxDelayTarget": 20,
      "numRetries": 3,
      "numMaxDelayRetries": 0,
      "numNoDelayRetries": 0,
      "numMinDelayRetries": 0,
      "backoffFunction": "linear"
    },
    "disableSubscriptionOverrides": false,
    "defaultThrottlePolicy": {
      "maxReceivesPerSecond": 1
    } }}
EOF
policy = ${aws_iam_policy.sns_policy}
}

resource "aws_iam_policy" "sns_policy" {
  name        = "test_policy"
  path        = "/"
  description = "My test policy"
  policy = <<EOF
{
  "Version": "2008-10-17",
  "Id": "sns_policy_ID",
  "Statement": [
    {
      "Sid": "sns_statement_ID",
      "Effect": "Allow",
      "Principal": {
        "AWS": "*"
      },
      "Action": [
        "SNS:GetTopicAttributes",
        "SNS:SetTopicAttributes",
        "SNS:AddPermission",
        "SNS:RemovePermission",
        "SNS:DeleteTopic",
        "SNS:Subscribe",
        "SNS:ListSubscriptionsByTopic",
        "SNS:Publish",
        "SNS:Receive"
      ],
      "Resource": "${aws_sns_topic.email_request.arn}",
      "Condition": {
        "StringEquals": {
          "AWS:SourceOwner": "${var.account_id}"
        }
      }
    }
  ]
}
EOF
}
resource "aws_iam_role" "iam_for_sns" {
  name = "iam_for_sns"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ssm.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}
resource "aws_iam_role_policy_attachment" "snspolicy_role_attach" {
  role       = "${aws_iam_role.iam_for_sns.name}"
  policy_arn = "${aws_iam_policy.sns_policy.arn}"
}
#================================ Lamda Function ======================================

#-------------- Create Lambda Policy ---------------

resource "aws_iam_policy" "lambda_logging" {
  name = "lambda_logging"
  path = "/"
  description = "IAM policy for logging from a lambda"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "logs:DescribeLogStreams"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    },
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:DescribeStream",
        "dynamodb:GetRecords",
        "dynamodb:GetShardIterator",
        "dynamodb:ListStreams","dynamodb:GetItem",
        "dynamodb:DeleteItem",
        "dynamodb:PutItem",
        "dynamodb:Scan",
        "dynamodb:Query",
        "dynamodb:UpdateItem",
        "dynamodb:BatchWriteItem",
        "dynamodb:BatchGetItem",
        "dynamodb:DescribeTable"
      ],
      "Resource": "${aws_dynamodb_table.snslambda.arn}"
  },
  {
     "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject"
      ],
      "Resource": ["${aws_s3_bucket.bucket.arn}",
		              "${aws_s3_bucket.bucket.arn}/*",
                  "${aws_s3_bucket.bucket_image.arn}",
		              "${aws_s3_bucket.bucket_image.arn}/*"]
  }
  ]
}
EOF
}

#------------- Create IAM Lambda Role ----------------

resource "aws_iam_role" "iam_for_lambda" {
  name = "iam_for_lambda"

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

#------- Attach IAM Lambda Role with Log Policies -----------

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role = "${aws_iam_role.iam_for_lambda.name}"
  policy_arn = "${aws_iam_policy.lambda_logging.arn}"
}

#--------- Create Lambda Function --------------

resource "aws_lambda_function" "func_lambda" {
  filename      = "${var.filename}"
  function_name = "${var.function_name}"
  role          = "${aws_iam_role.iam_for_lambda.arn}"
  handler       = "${var.handler}"
  runtime	    	= "java8"
  timout        = 900
  reserved_concurrent_executions = 1
  /*vpc_config{
	  subnet_ids = ["${aws_subnet.subnet_tf[1].id}", 
	  				"${aws_subnet.subnet_tf[2].id}",
					"${aws_subnet.subnet_tf[3].id}"]
					
  }*/
  depends_on     = ["aws_sns_topic.email_request"]
   # Pass the SNS topic ARN and DynamoDB table name in the environment.
  environment {
    variables = {
      sns_arn           = "${aws_sns_topic.email_request.arn}"
      dynamo_table_name = "${aws_dynamodb_table.basic-dynamodb-table.name}"
    }
  }
  }

  #-------------SNS permissions to invoke lambda function ---------------

resource "aws_lambda_permission" "sns" {
  statement_id  = "AllowExecutionFromSNSToLambda"
  action        = "lambda:InvokeFunction"
  function_name = "${aws_lambda_function.func_lambda.arn}"
  principal     = "sns.amazonaws.com"
  source_arn = "${aws_sns_topic.email_request.arn}"
}

#---------------- Subscribe Lambda function to SNS topic ---------------

resource "aws_sns_topic_subscription" "sns_subscription" {
  depends_on = ["${aws_lambda_function.func_lambda}"]
  topic_arn = "${aws_sns_topic.email_request.arn}"
  protocol = "lambda"
  endpoint = "${aws_lambda_function.func_lambda.arn}"
}

#----------------- Lambdasic-dynamodb-tablesource Mapping -------------------

resource "aws_lambda_event_souasic-dynamodb-tableg" "lambda_dynamo_mapping" {
  event_source_arn  = "${aws_dasic-dynamodb-tableble.snslambda.stream_arn}"
  function_name     = "${aws_lasic-dynamodb-tabletion.func_lambda.arn}"
  starting_position = "LATEST"
}

# ----------------------- Lambda SNS table ---------------------------------

resource "aws_dynamodb_table" "snslambda_table" {
	 name           = "snslambda"
	 hash_key       = "id"
	 read_capacity = "20"
	 write_capacity = "20"
   stream_enabled = true
	 attribute {
		name = "id"
		type = "S"
  	}
    } 