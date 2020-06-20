Contributing to Paper
==========================
PaperMC has a very lenient policy towards PRs, but would prefer that you try and adhere to the following guidelines.

## Use a Personal Fork and not Organization
Paper will routinely modify your PR for you to rebase it on latest or take care of any minor nitpicks that we can
just solve for you instead of making you go back for a trivial item.

However, if you use an Organization for the PR, this prevents Paper from doing this, and will require us to manually
merge it and close the PR instead of it being marked as the purple merged.

We much prefer to have merged PR's show as merged, so please do not use organizations repo's for PR as GitHub sadly
does not support maintainers pushing to them.

See [https://github.com/isaacs/github/issues/1681](https://github.com/isaacs/github/issues/1681)

## Understanding Patches
Patches to Paper are very simple, but center around the directories 'Paper-API' and 'Paper-Server'

Assuming you already have forked the repository:

1. Pull the latest changes from the main repository
1. Type `./paper patch` in git bash to apply the changes from upstream
1. cd into `Paper-Server` for server changes, and `Paper-API` for API changes

These directories aren't git repositories in the traditional sense:

- Every single commit in Paper-Server/API is a patch. 
- 'origin/master' points to a directory similar to Paper-Server/API but for Paper
- Typing `git status` should show that we are 10 or 11 commits ahead of master, meaning we have 10 or 11 patches that Paper and Spigot don't
  - If it says something like `212 commits ahead, 207 commits behind`, then type `git fetch` to update spigot/paper

## Adding Patches
Adding patches to Paper is very simple:

1. Modify `Paper-Server` and/or `Paper-API` with the appropriate changes
1. Type `git add .` to add your changes
1. Run `git commit` with the desired patch message
1. Run `./paper rebuild` in the main directory to convert your commit into a new patch
1. PR your patches back to this repository

Your commit will be converted into a patch that you can then PR into Paper

## Help! I can't find the file I'm looking for!
By default, Paper (and upstream) only import files that we make changes to.
If you would like to make changes to a file that isn't present in Paper-Server's source directory, you
just need to add it to our import script to be ran during the patch process.

1. Save (rebuild) any patches you are in the middle of working on!
1. Identify the names of the files you want to import.
   - A complete list of all possible file names can be found at ```./work/Minecraft/$MCVER/spigot/net/minecraft/server```
1. Open the file at `./scripts/importmcdev.sh` and add the name of your file to the script.
1. Re-patch the server `./paper patch`
1. Edit away!

This change is temporary! DO NOT COMMIT CHANGES TO THIS FILE!
Once you have made your changes to the new file, and rebuilt patches, you may undo your changes to importmcdev.sh

Any file modified in a patch file gets automatically imported, so you only need this temporarily
to import it to create the first patch.

To undo your changes to the file, type `git checkout scripts/importmcdev.sh`


## Modifying Patches
Modifying previous patches is a bit more complex:

### Method 1
This method works by temporarily resetting HEAD to the desired commit to edit using rebase.

However, while in the middle of an edit, unless you also reset your API to a related commit, you will not be able to compile.

#### Using the Paper tool
The PaperMC build tool provides a handy command to automatically do this type of patch modification.

1. Type `./paper edit server` or `./paper edit api` depending on which project you want to edit.
   - It should show something like [this](https://gist.github.com/zachbr/21e92993cb99f62ffd7905d7b02f3159).
1. Replace `pick` with `edit` for the commit/patch you want to modify, and "save" the changes
   - Only do this for one commit at a time.
1. Make the changes you want to make to the patch.
1. Type `./paper edit continue` to finish and rebuild patches.
1. PR your modifications back to this project.

#### Manual method - Stashing
In case you need something more complex or want more control, this step-by-step instruction does
exactly what the above slightly automated system does.

1. If you have changes you are working on type `git stash` to store them for later.
   - Later you can type `git stash pop` to get them back.
1. Type `git rebase -i upstream/upstream`
   - It should show something like [this](https://gist.github.com/zachbr/21e92993cb99f62ffd7905d7b02f3159).
1. Replace `pick` with `edit` for the commit/patch you want to modify, and "save" the changes.
   - Only do this for one commit at a time.
1. Make the changes you want to make to the patch.
1. Type `git add .` to add your changes.
1. Type `git commit --amend` to commit.
   - **MAKE SURE TO ADD `--amend`** or else a new patch will be created.
   - You can also modify the commit message here.
1. Type `git rebase --continue` to finish rebasing.
1. Type `./paper rebuild` in the main directory.
   - This will modify the appropriate patches based on your commits.
1. PR your modifications back to this project.

### Method 2 (sometimes easier) - Manually moving commits
If you are simply editing a more recent commit or your change is small, simply making the change at HEAD and then moving the commit after you have tested it may be easier.

This method has the benefit of being able to compile to test your change without messing with your API HEAD.

1. Make your change while at HEAD
1. Make a temporary commit. You don't need to make a message for this.
1. Type `git rebase -i upstream/upstream`, move (cut) your temporary commit and move it under the line of the patch you wish to modify.
1. Change the `pick` with `f` (fixup) or `s` (squash) if you need to edit the commit message 
1. Type `./paper rebuild` in the main directory
   - This will modify the appropriate patches based on your commits
1. PR your modifications to github

### Method 3 - Fixup Commits
This is the automated way of doing Method 2. Basically, you make your changes at HEAD, but use fixup commits to modify earlier patches and git will rebase the commits to the right place and squash them.

1. Make your change while at HEAD
1. Make a fixup commit. `git commit -a --fixup <hashOfPatchToFix>`. You can get the hash by looking at `git log` or `git blame`, your IDE can assist you too.  
Alternatively, if you only know the name of the patch, you can do `git commit -a --fixup "Subject of Patch name"`
1. Rebase with autosquash: `git rebase --autosquash -i upstream/upstream`. It will move your fixup commit to the right place.
1. Type `./paper rebuild` in the main directory
   - This will modify the appropriate patches based on your commits
1. PR your modifications to github

## PR Policy
We'll accept changes that make sense. You should be able to justify their existence, along with any maintenance costs that come with them. Remember, these changes will affect everyone who runs Paper, not just you and your server.
While we will fix minor formatting issues, you should stick to the guide below when making and submitting changes.

## Formatting
All modifications to non-Paper files should be marked
- Multi line changes start with `// Paper start` and end with `// Paper end`
- You can put a messages with a change if it isn't obvious, like this: `// Paper start - reason`
  - Should generally be about the reason the change was made, what it was before, or what the change is
  - Multi-line messages should start with `// Paper start` and use `/* Multi line message here */` for the message itself
- Single line changes should have `// Paper` or `// Paper - reason`
- For example:
````java
entity.getWorld().dontbeStupid(); // Paper - was beStupid() which is bad
entity.getFriends().forEach(Entity::explode);
entity.a();
entity.b();
// Paper start - use plugin-set spawn
// entity.getWorld().explode(entity.getWorld().getSpawn());
Location spawnLocation = ((CraftWorld)entity.getWorld()).getSpawnLocation();
entity.getWorld().explode(new BlockPosition(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()));
// Paper end
````
- We generally follow usual java style, or what is programmed into most IDEs and formatters by default
  - This is also known as oracle style
  - It is fine to go over 80 lines as long as it doesn't hurt readability
  - There are exceptions, especially in Spigot-related files
  - When in doubt, use the same style as the surrounding code
  
## Patch Notes
When submitting patches to Paper, we may ask you to add notes to the patch header.
While we do not require it for all changes, you should add patch notes when the changes you're making are technical or complex.
It is very likely that your patch will remain long after we've all forgotten about the details of your PR, patch notes will help
us maintain it without having to dig back through GitHub history looking for your PR.

These notes should express the intent of your patch, as well as any pertinent technical details we should keep in mind long-term.
Ultimately, they exist to make it easier for us to maintain the patch across major version changes.

If you add a long message to your commit in the Paper-Server/API repos, the rebuildPatches command will handle these patch
notes automatically as part of generating the patch file. Otherwise if you're careful they can be added by hand (though you should be careful when doing this, and run it through a patch and rebuild cycle once or twice).

```patch
From 02abc033533f70ef3165a97bfda3f5c2fa58633a Mon Sep 17 00:00:00 2001
From: Shane Freeder <theboyetronic@gmail.com>
Date: Sun, 15 Oct 2017 00:29:07 +0100
Subject: [PATCH] revert serverside behavior of keepalives

This patch intends to bump up the time that a client has to reply to the
server back to 30 seconds as per pre 1.12.2, which allowed clients
more than enough time to reply potentially allowing them to be less
tempermental due to lag spikes on the network thread, e.g. that caused
by plugins that are interacting with netty.

We also add a system property to allow people to tweak how long the server
will wait for a reply. There is a compromise here between lower and higher
values, lower values will mean that dead connections can be closed sooner,
whereas higher values will make this less sensitive to issues such as spikes
from networking or during connections flood of chunk packets on slower clients,
 at the cost of dead connections being kept open for longer.

diff --git a/src/main/java/net/minecraft/server/PlayerConnection.java b/src/main/java/net/minecraft/server/PlayerConnection.java
index a92bf8967..d0ab87d0f 100644
--- a/src/main/java/net/minecraft/server/PlayerConnection.java
+++ b/src/main/java/net/minecraft/server/PlayerConnection.java
```

## Obfuscation Helpers
In an effort to make future updates easier on ourselves, Paper tries to use obfuscation helpers whenever possible. The purpose of these helpers is to make the code more readable. These helpers should be be made as easy to inline as possible by the JVM whenever possible.

An obfuscation helper to get an obfuscated field may be as simple as something like this:
```java
public final int getStuckArrows() { return this.bY(); } // Paper - OBFHELPER
```
Or it may be as complex as forwarding an entire method so that it can be overriden later:
```java
public boolean be() {
    // Paper start - OBFHELPER
    return this.pushedByWater();
}

public boolean pushedByWater() {
    // Paper end
    return true;
}
```
While they may not always be done in exactly the same way each time, the general goal is always to improve readability and maintainability, so use your best judgement.

## Configuration files
To use a configurable value in your patch, add a new entry in either ```PaperConfig``` or ```PaperWorldConfig```. Use the former if a value must remain the same throughout all worlds, or the latter if it can change between worlds. The latter is preferred whenever possible.

### PaperConfig example:
```java
public static boolean saveEmptyScoreboardTeams = false;
private static void saveEmptyScoreboardTeams() {
    saveEmptyScoreboardTeams = getBoolean("settings.save-empty-scoreboard-teams", false);
}
```
Notice that the field is always public, but the setter is always private. This is important to the way the configuration generation system works. To access this value, reference it as you would any other static value:
```java
if (!PaperConfig.saveEmptyScoreboardTeams) {
```

### PaperWorldConfig example:
```java
public boolean useInhabitedTime = true;
private void useInhabitedTime() {
    useInhabitedTime = getBoolean("use-chunk-inhabited-timer", true);
}
```
Again, notice that the field is always public, but the setter is always private. To access this value, you'll need an instance of the ```net.minecraft.World``` object:

```java
return this.world.paperConfig.useInhabitedTime ? this.w : 0;
```
