#!/usr/bin/env bash

cp ./Paper-Server/target/paper*-SNAPSHOT.jar ./Paperclip/paper-1.9.jar
cp ./work/1.9/1.9.jar ./Paperclip/minecraft_server.1.9.jar
cd ./Paperclip
mvn clean package
cd ..
cp ./Paperclip/target/paperclip*-SNAPSHOT.jar ./paperclip.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/paperclip.jar"
