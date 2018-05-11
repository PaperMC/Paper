#!/usr/bin/env bash

(
PS1="$"
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
gpgsign="$(git config commit.gpgsign || echo "false")"
echo "Rebuilding Forked projects.... "

function applyPatch {
    what=$1
    what_name=$(basename "$what")
    target=$2
    branch=$3

    cd "$basedir/$what"
    git fetch
    git branch -f upstream "$branch" >/dev/null

    cd "$basedir"
    if [ ! -d  "$basedir/$target" ]; then
        git clone "$what" "$target"
    fi
    cd "$basedir/$target"

    # Disable GPG signing before AM, slows things down and doesn't play nicely.
    # There is also zero rational or logical reason to do so for these sub-repo AMs.
    # Calm down kids, it's re-enabled (if needed) immediately after, pass or fail.
    git config commit.gpgsign false

    echo "Resetting $target to $what_name..."
    git remote rm upstream > /dev/null 2>&1
    git remote add upstream "$basedir/$what" >/dev/null 2>&1
    git checkout master 2>/dev/null || git checkout -b master
    git fetch upstream >/dev/null 2>&1
    git reset --hard upstream/upstream

    echo "  Applying patches to $target..."

    git am --abort >/dev/null 2>&1
	for patch in "$basedir/${what_name}-Patches/"*.patch;
	do
		git am --3way --ignore-whitespace "$patch";
	done;
    if [ "$?" != "0" ]; then
        echo "  Something did not apply cleanly to $target."
        echo "  Please review above details and finish the apply then"
        echo "  save the changes with rebuildPatches.sh"
        exit 1
    else
        echo "  Patches applied cleanly to $target"
    fi
}

function enableCommitSigningIfNeeded {
    if [[ "$gpgsign" == "true" ]]; then
        git config commit.gpgsign true
    fi
}

# Move into spigot dir
cd "$workdir/Spigot"
basedir=$(pwd)
# Apply Spigot
(
	applyPatch ../Bukkit Spigot-API HEAD &&
	applyPatch ../CraftBukkit Spigot-Server patched
) || (
	echo "Failed to apply Spigot Patches"
    enableCommitSigningIfNeeded
	exit 1
) || exit 1
# Move out of Spigot
basedir="$1"
cd "$basedir"

echo "Importing MC Dev"

./scripts/importmcdev.sh "$basedir" >/dev/null 2>&1

# Apply paper
cd "$basedir"
(
	applyPatch "work/Spigot/Spigot-API" Paper-API HEAD &&
	applyPatch "work/Spigot/Spigot-Server" Paper-Server HEAD
    enableCommitSigningIfNeeded
) || (
	echo "Failed to apply Paper Patches"
    enableCommitSigningIfNeeded
	exit 1
) || exit 1
)
