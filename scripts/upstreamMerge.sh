#!/usr/bin/env bash

(
set -e
PS1="$"
basedir="$(cd "$1" && pwd -P)"
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

if [[ "$2" = "all" || "$2" = "a" ]] ; then
	update BuildData
	update Paperclip
fi
)
