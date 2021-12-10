#!/usr/bin/env bash

basedir="$(cd "$1" && pwd -P)"

cp ./PaperSpigot-Server/target/paperspigot*-SNAPSHOT.jar ./Paperclip/paperspigot-1.8.8.jar
cp ./work/1.8.8/1.8.8.jar ./Paperclip/minecraft_server.1.8.8.jar
cd ./Paperclip
mvn clean package -Dmcver=1.8.8 "-Dpaperjar=$basedir/Paperclip/paperspigot-1.8.8.jar" "-Dvanillajar=$basedir/Paperclip/minecraft_server.1.8.8.jar"
cd ..
cp ./Paperclip/target/paperclip*-SNAPSHOT.jar ./Paperclip.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/Paperclip.jar"
