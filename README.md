PaperSpigot [![CI Status](http://ci.destroystokyo.com/buildStatus/icon?job=PaperSpigot)](http://ci.destroystokyo.com/job/PaperSpigot/) [![Travis Status](https://travis-ci.org/PaperSpigot/PaperSpigot.svg?branch=master)](https://travis-ci.org/PaperSpigot/PaperSpigot)
===========

High performance Spigot fork that aims to fix gameplay and mechanics inconsistencies.

[IRC Support and Project Discussion](http://irc.spi.gt/iris/?channels=PaperSpigot)




How To
------
Download a copy of PaperTools.jar from our buildserver here:
https://ci.destroystokyo.com/job/PaperSpigot-BuildTools/

Place PaperTools.jar into an empty directory and run it with `java -jar PaperTools.jar` from Git-Bash/Bash

It requires you have git installed on your local system as well as JDK 7 or above.






How To (Advanced & Legacy Users)
---------------------------------

After running the PaperTools jar, you can compile at any time. A brief overview is provided below.
For more novice users, we recommend you avoid these steps and stick to the PaperTools.jar build tool.

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
