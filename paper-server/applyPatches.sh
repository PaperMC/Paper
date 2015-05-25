#!/bin/bash

if [ -z "$1" ]
then
    echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
    exit
fi

nms=$1/net/minecraft/server
cb=src/main/java/net/minecraft/server
#clean up and rebuild
rm -rf $cb
mkdir -p $cb
for file in $(/bin/ls nms-patches)
do
    patchFile="nms-patches/$file"
    file="$(echo $file | cut -d. -f1).java"

    echo "Patching $file < $patchFile"
    sed -i 's/\r//' "$nms/$file" > /dev/null

    cp "$nms/$file" "$cb/$file"
    patch -d src/main/java/ "net/minecraft/server/$file" < "$patchFile"
done
