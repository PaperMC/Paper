package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

/**
 * The registrar for custom commands. Supports Brigadier commands and {@link BasicCommand}.
 * <p>
 * An example of a command being registered is below
 * <pre>{@code
 * class YourPluginClass extends JavaPlugin {
 *
 *     @Override
 *     public void onEnable() {
 *         LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
 *         manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
 *             final Commands commands = event.registrar();
 *             commands.register(
 *                 Commands.literal("new-command")
 *                     .executes(ctx -> {
 *                         ctx.getSource().getSender().sendPlainMessage("some message");
 *                         return Command.SINGLE_SUCCESS;
 *                     })
 *                     .build(),
 *                 "some bukkit help description string",
 *                 List.of("an-alias")
 *             );
 *         });
 *     }
 * }
 * }</pre>
 * <p>
 * You can also register commands in {@link PluginBootstrap} by getting the {@link LifecycleEventManager} from
 * {@link BootstrapContext}.
 * Commands registered in the {@link PluginBootstrap} will be available for datapack's
 * command function parsing.
 * Note that commands registered via {@link PluginBootstrap} with the same literals as a vanilla command will override
 * that command within all loaded datapacks.
 * </p>
 * <p>The {@code register} methods that <b>do not</b> have {@link PluginMeta} as a parameter will
 * implicitly use the {@link PluginMeta} for the plugin that the {@link io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler}
 * was registered with.</p>
 *
 * @see io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents#COMMANDS
 */
@ApiStatus.NonExtendable
public interface Commands extends Registrar {

    /**
     * Utility to create a literal command node builder with the correct generic.
     *
     * @param literal literal name
     * @return a new builder instance
     */
    static LiteralArgumentBuilder<CommandSourceStack> literal(final String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    /**
     * Utility to create a required argument builder with the correct generic.
     *
     * @param name         the name of the argument
     * @param argumentType the type of the argument
     * @param <T>          the generic type of the argument value
     * @return a new required argument builder
     */
    static <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(final String name, final ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    /**
     * Creates a restricted {@link Predicate} that wraps the given predicate.
     * <p>
     * A restricted predicate prevents execution in unattended contexts, such as from
     * chat click events. A warning is shown on the client before executing the command.
     * <p>
     * This is used by vanilla to prevent invocation of sensitive commands (like op) from
     * players without their knowledge.
     *
     * @param predicate the original predicate to wrap
     * @return a new predicate with restricted execution behavior
     */
    static Predicate<CommandSourceStack> restricted(final Predicate<CommandSourceStack> predicate) {
        return InternalAPIBridge.get().restricted(predicate);
    }

    /**
     * Gets the underlying {@link CommandDispatcher}.
     *
     * <p><b>Note:</b> This is a delicate API that must be used with care to ensure a consistent user experience.</p>
     *
     * <p>When registering commands, it should be preferred to use the {@link #register(PluginMeta, LiteralCommandNode, String, Collection) register methods}
     * over directly registering to the dispatcher wherever possible.
     * {@link #register(PluginMeta, LiteralCommandNode, String, Collection) Register methods} automatically handle
     * command namespacing, command help, plugin association with commands, and more.</p>
     *
     * <p>Example use cases for this method <b>may</b> include:
     * <ul>
     *   <li>Implementing integration between an external command framework and Paper (although {@link #register(PluginMeta, LiteralCommandNode, String, Collection) register methods} should still be preferred where possible)</li>
     *   <li>Registering new child nodes to an existing plugin command (for example an "addon" plugin to another plugin may want to do this)</li>
     *   <li>Retrieving existing command nodes to build redirects</li>
     * </ul>
     *
     * @return the dispatcher instance
     */
    @ApiStatus.Experimental
    CommandDispatcher<CommandSourceStack> getDispatcher();

    /**
     * Registers a command for the current plugin context.
     *
     * <p>Commands have certain overriding behavior:
     * <ul>
     *   <li>Aliases will not override already existing commands (excluding namespaced ones)</li>
     *   <li>Aliases are <b>not</b> Brigadier redirects, they just copy the command to a different label</li>
     *   <li>The main command/namespaced label will override already existing commands</li>
     * </ul>
     *
     * @param node the built literal command node
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final LiteralCommandNode<CommandSourceStack> node) {
        return this.register(node, null, Collections.emptyList());
    }

    /**
     * Registers a command for the current plugin context.
     *
     * <p>Commands have certain overriding behavior:
     * <ul>
     *   <li>Aliases will not override already existing commands (excluding namespaced ones)</li>
     *   <li>Aliases are <b>not</b> Brigadier redirects, they just copy the command to a different label</li>
     *   <li>The main command/namespaced label will override already existing commands</li>
     * </ul>
     *
     * @param node        the built literal command node
     * @param description the help description for the root literal node
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final LiteralCommandNode<CommandSourceStack> node, final @Nullable String description) {
        return this.register(node, description, Collections.emptyList());
    }

    /**
     * Registers a command for the current plugin context.
     *
     * <p>Commands have certain overriding behavior:
     * <ul>
     *   <li>Aliases will not override already existing commands (excluding namespaced ones)</li>
     *   <li>Aliases are <b>not</b> Brigadier redirects, they just copy the command to a different label</li>
     *   <li>The main command/namespaced label will override already existing commands</li>
     * </ul>
     *
     * @param node the built literal command node
     * @param aliases a collection of aliases to register the literal node's command to
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final LiteralCommandNode<CommandSourceStack> node, final Collection<String> aliases) {
        return this.register(node, null, aliases);
    }

    /**
     * Registers a command for the current plugin context.
     *
     * <p>Commands have certain overriding behavior:
     * <ul>
     *   <li>Aliases will not override already existing commands (excluding namespaced ones)</li>
     *   <li>Aliases are <b>not</b> Brigadier redirects, they just copy the command to a different label</li>
     *   <li>The main command/namespaced label will override already existing commands</li>
     * </ul>
     *
     * @param node        the built literal command node
     * @param description the help description for the root literal node
     * @param aliases     a collection of aliases to register the literal node's command to
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    @Unmodifiable Set<String> register(LiteralCommandNode<CommandSourceStack> node, @Nullable String description, Collection<String> aliases);

    /**
     * Registers a command for a plugin.
     *
     * <p>Commands have certain overriding behavior:
     * <ul>
     *   <li>Aliases will not override already existing commands (excluding namespaced ones)</li>
     *   <li>Aliases are <b>not</b> Brigadier redirects, they just copy the command to a different label</li>
     *   <li>The main command/namespaced label will override already existing commands</li>
     * </ul>
     *
     * @param pluginMeta  the owning plugin's meta
     * @param node        the built literal command node
     * @param description the help description for the root literal node
     * @param aliases     a collection of aliases to register the literal node's command to
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    @Unmodifiable Set<String> register(PluginMeta pluginMeta, LiteralCommandNode<CommandSourceStack> node, @Nullable String description, Collection<String> aliases);

    /**
     * This allows configuring the registration of your command, which is not intended for public use.
     * See {@link Commands#register(PluginMeta, LiteralCommandNode, String, Collection)} for more information.
     *
     * @param pluginMeta  the owning plugin's meta
     * @param node        the built literal command node
     * @param description the help description for the root literal node
     * @param aliases     a collection of aliases to register the literal node's command to
     * @param flags       a collection of registration flags that control registration behaviour.
     * @return successfully registered root command labels (including aliases and namespaced variants)
     *
     * @apiNote This method is not guaranteed to be stable as it is not intended for public use.
     * See {@link CommandRegistrationFlag} for a more indepth explanation of this method's use-case.
     */
    @ApiStatus.Internal
    @Unmodifiable Set<String> registerWithFlags(PluginMeta pluginMeta, LiteralCommandNode<CommandSourceStack> node, @Nullable String description, Collection<String> aliases, Set<CommandRegistrationFlag> flags);

    /**
     * Registers a command under the same logic as {@link Commands#register(LiteralCommandNode, String, Collection)}.
     *
     * @param label        the label of the to-be-registered command
     * @param basicCommand the basic command instance to register
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final String label, final BasicCommand basicCommand) {
        return this.register(label, null, Collections.emptyList(), basicCommand);
    }

    /**
     * Registers a command under the same logic as {@link Commands#register(LiteralCommandNode, String, Collection)}.
     *
     * @param label        the label of the to-be-registered command
     * @param description  the help description for the root literal node
     * @param basicCommand the basic command instance to register
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final String label, final @Nullable String description, final BasicCommand basicCommand) {
        return this.register(label, description, Collections.emptyList(), basicCommand);
    }

    /**
     * Registers a command under the same logic as {@link Commands#register(LiteralCommandNode, String, Collection)}.
     *
     * @param label        the label of the to-be-registered command
     * @param aliases      a collection of aliases to register the basic command under.
     * @param basicCommand the basic command instance to register
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    default @Unmodifiable Set<String> register(final String label, final Collection<String> aliases, final BasicCommand basicCommand) {
        return this.register(label, null, aliases, basicCommand);
    }

    /**
     * Registers a command under the same logic as {@link Commands#register(LiteralCommandNode, String, Collection)}.
     *
     * @param label        the label of the to-be-registered command
     * @param description  the help description for the root literal node
     * @param aliases      a collection of aliases to register the basic command under.
     * @param basicCommand the basic command instance to register
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    @Unmodifiable Set<String> register(String label, @Nullable String description, Collection<String> aliases, BasicCommand basicCommand);

    /**
     * Registers a command under the same logic as {@link Commands#register(PluginMeta, LiteralCommandNode, String, Collection)}.
     *
     * @param pluginMeta   the owning plugin's meta
     * @param label        the label of the to-be-registered command
     * @param description  the help description for the root literal node
     * @param aliases      a collection of aliases to register the basic command under.
     * @param basicCommand the basic command instance to register
     * @return successfully registered root command labels (including aliases and namespaced variants)
     */
    @Unmodifiable Set<String> register(PluginMeta pluginMeta, String label, @Nullable String description, Collection<String> aliases, BasicCommand basicCommand);
}
