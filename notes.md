- Cookie handling will stall the connection when needed to prevent the connection from finishing before the cookie has been received.
  - A player can still get timed out while waiting for a cookie.

Previously, the cookie handling for login occurred after the PlayerLoginEvent only. This meant that cookies could not be sent before the player was created.
New Use Cases:
- Instantly terminate the connection if a cookie is not present.

Behavior changes:
- PlayerLinksSendEvent now takes a configuration connection
- PlayerLoginEvent no longer blocks cookies
- PlayerLoginEvent now triggers twice due to reasons 
- Disconnecting a duplicate player no longer causes a forced save. Check to make sure that still occurs

TODO:
- Figure out connection hierarchy 
- Figure out naming

New Events:
PlayerServerFullCheckEvent - allows you to make players bypass the server fullness check without using PlayerLoginEvent

Protocol Lifecycles:
LOGIN:
    entering login: AsyncPlayerPreLoginEvent
    validating login: PlayerConnectionValidateLoginEvent (basically PlayerLoginEvent)
    exiting login: PlayerFinishConnectionPhaseEvent (allows you to disconnect to prevent a ClientboundLoginFinishedPacket from being sent)
CONFIGURATION:
    entering config: PlayerConnectionInitialConfigureEvent / PlayerConnectionReconfigurateEvent (split for convenience)
    configuring player: AsyncPlayerConnectionConfigureEvent (allows for async processing, can run for as long as needed)
    validating login: PlayerConnectionValidateLoginEvent  (basically PlayerLoginEvent)
    exiting config: PlayerFinishConnectionPhaseEvent (allows you to disconnect to prevent a player being placed/created)



