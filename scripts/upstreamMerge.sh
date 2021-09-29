#!/usr/bin/env bash

set -euo pipefail

function gitcmd {
  git -c commit.gpgsign=false "${@}"
}

readonly basedir="$(cd "$(dirname "$0")/.." && pwd -P)"
readonly workdir="${basedir}/work"

function update {
  local -r submodule="${1?Submodule is missing}"
  local -r submodule_dir="${workdir}/${submodule}"
  cd "${submodule_dir}"
  gitcmd fetch && gitcmd clean -fd && gitcmd reset --hard origin/master || return 1
  cd "${basedir}"
  gitcmd add --force "${submodule_dir}"
}

function main {
  local -r update_all="${1:-}"
  local -r skip_rebuild="${2:-}"
  cd "${basedir}"
  gitcmd submodule update --init
  update Bukkit
  update CraftBukkit
  update Spigot
  case "${update_all:-}" in
    all|a|yes|y) update BuildData
  esac
  if ! gitcmd diff --cached --quiet --exit-code -- "${workdir}" \
      && [[ "${skip_rebuild:-}" != yes ]] \
      && [[ "${skip_rebuild:-}" != y ]]
    then
    echo "Rebuilding patches without filtering to improve apply ability"
    cd "${basedir}"
    ./gradlew cleanCache || exit 1 # todo: Figure out why this is necessary
    ./gradlew applyPatches -Dpaperweight.debug=true || exit 1
    ./gradlew rebuildPatches --filter-patches=false || exit 1
  fi
}


if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
  main "$@"
fi
