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
    echo "Rebuilding patches"
    cd "${basedir}"
    ./gradlew cleanCache || exit 1 # todo: Figure out why this is necessary
    ./gradlew applyPatches -Dpaperweight.debug=true || exit 1
    # TODO: rebuild patches without filtering to improve apply ability
    # Use separate steps for rebuild{Api,Server}Patches due to https://docs.gradle.org/current/userguide/custom_tasks.html#limitations
    # Can't use it for now, since you can't set boolean gradle option to false through CLI: https://github.com/gradle/gradle/issues/4311
    ./gradlew rebuildPatches || exit 1
  fi
}


if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
  main "$@"
fi
