#!/bin/bash

basedir=$(dirname $(readlink -f $0))
echo "Rebuilding Forked projects.... "

function applyPatch {
    what=$1
    target=$2
    cd $basedir
    if [ ! -d  "$basedir/$target" ]; then
        git clone $1 $target
    fi
    cd "$basedir/$target"
    echo "Resetting $target to $what..."
    git remote rm upstream 2>/dev/null
    git remote add upstream ../$what
    git checkout master
    git fetch upstream
    git reset --hard upstream/master
    echo "  Applying patches to $target..."
    git am --3way $basedir/${what}-Patches/*.patch
    if [ "$?" != "0" ]; then
        echo "  Something did not apply cleanly to $target."
        echo "  Please review above details and finish the apply then"
        echo "  save the changes with rebuildPatches.sh"
    else
        echo "  Patches applied cleanly to $target"
    fi
}

applyPatch Bukkit Spigot-API
applyPatch CraftBukkit Spigot
