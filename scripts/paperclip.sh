#!/usr/bin/env sh
set -ue

# We define gitcmd and some helper functions in here.
. "$basedir"/scripts/functions.sh

basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
mcver=$(grep minecraftVersion "$workdir/BuildData/info.json" | cut -d '"' -f 4)
paperjar="$basedir/Paper-Server/target/paper-$mcver.jar"
vanillajar="$workdir/Minecraft/$mcver/$mcver.jar"

(
    cd "$workdir/Paperclip"
    mvn clean package "-Dmcver=$mcver" "-Dpaperjar=$paperjar" "-Dvanillajar=$vanillajar"
)
cp "$workdir/Paperclip/assembly/target/paperclip-${mcver}.jar" "$basedir/paperclip.jar"

echo ""
echo ""
echo ""
echo "Build success!"
echo "Copied final jar to $basedir/paperclip.jar"
