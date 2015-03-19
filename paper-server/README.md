CraftBukkit
======
An implemenation of the [Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit) plugin API for [Minecraft](https://minecraft.net/) servers, currently maintained by [SpigotMC](http://www.spigotmc.org/).

Bug Reporting
-------------
The development team is very open to both bug and feature requests / suggestions. You can submit these on the [JIRA Issue Tracker](http://hub.spigotmc.org/jira/).

Compilation
-----------
CraftBukkit is a Java program which uses [Maven 3](http://maven.apache.org/) for compilation. To compile fresh from Git, simply perform the following steps:

* Install Git using your preferred installation methods.
* Download and run [BuildTools](https://www.spigotmc.org/wiki/buildtools/)
 
Some IDEs such as [NetBeans](https://netbeans.org/) can perform these steps for you. Any Maven capable Java IDE can be used to develop with CraftBukkit, however the current team's personal preference is to use NetBeans.

Contributing
------------
Contributions of all sorts are welcome. To manage community contributions, we use the pull request functionality of Stash. In to gain access to Stash and create a pull request, you will first need to perform the following steps:

* Create an account on [JIRA](http://hub.spigotmc.org/jira/).
* Fill in the [SpigotMC CLA](http://www.spigotmc.org/go/cla) and wait up to 24 hours for your Stash account to be activated. Please ensure that your username and email addresses match.
* Log into Stash using your JIRA credentials.
 
Once you have performed these steps you can create a fork, push your code changes, and then submit it for review.

If you submit a PR involving both Bukkit and CraftBukkit, it's appreciated if each PR links the other. Additionally, every reference to an obfuscated field/method in NMS should be marked with `// PAIL: Rename` and optionally a suggested name, to make mapping creation easier. E.g.:
```
    entity.k.get(i).f(); // PAIL Rename pathfinders, navigateToHome 
```
Also, make sure to include `// Craftbukkit` comments to indicate modified NMS sources.
