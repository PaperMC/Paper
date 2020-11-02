# Shit to check

* Mini: "Optimize World Server Map": Figure out how to fill PaperWorldMap, it needs a dim key which doesnt exist anymore?
* Mini: "MC-50319": fix if still works
* Make sure the flat bedrock setting doesn't do anything stupid
* Check DataBits foreach
* lighting is bork (load chunk, fly away, come back, everything or parts are black)
* chunk generation seems slow with a lot of it happening
* Fix IDE Debug JVM Flag for new versions of minecraft

* Check if `PlayerEditBookEvent`: https://github.com/PaperMC/Paper/pull/1751  
The PlayerEditBookEvent is straight up not called anymore.  
The method to call it must now be `PlayerConnection#a(List<String>, int)` (CB bug).  
The item is presumably edited with the new page contents before it ever reaches this method?  