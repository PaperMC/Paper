#### compile

To compile Paper, you need __JDK 8__, __maven__, and an internet connection. Clone this repo, run `./paper jar` from __
bash__, grab `paperclip.jar`.

#### build jar

(in project root!)  
`./paper jar`

#### create a patch

move to `./Paper-Server` or `./Paper-API`  
work on files in (probably) `Paper-Server`  
to finalize patch  
(in the sub repo folder!)  
`git add .`  
`git commit`

#### convert committed changes to patch

(in project root!)  
`./paper rebuild`  
`./paper jar`

#### run test server

(in project root!)  
`./paper test`

#### prepare pull request

when everything is fine:  
(in project root!)  
`git checkout -b antigravity-generator-patch`  
`git add Spigot-Server-Patches/0420-antigravity-generator-patch.patch`  
`git commit`  
`git push --set-upstream origin antigravity-generator-patch`

#### edit patches (continue working on saved state)

(in project root!)  
run `./paper edit server`, for the patch you want to edit, replace the prefix `pick` with `edit`.  
save and close the file, `./Paper-Server` is prepared now.  
do code changes and when done, run: `./paper edit continue`  
see [CONTRIBUTING.md](https://github.com/nothub/paper-1.12.2/blob/master/CONTRIBUTING.md) Modifying Patches -> Method 1

#### patch untouched vanilla code

if you want to patch code that is not yet listed in the Paper-Server subrepo:  
go to `./work/Minecraft/1.12.2/net/minecraft/server`, check for the thing that need patching  
open `./scripts/importmcdev.sh` and add the corresponding import to the list  
in project root, run: `./paper patch`  
the needed classes should be listed in `./Paper-Server` now  
for everything advanced, read [CONTRIBUTING.md](https://github.com/nothub/paper-1.12.2/blob/master/CONTRIBUTING.md)

#### sha1 information is lacking or useless / could not build fake ancestor

```
error: sha1 information is lacking or useless (src/main/java/net/minecraft/server/PathfinderGoalSelector.java).
error: could not build fake ancestor
hint: Use 'git am --show-current-patch=diff' to see the failed patch
Patch failed at 0153 configurable pathfinder goal selection delay
When you have resolved this problem, run "git am --continue".
If you prefer to skip this patch, run "git am --skip" instead.
To restore the original branch and stop patching, run "git am --abort".
  Something did not apply cleanly to Paper-Server.
  Please review above details and finish the apply then
  save the changes with rebuildPatches.sh

  Because you're on Windows you'll need to finish the AM,
  rebuild all patches, and then re-run the patch apply again.
  Consider using the scripts with Windows Subsystem for Linux.
Failed to apply Paper Patches
Failed to build Paper
```

an error with this message (sha1 information is lacking or useless / could not build fake ancestor) is most likely
caused by a missing import in `./scripts/importmcdev.sh`.

for further info, read: [CONTRIBUTING.md](https://github.com/nothub/paper-1.12.2/blob/master/CONTRIBUTING.md)
