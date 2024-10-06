SPIGOT-10280: Implement EntityRemoveFromWorldEvent for enhanced entity removal tracking
Pull Request Message Expectations
This PR introduces the EntityRemoveFromWorldEvent class, designed to handle entity removal events within the Bukkit API, providing enhanced tracking and flexibility.

Summary of Changes:
Added EntityRemoveFromWorldEvent class for monitoring entity removal.
Defined an enum Cause representing various reasons for entity removal, including:
DEATH
DESPAWN
DROP
ENTER_BLOCK
EXPLODE
HIT
MERGE
OUT_OF_WORLD
PICKUP
PLUGIN
TRANSFORMATION
UNLOAD
OTHER
Included an inner EntityRemoveEvent class to handle entity removal actions with a trigger method.
Added an EntityRemoveListener class for event handling.
Implemented an EntityManager class to manage and remove entities, dispatching removal events.
Submitting the Changes
Push your changes to a topic branch in your fork of the repository.
Submit a pull request to the relevant repository in the Spigot organization.
Make sure your pull request meets our code requirements.
If you are fixing a JIRA ticket, be sure to update the ticket with a link to the pull request.
All bug fixes must be accompanied by a JIRA ticket.
Ensure all changes (including in patches) have our Minimal Diff Policy in mind.
