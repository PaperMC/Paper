#!/usr/bin/env sh
set -ue

# We define gitcmd and some helper functions in here.
. "$basedir"/scripts/functions.sh

PS1="$" # TODO: Find out why we set PS1
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir"/work
# TODO: Use jq instead in case something breaks?
minecraftversion=$(grep minecraftVersion "$workdir"/BuildData/info.json | cut -d '"' -f 4)
applycmd="$gitcmd am --3way --ignore-whitespace"

# Windows detection to workaround ARG_MAX limitation
case "${OSTYPE:-}" in
    "cygwin" | "msys")
        windows=true
        ;;
esac

echo "Rebuilding Forked projects.... "

applyPatch() {
    what="$1"
    what_name="$(basename "$what")"
    target="$2"
    branch="$3"

    cd "$basedir/$what"
    $gitcmd fetch
    $gitcmd branch -f upstream "$branch" >/dev/null

    cd "$basedir"
    if [ ! -d "$basedir/$target" ]; then
        $gitcmd clone "$what" "$target"
    fi
    cd "$basedir/$target"

    echo "Resetting $target to $what_name..."
    $gitcmd remote rm upstream > /dev/null 2>&1 || true # We don't care if there was none
    $gitcmd remote add upstream "$basedir/$what" >/dev/null 2>&1
    $gitcmd checkout master 2>/dev/null || $gitcmd checkout -b master
    $gitcmd fetch upstream >/dev/null 2>&1
    $gitcmd reset --hard upstream/upstream

    echo "  Applying patches to $target..."

    statusfile=".git/patch-apply-failed"
    rm -f "$statusfile"
    git config commit.gpgsign false
    $gitcmd am --abort >/dev/null 2>&1

    # Special case Windows handling because of ARG_MAX constraint
    if [ "${windows:-}" ]; then
        echo "  Using workaround for Windows ARG_MAX constraint"
        find "$basedir/${what_name}-Patches"/*.patch -print0 | xargs -0 $applycmd
    else
        $applycmd "$basedir/${what_name}-Patches"/*.patch
    fi

    if [ $? ]; then
        echo 1 > "$statusfile"
        echo "  Something did not apply cleanly to $target."
        echo "  Please review above details and finish the apply then"
        echo "  save the changes with rebuildPatches.sh"

        # On Windows, finishing the patch apply will only fix the latest patch.
        # Users will need to rebuild from that point and then re-run the patch
        # process to continue.
        if [ "${windows:-}" ]; then
            echo ""
            echo "  Because you're on Windows you'll need to finish the AM,"
            echo "  rebuild all patches, and then re-run the patch apply again."
            echo "  Consider using the scripts with Windows Subsystem for Linux."
        fi

        exit 1
    else
        rm -f "$statusfile"
        echo "  Patches applied cleanly to $target"
    fi
}

# Apply Spigot
(
    cd "$workdir/Spigot"
    basedir=$(pwd)
    applyPatch ../Bukkit Spigot-API HEAD &&
    applyPatch ../CraftBukkit Spigot-Server patched
) || (echo "Failed to apply Spigot Patches"; return 1)

echo "Importing MC Dev"

"$basedir"/scripts/importmcdev.sh "$basedir" || exit 1

# Apply paper
(
    applyPatch "work/Spigot/Spigot-API" Paper-API HEAD &&
    applyPatch "work/Spigot/Spigot-Server" Paper-Server HEAD
    cd "$basedir"

    # if we have previously ran ./paper mcdev, update it
    if [ -d "$workdir/Minecraft/$minecraftversion/src" ]; then
        "$basedir"/scripts/makemcdevsrc.sh "$basedir"
    fi
) || (echo "Failed to apply Paper Patches"; return 1)
