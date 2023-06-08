Contributing to Parrot
==========================
ParrotMC is happy you're willing to contribute to our projects. We are usually
very lenient with all submitted PRs, but there are still some guidelines you
can follow to make the approval process go more smoothly.

## Use a Personal Fork and not an Organization

Parrot will routinely modify your PR, whether it's a quick rebase or to take care
of any minor nitpicks we might have. Often, it's better for us to solve these
problems for you than make you go back and forth trying to fix them yourself.

Unfortunately, if you use an organization for your PR, it prevents Parrot from
modifying it. This requires us to manually merge your PR, resulting in us
closing the PR instead of marking it as merged.

We much prefer to have PRs show as merged, so please do not use repositories
on organizations for PRs.

See <https://github.com/isaacs/github/issues/1681> for more information on the
issue.

## Requirements

To get started with PRing changes, you'll need the following software, most of
which can be obtained in (most) package managers such as `apt` (Debian / Ubuntu;
you will most likely use this for WSL), `homebrew` (macOS / Linux), and more:

- `git` (package `git` everywhere);
- A Java 17 or later JDK (packages vary, use Google/DuckDuckGo/etc.).
  - [Adoptium](https://adoptium.net/) has builds for most operating systems.
  - Parrot requires JDK 17 to build, however, makes use of Gradle's
    [Toolchains](https://docs.gradle.org/current/userguide/toolchains.html)
    feature to allow building with only JRE 11 or later installed. (Gradle will
    automatically provision JDK 17 for compilation if it cannot find an existing
    install).

If you're on Windows, check
[the section on WSL](#patching-and-building-is-really-slow-what-can-i-do).

If you're compiling with Docker, you can use Adoptium's
[`eclipse-temurin`](https://hub.docker.com/_/eclipse-temurin/) images like so:

```console
# docker run -it -v "$(pwd)":/data --rm eclipse-temurin:17.0.1_12-jdk bash
Pulling image...

root@abcdefg1234:/# javac -version
javac 17.0.1
```

## Understanding Patches

Parrot is mostly patches and extensions to Spigot. These patches/extensions are
split into different directories which target certain parts of the code. These
directories are:

- `Parrot-API` - Modifications to `Spigot-API`/`Bukkit`;
- `Parrot-MojangAPI` - An API for [Mojang's Brigadier](https://github.com/Mojang/brigadier);
- `Parrot-Server` - Modifications to `Parrot`/`CraftBukkit`.

Because the entire structure is based on patches and git, a basic understanding
of how to use git is required. A basic tutorial can be found here:
<https://git-scm.com/docs/gittutorial>.

Assuming you have already forked the repository:

1. Clone your fork to your local machine;
2. Type `./gradlew applyPatches` in a terminal to apply the changes from upstream.
On Windows, leave out the `./` at the beginning for all `gradlew` commands;
3. cd into `Parrot-Server` for server changes, and `Parrot-API` for API changes.  
<!--You can also run `./parrot server` or `./parrot api` for these same directories
respectively.
1. You can also run `./parrot setup`, which allows you to type `parrot <command>`
from anywhere in the Parrot structure in most cases.-->

`Parrot-Server` and `Parrot-API` aren't git repositories in the traditional sense:
