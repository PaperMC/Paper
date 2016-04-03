#!/usr/bin/env bash

(
set -e
PS1="$"
basedir=$(realpath "$1")
workdir="$basedir/work"

function update {
    cd "$workdir/$1"
    git fetch && git reset --hard origin/master
    cd ../
    git add $1
}

update Bukkit
update CraftBukkit
update Spigot
)
