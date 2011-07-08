package net.minecraft.server;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.craftbukkit.command.ServerCommandListener;
import org.bukkit.craftbukkit.entity.CraftPlayer;
// CraftBukkit end

public class ConsoleCommandHandler {

    private static Logger a = Logger.getLogger("Minecraft");
    private MinecraftServer server;
    private ICommandListener listener; // CraftBukkit

    public ConsoleCommandHandler(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public boolean handle(ServerCommand servercommand) { // CraftBukkit - returns boolean
        String s = servercommand.command;
        ICommandListener icommandlistener = servercommand.b;
        String s1 = icommandlistener.getName();
        this.listener = icommandlistener; // CraftBukkit
        ServerConfigurationManager serverconfigurationmanager = this.server.serverConfigurationManager;

        if (!s.toLowerCase().startsWith("help") && !s.toLowerCase().startsWith("?")) {
            if (s.toLowerCase().startsWith("list")) {
                icommandlistener.sendMessage("Connected players: " + serverconfigurationmanager.c());
            } else if (s.toLowerCase().startsWith("stop")) {
                this.print(s1, "Stopping the server..");
                this.server.a();
            } else {
                int i;
                WorldServer worldserver;

                if (s.toLowerCase().startsWith("save-all")) {
                    this.print(s1, "Forcing save..");
                    if (serverconfigurationmanager != null) {
                        serverconfigurationmanager.savePlayers();
                    }

                    // CraftBukkit start
                    for (i = 0; i < this.server.worlds.size(); ++i) {
                        worldserver = this.server.worlds.get(i);
                        boolean save = worldserver.canSave;
                        worldserver.canSave = false;
                        worldserver.save(true, (IProgressUpdate) null);
                        worldserver.canSave = save;
                    }
                    // CraftBukkit end

                    this.print(s1, "Save complete.");
                } else if (s.toLowerCase().startsWith("save-off")) {
                    this.print(s1, "Disabling level saving..");

                    for (i = 0; i < this.server.worlds.size(); ++i) { // CraftBukkit
                        worldserver = this.server.worlds.get(i); // CraftBukkit
                        worldserver.canSave = true;
                    }
                } else if (s.toLowerCase().startsWith("save-on")) {
                    this.print(s1, "Enabling level saving..");

                    for (i = 0; i < this.server.worlds.size(); ++i) { // CraftBukkit
                        worldserver = this.server.worlds.get(i); // CraftBukkit
                        worldserver.canSave = false;
                    }
                } else {
                    String s2;

                    if (s.toLowerCase().startsWith("op ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.e(s2);
                        this.print(s1, "Opping " + s2);
                        serverconfigurationmanager.a(s2, "\u00A7eYou are now op!");
                    } else if (s.toLowerCase().startsWith("deop ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.f(s2);
                        serverconfigurationmanager.a(s2, "\u00A7eYou are no longer op!");
                        this.print(s1, "De-opping " + s2);
                    } else if (s.toLowerCase().startsWith("ban-ip ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.c(s2);
                        this.print(s1, "Banning ip " + s2);
                    } else if (s.toLowerCase().startsWith("pardon-ip ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.d(s2);
                        this.print(s1, "Pardoning ip " + s2);
                    } else {
                        EntityPlayer entityplayer;

                        if (s.toLowerCase().startsWith("ban ")) {
                            s2 = s.substring(s.indexOf(" ")).trim();
                            serverconfigurationmanager.a(s2);
                            this.print(s1, "Banning " + s2);
                            entityplayer = serverconfigurationmanager.i(s2);
                            if (entityplayer != null) {
                                entityplayer.netServerHandler.disconnect("Banned by admin");
                            }
                        } else if (s.toLowerCase().startsWith("pardon ")) {
                            s2 = s.substring(s.indexOf(" ")).trim();
                            serverconfigurationmanager.b(s2);
                            this.print(s1, "Pardoning " + s2);
                        } else {
                            int j;

                            if (s.toLowerCase().startsWith("kick ")) {
                                // CraftBukkit start - Add kick message compatibility
                                String[] parts = s.split(" ");
                                s2 = parts.length >= 2 ? parts[1] : "";
                                // CraftBukkit end
                                entityplayer = null;

                                for (j = 0; j < serverconfigurationmanager.players.size(); ++j) {
                                    EntityPlayer entityplayer1 = (EntityPlayer) serverconfigurationmanager.players.get(j);

                                    if (entityplayer1.name.equalsIgnoreCase(s2)) {
                                        entityplayer = entityplayer1;
                                    }
                                }

                                if (entityplayer != null) {
                                    entityplayer.netServerHandler.disconnect("Kicked by admin");
                                    this.print(s1, "Kicking " + entityplayer.name);
                                } else {
                                    icommandlistener.sendMessage("Can\'t find user " + s2 + ". No kick.");
                                }
                            } else {
                                EntityPlayer entityplayer2;
                                String[] astring;

                                if (s.toLowerCase().startsWith("tp ")) {
                                    astring = s.split(" ");
                                    if (astring.length == 3) {
                                        entityplayer = serverconfigurationmanager.i(astring[1]);
                                        entityplayer2 = serverconfigurationmanager.i(astring[2]);
                                        if (entityplayer == null) {
                                            icommandlistener.sendMessage("Can\'t find user " + astring[1] + ". No tp.");
                                        } else if (entityplayer2 == null) {
                                            icommandlistener.sendMessage("Can\'t find user " + astring[2] + ". No tp.");
                                        } else if (entityplayer.dimension != entityplayer2.dimension) {
                                            icommandlistener.sendMessage("User " + astring[1] + " and " + astring[2] + " are in different dimensions. No tp.");
                                        } else {
                                            entityplayer.netServerHandler.a(entityplayer2.locX, entityplayer2.locY, entityplayer2.locZ, entityplayer2.yaw, entityplayer2.pitch);
                                            this.print(s1, "Teleporting " + astring[1] + " to " + astring[2] + ".");
                                        }
                                    } else {
                                        icommandlistener.sendMessage("Syntax error, please provice a source and a target.");
                                    }
                                } else {
                                    String s3;
                                    int k;

                                    if (s.toLowerCase().startsWith("give ")) {
                                        astring = s.split(" ");
                                        if (astring.length != 3 && astring.length != 4) {
                                            return true; // CraftBukkit
                                        }

                                        s3 = astring[1];
                                        entityplayer2 = serverconfigurationmanager.i(s3);
                                        if (entityplayer2 != null) {
                                            try {
                                                k = Integer.parseInt(astring[2]);
                                                if (Item.byId[k] != null) {
                                                    this.print(s1, "Giving " + entityplayer2.name + " some " + k);
                                                    int l = 1;

                                                    if (astring.length > 3) {
                                                        l = this.a(astring[3], 1);
                                                    }

                                                    if (l < 1) {
                                                        l = 1;
                                                    }

                                                    if (l > 64) {
                                                        l = 64;
                                                    }

                                                    entityplayer2.b(new ItemStack(k, l, 0));
                                                } else {
                                                    icommandlistener.sendMessage("There\'s no item with id " + k);
                                                }
                                            } catch (NumberFormatException numberformatexception) {
                                                icommandlistener.sendMessage("There\'s no item with id " + astring[2]);
                                            }
                                        } else {
                                            icommandlistener.sendMessage("Can\'t find user " + s3);
                                        }
                                    } else if (s.toLowerCase().startsWith("time ")) {
                                        astring = s.split(" ");
                                        if (astring.length != 3) {
                                            return true; // CraftBukkit
                                        }

                                        s3 = astring[1];

                                        try {
                                            j = Integer.parseInt(astring[2]);
                                            WorldServer worldserver1;

                                            if ("add".equalsIgnoreCase(s3)) {
                                                for (k = 0; k < this.server.worlds.size(); ++k) { // CraftBukkit
                                                    worldserver1 = this.server.worlds.get(k); // CraftBukkit
                                                    worldserver1.setTimeAndFixTicklists(worldserver1.getTime() + (long) j);
                                                }

                                                this.print(s1, "Added " + j + " to time");
                                            } else if ("set".equalsIgnoreCase(s3)) {
                                                for (k = 0; k < this.server.worlds.size(); ++k) { // CraftBukkit
                                                    worldserver1 = this.server.worlds.get(k); // CraftBukkit
                                                    worldserver1.setTimeAndFixTicklists((long) j);
                                                }

                                                this.print(s1, "Set time to " + j);
                                            } else {
                                                icommandlistener.sendMessage("Unknown method, use either \"add\" or \"set\"");
                                            }
                                        } catch (NumberFormatException numberformatexception1) {
                                            icommandlistener.sendMessage("Unable to convert time value, " + astring[2]);
                                        }
                                    } else if (s.toLowerCase().startsWith("say ")) {
                                        s = s.substring(s.indexOf(" ")).trim();
                                        a.info("[" + s1 + "] " + s);
                                        serverconfigurationmanager.sendAll(new Packet3Chat("\u00A7d[Server] " + s));
                                    } else if (s.toLowerCase().startsWith("tell ")) {
                                        astring = s.split(" ");
                                        if (astring.length >= 3) {
                                            s = s.substring(s.indexOf(" ")).trim();
                                            s = s.substring(s.indexOf(" ")).trim();
                                            a.info("[" + s1 + "->" + astring[1] + "] " + s);
                                            s = "\u00A77" + s1 + " whispers " + s;
                                            a.info(s);
                                            if (!serverconfigurationmanager.a(astring[1], (Packet) (new Packet3Chat(s)))) {
                                                icommandlistener.sendMessage("There\'s no player by that name online.");
                                            }
                                        }
                                    } else if (s.toLowerCase().startsWith("whitelist ")) {
                                        this.a(s1, s, icommandlistener);
                                    } else {
                                        icommandlistener.sendMessage("Unknown console command. Type \"help\" for help."); // CraftBukkit
                                        return false; // CraftBukkit
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            this.a(icommandlistener);
        }

        return true; // CraftBukkit
    }

    private void a(String s, String s1, ICommandListener icommandlistener) {
        String[] astring = s1.split(" ");
        this.listener = icommandlistener; // CraftBukkit

        if (astring.length >= 2) {
            String s2 = astring[1].toLowerCase();

            if ("on".equals(s2)) {
                this.print(s, "Turned on white-listing");
                this.server.propertyManager.b("white-list", true);
            } else if ("off".equals(s2)) {
                this.print(s, "Turned off white-listing");
                this.server.propertyManager.b("white-list", false);
            } else if ("list".equals(s2)) {
                Set set = this.server.serverConfigurationManager.e();
                String s3 = "";

                String s4;

                for (Iterator iterator = set.iterator(); iterator.hasNext(); s3 = s3 + s4 + " ") {
                    s4 = (String) iterator.next();
                }

                icommandlistener.sendMessage("White-listed players: " + s3);
            } else {
                String s5;

                if ("add".equals(s2) && astring.length == 3) {
                    s5 = astring[2].toLowerCase();
                    this.server.serverConfigurationManager.k(s5);
                    this.print(s, "Added " + s5 + " to white-list");
                } else if ("remove".equals(s2) && astring.length == 3) {
                    s5 = astring[2].toLowerCase();
                    this.server.serverConfigurationManager.l(s5);
                    this.print(s, "Removed " + s5 + " from white-list");
                } else if ("reload".equals(s2)) {
                    this.server.serverConfigurationManager.f();
                    this.print(s, "Reloaded white-list from file");
                }
            }
        }
    }

    private void a(ICommandListener icommandlistener) {
        icommandlistener.sendMessage("To run the server without a gui, start it like this:");
        icommandlistener.sendMessage("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
        icommandlistener.sendMessage("Console commands:");
        icommandlistener.sendMessage("   help  or  ?               shows this message");
        icommandlistener.sendMessage("   kick <player>             removes a player from the server");
        icommandlistener.sendMessage("   ban <player>              bans a player from the server");
        icommandlistener.sendMessage("   pardon <player>           pardons a banned player so that they can connect again");
        icommandlistener.sendMessage("   ban-ip <ip>               bans an IP address from the server");
        icommandlistener.sendMessage("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
        icommandlistener.sendMessage("   op <player>               turns a player into an op");
        icommandlistener.sendMessage("   deop <player>             removes op status from a player");
        icommandlistener.sendMessage("   tp <player1> <player2>    moves one player to the same location as another player");
        icommandlistener.sendMessage("   give <player> <id> [num]  gives a player a resource");
        icommandlistener.sendMessage("   tell <player> <message>   sends a private message to a player");
        icommandlistener.sendMessage("   stop                      gracefully stops the server");
        icommandlistener.sendMessage("   save-all                  forces a server-wide level save");
        icommandlistener.sendMessage("   save-off                  disables terrain saving (useful for backup scripts)");
        icommandlistener.sendMessage("   save-on                   re-enables terrain saving");
        icommandlistener.sendMessage("   list                      lists all currently connected players");
        icommandlistener.sendMessage("   say <message>             broadcasts a message to all players");
        icommandlistener.sendMessage("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
    }

    private void print(String s, String s1) {
        String s2 = s + ": " + s1;

        // CraftBukkit start
        this.listener.sendMessage(s1);
        this.informOps("\u00A77(" + s2 + ")");
        if (this.listener instanceof MinecraftServer) {
            return; // Already logged so don't call a.info()
        }
        // CraftBukkit end
        a.info(s2);
    }

    // CraftBukkit start
    private void informOps(String msg) {
        Packet3Chat packet3chat = new Packet3Chat(msg);
        EntityPlayer sender = null;
        if (this.listener instanceof ServerCommandListener) {
            org.bukkit.command.CommandSender commandSender = ((ServerCommandListener) this.listener).getSender();
            if (commandSender instanceof CraftPlayer) {
                sender = ((CraftPlayer) commandSender).getHandle();
            }
        }
        java.util.List<EntityPlayer> players = this.server.serverConfigurationManager.players;
        for (int i = 0; i < players.size(); ++i) {
            EntityPlayer entityPlayer = (EntityPlayer) players.get(i);
            if (sender != entityPlayer && this.server.serverConfigurationManager.isOp(entityPlayer.name)) {
                entityPlayer.netServerHandler.sendPacket(packet3chat);
            }
        }
    }
    // CraftBukkit end

    private int a(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            return i;
        }
    }
}
