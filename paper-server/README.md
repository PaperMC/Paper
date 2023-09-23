CraftBukkit
======
An implementation of the [Bukkit](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit) plugin API for [Minecraft](https://minecraft.net/) servers, currently maintained by [SpigotMC](https://www.spigotmc.org/).

#### Index

* [Bug Reporting](#bug-reporting)
* [Compilation](#compilation)
* [Contributing](#contributing)
* [Code Requirements](#code-requirements)
    * [Applying Patches](#applying-patches)
    * [Making Changes to Minecraft](#making-changes-to-minecraft)
    * [Minimal Diff Policy](#minimal-diff-policy)
    * [CraftBukkit Comments](#craftbukkit-comments)
* [Creating Pull Requests](#creating-pull-requests)
* [Useful Resources](#useful-resources)

Bug Reporting
-------------
<a name="bug-reporting"></a>
The development team is very open to both bug and feature requests / suggestions. You can submit these on the [JIRA Issue Tracker](https://hub.spigotmc.org/jira/).

Compilation
-----------
<a name="compilation"></a>
CraftBukkit is a Java program which uses [Maven 3](https://maven.apache.org/) for compilation. To compile fresh from Git, simply perform the following steps:

* Install Git using your preferred installation methods.
* Download and run [BuildTools](https://www.spigotmc.org/wiki/buildtools/)

Some IDEs such as [NetBeans](https://netbeans.org/) can perform these steps for you. Any Maven capable Java IDE can be used to develop with CraftBukkit, however the current team's personal preference is to use NetBeans.

Contributing
------------
<a name="contributing"></a>
Contributions of all sorts are welcome. To manage community contributions, we use the pull request functionality of Stash. In order to gain access to Stash and create a pull request, you will first need to perform the following steps:

* Create an account on [JIRA](https://hub.spigotmc.org/jira/).
* Fill in the [SpigotMC CLA](https://www.spigotmc.org/go/cla) and wait up to 24 hours for your Stash account to be activated. Please ensure that your username and email addresses match.
* Log into Stash using your JIRA credentials.

Once you have performed these steps you can create a fork, push your code changes, and then submit it for review.

If you submit a PR involving both Bukkit and CraftBukkit, each PR should link the other.

The minimum requirement for style, compilation & usage is Java 16 unless there is a compelling reason.

Code Requirements
-----------------
<a name="code-requirements"></a>
* For the most part, CraftBukkit and Bukkit use the [Sun/Oracle coding standards](https://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).
* No tabs; use 4 spaces instead.
    * Empty lines should contain no spaces.
* No trailing whitespaces.
* No 80 character column limit, or 'weird' mid-statement newlines unless absolutely necessary.
    * The 80 character column limit still applies to documentation.
* No one-line methods.
* All major additions should have documentation.
* Try to follow test driven development where available.
* All code should be free of magic values. If this is not possible, it should be marked with a TODO comment indicating it should be addressed in the future.
  * If magic values are absolutely necessary for your change, what those values represent should be documented in the code as well as an explanation in the Pull Request description on why those values are necessary.
* No unnecessary code changes. Look through all your changes before you submit it.
* Do not attempt to fix multiple problems with a single patch or pull request.
* Do not submit your personal changes to configuration files.
* Avoid moving or renaming classes.

Bukkit/CraftBukkit employs [JUnit 5](https://www.vogella.com/tutorials/JUnit/article.html) for testing. Pull Requests(PR) should attempt to integrate within that framework as appropriate.
Bukkit is a large project and what seems simple to a PR author at the time of writing may easily be overlooked by other authors and updates. Including unit tests with your PR
will help to ensure the PR can be easily maintained over time and encourage the Spigot team to pull the PR.

* There needs to be a new line at the end of every file.
* Imports should be organised in a logical manner.
    * Do not group packages
    * All new imports should be within existing CraftBukkit comments if any are present. If not, make them.
    * __Absolutely no wildcard imports outside of tests.__
    * If you only use an import once, don't import it. Use the fully qualified name.

Any questions about these requirements can be asked in #help-development in Discord.

Applying Patches
----------------
<a name="applying-patches"></a>
Any time new patches are created and/or updated in CraftBukkit, you need to apply them to your development environment.

1. Pull changes from CraftBukkit repo.
2. Run the `applyPatches.sh` script in the CraftBukkit directory.
    - This script requires a decompile directory from BuildTools. (e.g. <BuildTools Directory>/work/decompile-XXXXXX)
3. Your development environment is now up to date with the latest CraftBukkit patches!

Making Changes to Minecraft
---------------------------
<a name="making-changes-to-minecraft"></a>
Importing new NMS classes to CraftBukkit is actually very simple.

1. Find the `work/decompile-XXXXXX` folder in your BuildTools folder.
2. Find the class you want to add in the `net/minecraft/server` folder and copy it.
3. Copy the selected file to the `src/main/java/net/minecraft/server` folder in CraftBukkit.
4. Implement changes.
5. Run `makePatches.sh` to create a new patch for the new class.
    * _Be sure that Git recognizes the new file. This might require manually adding the file._
6. Commit new patch.

Done! You have added a new patch for CraftBukkit!

**Making Changes to NMS Classes**

Bukkit/CB employs a Minimal Diff policy to help guide when changes should be changed to Minecraft and what those changes should be.
This is to ensure that any changes have the smallest impact possible on the update process whenever a new Minecraft version is released.
As well as the Minimal Diff Policy, *every* change made to a Minecraft class must be marked with the appropriate CraftBukkit comment.
At no point should you rename an existing/obfuscated field or method. All references to existing/obfusacted fields/methods should be marked with the `// PAIL rename` comment.
Mapping of new fields/methods are done when there are enough changes to warrant the work. (Any questions can be asked in our [Discord](https://www.spigotmc.org/go/discord))

__*Key Points*__:
* All additions to patches must be accompanied by an appropriate comment.
* To avoid large patches, avoid adding methods where possible. We prefer making fields public over adding methods in patches.
  * If you *have* to add a method to a patch, please explain why in the Pull Request description.
* __Never__ rename an existing field or method. If you want something renamed, include a ```PAIL rename``` comment
* Converting a method/class from one access level to another (i.e. private to public) is fine as long as that method is not overridden in subclasses.
  * If a method is overridden in its' subclasses, create a new method that calls that method instead (along with appropriate CraftBukkit comments).
* If you can use a field to accomplish something, use that over creating a new method.

Minimal Diff Policy
-------------------
<a name="minimal-diff-policy"></a>

The Minimal Diff Policy is key to any changes made within Minecraft classes. When people think of the phrase "minimal diffs", they often take it
to the extreme - they go completely out of their way to abstract the changes they are trying to make away from editing Minecraft's classes as much as possible.
However, this is not what is meant by "minimal diffs". Instead, when trying to understand this policy, it helps to keep in mind its goal: to reduce the impact of changes we make
to Minecraft's internals have on our update process.

To put it simply, the Minimal Diffs Policy simply means to make the smallest change in a Minecraft class possible without duplicating logic.

Here are a few tips you should keep in mind, or common areas you should focus on:

* Try to avoid duplicating logic or code when making changes.
* Try to keep your changes easily discernible - don't nest or group several unrelated changes together.
    * All changes must be surrounded by [CraftBukkit comments](#craftbukkit-comments).
* If you only use an import once within a class, don't import it and use a fully qualified name instead.
* Try to employ "short-circuiting" of logic if at all possible. This means you should force a conditional to be the value needed to side step the code block to achieve your desired effect.

__For example, to short circuit this:__
```java
if (!this.world.isClientSide && !this.isDead && (d0*d0) + d1 + (d2*d2) > 0.0D) {
    this.die();
    this.h();
}
```
__You would do this:__
```java
if (false && !this.world.isClientSide && !this.isDead && (d0*d0) + d1 + (d2*d2) > 0.0D) {
    this.die();
    this.h();
}
```
* For checks related to API where an exception needs to be thrown in a specific case we recommend use the package `Preconditions`
  * For checking arguments we recommend using `Preconditions#checkArgument` where a failing check should throw a `IllegalArgumentException`
    * We recommend using this to ensure the behaviour of `@NotNull` in the Bukkit API
  * For checking arguments we recommend using `Preconditions#checkState` where a failing check should throw a `IllegalStateException`

__For example, you should use:__
```java
private Object messenger;

public void sendMessage(Sender sender, String message) {
    Preconditions.checkArgument(sender != null, "sender cannot be null");
    Preconditions.checkState(this.messenger != null, "The messenger instance cannot be used")
}
```
__Instead of:__
```java
private Object messenger;

public void sendMessage(Sender sender, String message) {
    if (sender == null) {
        throw new IllegalArgumentException("Sender cannot be null");
    }

    if (this.messenger == null) {
        throw new IllegalStateException("The messenger instance cannot be used");
    }
}
```

* When the change you are trying to make involves removing code, or delegating it somewhere else, instead of removing it, you should comment it out.

__For example:__
```java
// CraftBukkit start - special case dropping so we can get info from the tile entity
public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
    if (world.random.nextFloat() < f) {
        ItemStack itemstack = new ItemStack(Item.SKULL, 1, this.getDropData(world, i, j, k));
        TileEntitySkull tileentityskull = (TileEntitySkull) world.getTileEntity(i, j, k);

        if (tileentityskull.getSkullType() == 3 && tileentityskull.getExtraType() != null && tileentityskull.getExtraType().length() > 0) {
            itemstack.setTag(new NBTTagCompound());
            itemstack.getTag().setString("SkullOwner", tileentityskull.getExtraType());
        }

        this.b(world, i, j, k, itemstack);
    }
}
// CraftBukkit end

public void remove(World world, int i, int j, int k, int l, int i1) {
    if (!world.isStatic) {
        /* CraftBukkit start - drop item in code above, not here
        if ((i1 & 8) == 0) {
            ItemStack itemstack = new ItemStack(Item.SKULL, 1, this.getDropData(world, i, j, k));
            TileEntitySkull tileentityskull = (TileEntitySkull) world.getTileEntity(i, j, k);

            if (tileentityskull.getSkullType() == 3 && tileentityskull.getExtraType() != null && tileentityskull.getExtraType().length() > 0) {
                itemstack.setTag(new NBTTagCompound());
                itemstack.getTag().setString("SkullOwner", tileentityskull.getExtraType());
            }

            this.b(world, i, j, k, itemstack);
        }
        // CraftBukkit end */

        super.remove(world, i, j, k, l, i1);
    }
}
```

#### CraftBukkit Comments
<a name="craftbukkit-comments"></a>

Changes to a Minecraft class should be clearly marked using CraftBukkit comments.

* All CraftBukkit comments should be capitalised appropriately. (i.e. CraftBukkit, not CB/craftBukkit, etc.)
* If the change only affects one line of code, use an end of line CraftBukkit comment

__Examples:__

If the change is obvious, then you need a simple end of line comment.
```java
if (true || minecraftserver.getAllowNether()) { // CraftBukkit
```

Every reference to an obfuscated field/method in NMS should be marked with:
```java
// PAIL rename newName
```
All PAIL rename comments must include a new name.

If, however, the change is something important to note or difficult to discern, you should include a reason at the end of the comment
```java
public int fireTicks; // PAIL private -> public
```
Changing access levels must include a PAIL comment indicating the previous access level and the new access level.

If adding the CraftBukkit comment negatively affects the readability of the code, then you should place the comment on a new line *above* the change you made.
```java
// CraftBukkit
if (!isEffect && !world.isStatic && world.difficulty >= 2 && world.areChunksLoaded(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
```

* If the change affects more than one line, you should use a multi-line CraftBukkit comment.

__Example:__

The majority of the time, multi-line changes should be accompanied by a reason since they're usually much more complicated than a single line change.
*If the change is something important to note or difficult to discern, you should include a reason at the end of line CraftBukkit comment.*
```java
// CraftBukkit start - special case dropping so we can get info from the tile entity
public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
    if (world.random.nextFloat() < f) {
        ItemStack itemstack = new ItemStack(Item.SKULL, 1, this.getDropData(world, i, j, k));
        TileEntitySkull tileentityskull = (TileEntitySkull) world.getTileEntity(i, j, k);

        if (tileentityskull.getSkullType() == 3 && tileentityskull.getExtraType() != null && tileentityskull.getExtraType().length() > 0) {
            itemstack.setTag(new NBTTagCompound());
            itemstack.getTag().setString("SkullOwner", tileentityskull.getExtraType());
        }

        this.b(world, i, j, k, itemstack);
    }
}
// CraftBukkit end
```
Otherwise, if the change is obvious, such as firing an event, then you can simply use a multi-line comment.
```java
    // CraftBukkit start
    BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), BlockIgniteEvent.IgniteCause.LIGHTNING, null);
    world.getServer().getPluginManager().callEvent(event);

    if (!event.isCancelled()) {
        world.setTypeIdUpdate(i, j, k, Block.FIRE);
    }
    // CraftBukkit end
```
* All CraftBukkit comments should be on the same indentation level the code block it is in.

__Imports in Minecraft Classes__
* Do not remove unused imports if they are not marked by CraftBukkit comments.

Creating Pull Requests
----------------------
<a name="creating-pull-requests"></a>
To learn what Spigot expects of a Pull Request please view the [Contributing guidelines](CONTRIBUTING.md)

Useful Resources
----------------
<a name="useful-resources"></a>

* [An example pull request demonstrating the things we look out for](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/pull-requests/365/overview)
* [JIRA, our bug tracker](https://hub.spigotmc.org/jira/)
* [Join us on Discord - #help-development](https://www.spigotmc.org/go/discord)