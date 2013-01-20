#!/bin/bash

basedir=$(dirname $(readlink -f $0))
echo "Rebuilding patch files from current fork state..."
function cleanupPatches {
    cd $1
    for patch in *.patch; do
        lines=$(git diff --staged $patch | grep -E "^(\+|\-)" | grep -Ev "(From [a-z0-9]{32,}|\-\-\- a|\+\+\+ b|.index)" | wc -l)
        if [ "$lines" == "0" ] ; then
            git reset HEAD $patch >/dev/null
            git checkout -- $patch >/dev/null
        fi
    done
}
function savePatches {
    what=$1
    target=$2
    cd $basedir/$target/
    git format-patch -N -o $basedir/${what}-Patches/ upstream/upstream
    cd $basedir
    git add $basedir/${what}-Patches
    cleanupPatches $basedir/${what}-Patches
    echo "  Patches saved for $what to $what-Patches/"
}

savePatches Bukkit Spigot-API
savePatches CraftBukkit Spigot
