#!/bin/bash

if [ -z "$1" ]
then
    echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
    exit
fi

# https://stackoverflow.com/a/38595160
# https://stackoverflow.com/a/800644
if sed --version >/dev/null 2>&1; then
  strip_cr() {
    sed -i -- "s/\r//" "$@"
  }
else
  strip_cr () {
    sed -i "" "s/$(printf '\r')//" "$@"
  }
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
    strip_cr "$nms/$file" > /dev/null

    cp "$nms/$file" "$cb/$file"
    patch -d src/main/java/ "net/minecraft/server/$file" < "$patchFile"
done
