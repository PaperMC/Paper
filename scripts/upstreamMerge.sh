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

function update_submodules() {
  gitcmd submodule update --init
  local submodule
  for submodule in "${@}"; do
    update "${submodule}"
  done
}

function submodule_has_update {
  local -r submodule="${1?Submodule is missing}"
  local -r submodule_dir="${workdir}/${submodule}"
  cd "${submodule_dir}"
  gitcmd fetch && ! gitcmd diff --quiet --exit-code origin/master
}

function any_submodule_has_update {
  local -r submodules=( "${@}" )
  local submodule
  for submodule in "${submodules[@]}"; do
    submodule_has_update "${submodule}" && return 0
  done
}

function apply_rebuild {
  ./gradlew applyPatches -Dpaperweight.debug=true || return $?
  ./gradlew rebuildPatches -Ppaperweight.filter-patches=false || return $?
}

function is_yes {
  local -r value="${1:-}"
  [ "${value:-}" = yes ] || [ "${value:-}" = y ]
}

function skip_rebuild {
  # expected to have variable called `skip_rebuild` set in context
  is_yes "${skip_rebuild:-}"
}

function main {
  local -r update_all="${1:-}"
  local -r skip_rebuild="${2:-}"
  local submodules_to_update=(
    Bukkit
    CraftBukkit
    Spigot
  )
  case "${update_all:-}" in
    all|a|yes|y) submodules_to_update+=( BuildData )
  esac
  if ! any_submodule_has_update "${submodules_to_update[@]}"; then
    echo "Didn't find updates for any submodule - skipping..."
    return 0
  fi
  cd "${basedir}"
  if ! skip_rebuild; then
    echo "Rebuilding patches without filtering to improve mergeability"
    apply_rebuild || return $?
  fi
  update_submodules "${submodules_to_update[@]}"
  if ! gitcmd diff --cached --quiet --exit-code -- "${workdir}"; then
    if ! skip_rebuild; then
      echo "Rebuilding patches without filtering to improve apply ability"
      cd "${basedir}"
      ./gradlew cleanCache || return $? # todo: Figure out why this is necessary
      apply_rebuild || return $?
    else
      echo "Skipping rebuild..."
    fi
  else
    echo "Something is broken - should find changes!"
    return 1
  fi
}


if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
  main "$@"
fi
