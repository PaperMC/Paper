#!/usr/bin/env sh
set -ue
PS1="$" # TODO: Why?

# We define gitcmd and some helper functions in here.
. "$basedir"/scripts/functions.sh

basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"

getRef() {
    git ls-tree "$1" "$2" | cut -d' ' -f3 | cut -f1
}
update() {
    cd "$workdir/$1"
    $gitcmd fetch && $gitcmd clean -fd && $gitcmd reset --hard origin/master
    refRemote=$(git rev-parse HEAD)
    cd ../
    $gitcmd add "$1" -f
    refHEAD=$(getRef HEAD "$workdir/$1")
    echo "$1 $refHEAD - $refRemote"
    if [ "$refHEAD" != "$refRemote" ]; then
        updated=true
    fi
}

(
    update Bukkit
    update CraftBukkit
    update Spigot
)

if [ "$2" = "all" ] || [ "$2" = "a" ]; then
	(
        update BuildData
        update Paperclip
    )
fi
if [ "${updated:-}" ]; then
    echo "Rebuilding patches without filtering to improve apply ability"
    "$basedir"/scripts/rebuildPatches.sh "$basedir" nofilter >/dev/null || exit 1
fi
