# How to Contribute

The Bukkit project prides itself on being community built and driven.  We love it when members of our community want to jump right in and get involved, so here's what you need to know.

## Quick Guide
1. Create or find an issue to address on our [JIRA issue tracker](http://leaky.bukkit.org).
- Does your proposed change [fit Bukkit's goals](#does-the-change-fit-bukkits-goals)?
- Fork the repository if you haven't done so already.
- Make your changes in a new branch (if your change affects both Bukkit and CraftBukkit, we highly suggest you use the same name for your branches in both repos).
- Test your changes.
- Push to your fork and submit a pull request.
- **Note:** The project is put under a code freeze leading up to the release of a Minecraft update in order to give the Bukkit team a static code base to work on.

![Life Cycle of a Bukkit Improvement](http://i.imgur.com/Ed6T7AE.png)

## Getting Started
- You'll need a free [JIRA account](http://leaky.bukkit.org) (on our issue tracker affectionately called Leaky).
- You'll need a free [GitHub account](https://github.com/signup/free).
- Make sure you have a JIRA ticket for your issue at hand.
    * Either search the list of current issues and find an appropriate issue.
    * Or create one yourself if one does not already exist.
        * When creating an issue, make sure to clearly describe the issue (including steps to reproduce it if it is a bug).
- Fork the repository on GitHub.
- **Note:** The project is put under a code freeze leading up to the release of a Minecraft update in order to give the Bukkit team a static code base to work on.

## Does the Change Fit Bukkit's Goals?
As a rough guideline, ask yourself the following questions to determine if your proposed change fits the Bukkit project's goals. Please remember that this is only a rough guideline and may or may not reflect the definitive answer to this question.

* Does it expose an implementation detail of the server software, the protocol or file formats?

    If your change revolves around an implementation detail then it is not proper API design. Examples of bad API design would be along the lines of a packet API, an NBT storage API, or basing an enum on implementation values.
    
* Does it result in unexpected behaviour as defined by the Vanilla specification?

    One of  the goals of the Bukkit project is to be an extended Minecraft vanilla server - meaning: if you choose to run the Bukkit server without any plugins, it should function exactly as the Minecraft server would with some rare exceptions. If your change alters the behaviour of the server in such a way that you would not have the same experience as you would in Vanilla, your change does not fit the Bukkit project's goals.

* Does it expose an issue or vulnerability when operating within the Vanilla environment?

    One of the goals of the Bukkit project is to be able to operate within the limitations of the Vanilla environment. If your change results in or exposes the ability to, for example, crash the client when invalid data is set, it does not fit the Bukkit project's needs.
    
If you answered yes to any of these questions, chances are high your change does not fit the Bukkit project's goals and will most likely not be accepted. Regardless, there are a few other important questions you need to ask yourself before you start working on a change:

* Is this change reasonably supportable and maintainable?

* Is this change future proof?

## Making the Changes
* Create a branch on your fork where you'll be making your changes.
    * Name your branch something relevant to the change you are looking to make.
    * Note: if your changes affect both Bukkit and CraftBukkit, it is highly suggested you use the same branch name on both repos.
    * To create a branch in Git;
        * `git branch relevantBranchName`
        * Then checkout the new branch with `git checkout relevantBranchName`
* Check for unnecessary whitespace with `git diff --check` before committing.
* Make sure your code meets [our requirements](#code-requirements).
* If the work you want to do involves editing Minecraft classes, be sure to read over the [Using Minecraft Internals](#using-minecraft-internals) section.
* Make sure your commit messages are in the [proper format](#commit-message-example).
* Test your changes to make sure it actually addresses the issue it should.
* Make sure your code compiles under Java 6, as that is what the project has to be built with.

### Code Requirements
* We generally follow the [Sun/Oracle coding standards](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

* No tabs; use 4 spaces instead.

* No trailing whitespaces.

* No CRLF line endings, LF only, set your Gits 'core.autocrlf' to 'true'.

    These whitespace requirements are easily and often overlooked.  They are critical formatting requirements designed to help simplify a shared heterogeneous development environment.  Learn how your IDE functions in order to show you these characters and verify them.  Analyse the git diff closely to verify every character and if the PR should include the character change.  It is tedious and it is critical.
    
    Eclipse: http://stackoverflow.com/a/11596227/532590  
    NetBeans: http://stackoverflow.com/a/1866385/532590

* No 80 column limit or 'weird' midstatement newlines.

* Any major additions should have documentation ready and provided if applicable (this is usually the case).

* Try to follow test driven development where applicable.

    Bukkit employs JUnit (http://www.vogella.com/articles/JUnit/article.html) for testing and PRs should attempt to integrate with that framework as appropriate.  Bukkit is a large project and what seems simple to a PR author at the time of writing a PR may easily be overlooked later by other authors and updates.  Including unit tests with your PR will help to ensure the PR can be easily maintained over time and encourage the Bukkit Team to pull the PR.
    
* There needs to be a new line at the end of every file.

* Imports should be organised by alphabetical order, separated and grouped by package.

    **For example:**

    ```java
    import java.io.ByteArrayInputStream;
    import java.io.DataInputStream;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.Random;
    import java.util.concurrent.Callable;
    
    // CraftBukkit start
    import java.io.UnsupportedEncodingException;
    import java.util.concurrent.ExecutionException;
    import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
    import java.util.logging.Level;
    import java.util.HashSet;
    
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.craftbukkit.CraftWorld;
    import org.bukkit.craftbukkit.inventory.CraftInventoryView;
    import org.bukkit.craftbukkit.inventory.CraftItemStack;
    import org.bukkit.craftbukkit.util.LazyPlayerSet;
    import org.bukkit.craftbukkit.util.Waitable;
    import org.bukkit.craftbukkit.entity.CraftPlayer;
    import org.bukkit.craftbukkit.event.CraftEventFactory;
    import org.bukkit.entity.Player;
    import org.bukkit.event.Event;
    import org.bukkit.event.block.Action;
    import org.bukkit.event.block.SignChangeEvent;
    import org.bukkit.event.player.AsyncPlayerChatEvent;
    import org.bukkit.event.player.PlayerAnimationEvent;
    import org.bukkit.event.player.PlayerChatEvent;
    import org.bukkit.event.player.PlayerCommandPreprocessEvent;
    import org.bukkit.event.player.PlayerInteractEntityEvent;
    import org.bukkit.event.player.PlayerItemHeldEvent;
    import org.bukkit.event.player.PlayerKickEvent;
    import org.bukkit.event.player.PlayerMoveEvent;
    import org.bukkit.event.player.PlayerTeleportEvent;
    import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
    import org.bukkit.event.player.PlayerToggleSneakEvent;
    import org.bukkit.event.player.PlayerToggleSprintEvent;
    import org.bukkit.event.inventory.*;
    import org.bukkit.event.inventory.InventoryType.SlotType;
    import org.bukkit.event.player.PlayerPortalEvent;
    import org.bukkit.event.player.PlayerToggleFlightEvent;
    import org.bukkit.inventory.CraftingInventory;
    import org.bukkit.inventory.InventoryView;
    // CraftBukkit end
    ```

### Using Minecraft Internals
#### Importing a New Minecraft Class
When contributing to the Bukkit project, you will likely find that you need to edit a Minecraft class that isn't already found within the project. In this case, you need to look at [our mc-dev repository](https://github.com/Bukkit/mc-dev), find the class you need, add it to the CraftBukkit repo and include it in its own special commit separate from your other changes. The commit message of this special commit should simply be "Add x for diff visibility", where x is the name of the file you are adding from mc-dev.

If, however, you need to import multiple files from mc-dev into the Bukkit project, they should all be contained in the same special commit with the commit message "Add files for diff visibility". Note how the commit message no longer specifically mentions any class names.

#### Making Changes to Minecraft Classes
The Bukkit project employs a Minimal Diff policy to help guide when changes should be made to Minecraft classes and what those changes should be. This is to ensure that any changes made have the smallest impact possible on the update process we go through whenever a Minecraft update is released. As well as keeping the Minimal Diff policy in mind, every change made to a Minecraft class needs to be marked as such with the appropriate CraftBukkit comment.

##### Minimal Diff Policy
The Minimal Diff policy is a really important part of the project as it reminds us that every change to the Minecraft Internals has an impact on our update process. When people think of the phrase "minimal diffs", they often take it to the extreme - they go completely out of their way to abstract the changes they are trying to make away from editing Minecraft's classes as much as possible. However, this isn't what we mean by "minimal diffs". Instead, when trying to understand the minimal diffs policy, it helps to keep in mind its end goal: to reduce the impact changes we make to Minecraft's internals have on our update process.

Put simply, the Minimal Diffs Policy simply means to make the smallest change in a Minecraft class possible without duplicating logic.

Here are a few tips you should keep in mind or common areas you should focus on:

* Try to avoid duplicating logic or code when making changes.

* Try to keep your changes easily discernible - don't nest or group several unrelated changes together.

* If you only use an import once within a class, don't import it and use fully qualified names instead.

* Try to employ "short circuiting" of logic if at all possible. This means that you should force a conditional to be the value needed to side-step the code block if you would like to ignore that block of code.

    **For example, to short circuit this:**
    
    ```java
        if (!this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            this.die();
            this.h();
        }
    ```
    **You would do this:**
    
    ```java
        if (false && !this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.die();
            this.h();
        }
    ```

* When adding a validation check, see if the Validate package we already use has a better, more concise method you can use instead.

    **For example, you should use:**
    
    ```java
    Validate.notNull(sender, "Sender cannot be null");
    ```

   **Instead of:**
   
    ```java
    if (sender == null) {
        throw new IllegalArgumentException("Sender cannot be null");
    }
    ```

* When the change you are attempting to make involves removing code, instead of removing it outright, you should comment it out. 

    **For example:**
    
    ```java
    // CraftBukkit start - special case dropping so we can get info from the tile entity
    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (world.random.nextFloat() < f) {
            ItemStack itemstack = new ItemStack(Item.SKULL.id, 1, this.getDropData(world, i, j, k));
            TileEntitySkull tileentityskull = (TileEntitySkull) world.getTileEntity(i, j, k);

            if (tileentityskull.getSkullType() == 3 && tileentityskull.getExtraType() != null && tileentityskull.getExtraType().length() > 0) {
                itemstack.setTag(new NBTTagCompound());
                itemstack.getTag().setString("SkullOwner", tileentityskull.getExtraType());
            }

            this.b(world, i, j, k, itemstack);
        }
    }
    // CraftBukkit end

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        if (entityhuman.abilities.canInstantlyBuild) {
            l |= 8;
            world.setData(i, j, k, l, 4);
        }

        super.a(world, i, j, k, l, entityhuman);
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        if (!world.isStatic) {
            /* CraftBukkit start - drop item in code above, not here
            if ((i1 & 8) == 0) {
                ItemStack itemstack = new ItemStack(Item.SKULL.id, 1, this.getDropData(world, i, j, k));
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

##### General Guidelines
When editing Minecraft's classes, we have a set of rules and guidelines that need to be followed to keep us sane when it comes time for us to update Bukkit.  

**CraftBukkit comments**  
Changes to a Minecraft class should be clearly marked using CraftBukkit comments. Here are a few tips to help explain what kind of CraftBukkit comment to use and where to use them:

* Regardless of what kind of CraftBukkit comment you use, please take care to be explicit and exact with your usage. If the "C" in "CraftBukkit" is capitalised in the example, you should capitalise it when you use it. If the "start" begins with a lowercase "s", you should make sure yours does too.

* If the change only affects one line of code, you should use an end of line CraftBukkit comment.

    **Examples:**
    
    If the change is obvious when looking at the diff, then you just need a simple end of line CraftBukkit comment.
    
    ```java
    if (true || minecraftserver.getAllowNether()) { // CraftBukkit
    ```

    If, however, the change is something important to note or difficult to discern, you should include a reason at the end of the end of line CraftBukkit comment.
    
    ```java
    public int fireTicks; // CraftBukkit - private -> public
    ```
    
    If adding the CraftBukkit comment to the end of the line negatively affects the readability of the code, then you should place the CraftBukkit comment on a new line above the change you made.
    
    ```java
        // CraftBukkit
        if (!isEffect && !world.isStatic && world.difficulty >= 2 && world.areChunksLoaded(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
    ```

* If the change affects more than one line, you should use a multi-line CraftBukkit comment.

    **Examples:**
    
    The majority of the time multi-line changes should be accompanied by a reason since they're usually much more complicated than a single line change. We'd like to suggest you follow the same rule as above: if the change is something important to note or difficult to discern, you should include a reason at the end of the end of line CraftBukkit comment, however it is not always clear if this is the case. Looking through the code in the project, you'll see that we sometimes include a reason when we should have left it off and vice versa.
    
    ```java
    // CraftBukkit start - special case dropping so we can get info from the tile entity
    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (world.random.nextFloat() < f) {
            ItemStack itemstack = new ItemStack(Item.SKULL.id, 1, this.getDropData(world, i, j, k));
            TileEntitySkull tileentityskull = (TileEntitySkull) world.getTileEntity(i, j, k);

            if (tileentityskull.getSkullType() == 3 && tileentityskull.getExtraType() != null && tileentityskull.getExtraType().length() > 0) {
                itemstack.setTag(new NBTTagCompound());
                itemstack.getTag().setString("SkullOwner", tileentityskull.getExtraType());
            }

            this.b(world, i, j, k, itemstack);
        }
    }
    // CraftBukkit end
    ````
    
    Otherwise, just use a multi-line CraftBukkit comment without a reason.
    
    ```java
                // CraftBukkit start
                BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), BlockIgniteEvent.IgniteCause.LIGHTNING, null);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    world.setTypeIdUpdate(i, j, k, Block.FIRE.id);
                }
                // CraftBukkit end
    ```

* CraftBukkit comments should be on the same indentation level of the code block it is in.

    **For example:**
    
    ```java
                        if (j == 1) {
                            // CraftBukkit start - store a reference
                            ItemStack itemstack4 = playerinventory.getCarried();
                            if (itemstack4.count > 0) {
                                entityhuman.drop(itemstack4.a(1));
                            }

                            if (itemstack4.count == 0) {
                                // CraftBukkit end
                                playerinventory.setCarried((ItemStack) null);
                            }
                        }
    ```

**Other guidelines**  

* When adding imports to a Minecraft class, they should be organised by alphabetical order, separated and grouped by package.

    **For example:**

    ```java
    import java.io.ByteArrayInputStream;
    import java.io.DataInputStream;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.Random;
    import java.util.concurrent.Callable;
    
    // CraftBukkit start
    import java.io.UnsupportedEncodingException;
    import java.util.concurrent.ExecutionException;
    import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
    import java.util.logging.Level;
    import java.util.HashSet;
    
    import org.bukkit.Bukkit;
    import org.bukkit.Location;
    import org.bukkit.craftbukkit.CraftWorld;
    import org.bukkit.craftbukkit.inventory.CraftInventoryView;
    import org.bukkit.craftbukkit.inventory.CraftItemStack;
    import org.bukkit.craftbukkit.util.LazyPlayerSet;
    import org.bukkit.craftbukkit.util.Waitable;
    import org.bukkit.craftbukkit.entity.CraftPlayer;
    import org.bukkit.craftbukkit.event.CraftEventFactory;
    import org.bukkit.entity.Player;
    import org.bukkit.event.Event;
    import org.bukkit.event.block.Action;
    import org.bukkit.event.block.SignChangeEvent;
    import org.bukkit.event.player.AsyncPlayerChatEvent;
    import org.bukkit.event.player.PlayerAnimationEvent;
    import org.bukkit.event.player.PlayerChatEvent;
    import org.bukkit.event.player.PlayerCommandPreprocessEvent;
    import org.bukkit.event.player.PlayerInteractEntityEvent;
    import org.bukkit.event.player.PlayerItemHeldEvent;
    import org.bukkit.event.player.PlayerKickEvent;
    import org.bukkit.event.player.PlayerMoveEvent;
    import org.bukkit.event.player.PlayerTeleportEvent;
    import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
    import org.bukkit.event.player.PlayerToggleSneakEvent;
    import org.bukkit.event.player.PlayerToggleSprintEvent;
    import org.bukkit.event.inventory.*;
    import org.bukkit.event.inventory.InventoryType.SlotType;
    import org.bukkit.event.player.PlayerPortalEvent;
    import org.bukkit.event.player.PlayerToggleFlightEvent;
    import org.bukkit.inventory.CraftingInventory;
    import org.bukkit.inventory.InventoryView;
    // CraftBukkit end
    ```

* Do not remove unused imports if they are not marked by CraftBukkit comments.

### Commit Message Example
>   Provide an example commit for CONTRIBUTING.md. Fixes BUKKIT-1
> 
> The CONTRIBUTING.md is missing an example commit message.  Without this 
> commit, we are unable to provide potential contributors with a helpful example, 
> forcing developers to guess at what an acceptable commit message would look 
> like. This commit fixes this issue by providing a clear and informative example 
> for contributors to base their work off of.

### Commit Message Expectations
The first line in a commit message is an imperative statement briefly explaining what the commit is achieving with an associated ticket number from our JIRA, in the form of BUKKIT-#. See the list of acceptable keywords to reference tickets with for more information on this.

The body of the commit message needs to describe how the code behaves without this change, why this is a problem and how this commit addresses it. The body of the commit message should be restricted by a 78 character, plus newline, limit per line (meaning: once you hit about 78 characters, you should explicitly start a new line in the commit message).
    
Acceptable keywords to reference tickets with:

* **Fixes** BUKKIT-1 - this commit fixes the bug detailed in BUKKIT-1
* **Adds** BUKKIT-2 - this commit adds the new feature requested by BUKKIT-2

You can reference multiple tickets in a single commit message, for example: "Fixes BUKKIT-1, BUKKIT-2" or "Adds BUKKIT-1, BUKKIT-2" without closing punctuation.

## Submitting the Changes

* Push your changes to a topic branch in your fork of the repository.
* Submit a pull request to the relevant repository in the Bukkit organization.
    * Make sure your pull request meets [our expectations](#pull-request-formatting-expectations) before submitting.
    * No merges should be included in any pull requests.
* Update your JIRA ticket to reflect that you have submitted a pull request and are ready for it to be reviewed.
    * Include a link to the pull request in the ticket.
* Follow our [Tips to Get Your Pull Request Accepted](#tips-to-get-your-pull-request-accepted).
* **Note:** The project is put under a code freeze leading up to the release of a Minecraft update in order to give the Bukkit team a static code base to work on.
  
### Pull Request Formatting Expectations
#### Title
> [PR Type] Brief summary. Fixes BUKKIT-####  
> PR Type can be B for Bukkit, C for CraftBukkit, B+C for a PR in both sides
> 
> Title Example:  
> [B+C] Provide an example commit for CONTRIBUTING.md. Fixes BUKKIT-1

#### Description:  
> ##### The Issue:
> Paragraphs explaining what the issue the PR is meant to be addressing.
> 
> ##### Justification for this PR:
> Paragraphs providing justification for the PR
>
> ##### PR Breakdown:
> Paragraphs breaking down what the PR is doing, in detail.
> 
> ##### Testing Results and Materials:
> Paragraphs describing what you did to test the code in this PR and links to pre-compiled test binaries and source.
>
> ##### Relevant PR(s): 
> This should be links to accompanying PRs, or alternate PRs that attempted to perform the task.  Each reference should have a reason attached as to why it is being referenced (for example: "Similar to PR ### but won't empty your Bukkits").  Accompanying PRs need no explanation, but still need to be linked.
> 
> B-#### - https://github.com/Bukkit/Bukkit/pull/#### - Reason  
> CB-#### - https://github.com/Bukkit/CraftBukkit/pull/#### - Reason
> 
> ##### JIRA Ticket: 
> BUKKIT-#### - https://bukkit.atlassian.net/browse/BUKKIT-####
>
> ##### Pull Request Check List (For Your Use):
>
>**General:**
>
>- [ ] Fits Bukkit's Goals
>- [ ] Leaky Ticket Ready
>- [ ] Code Meets Requirements
>- [ ] Code is Documented
>- [ ] Code Addresses Leaky Ticket
>- [ ] Followed Pull Request Format
>- [ ] Tested Code
>- [ ] Included Test Material and Source
> 
>**If Applicable:**
>
>- [ ] Importing New Minecraft Classes In Special Commit
>- [ ] Follows Minimal Diff Policy
>- [ ] Uses Proper CraftBukkit Comments
>- [ ] Imports Are Ordered, Separated and Organised Properly

### Tips to Get Your Pull Request Accepted
Making sure you follow the above conventions is important, but just the beginning. Follow these tips to better the chances of your pull request being accepted and pulled.

* Your change should [fit with Bukkit's goals](#does-the-change-fit-bukkits-goals).
* Make sure you follow all of our conventions to the letter.
* Make sure your code compiles under Java 6.
* Check for misplaced whitespaces. It may be invisible, but [we notice](https://github.com/Bukkit/CraftBukkit/pull/1070).
* Provide proper JavaDocs where appropriate.
    * JavaDocs should detail every limitation, caveat and gotcha the code has.
* Provide proper accompanying documentation where appropriate.
* Test your code and provide testing material.
    * For example: adding an event? Test it with a test plugin and provide us with that plugin and its source.
* Make sure to follow coding best practises.
* Your pull request should adhere to our [Pull Request Formatting Expectations](#pull-request-formatting-expectations).
* **Note:** The project is put under a code freeze leading up to the release of a Minecraft update in order to give the Bukkit team a static code base to work on.

## Useful Resources
* [An example pull request demonstrating the things we look out for](https://github.com/Bukkit/CraftBukkit/pull/1070)
* [Handy gist version of our Pull Request Format Template](https://gist.github.com/EvilSeph/35bb477eaa1dffc5f1d7)
* [More information on contributing](http://wiki.bukkit.org/Getting_Involved)
* [Leaky, Our Issue Tracker (JIRA)](http://leaky.bukkit.org)
* [General GitHub documentation](http://help.github.com/)
* [GitHub pull request documentation](http://help.github.com/send-pull-requests/)
* [Join us on IRC - #bukkitdev @ irc.esper.net](http://wiki.bukkit.org/IRC)
