#!/usr/bin/env bash

(
set -e
PS1="$"
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
minecraftversion=$(cat "$workdir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
decompiledir="$workdir/Minecraft/$minecraftversion"
nms="$decompiledir/net/minecraft/server"
cb="src/main/java/net/minecraft/server"
gitcmd="git -c commit.gpgsign=false"

patch=$(which patch 2>/dev/null)
if [ "x$patch" == "x" ]; then
    patch="$basedir/hctap.exe"
fi

echo "Applying CraftBukkit patches to NMS..."
cd "$workdir/CraftBukkit"
$gitcmd checkout -B patched HEAD >/dev/null 2>&1
rm -rf "$cb"
mkdir -p "$cb"
for file in $(ls nms-patches)
do
    patchFile="nms-patches/$file"
    file="$(echo "$file" | cut -d. -f1).java"

    echo "Patching $file < $patchFile"
    set +e
    sed -i 's/\r//' "$nms/$file" > /dev/null
    set -e

    cp "$nms/$file" "$cb/$file"
    "$patch" -s -d src/main/java/ "net/minecraft/server/$file" < "$patchFile"
done

$gitcmd add src
$gitcmd commit -m "CraftBukkit $ $(date)" --author="Auto <auto@mated.null>"
$gitcmd checkout -f HEAD^
)
