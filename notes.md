- Cookie handling is now async on login + works better, able to be requested at any stage.

Previously, the cookie handling for login occurred after the PlayerLoginEvent only. This meant that cookies could not be sent before the player was created.
New Use Cases:
- Instantly terminate the connection if a cookie is not present.

Behavior changes:
- PlayerLoginEvent no longer blocks cookies
- PlayerLoginEvent now triggers twice due to reasons 
- Disconnecting a duplicate player no longer causes a forced save. Check to make sure that still occurs

TODO:
- PlayerLinksSendEvent
- Figure out connection hierarchy 
- Figure out naming
