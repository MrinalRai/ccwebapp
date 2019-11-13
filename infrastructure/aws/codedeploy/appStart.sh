#!/bin/bash
# sudo systemctl daemon-reload
# sudo systemctl enable tomcat
# sudo systemctl restart tomcat.service
cd ~
sudo chmod 777 Cloud*.war
# cd /
java -jar CloudAssignment2-0.0.1-SNAPSHOT.war
