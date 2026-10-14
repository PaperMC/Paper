package io.papermc.paper.command.brigadier.argument.resolvers;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;
import org.jetbrains.annotations.ApiStatus;

/**
 * An {@link ArgumentResolver} that's capable of resolving
 *  argument value using a {@link CommandSourceStack}.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#playerProfiles()
 */
@ApiStatus.NonExtendable
public interface PlayerProfileListResolver extends ArgumentResolver<Collection<PlayerProfile>> {
}
