#!/bin/bash

PS1="$"
basedir=`pwd`
echo "Rebuilding Forked projects.... "

function applyPatch {
    what=$1
    what_name=$(basename $what)
    target=$2
    branch=$3

    cd "$basedir/$what"
    git fetch
    git branch -f upstream "$branch" >/dev/null

    cd "$basedir"
    if [ ! -d  "$basedir/$target" ]; then
        git clone "$what" "$target"
    fi
    cd "$basedir/$target"
    echo "Resetting $target to $what_name..."
    git remote rm upstream > /dev/null 2>&1
    git remote add upstream $basedir/$what >/dev/null 2>&1
    git checkout master 2>/dev/null || git checkout -b master
    git fetch upstream >/dev/null 2>&1
    git reset --hard upstream/upstream
    echo "  Applying patches to $target..."
    git am --abort >/dev/null 2>&1
    git am --3way --ignore-whitespace "$basedir/${what_name}-Patches/"*.patch
    if [ "$?" != "0" ]; then
        echo "  Something did not apply cleanly to $target."
        echo "  Please review above details and finish the apply then"
        echo "  save the changes with rebuildPatches.sh"
        exit 1
    else
        echo "  Patches applied cleanly to $target"
    fi
}

# Move into spigot dir
pushd Spigot
basedir=$basedir/Spigot
# Apply Spigot
(
	applyPatch ../Bukkit Spigot-API HEAD &&
	applyPatch ../CraftBukkit Spigot-Server patched
) || (
	echo "Failed to apply Spigot Patches"
	exit 1
) || exit 1
# Move out of Spigot
popd
basedir=$(dirname "$basedir")

# Apply paper
(
	applyPatch Spigot/Spigot-API Paper-API HEAD &&
	applyPatch Spigot/Spigot-Server Paper-Server HEAD
) || (
	echo "Failed to apply Paper Patches"
	exit 1
) || exit 1

