#!/usr/bin/env bash

nms="net/minecraft/server"
export MODLOG=""
PS1="$"
basedir=`pwd`

workdir=$basedir/work
minecraftversion=$(cat BuildData/info.json | grep minecraftVersion | cut -d '"' -f 4)
decompiledir=$workdir/$minecraftversion

export importedmcdev=""
function import {
	export importedmcdev="$importedmcdev $1"
	file="${1}.java"
	target="$basedir/Spigot/Spigot-Server/src/main/java/$nms/$file"
	base="$decompiledir/$nms/$file"

	if [[ ! -f "$target" ]]; then
		export MODLOG="$MODLOG  Imported $file from mc-dev\n";
		echo "Copying $base to $target"
		cp "$base" "$target"
	fi
}

(
	cd Spigot/Spigot-Server/
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
import BlockPosition
import BlockStateList
import ChunkProviderFlat
import ChunkProviderGenerate
import ChunkProviderHell
import CommandAbstract
import CommandScoreboard
import CommandWhitelist
import DataConverterMaterialId
import EULA
import EntitySquid
import FileIOThread
import ItemBlock
import PacketPlayInResourcePackStatus
import PacketPlayInUseEntity
import PacketPlayOutPlayerListHeaderFooter
import PacketPlayOutTitle
import PacketPlayOutUpdateTime
import PathfinderAbstract
import PathfinderGoalFloat
import PersistentVillage
import TileEntityEnderChest

(
	cd Spigot/Spigot-Server/
	git add src -A
	echo -e "mc-dev Imports\n\n$MODLOG" | git commit src -F -
)
