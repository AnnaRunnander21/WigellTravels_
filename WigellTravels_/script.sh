#!/bin/bash
echo "Stopping microservice-a"
docker stop wigelltravels_ 2>/dev/null || true

echo "Deleting container microservice-a"
docker rm wigelltravels_ 2>/dev/null || true

echo "Deleting image microservice-a"
docker rmi wigelltravels_ 2>/dev/null || true

echo "Running mvn package"
mvn clean package -DskipTests

echo "Creating image microservice-a"
docker build -t wigelltravels_ .

echo "Creating and running container microservice-a"
docker run -d -p 4545:4545 --name wigelltravels_ --network services-network wigelltravels_

echo "Done!"
