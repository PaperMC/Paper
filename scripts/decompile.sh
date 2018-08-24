#!/usr/bin/env bash

(
set -e
PS1="$"
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
minecraftversion=$(cat "$workdir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
windows="$([[ "$OSTYPE" == "cygwin" || "$OSTYPE" == "msys" ]] && echo "true" || echo "false")"
decompiledir="$workdir/Minecraft/$minecraftversion"
classdir="$decompiledir/classes"
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
    java -jar "$workdir/BuildData/bin/fernflower.jar" -dgs=1 -hdc=0 -asc=1 -udv=0 "$classdir" "$decompiledir"
    if [ "$?" != "0" ]; then
        echo "Failed to decompile classes."
        exit 1
    fi
fi

# set a symlink to current
currentlink="$workdir/Minecraft/current"
if ([ ! -e "$currentlink" ] || [ -L "$currentlink" ]) && [ "$windows" == "false" ]; then
	set +e
	echo "Pointing $currentlink to $minecraftversion"
	rm -rf "$currentlink" || true
	ln -sfn "$minecraftversion" "$currentlink" || echo "Failed to set current symlink"
fi

)
