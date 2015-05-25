#!/bin/bash

if [ -z "$1" ]
then
    echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
    exit
fi
cb=src/main/java/net/minecraft/server
nms="$1/net/minecraft/server"

for file in $(/bin/ls $cb)
do
    echo "Diffing $file"
    sed -i 's/\r//' "$nms/$file"
	sed -i 's/\r//' "$cb/$file"
    outName=$(echo nms-patches/"$(echo $file | cut -d. -f1)".patch)
    patchNew=$(diff -u --label a/net/minecraft/server/$file "$nms/$file" --label b/net/minecraft/server/$file "$cb/$file")
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