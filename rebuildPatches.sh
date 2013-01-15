#!/bin/bash

basedir=$(dirname $(readlink -f $0))
echo "Rebuilding patch files from current fork state..."

function savePatches {
    what=$1
    target=$2
    cd $basedir/$target/
    git format-patch -o $basedir/${what}-Patches/ upstream/master
    cd $basedir
    git add $basedir/${what}-Patches
    echo "  Patches saved for $what to $what-Patches/"
}

savePatches Bukkit Spigot-API
savePatches CraftBukkit Spigot
