Bukkit
======
A plugin API for [Minecraft](https://minecraft.net/) servers, currently maintained by [SpigotMC](http://www.spigotmc.org/).

Bug Reporting
-------------
The development team is very open to both bug and feature requests / suggestions. You can submit these on the [JIRA Issue Tracker](http://hub.spigotmc.org/jira/).

Compilation
-----------
Bukkit is a Java program which uses [Maven 3](http://maven.apache.org/) for compilation. To compile fresh from Git, simply perform the following steps:

* Install Maven and Git using your preferred installation methods.
* `git clone https://hub.spigotmc.org/stash/scm/spigot/bukkit.git`.
* `mvn clean install`.
 
Some IDEs such as [NetBeans](https://netbeans.org/) can perform these steps for you. Any Maven capable Java IDE can be used to develop with Bukkit, however the current team's personal preference is to use NetBeans.

Contributing
------------
Contributions of all sorts are welcome. To manage community contributions, we use the pull request functionality of Stash. In to gain access to Stash and create a pull request, you will first need to perform the following steps:

* Create an account on [JIRA](http://hub.spigotmc.org/jira/).
* Fill in the [SpigotMC CLA](http://www.spigotmc.org/go/cla) and wait up to 24 hours for your Stash account to be activated. Please ensure that your username and email addresses match.
* Log into Stash using your JIRA credentials.
 
Once you have performed these steps you can create a fork, push your code changes, and then submit it for review.

Bukkit's Goals
--------------

As a rough guideline, ask yourself the following questions to determine if the proposed change fits the Bukkit project's goals. Please remember that this is only a rough guideline
and it may or may not reflect the definitive answer to this question.
Discussions about proposed changes are held in the [Spigot IRC](https://www.spigotmc.org/wiki/irc-guide/).

* Does it expose an implementation detail of the server software, or the protocol or file formats?

    1. If your change revolves around an implementation detail then it is not proper API design. Examples of bad API design would be along the lines of
    a packet API or an NBT storage API.
* Does it result in unexpected behaviour as defined by the Vanilla specification?
    1. One of the goals of the Bukkit project is to be an extended Minecraft Server. Meaning if you choose to run the Bukkit server without any plugin, it should function
    exactly as the Minecraft Server would with some rare exceptions. If your change alters the behaviour of the server in such a way that you would not have the same experience as you
    would in Vanilla, your change does not fit with the Bukkit project's goals.
* Does it expose an issue or vulnerability when operating within the Vanilla environment?
    1. One of the goals of the Bukkit project is to be able to operate within the limitations of the Vanilla environment. If your change results in, or exposes, the ability to, for example,
    crash the client invalid data is set, it does not fit the Bukkit project's needs.

If you answered yes to any of these questions, chances are high your change does not fit within the Bukkit project's goals and will likely not be accepted.
Regardless, there are a few other important questions that need to be asked before you start working on a change:
* Is this change reasonably supportable and maintainable?
    1. *Are there tests for this change? Does this change rely on magic numbers?*
* Is this change reasonably future proof?

Code Requirements
-----------------
* For the most part, CraftBukkit and Bukkit use the [Sun/Oracle coding standards](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).
* No tabs; use 4 spaces instead.
    * Empty lines should contain no spaces.
* No trailing whitespaces.
* No 80 character column limit, or 'weird' mid-statement newlines unless absolutely necessary.
* No one-line methods.
* All major additions should have documentation(e.g. javadocs).
* Try to follow test driven development where available.
* All code should be free of magic values. If this is not possible, it should be marked with a TODO comment indicating it should be addressed in the future.

Bukkit/CraftBukkit employs [JUnit 4](http://www.vogella.com/articles/JUnit/article.html) for testing. Pull Requests(PR) should attempt to integrate within that framework as appropriate.
Bukkit is a large project and what seems simple to a PR author at the time of writing may easily be overlooked by other authors and updates. Including unit tests with your PR
will help to ensure the PR can be easily maintained over time and encourage the Spigot team to pull the PR.

* There needs to be a new line at the end of every file.
* Absolutely no wildcard imports.
