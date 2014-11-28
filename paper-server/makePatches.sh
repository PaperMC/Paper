#!/bin/bash

if [ -z "$1" ]
then
    echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
    exit
fi

for file in $(ls src/main/java/net/minecraft/server)
do
    echo "Diffing $file"
    dos2unix -q $1/net/minecraft/server/$file $1/net/minecraft/server/$file
    outName=$(echo nms-patches/"$(echo $file | cut -d. -f1)".patch)
    patchNew=$(diff -u $1/net/minecraft/server/$file src/main/java/net/minecraft/server/$file)
    if [ -f "$outName" ]
    then
        patchCut=$(echo "$patchNew" | tail -n +3)
        patchOld=$(cat "$outName" | tail -n +3)
        if [ "$patchCut" != "$patchOld" ] ; then
            echo "$outName changed"
            echo "$patchNew" > "$outName"
        fi
    else
        echo "New patch $outName"
        echo "$patchNew" > "$outName"
    fi
done