#!/bin/bash

#git submodule update --init && ./remap.sh && ./decompile.sh && ./init.sh && ./applyPatches.sh
if [ "$1" == "--jar" ]; then
	mvn clean install && ./paperclip.sh
fi
