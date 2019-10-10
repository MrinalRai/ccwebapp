#!/bin/bash

read -p "Enter Network STACK_NAME: " name
vpcName="$name-cc-vpc"

mapfile -t stackarray < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
for i in "${stackarray[@]}";
do
    if [ "$i" == "$stackName" ] ; then
        printf "\n\n....... Stack with same name already exists, please use a different name ......... \n\n"
        exit
    fi
done
printf "......................  Please wait  ...................... \n"
aws cloudformation create-stack --stack-name $name --template-body file://csye6225-cf-networking.json --parameters ParameterKey=VPCName,ParameterValue=$vpcName ParameterKey=RouteTable,ParameterValue=$name-cc-rt ParameterKey=PublicSubnet1,ParameterValue=$name-cc-subnet1 ParameterKey=PublicSubnet2,ParameterValue=$name-cc-subnet2 ParameterKey=PublicSubnet3,ParameterValue=$name-cc-subnet3 ParameterKey=InternetGateway,ParameterValue=$name-cc-internetgateway

		aws cloudformation wait stack-create-complete --stack-name $name
		if [ $? -eq 0 ]; then
			printf "\n\n"
			printf "######################  Creating Stack, VPC, Subnets, InternetGateway, RouteTable and Routes  ###################### \n"
			printf "\n\n"
			printf "......................  Created VPC '$vpcName' ...................... \n\n"
			printf "......................  Created 3 Subnets  ...................... \n\n"
			printf "......................  Created InternetGateway  ...................... \n\n"
			printf "......................  Created RouteTable  ...................... \n\n"
			printf "......................  Created Routes  ...................... \n\n"
			printf "......................  Associated Routes  ...................... \n\n"
			printf "<<<<<<<<<<<<<<<<<<<<<<  Stack created successfully as '$name'  >>>>>>>>>>>>>>>>>>>>>> \n\n"
    			printf "\n\n"
    		exit
		else
			printf "\n\n"
    		printf "!!!!!!!!!!!!!!!!!!!!!!  Stack creation failed  !!!!!!!!!!!!!!!!!!!!!! \n"
    		printf "\n\n"
    		exit
		fi
		printf "\n"	
