#!/usr/bin/env bash

(
set -e
PS1="$"
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
minecraftversion=$(cat "$workdir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
spigotdecompiledir="$workdir/Minecraft/$minecraftversion/spigot"
nms="$spigotdecompiledir/net/minecraft/server"
cb="src/main/java/net/minecraft/server"
gitcmd="git -c commit.gpgsign=false"

patch=$(which patch 2>/dev/null)
if [ "x$patch" == "x" ]; then
    patch="$basedir/hctap.exe"
fi

# apply patches directly to the file tree
# used to fix issues from upstream source repos
cd "$basedir"
prepatchesdir="$basedir/scripts/pre-source-patches"
for file in $(ls "$prepatchesdir")
do
    if [ $file == "README.md" ]; then
        continue
    fi

    echo "--==-- Applying PRE-SOURCE patch: $file --==--"
    $patch -p0 < "$prepatchesdir/$file"
done

echo "Applying CraftBukkit patches to NMS..."
cd "$workdir/CraftBukkit"
$gitcmd checkout -B patched HEAD >/dev/null 2>&1
rm -rf "$cb"
mkdir -p "$cb"
# create baseline NMS import so we can see diff of what CB changed
for file in $(ls nms-patches)
do
    patchFile="nms-patches/$file"
    file="$(echo "$file" | cut -d. -f1).java"
    cp "$nms/$file" "$cb/$file"
done
$gitcmd add src
$gitcmd commit -m "Minecraft $ $(date)" --author="Vanilla <auto@mated.null>"

# apply patches
for file in $(ls nms-patches)
do
    patchFile="nms-patches/$file"
    file="$(echo "$file" | cut -d. -f1).java"

    echo "Patching $file < $patchFile"
    set +e
    sed -i 's/\r//' "$nms/$file" > /dev/null
    set -e

    "$patch" -s -d src/main/java/ "net/minecraft/server/$file" < "$patchFile"
done

$gitcmd add src
$gitcmd commit -m "CraftBukkit $ $(date)" --author="CraftBukkit <auto@mated.null>"
$gitcmd checkout -f HEAD~2
)
