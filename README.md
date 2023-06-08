Paper [![Paper Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=master)](https://github.com/PaperMC/Paper/actions)

A brand new fork of the popular server software @PaperMC


How To (Server Admins)
------
ParrotMC is still being developed!

Parrot Version: 1.19.4-REL-1

How to compile Parrot for use
------
To compile Parrot, you need JDK 17 and an internet connection.

Clone this repo, run `./gradlew applyPatches`, then `./gradlew createReobfBundlerJar` from your terminal. You can find the compiled jar in the project root's `build/libs` directory.

To get a full list of tasks, run `./gradlew tasks`.
