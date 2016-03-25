#!/bin/bash

(git submodule update --init && ./remap.sh && ./decompile.sh && ./init.sh && ./applyPatches.sh) || (
	echo "Failed to build Paper"
	exit 1
) || exit 1
if [ "$1" == "--jar" ]; then
	(mvn clean install && ./paperclip.sh) || exit 1
fi
