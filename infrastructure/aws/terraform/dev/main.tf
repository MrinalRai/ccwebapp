provider "aws" {
  profile = "dev"
  region = "us-east-1"
}

module "network_mod"{
    source= "../modules/net"
    
    vpc-cluster-name="lambda_check"
    VPC_ciderBlock="10.0.0.0/16"
    region="us-east-1"
    profile="dev"
    vpcop_id="${module.network_mod.vpcop_id}"
    subnets="${module.network_mod.subnets}"
    //subnetIds = "${module.network_mod.subnetIds}"
}

module "application_mod"{
    source= "../modules/application"
    domain-name="dev.csye6225mrinal.me"
    ami_id="ami-01d78ed9d706303e8"
    ami_name="csye6225_1573741914"
    ami_key_pair_name="csye6225_ssh"
    instance_type="t2.micro"
    volume_size="20"
    volume_type="gp2"
    region="us-east-1"
    profile="dev"
    vpc-cluster-name="lambda-check"
    VPC_ciderBlock="10.0.0.0/16"
    account_id="227417268421"
    vpcop_id="${module.network_mod.vpcop_id}"
    subnets="${module.network_mod.subnets}"
    certiArn = "arn:aws:acm:us-east-1:227417268421:certificate/d4ff8a0b-ba70-413c-a339-253cf56d61cf"
}
