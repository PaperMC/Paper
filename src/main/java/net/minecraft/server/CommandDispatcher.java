package net.minecraft.server;

import com.google.common.collect.Maps;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import com.google.common.base.Joiner;
import java.util.Collection;
import java.util.LinkedHashSet;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.server.ServerCommandEvent;
// CraftBukkit end

public class CommandDispatcher {

    private static final Logger LOGGER = LogManager.getLogger();
    private final com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> b = new com.mojang.brigadier.CommandDispatcher();

    public CommandDispatcher(CommandDispatcher.ServerType commanddispatcher_servertype) {
        this(); // CraftBukkit
        CommandAdvancement.a(this.b);
        CommandAttribute.a(this.b);
        CommandExecute.a(this.b);
        CommandBossBar.a(this.b);
        CommandClear.a(this.b);
        CommandClone.a(this.b);
        CommandData.a(this.b);
        CommandDatapack.a(this.b);
        CommandDebug.a(this.b);
        CommandGamemodeDefault.a(this.b);
        CommandDifficulty.a(this.b);
        CommandEffect.a(this.b);
        CommandMe.a(this.b);
        CommandEnchant.a(this.b);
        CommandXp.a(this.b);
        CommandFill.a(this.b);
        CommandForceload.a(this.b);
        CommandFunction.a(this.b);
        CommandGamemode.a(this.b);
        CommandGamerule.a(this.b);
        CommandGive.a(this.b);
        CommandHelp.a(this.b);
        CommandKick.a(this.b);
        CommandKill.a(this.b);
        CommandList.a(this.b);
        CommandLocate.a(this.b);
        CommandLocateBiome.a(this.b);
        CommandLoot.a(this.b);
        CommandTell.a(this.b);
        CommandParticle.a(this.b);
        CommandPlaySound.a(this.b);
        CommandReload.a(this.b);
        CommandRecipe.a(this.b);
        CommandReplaceItem.a(this.b);
        CommandSay.a(this.b);
        CommandSchedule.a(this.b);
        CommandScoreboard.a(this.b);
        CommandSeed.a(this.b, commanddispatcher_servertype != CommandDispatcher.ServerType.INTEGRATED);
        CommandSetBlock.a(this.b);
        CommandSpawnpoint.a(this.b);
        CommandSetWorldSpawn.a(this.b);
        CommandSpectate.a(this.b);
        CommandSpreadPlayers.a(this.b);
        CommandStopSound.a(this.b);
        CommandSummon.a(this.b);
        CommandTag.a(this.b);
        CommandTeam.a(this.b);
        CommandTeamMsg.a(this.b);
        CommandTeleport.a(this.b);
        CommandTellRaw.a(this.b);
        CommandTime.a(this.b);
        CommandTitle.a(this.b);
        CommandTrigger.a(this.b);
        CommandWeather.a(this.b);
        CommandWorldBorder.a(this.b);
        if (SharedConstants.d) {
            GameTestHarnessTestCommand.a(this.b);
        }

        if (commanddispatcher_servertype.e) {
            CommandBanIp.a(this.b);
            CommandBanList.a(this.b);
            CommandBan.a(this.b);
            CommandDeop.a(this.b);
            CommandOp.a(this.b);
            CommandPardon.a(this.b);
            CommandPardonIP.a(this.b);
            CommandSaveAll.a(this.b);
            CommandSaveOff.a(this.b);
            CommandSaveOn.a(this.b);
            CommandIdleTimeout.a(this.b);
            CommandStop.a(this.b);
            CommandWhitelist.a(this.b);
        }

        if (commanddispatcher_servertype.d) {
            CommandPublish.a(this.b);
        }

        this.b.findAmbiguities((commandnode, commandnode1, commandnode2, collection) -> {
            // CommandDispatcher.LOGGER.warn("Ambiguity between arguments {} and {} with inputs: {}", this.b.getPath(commandnode1), this.b.getPath(commandnode2), collection); // CraftBukkit
        });
    }

    // CraftBukkit start
    public CommandDispatcher() {
        this.b.setConsumer((commandcontext, flag1, i) -> {
            ((CommandListenerWrapper) commandcontext.getSource()).a(commandcontext, flag1, i);
        });
    }

    public int dispatchServerCommand(CommandListenerWrapper sender, String command) {
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandEvent event = new ServerCommandEvent(sender.getBukkitSender(), command);
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return 0;
        }
        command = event.getCommand();

        String[] args = command.split(" ");

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        if (sender.getWorld().getServer().getCommandBlockOverride(args[0])) {
            args[0] = "minecraft:" + args[0];
        }

        return this.a(sender, joiner.join(args));
    }

    public int a(CommandListenerWrapper commandlistenerwrapper, String s) {
        return this.a(commandlistenerwrapper, s, s);
    }

    public int a(CommandListenerWrapper commandlistenerwrapper, String s, String label) {
        // CraftBukkit end
        StringReader stringreader = new StringReader(s);

        if (stringreader.canRead() && stringreader.peek() == '/') {
            stringreader.skip();
        }

        commandlistenerwrapper.getServer().getMethodProfiler().enter(s);

        byte b0;

        try {
            byte b1;

            try {
                int i = this.b.execute(stringreader, commandlistenerwrapper);

                return i;
            } catch (CommandException commandexception) {
                commandlistenerwrapper.sendFailureMessage(commandexception.a());
                b1 = 0;
                return b1;
            } catch (CommandSyntaxException commandsyntaxexception) {
                commandlistenerwrapper.sendFailureMessage(ChatComponentUtils.a(commandsyntaxexception.getRawMessage()));
                if (commandsyntaxexception.getInput() != null && commandsyntaxexception.getCursor() >= 0) {
                    int j = Math.min(commandsyntaxexception.getInput().length(), commandsyntaxexception.getCursor());
                    IChatMutableComponent ichatmutablecomponent = (new ChatComponentText("")).a(EnumChatFormat.GRAY).format((chatmodifier) -> {
                        return chatmodifier.setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, label)); // CraftBukkit
                    });

                    if (j > 10) {
                        ichatmutablecomponent.c("...");
                    }

                    ichatmutablecomponent.c(commandsyntaxexception.getInput().substring(Math.max(0, j - 10), j));
                    if (j < commandsyntaxexception.getInput().length()) {
                        IChatMutableComponent ichatmutablecomponent1 = (new ChatComponentText(commandsyntaxexception.getInput().substring(j))).a(new EnumChatFormat[]{EnumChatFormat.RED, EnumChatFormat.UNDERLINE});

                        ichatmutablecomponent.addSibling(ichatmutablecomponent1);
                    }

                    ichatmutablecomponent.addSibling((new ChatMessage("command.context.here")).a(new EnumChatFormat[]{EnumChatFormat.RED, EnumChatFormat.ITALIC}));
                    commandlistenerwrapper.sendFailureMessage(ichatmutablecomponent);
                }

                b1 = 0;
                return b1;
            } catch (Exception exception) {
                ChatComponentText chatcomponenttext = new ChatComponentText(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());

                if (CommandDispatcher.LOGGER.isDebugEnabled()) {
                    CommandDispatcher.LOGGER.error("Command exception: {}", s, exception);
                    StackTraceElement[] astacktraceelement = exception.getStackTrace();

                    for (int k = 0; k < Math.min(astacktraceelement.length, 3); ++k) {
                        chatcomponenttext.c("\n\n").c(astacktraceelement[k].getMethodName()).c("\n ").c(astacktraceelement[k].getFileName()).c(":").c(String.valueOf(astacktraceelement[k].getLineNumber()));
                    }
                }

                commandlistenerwrapper.sendFailureMessage((new ChatMessage("command.failed")).format((chatmodifier) -> {
                    return chatmodifier.setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_TEXT, chatcomponenttext));
                }));
                if (SharedConstants.d) {
                    commandlistenerwrapper.sendFailureMessage(new ChatComponentText(SystemUtils.d(exception)));
                    CommandDispatcher.LOGGER.error("'" + s + "' threw an exception", exception);
                }

                b0 = 0;
            }
        } finally {
            commandlistenerwrapper.getServer().getMethodProfiler().exit();
        }

        return b0;
    }

    public void a(EntityPlayer entityplayer) {
        // CraftBukkit start
        // Register Vanilla commands into builtRoot as before
        Map<CommandNode<CommandListenerWrapper>, CommandNode<ICompletionProvider>> map = Maps.newIdentityHashMap(); // Use identity to prevent aliasing issues
        RootCommandNode vanillaRoot = new RootCommandNode();

        RootCommandNode<CommandListenerWrapper> vanilla = entityplayer.server.vanillaCommandDispatcher.a().getRoot();
        map.put(vanilla, vanillaRoot);
        this.a(vanilla, vanillaRoot, entityplayer.getCommandListener(), (Map) map);

        // Now build the global commands in a second pass
        RootCommandNode<ICompletionProvider> rootcommandnode = new RootCommandNode();

        map.put(this.b.getRoot(), rootcommandnode);
        this.a(this.b.getRoot(), rootcommandnode, entityplayer.getCommandListener(), (Map) map);

        Collection<String> bukkit = new LinkedHashSet<>();
        for (CommandNode node : rootcommandnode.getChildren()) {
            bukkit.add(node.getName());
        }

        PlayerCommandSendEvent event = new PlayerCommandSendEvent(entityplayer.getBukkitEntity(), new LinkedHashSet<>(bukkit));
        event.getPlayer().getServer().getPluginManager().callEvent(event);

        // Remove labels that were removed during the event
        for (String orig : bukkit) {
            if (!event.getCommands().contains(orig)) {
                rootcommandnode.removeCommand(orig);
            }
        }
        // CraftBukkit end
        entityplayer.playerConnection.sendPacket(new PacketPlayOutCommands(rootcommandnode));
    }

    private void a(CommandNode<CommandListenerWrapper> commandnode, CommandNode<ICompletionProvider> commandnode1, CommandListenerWrapper commandlistenerwrapper, Map<CommandNode<CommandListenerWrapper>, CommandNode<ICompletionProvider>> map) {
        Iterator iterator = commandnode.getChildren().iterator();

        while (iterator.hasNext()) {
            CommandNode<CommandListenerWrapper> commandnode2 = (CommandNode) iterator.next();

            if (commandnode2.canUse(commandlistenerwrapper)) {
                ArgumentBuilder argumentbuilder = commandnode2.createBuilder(); // CraftBukkit - decompile error

                argumentbuilder.requires((icompletionprovider) -> {
                    return true;
                });
                if (argumentbuilder.getCommand() != null) {
                    argumentbuilder.executes((commandcontext) -> {
                        return 0;
                    });
                }

                if (argumentbuilder instanceof RequiredArgumentBuilder) {
                    RequiredArgumentBuilder<ICompletionProvider, ?> requiredargumentbuilder = (RequiredArgumentBuilder) argumentbuilder;

                    if (requiredargumentbuilder.getSuggestionsProvider() != null) {
                        requiredargumentbuilder.suggests(CompletionProviders.b(requiredargumentbuilder.getSuggestionsProvider()));
                    }
                }

                if (argumentbuilder.getRedirect() != null) {
                    argumentbuilder.redirect((CommandNode) map.get(argumentbuilder.getRedirect()));
                }

                CommandNode commandnode3 = argumentbuilder.build(); // CraftBukkit - decompile error

                map.put(commandnode2, commandnode3);
                commandnode1.addChild(commandnode3);
                if (!commandnode2.getChildren().isEmpty()) {
                    this.a(commandnode2, commandnode3, commandlistenerwrapper, map);
                }
            }
        }

    }

    public static LiteralArgumentBuilder<CommandListenerWrapper> a(String s) {
        return LiteralArgumentBuilder.literal(s);
    }

    public static <T> RequiredArgumentBuilder<CommandListenerWrapper, T> a(String s, ArgumentType<T> argumenttype) {
        return RequiredArgumentBuilder.argument(s, argumenttype);
    }

    public static Predicate<String> a(CommandDispatcher.b commanddispatcher_b) {
        return (s) -> {
            try {
                commanddispatcher_b.parse(new StringReader(s));
                return true;
            } catch (CommandSyntaxException commandsyntaxexception) {
                return false;
            }
        };
    }

    public com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> a() {
        return this.b;
    }

    @Nullable
    public static <S> CommandSyntaxException a(ParseResults<S> parseresults) {
        return !parseresults.getReader().canRead() ? null : (parseresults.getExceptions().size() == 1 ? (CommandSyntaxException) parseresults.getExceptions().values().iterator().next() : (parseresults.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parseresults.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parseresults.getReader())));
    }

    public static void b() {
        RootCommandNode<CommandListenerWrapper> rootcommandnode = (new CommandDispatcher(CommandDispatcher.ServerType.ALL)).a().getRoot();
        Set<ArgumentType<?>> set = ArgumentRegistry.a((CommandNode) rootcommandnode);
        Set<ArgumentType<?>> set1 = (Set) set.stream().filter((argumenttype) -> {
            return !ArgumentRegistry.a(argumenttype);
        }).collect(Collectors.toSet());

        if (!set1.isEmpty()) {
            CommandDispatcher.LOGGER.warn("Missing type registration for following arguments:\n {}", set1.stream().map((argumenttype) -> {
                return "\t" + argumenttype;
            }).collect(Collectors.joining(",\n")));
            throw new IllegalStateException("Unregistered argument types");
        }
    }

    public static enum ServerType {

        ALL(true, true), DEDICATED(false, true), INTEGRATED(true, false);

        private final boolean d;
        private final boolean e;

        private ServerType(boolean flag, boolean flag1) {
            this.d = flag;
            this.e = flag1;
        }
    }

    @FunctionalInterface
    public interface b {

        void parse(StringReader stringreader) throws CommandSyntaxException;
    }
}
