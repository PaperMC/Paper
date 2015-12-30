#!/bin/bash

PS1="$"
basedir=`pwd`
workdir=$basedir/work
minecraftversion=$(cat BuildData/info.json | grep minecraftVersion | cut -d '"' -f 4)
decompiledir=$workdir/$minecraftversion
classdir=$decompiledir/classes

echo "Extracting NMS classes..."
if [ ! -d "$classdir" ]; then
    mkdir -p "$classdir"
    cd "$classdir"
    jar xf "$decompiledir/$minecraftversion-mapped.jar" net/minecraft/server
    if [ "$?" != "0" ]; then
        cd "$basedir"
        echo "Failed to extract NMS classes."
        exit 1
    fi
fi

echo "Decompiling classes..."
if [ ! -d "$decompiledir/net/minecraft/server" ]; then
    cd "$basedir"
    java -jar BuildData/bin/fernflower.jar -dgs=1 -hdc=0 -rbr=0 -asc=1 -udv=0 "$classdir" "$decompiledir"
    if [ "$?" != "0" ]; then
        echo "Failed to decompile classes."
        exit 1
    fi
fi
