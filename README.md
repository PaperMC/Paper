Spigot [![Build Status](https://travis-ci.org/SpigotMC/Spigot.png)](https://travis-ci.org/SpigotMC/Spigot)
===========

High performance Minecraft server implementation


How To
-----------

Init a Craftbukkit and Bukkit module : `git submodule update --init`

Apply Patches : `./applyPatches.sh`

### Create patch for server ###

`cd Spigot-Server`

Add your file for commit : `git add <file>`

Commit : `git commit -m <msg>`

`cd ..`

Create Patch `./rebuildPatches.sh`

### Create patch for API ###

`cd Spigot-API`

Add your file for commit : `git add <file>`

Commit : `git commit -m <msg>`

`cd ..`

Create Patch `./rebuildPatches.sh`




Compilation
-----------

We use maven to handle our dependencies.

* Install [Maven 3](http://maven.apache.org/download.html)
* Clone this repo and: `mvn clean install`
