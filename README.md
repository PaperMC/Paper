CraftBukkit
===========

A Bukkit (Minecraft Server API) implementation

Compilation
-----------

We use maven to handle our dependencies.

* Install [Maven 3](http://maven.apache.org/download.html)
* Check out and install [Bukkit](http://github.com/Bukkit/Bukkit)
    * *Note*: this is not needed as the repository we use has Bukkit too, but you might have a newer one (with your own changes :D)
* Check out this repo and: `mvn clean package`

Coding and Pull Request Conventions
-----------

* We generally follow the Sun/Oracle coding standards.
* No tabs; use 4 spaces instead.
* No trailing whitespaces.
* No 80 column limit or 'weird' midstatement newlines.
* The number of commits in a pull request should be kept to a minimum (squish them into one most of the time - use common sense!).
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Pull requests should be tested (does it compile? AND does it work?) before submission.

If you make changes or add net.minecraft.server classes it is mandatory to:

* Get the files from the [mc-dev repo](https://github.com/Bukkit/mc-dev) - make sure you have the last version!
* Mark your changes with:
    * 1 line; add a trailing: `// CraftBukkit [- Optional reason]`
    * 2+ lines; add
        * Before: `// CraftBukkit start [- Optional comment]`
        * After: `// CraftBukkit end`
* Keep the diffs to a minimum (*really* important)

Follow the above conventions if you want your pull requests accepted.
