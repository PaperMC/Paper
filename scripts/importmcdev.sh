#!/usr/bin/env bash

(
set -e
nms="net/minecraft/server"
export MODLOG=""
PS1="$"
basedir="$(cd "$1" && pwd -P)"

workdir="$basedir/work"
minecraftversion=$(cat "$workdir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
decompiledir="$workdir/$minecraftversion"

export importedmcdev=""
function import {
	export importedmcdev="$importedmcdev $1"
	file="${1}.java"
	target="$workdir/Spigot/Spigot-Server/src/main/java/$nms/$file"
	base="$decompiledir/$nms/$file"

	if [[ ! -f "$target" ]]; then
		export MODLOG="$MODLOG  Imported $file from mc-dev\n";
		echo "Copying $base to $target"
		cp "$base" "$target"
	else
		echo "UN-NEEDED IMPORT: $file"
	fi
}

(
	cd "$workdir/Spigot/Spigot-Server/"
	lastlog=$(git log -1 --oneline)
	if [[ "$lastlog" = *"mc-dev Imports"* ]]; then
		git reset --hard HEAD^
	fi
)

import BaseBlockPosition
import BiomeBase
import BiomeMesa
import BlockChest
import BlockFalling
import BlockFluids
import BlockFurnace
import BlockIceFrost
import BlockPosition
import ChunkCache
import ChunkProviderFlat
import ChunkProviderGenerate
import ChunkProviderHell
import CommandAbstract
import CommandScoreboard
import CommandWhitelist
import DataBits
import DataConverterMaterialId
import EULA
import EntitySquid
import EntityWaterAnimal
import FileIOThread
import IHopper
import ItemBlock
import NavigationAbstract
import NBTTagCompound
import NBTTagList
import PersistentScoreboard
import PacketPlayInResourcePackStatus
import PacketPlayInUseEntity
import PacketPlayOutPlayerListHeaderFooter
import PacketPlayOutScoreboardTeam
import PacketPlayOutTitle
import PacketPlayOutUpdateTime
import PathfinderAbstract
import PathfinderGoalFloat
import PathfinderWater
import PersistentVillage
import RemoteControlListener
import TileEntityEnderChest
import TileEntityLootable
import WorldProvider

cd "$workdir/Spigot/Spigot-Server/"
rm -rf nms-patches applyPatches.sh makePatches.sh >/dev/null 2>&1
git add . -A >/dev/null 2>&1
echo -e "mc-dev Imports\n\n$MODLOG" | git commit . -F -
)
