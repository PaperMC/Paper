package org.bukkit.craftbukkit.command;

import java.util.List;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityMinecartCommandBlock;
import net.minecraft.server.EntityMinecartCommandBlockListener;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerSelector;
import net.minecraft.server.RemoteControlCommandListener;
import net.minecraft.server.TileEntityCommandListener;
import net.minecraft.server.WorldServer;

import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.Level;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
        super(vanillaCommand.c());
        this.vanillaCommand = vanillaCommand;
    }

    public VanillaCommandWrapper(CommandAbstract vanillaCommand, String usage) {
        super(vanillaCommand.c());
        this.vanillaCommand = vanillaCommand;
        this.description = "A Mojang provided command.";
        this.usageMessage = usage;
        this.setPermission("minecraft.command." + vanillaCommand.c());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        ICommandListener icommandlistener = getListener(sender);
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer.getServer().worldServer = new WorldServer[]{(WorldServer) icommandlistener.getWorld()};
        try {
            vanillaCommand.b(icommandlistener, args);
        } catch (ExceptionUsage exceptionusage) {
            ChatMessage chatmessage = new ChatMessage("commands.generic.usage", new Object[] {new ChatMessage(exceptionusage.getMessage(), exceptionusage.a())});
            chatmessage.b().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage);
        } catch (CommandException commandexception) {
            ChatMessage chatmessage = new ChatMessage(commandexception.getMessage(), commandexception.a());
            chatmessage.b().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage);
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        return (List<String>) vanillaCommand.a(getListener(sender), args);
    }

    public final int dispatchVanillaCommandBlock(CommandBlockListenerAbstract icommandlistener, String s) {
        // Copied from net.minecraft.server.CommandHandler
        s = s.trim();
        if (s.startsWith("/")) {
            s = s.substring(1);
        }
        String as[] = s.split(" ");
        as = dropFirstArgument(as);
        int i = getPlayerListSize(as);
        int j = 0;
        // Some commands use the worldserver variable but we leave it full of null values,
        // so we must temporarily populate it with the world of the commandsender
        WorldServer[] prev = MinecraftServer.getServer().worldServer;
        MinecraftServer.getServer().worldServer = new WorldServer[]{(WorldServer) icommandlistener.getWorld()};
        try {
            if (vanillaCommand.a(icommandlistener)) {
                if (i > -1) {
                    EntityPlayer aentityplayer[] = PlayerSelector.getPlayers(icommandlistener, as[i]);
                    String s2 = as[i];
                    EntityPlayer aentityplayer1[] = aentityplayer;
                    int k = aentityplayer1.length;
                    for (int l = 0; l < k;) {
                        EntityPlayer entityplayer = aentityplayer1[l];
                        as[i] = entityplayer.getName();
                        try {
                            vanillaCommand.b(icommandlistener, as);
                            j++;
                            continue;
                        } catch (CommandException commandexception1) {
                            ChatMessage chatmessage4 = new ChatMessage(commandexception1.getMessage(), commandexception1.a());
                            chatmessage4.b().setColor(EnumChatFormat.RED);
                            icommandlistener.sendMessage(chatmessage4);
                            l++;
                        }
                    }

                    as[i] = s2;
                } else {
                    vanillaCommand.b(icommandlistener, as);
                    j++;
                }
            } else {
                ChatMessage chatmessage = new ChatMessage("commands.generic.permission", new Object[0]);
                chatmessage.b().setColor(EnumChatFormat.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (ExceptionUsage exceptionusage) {
            ChatMessage chatmessage1 = new ChatMessage("commands.generic.usage", new Object[] { new ChatMessage(exceptionusage.getMessage(), exceptionusage.a()) });
            chatmessage1.b().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage1);
        } catch (CommandException commandexception) {
            ChatMessage chatmessage2 = new ChatMessage(commandexception.getMessage(), commandexception.a());
            chatmessage2.b().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage2);
        } catch (Throwable throwable) {
            ChatMessage chatmessage3 = new ChatMessage("commands.generic.exception", new Object[0]);
            chatmessage3.b().setColor(EnumChatFormat.RED);
            icommandlistener.sendMessage(chatmessage3);
            if(icommandlistener instanceof TileEntityCommandListener) {
                TileEntityCommandListener listener = (TileEntityCommandListener) icommandlistener;
                MinecraftServer.av().log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().x, listener.getChunkCoordinates().y, listener.getChunkCoordinates().z), throwable);
            } else if (icommandlistener instanceof EntityMinecartCommandBlockListener) {
                EntityMinecartCommandBlockListener listener = (EntityMinecartCommandBlockListener) icommandlistener;
                MinecraftServer.av().log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().x, listener.getChunkCoordinates().y, listener.getChunkCoordinates().z), throwable);
            } else {
                MinecraftServer.av().log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            MinecraftServer.getServer().worldServer = prev;
        }
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
            return ((EntityMinecartCommandBlock) ((CraftMinecartCommand) sender).getHandle()).e();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return RemoteControlCommandListener.instance;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer) sender.getServer()).getServer();
        }
        return null;
    }

    private int getPlayerListSize(String as[]) {
        for (int i = 0; i < as.length; i++) {
            if (vanillaCommand.a(as, i) && PlayerSelector.isList(as[i])) {
                return i;
            }
        }
        return -1;
    }

    private String[] dropFirstArgument(String as[]) {
        String as1[] = new String[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            as1[i - 1] = as[i];
        }

        return as1;
    }
}
