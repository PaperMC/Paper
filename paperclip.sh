#!/usr/bin/env bash

cp ./PaperSpigot-Server/target/paperspigot*-SNAPSHOT.jar ./Paperclip/paperspigot-1.8.8.jar
cp ./work/1.8.8/1.8.8.jar ./Paperclip/minecraft_server.1.8.8.jar
cd ./Paperclip
mvn clean package
cd ..
cp ./Paperclip/target/paperclip*-SNAPSHOT.jar ./Paperclip.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/Paperclip.jar"