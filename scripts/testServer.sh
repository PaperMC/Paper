#!/bin/bash
set -e
PS1="$"
basedir="$(pwd -P)"
workdir="$basedir/work"
minecraftversion=$(cat "$workdir/BuildData/info.json"  | grep minecraftVersion | cut -d '"' -f 4)
decompiledir="$workdir/$minecraftversion"

mkdir -p "$workdir/test-server"
cd "$workdir/test-server"

cat > eula.txt <<- EOM
eula=true
EOM

if [ ! -f server.properties ]; then
cat > server.properties <<- EOM
spawn-protection=0
server-name=Paper Test Server
allow-nether=true
enable-query=false
pvp=true
snooper-enabled=false
rcon.ip=127.0.0.1
enable-command-block=true
max-players=200
server-port=${PAPER_TEST_PORT:-25565}
server-ip=${PAPER_TEST_IP:-0.0.0.0}
allow-flight=true
level-name=world
view-distance=7
generate-structures=true
online-mode=true
enable-rcon=false
motd=${PAPER_TEST_MOTD:-Paper Test Server}
EOM
fi

args="-server -Xmx2G -Dfile.encoding=UTF-8 -XX:MaxGCPauseMillis=50 -XX:+UseG1GC"
args="$args -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=70 "

cmd="java $args -jar ../../Paper-Server/target/paper-${minecraftversion}.jar"
if [ ! -z "$(which screen)" ]; then
	cmd="screen -DURS papertest $cmd >> logs/screen.log 2>&1"
#todo - someone add tmux if they want it
else
	echo "Screen not found - It is strongly recommended to install screen"
	sleep 3
fi
$cmd