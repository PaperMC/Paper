#!/bin/bash

git submodule update --init && ./remap.sh && ./decompile.sh && ./init.sh && ./applyPatches.sh && mvn clean install && ./paperclip.sh
