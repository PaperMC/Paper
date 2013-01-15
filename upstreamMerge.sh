#!/bin/bash
basedir=$(dirname $(readlink -f $0))
function update {
    cd $basedir/$1
    git fetch && git reset --hard origin/master
    cd ../
    git add $1
}
update Bukkit
update CraftBukkit
