Creating a Pull Request
-----------------------
##### Pull Request Title Example
The first line in a Pull Request(PR) message is an imperative statement briefly explaining what the PR is achieving.
If the PR fixes a bug, or implements a new feature as requested from the [JIRA](http://hub.spigotmc.org/jira/), then it should reference that ticket.
This is accomplished by simply type SPIGOT-####, where #### is the ticket number. (i.e. SPIGOT-3510).
You can reference multiple tickets in a single commit message; for example: "SPIGOT-1, SPIGOT-2" without the quotes.

__For Example:__
* SPIGOT-3510: Velocity broken for certain entities
* MC-111753, SPIGOT-2971: Brewing stand not reloading

As you can see, Minecraft tickets can be referenced by including the appropriate ticket number (i.e. MC-111753).
Where a pull request is spread across multiple repositories, please keep the title and first line of the commit message the same for each.

##### Pull Request Message Expectations
The body of a PR needs to describe how the ticket was resolved, or if there was no ticket, describe the problem itself.
If a PR is for both Bukkit and CraftBukkit it should include a link to the appropriate PR. [Read this to learn how to link to a PR.](https://confluence.atlassian.com/bitbucketserver053/markdown-syntax-guide-938022413.html?utm_campaign=in-app-help&utm_medium=in-app-help&utm_source=stash#Markdownsyntaxguide-Linkingtopullrequests)

##### Submitting the Changes
* Push your changes to a topic branch in your fork of the repository.
* Submit a pull request to the relevant repository in the Spigot organization.
    * Make sure your pull request meets our [code requirements.](README.md)
* If you are fixing a JIRA ticket, be sure to update the ticket with a link to the pull request.
    * All bug fixes must be accompanied by a JIRA ticket.
* Ensure all changes (including in patches) have our Minimal Diff Policy in mind.

##### Pull Request Feedback
Spigot has a lot of Pull Requests open. Some of these are old and no longer maintained. As a general rule, we do not delete PRs because they are old or abandoned.

You will eventually receive feedback on your Pull Requests. If the criticism is rough, do not take it personally. We have all started out, or just made bad PRs.
Take the advice and use it to make yourself better. If you think someone is being malicious contact an [admin](https://www.spigotmc.org/XenStaff/#Administrator).

Feedback goes both ways. If you believe a feature should be present then be prepared to argue your case.
Per our Code Requirements, formatting issues are almost always required to be fixed before a PR is accepted.

Although we have many discussions on Stash, there are also many discussions in the #spigot-dev channel of IRC.
Join us there if you plan on contributing to Spigot!

##### Tips to Get Your Pull Request Accepted
Making sure to follow the conventions!

* Your change should fit with Bukkit's goals.
* Make sure you follow our conventions to the letter.
* Check for formatting errors. They may be invisible, but [we notice.](https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/pull-requests/298/diff)
* Provide proper JavaDocs where appropriate.
    * JavaDocs should detail every limitation, caveat, and gotcha the code has.
* Provide proper accompanying documentation where appropriate.
* Test your code and provide adequate testing material and/or proof.
    * For example: adding an event? Test it with a plugin and provide us with the source.
* Make sure you follow our conventions to the letter.

__Note:__ The project is often on a code freeze leading up to the release of a Minecraft update in order to give the team a static code base to work with.
