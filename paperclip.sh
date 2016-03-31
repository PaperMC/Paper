#!/usr/bin/env bash
basedir=`pwd`
workdir=$basedir/work
mcver=$(cat BuildData/info.json | grep minecraftVersion | cut -d '"' -f 4)
decompiledir="$workdir/$mcver"

paperjar="$basedir/$(ls ./Paper-Server/target/paper*-SNAPSHOT.jar)"
vanillajar="${decompiledir}/${mcver}.jar"

cd ./Paperclip
mvn clean package -Dmcver=${mcver} -Dpaperjar="${paperjar}" -Dvanillajar="${vanillajar}"
cd ..
cp ./Paperclip/target/paperclip-${mcver}.jar ./paperclip-${mcver}.jar

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $(pwd)/paperclip-${mcver}.jar"
