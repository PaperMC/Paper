#!/usr/bin/env bash

set -euo pipefail

function gitcmd {
  git -c commit.gpgsign=false "${@}"
}

readonly basedir="$(cd "$(dirname "$0")/.." && pwd -P)"
readonly workdir="${basedir}/work"
readonly patchdir="${basedir}/patches"

function update {
  local -r submodule="${1?Submodule is missing}"
  local -r submodule_dir="${workdir}/${submodule}"
  cd "${submodule_dir}"
  gitcmd fetch && gitcmd clean -fd && gitcmd reset --hard origin/master || return 1
  cd "${basedir}"
  gitcmd add --force "${submodule_dir}"
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
  cd "${basedir}"
  if ! skip_rebuild; then
    echo "Rebuilding patches without filtering to improve mergeability"
    apply_rebuild || return $?
  fi
  gitcmd submodule update --init
  update Bukkit
  update CraftBukkit
  update Spigot
  case "${update_all:-}" in
    all|a|yes|y) update BuildData
  esac
  if ! gitcmd diff --cached --quiet --exit-code -- "${workdir}"; then
    if ! skip_rebuild; then
      echo "Rebuilding patches without filtering to improve apply ability"
      cd "${basedir}"
      ./gradlew cleanCache || return $? # todo: Figure out why this is necessary
      apply_rebuild || return $?
    else
      echo "Skipping rebuild..."
    fi
  elif ! skip_rebuild; then
    echo "No updates - reverting changes to patches"
    gitcmd restore -- "${patchdir}"
  fi
}


if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
  main "$@"
fi
