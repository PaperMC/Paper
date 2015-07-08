package org.bukkit.craftbukkit.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.server.*;

import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.defaults.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public final class VanillaCommandWrapper extends VanillaCommand {
    protected final CommandAbstract vanillaCommand;

    public VanillaCommandWrapper(CommandAbstract vanillaCommand) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(CommandAbstract vanillaCommand, String usage) {
        super(vanillaCommand.getCommand());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.getCommand());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        ICommandListener icommandlistener = getListener(sender);
        dispatchVanillaCommand(sender, icommandlistener, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.tabComplete(getListener(sender), args, new BlockPosition(0, 0, 0));
    }

    public static CommandSender lastSender = null; // Nasty :(

    public final int dispatchVanillaCommand(CommandSender bSender, ICommandListener icommandlistener, String[] as) {
        // Copied from net.minecraft.server.CommandHandler
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer server = MinecraftServer.getServer();
        server.worldServer = new WorldServer[server.worlds.size()];
        server.worldServer[0] = (WorldServer) icommandlistener.getWorld();
        int bpos = 0;
        for (int pos = 1; pos < server.worldServer.length; pos++) {
            WorldServer world = server.worlds.get(bpos++);
            if (server.worldServer[0] == world) {
                pos--;
                continue;
            }
            server.worldServer[pos] = world;
        }

        try {
            if (vanillaCommand.canUse(icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>)PlayerSelector.getPlayers(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];
                    
                    icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        CommandSender oldSender = lastSender;
                        lastSender = bSender;
                        try {
                            as[i] = entity.getUniqueID().toString();
                            vanillaCommand.execute(icommandlistener, as);
                            j++;
                        } catch (ExceptionUsage exceptionusage) {
                            ChatMessage chatmessage = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(exceptionusage.getMessage(), exceptionusage.getArgs())});
                            chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
                            icommandlistener.sendMessage(chatmessage);
                        } catch (CommandException commandexception) {
                            CommandAbstract.a(icommandlistener, vanillaCommand, 1, commandexception.getMessage(), commandexception.getArgs());
                        } finally {
                            lastSender = oldSender;
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, 1);
                    vanillaCommand.execute(icommandlistener, as);
                    j++;
                }
            } else {
                ChatMessage chatmessage = new ChatMessage("commands.generic.permission", new Object[0]);
                chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (ExceptionUsage exceptionusage) {
            ChatMessage chatmessage1 = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(exceptionusage.getMessage(), exceptionusage.getArgs()) });
            chatmessage1.getChatModifier().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage1);
        } catch (CommandException commandexception) {
            CommandAbstract.a(icommandlistener, vanillaCommand, 1, commandexception.getMessage(), commandexception.getArgs());
        } catch (Throwable throwable) {
            ChatMessage chatmessage3 = new ChatMessage("commands.generic.exception", new Object[0]);
            chatmessage3.getChatModifier().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage3);
            if (icommandlistener.f() instanceof EntityMinecartCommandBlock) {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getChunkCoordinates().getX(), icommandlistener.getChunkCoordinates().getY(), icommandlistener.getChunkCoordinates().getZ()), throwable);
            } else if(icommandlistener instanceof CommandBlockListenerAbstract) {
                CommandBlockListenerAbstract listener = (CommandBlockListenerAbstract) icommandlistener;
                MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().getX(), listener.getChunkCoordinates().getY(), listener.getChunkCoordinates().getZ()), throwable);
            } else {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }
        icommandlistener.a(CommandObjectiveExecutor.EnumCommandResult.SUCCESS_COUNT, j);
        return j;
    }

    private ICommandListener getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer) sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).getCommandBlock();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return RemoteControlCommandListener.getInstance();
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }

    private int getPlayerListSize(String as[]) {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.isListStart(as, i) && PlayerSelector.isList(as[i])) {
                return i;
            }
        }
        return -1;
    }

    public static String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
