#!/usr/bin/env bash

PS1="$"

basedir="$1"
workdir="$basedir/work"
minecraftversion=$(cat "$basedir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
decompiledir="$workdir/$minecraftversion"
nms="$decompiledir/net/minecraft/server"
papernms="Paper-Server/src/main/java/net/minecraft/server"
mcdevsrc="${decompiledir}/src/net/minecraft/server"
rm -rf "${mcdevsrc}"
mkdir -p "${mcdevsrc}"
cp ${nms}/*.java "${mcdevsrc}/"

for file in "${nms}/"*
do
    file=${file##*/}
    # test if in Paper folder - already imported
    if [ -f "${papernms}/${file}" ]; then
        # remove from mcdevsrc folder
        rm -f "${mcdevsrc}/${file}"
    fi
done
echo "Built $decompiledir/src to be included in your project for src access";
