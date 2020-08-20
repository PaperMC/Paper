#!/usr/bin/env sh
set -ue
PS1="$" # TODO: Why?

changelog() {
    base="$(git ls-tree HEAD "$1" | cut -d' ' -f3 | cut -f1)"
    cd "$1" && git log --oneline "${base}...HEAD"
}
bukkit="$(changelog work/Bukkit)"
cb="$(changelog work/CraftBukkit)"
spigot="$(changelog work/Spigot)"

updated=""
logsuffix=""
if [ -n "$bukkit" ]; then
    logsuffix="$logsuffix\n\nBukkit Changes:\n$bukkit"
    updated="Bukkit"
fi
if [ -n "$cb" ]; then
    logsuffix="$logsuffix\n\nCraftBukkit Changes:\n$cb"
    if [ -z "$updated" ]; then updated="CraftBukkit"; else updated="$updated/CraftBukkit"; fi
fi
if [ -n "$spigot" ]; then
    logsuffix="$logsuffix\n\nSpigot Changes:\n$spigot"
    if [ -z "$updated" ]; then updated="Spigot"; else updated="$updated/Spigot"; fi
fi
disclaimer="Upstream has released updates that appears to apply and compile correctly.\nThis update has not been tested by PaperMC and as with ANY update, please do your own testing"

if [ -n "${1:-}" ]; then
    disclaimer="$*"
fi

log="${UP_LOG_PREFIX}Updated Upstream ($updated)\n\n${disclaimer}${logsuffix}"

printf '%s' "$log" | git commit -F -
