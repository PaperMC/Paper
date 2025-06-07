package io.papermc.paper.command.brigadier;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link CommandRegistrationFlag} is used in {@link Commands} registration for internal purposes.
 * <p>
 * A command library may use this to achieve more specific customization on how their commands are registered.
 * @apiNote Stability of these flags is not promised! This api is not intended for public use.
 */
@ApiStatus.Internal
public enum CommandRegistrationFlag {

    /**
     * @deprecated This is the default behavior now.
     */
    @Deprecated(since = "1.21.4")
    FLATTEN_ALIASES,
    /**
     * @deprecated Removed as it causes a warning to appear on the client now.
     */
    @Deprecated(since = "1.21.6", forRemoval = true)
    SERVER_ONLY
}
