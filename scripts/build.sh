#!/usr/bin/env bash

(
set -e
basedir="$(cd "$1" && pwd -P)"
gitcmd="git -c commit.gpgsign=false"

($gitcmd submodule update --init && ./scripts/remap.sh "$basedir" && ./scripts/decompile.sh "$basedir" && ./scripts/init.sh "$basedir" && ./scripts/applyPatches.sh "$basedir") || (
    echo "Failed to build Paper"
    exit 1
) || exit 1
if [ "$2" == "--jar" ]; then
    mvn clean install && ./scripts/paperclip.sh "$basedir"
fi
) || exit 1
