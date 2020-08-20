#!/usr/bin/env sh
set -ue

# We define gitcmd and some helper functions in here.
. "$basedir"/scripts/functions.sh

($gitcmd submodule update --init && "$basedir"/scripts/remap.sh "$basedir" && "$basedir"/scripts/decompile.sh "$basedir" && "$basedir"/scripts/init.sh "$basedir" && "$basedir"/scripts/applyPatches.sh "$basedir") || (
    echo "Failed to build Paper"
    exit 1
) || exit 1
if [ "$2" = "--jar" ]; then
    mvn clean install && "$basedir"/scripts/paperclip.sh "$basedir"
fi
