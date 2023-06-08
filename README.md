Paper [![Paper Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=master)](https://github.com/PaperMC/Paper/actions)
[![Discord](https://img.shields.io/discord/289587909051416579.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/papermc)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/papermc?label=GitHub%20Sponsors)](https://github.com/sponsors/PaperMC)
[![Open Collective](https://img.shields.io/opencollective/all/papermc?label=OpenCollective%20Sponsors)](https://opencollective.com/papermc)
===========

A brand new fork of the popular server software @PaperMC


How To (Server Admins)
------
ParrotMC is still being developed!

```
 * Artifact Information:
```xml
<dependency>
    <groupId>io.featheredfriends.parrot</groupId>
    <artifactId>paper-api</artifactId>
    <version>1.19.4-SNAP-1.0</version>
    <scope>provided</scope>
</dependency>
 ```

How to compile Parrot for use
------
To compile Parrot, you need JDK 17 and an internet connection.

Clone this repo, run `./gradlew applyPatches`, then `./gradlew createReobfBundlerJar` from your terminal. You can find the compiled jar in the project root's `build/libs` directory.

To get a full list of tasks, run `./gradlew tasks`.
