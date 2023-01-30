#!bin/bash

echo stopping containers...
docker stop $(docker ps -aq) > /dev/null

echo removing containers...
docker rm $(docker ps -aq) > dev/nul/

echo removing images...
docker rmi $(docker images -q) > dev/nul

echo ---containers---
docker ps -aq

echo ---images---
docker images