PaperSpigot [![CI Status](http://ci.destroystokyo.com/buildStatus/icon?job=PaperSpigot)](http://ci.destroystokyo.com/job/PaperSpigot/) [![Travis Status](https://travis-ci.org/PaperSpigot/PaperSpigot.svg?branch=master)](https://travis-ci.org/PaperSpigot/PaperSpigot)
===========

High performance Spigot fork that aims to fix gameplay and mechanics inconsistencies.

[IRC Support and Project Discussion](http://irc.spi.gt/iris/?channels=PaperSpigot)

[Compiled Builds](http://ci.destroystokyo.com/job/PaperSpigot)

[Documentation and wiki](https://github.com/PaperSpigot/PaperSpigot/wiki)


How To
-----------

Init the submodules : `git submodule update --init`

Apply Patches : `./applyPatches.sh`

### Create patch for server ###

`cd PaperSpigot-Server`

Add your file for commit : `git add <file>`

Commit : `git commit -m <msg>`

`cd ..`

Create Patch `./rebuildPatches.sh`

### Create patch for API ###

`cd Paperspigot-API`

Add your file for commit : `git add <file>`

Commit : `git commit -m <msg>`

`cd ..`

Create Patch `./rebuildPatches.sh`




Compilation
-----------

We use maven to handle our dependencies.

* Install [Maven 3](http://maven.apache.org/download.html)
* Clone this repo and: `mvn clean install`
