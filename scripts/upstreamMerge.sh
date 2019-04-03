#!/usr/bin/env bash

(
set -e
PS1="$"
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
gitcmd="git -c commit.gpgsign=false"

function update {
    cd "$workdir/$1"
    $gitcmd fetch && $gitcmd clean -fd && $gitcmd reset --hard origin/master
    cd ../
    $gitcmd add $1
}

update Bukkit
update CraftBukkit
update Spigot

if [[ "$2" = "all" || "$2" = "a" ]] ; then
	update BuildData
	update Paperclip
fi
)
