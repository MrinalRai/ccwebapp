#!/bin/bash
sudo systemctl daemon-reload
sudo systemctl enable tomcat
sudo systemctl restart tomcat.service