Paper [![CI Status](http://ci.destroystokyo.com/buildStatus/icon?job=PaperSpigot)](http://ci.destroystokyo.com/job/PaperSpigot/)
===========

High performance Spigot fork that aims to fix gameplay and mechanics inconsistencies.

[IRC Support and Project Discussion](http://irc.spi.gt/iris/?channels=PaperSpigot)


Documentation
------
Access the paper docs here: [paper.readthedocs.org](https://paper.readthedocs.org/)

How To (Server Admins)
------
Paperclip is a jar file that you can download and run just like a normal jar file.

Download a copy of Paperclip.jar from our buildserver here:
https://ci.destroystokyo.com/job/PaperSpigot/

Run the Paperclip jar directly from your server. Just like old times

Paper requires [**Java 8**](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or above.

How To (Compiling Jar From Source)
------
To compile Paper, you need JDK 8, maven, and an internet connection.

Clone this repo, run `./build.sh --jar` from *bash*, get files.

Leave off --jar if you only wish to resync your checkout and not create a paperclip jar file just yet.

How To (Pull Request)
------
Clone this repo, run `./build.sh` from *bash*, get files.

Load Paper-API and Paper-Server into your IDE, make changes.
 
Commit your changes in each retrospective folder.

If you are editing a previous patch, commit, and then use `git rebase -i upstream/upstream` 
and then move your commit line to the commit you are editing and use code "f" or "s"
 
Once your commit is in place, cd back into Paper folder, and type `./rebuildPatches.sh`

You should now see either a new patch or an edited patch in your `git status`

Now, write a commit message for the patch files. For new patches, you can just repeat the commit message used in the patch commit itself, but if editing, explain what the change to the patch is.

now `git checkout -b someBranchName`, then `git remote add myorigin git@github.com:myname/Paper` and `git push myorigin someBranchName` and then proceed to PR.

Special Thanks To:
-------------

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

[YourKit](http://www.yourkit.com/), makers of the outstanding java profiler, support open source projects of all kinds with their full featured [Java](https://www.yourkit.com/java/profiler/index.jsp) and [.NET](https://www.yourkit.com/.net/profiler/index.jsp) application profilers. We thank them for granting Paper an OSS license so that we can make our software the best it can be.
