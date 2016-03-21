#!/bin/bash

PS1="$"
basedir=`pwd`

function update {
    cd "$basedir/$1"
    git fetch && git reset --hard origin/master
    cd ../
    git add $1
}

update Bukkit
update CraftBukkit
update Spigot
