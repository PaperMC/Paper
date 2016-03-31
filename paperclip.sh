#!/usr/bin/env bash
basedir=`pwd`
workdir=$basedir/work
mcver=$(cat BuildData/info.json | grep minecraftVersion | cut -d '"' -f 4)

cd ./Paperclip
mvn clean package
cd ..
cp ./Paperclip/target/paperclip-${mcver}.jar ./paperclip.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/paperclip.jar"
