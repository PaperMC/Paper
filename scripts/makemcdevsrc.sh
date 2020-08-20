#!/usr/bin/env sh
set -ue

# We define gitcmd and some helper functions in here.
. "$basedir"/scripts/functions.sh

PS1="$" # TODO: Why?

basedir="$(cd "$1" && pwd -P)"
cd "$basedir"
workdir="$basedir"/work
minecraftversion=$(grep minecraftVersion "$workdir"/BuildData/info.json | cut -d '"' -f 4)
decompiledir="$workdir/Minecraft/$minecraftversion"
nms="$decompiledir/spigot/net/minecraft/server"
papernms="$basedir/Paper-Server/src/main/java/net/minecraft/server"
mcdevsrc="${decompiledir}/src/net/minecraft/server"
rm -rf "${mcdevsrc}"
mkdir -p "${mcdevsrc}"
find "${nms}" -name '*.java' -print0 | xargs -I"{}" -0 cp "{}" "${mcdevsrc}/"

for file in "${nms}/"*
do
    file=${file##*/}
    # test if in Paper folder - already imported
    if [ -f "${papernms}/${file}" ]; then
        # remove from mcdevsrc folder
        rm -f "${mcdevsrc}/${file}"
    fi
done
echo "Built $decompiledir/src to be included in your project for src access"
