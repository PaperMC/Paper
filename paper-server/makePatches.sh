#!/bin/bash

if [ -z "$1" ]
then
    echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
    exit
fi
cb=src/main/java/net/minecraft/server
nms="$1/net/minecraft/server"
show_diff_msg=true

if [ $# -ge 2 ]
then
    show_diff_msg=$2
    if [ "$show_diff_msg" = false ]
    then
        echo "Suppressing normal output. Will only output for changed or created patches."
    fi
fi

for file in $(/bin/ls $cb)
do
    if [ "$show_diff_msg" = true ]
    then
        echo "Diffing $file"
    fi
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