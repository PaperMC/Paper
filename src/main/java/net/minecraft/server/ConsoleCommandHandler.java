package net.minecraft.server;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class ConsoleCommandHandler {

    private static Logger a = Logger.getLogger("Minecraft");
    private MinecraftServer b;

    public ConsoleCommandHandler(MinecraftServer minecraftserver) {
        this.b = minecraftserver;
    }

    // CraftBukkit - All calls to the following below:
    // this.a( String s1, String msg );
    // are changed to:
    // this.notify( ICommandListener icommandlistener, String msg );

    public boolean a(ServerCommand servercommand) { // CraftBukkit - returns boolean
        String s = servercommand.a;
        ICommandListener icommandlistener = servercommand.b;
        String s1 = icommandlistener.c();
        WorldServer worldserver = this.b.worlds.get(0); // CraftBukkit
        ServerConfigurationManager serverconfigurationmanager = this.b.f;

        if (!s.toLowerCase().startsWith("help") && !s.toLowerCase().startsWith("?")) {
            if (s.toLowerCase().startsWith("list")) {
                icommandlistener.b("Connected players: " + serverconfigurationmanager.c());
            } else if (s.toLowerCase().startsWith("stop")) {
                this.notify(icommandlistener, "Stopping the server.."); // CraftBukkit - notify command sender
                this.b.a();
            } else if (s.toLowerCase().startsWith("save-all")) {
                this.notify(icommandlistener, "Forcing save.."); // CraftBukkit - notify command sender
                this.b.f(); // CraftBukkit - We should save all worlds on save-all.
                this.notify(icommandlistener, "Save complete."); // CraftBukkit - notify command sender
            } else if (s.toLowerCase().startsWith("save-off")) {
                this.notify(icommandlistener, "Disabling level saving.."); // CraftBukkit - notify command sender
                worldserver.w = true;
            } else if (s.toLowerCase().startsWith("save-on")) {
                this.notify(icommandlistener, "Enabling level saving.."); // CraftBukkit - notify command sender
                worldserver.w = false;
            } else {
                String s2;

                if (s.toLowerCase().startsWith("op ")) {
                    s2 = s.substring(s.indexOf(" ")).trim();
                    serverconfigurationmanager.e(s2);
                    this.notify(icommandlistener, "Opping " + s2); // CraftBukkit - notify command sender
                    serverconfigurationmanager.a(s2, "\u00A7eYou are now op!");
                } else if (s.toLowerCase().startsWith("deop ")) {
                    s2 = s.substring(s.indexOf(" ")).trim();
                    serverconfigurationmanager.f(s2);
                    serverconfigurationmanager.a(s2, "\u00A7eYou are no longer op!");
                    this.notify(icommandlistener, "De-opping " + s2); // CraftBukkit - notify command sender
                } else if (s.toLowerCase().startsWith("ban-ip ")) {
                    s2 = s.substring(s.indexOf(" ")).trim();
                    serverconfigurationmanager.c(s2);
                    this.notify(icommandlistener, "Banning ip " + s2); // CraftBukkit - notify command sender
                } else if (s.toLowerCase().startsWith("pardon-ip ")) {
                    s2 = s.substring(s.indexOf(" ")).trim();
                    serverconfigurationmanager.d(s2);
                    this.notify(icommandlistener, "Pardoning ip " + s2); // CraftBukkit - notify command sender
                } else {
                    EntityPlayer entityplayer;

                    if (s.toLowerCase().startsWith("ban ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.a(s2);
                        this.notify(icommandlistener, "Banning " + s2); // CraftBukkit - notify command sender
                        entityplayer = serverconfigurationmanager.i(s2);
                        if (entityplayer != null) {
                            entityplayer.a.a("Banned by admin");
                        }
                    } else if (s.toLowerCase().startsWith("pardon ")) {
                        s2 = s.substring(s.indexOf(" ")).trim();
                        serverconfigurationmanager.b(s2);
                        this.notify(icommandlistener, "Pardoning " + s2); // CraftBukkit - notify command sender
                    } else {
                        int i;

                        if (s.toLowerCase().startsWith("kick ")) {
                            // CraftBukkit start - Add kick message compatibility
                            String[] parts = s.split(" ");
                            s2 = ( parts.length >= 2 ) ? parts[1] : "";
                            // CraftBukkit end
                            entityplayer = null;

                            for (i = 0; i < serverconfigurationmanager.b.size(); ++i) {
                                EntityPlayer entityplayer1 = (EntityPlayer) serverconfigurationmanager.b.get(i);

                                if (entityplayer1.name.equalsIgnoreCase(s2)) {
                                    entityplayer = entityplayer1;
                                }
                            }

                            if (entityplayer != null) {
                                entityplayer.a.a("Kicked by admin");
                                this.notify(icommandlistener, "Kicking " + entityplayer.name); // CraftBukkit - notify command sender
                            } else {
                                icommandlistener.b("Can\'t find user " + s2 + ". No kick.");
                            }
                        } else {
                            String[] astring;
                            EntityPlayer entityplayer2;

                            if (s.toLowerCase().startsWith("tp ")) {
                                astring = s.split(" ");
                                if (astring.length == 3) {
                                    entityplayer = serverconfigurationmanager.i(astring[1]);
                                    entityplayer2 = serverconfigurationmanager.i(astring[2]);
                                    if (entityplayer == null) {
                                        icommandlistener.b("Can\'t find user " + astring[1] + ". No tp.");
                                    } else if (entityplayer2 == null) {
                                        icommandlistener.b("Can\'t find user " + astring[2] + ". No tp.");
                                    } else {
                                        entityplayer.a.a(entityplayer2.locX, entityplayer2.locY, entityplayer2.locZ, entityplayer2.yaw, entityplayer2.pitch);
                                        this.notify(icommandlistener, "Teleporting " + astring[1] + " to " + astring[2] + "."); // CraftBukkit - notify command sender
                                    }
                                } else {
                                    icommandlistener.b("Syntax error, please provice a source and a target.");
                                }
                            } else {
                                String s3;

                                if (s.toLowerCase().startsWith("give ")) {
                                    astring = s.split(" ");
                                    if (astring.length != 3 && astring.length != 4) {
                                        return true;
                                    }

                                    s3 = astring[1];
                                    entityplayer2 = serverconfigurationmanager.i(s3);
                                    if (entityplayer2 != null) {
                                        try {
                                            int j = Integer.parseInt(astring[2]);

                                            if (Item.byId[j] != null) {
                                                this.notify(icommandlistener, "Giving " + entityplayer2.name + " some " + j); // CraftBukkit - notify command sender
                                                int k = 1;

                                                if (astring.length > 3) {
                                                    k = this.a(astring[3], 1);
                                                }

                                                if (k < 1) {
                                                    k = 1;
                                                }

                                                if (k > 64) {
                                                    k = 64;
                                                }

                                                entityplayer2.b(new ItemStack(j, k, 0));
                                            } else {
                                                icommandlistener.b("There\'s no item with id " + j);
                                            }
                                        } catch (NumberFormatException numberformatexception) {
                                            icommandlistener.b("There\'s no item with id " + astring[2]);
                                        }
                                    } else {
                                        icommandlistener.b("Can\'t find user " + s3);
                                    }
                                } else if (s.toLowerCase().startsWith("time ")) {
                                    astring = s.split(" ");
                                    if (astring.length != 3) {
                                        return true;
                                    }

                                    s3 = astring[1];

                                    try {
                                        i = Integer.parseInt(astring[2]);
                                        if ("add".equalsIgnoreCase(s3)) {
                                            worldserver.a(worldserver.k() + (long) i);
                                            this.notify(icommandlistener, "Added " + i + " to time"); // CraftBukkit - notify command sender
                                        } else if ("set".equalsIgnoreCase(s3)) {
                                            worldserver.a((long) i);
                                            this.notify(icommandlistener, "Set time to " + i); // CraftBukkit - notify command sender
                                        } else {
                                            icommandlistener.b("Unknown method, use either \"add\" or \"set\"");
                                        }
                                    } catch (NumberFormatException numberformatexception1) {
                                        icommandlistener.b("Unable to convert time value, " + astring[2]);
                                    }
                                } else if (s.toLowerCase().startsWith("say ")) {
                                    s = s.substring(s.indexOf(" ")).trim();
                                    a.info("[" + s1 + "] " + s);
                                    serverconfigurationmanager.a((Packet) (new Packet3Chat("\u00A7d[Server] " + s)));
                                } else if (s.toLowerCase().startsWith("tell ")) {
                                    astring = s.split(" ");
                                    if (astring.length >= 3) {
                                        s = s.substring(s.indexOf(" ")).trim();
                                        s = s.substring(s.indexOf(" ")).trim();
                                        a.info("[" + s1 + "->" + astring[1] + "] " + s);
                                        s = "\u00A77" + s1 + " whispers " + s;
                                        a.info(s);
                                        if (!serverconfigurationmanager.a(astring[1], (Packet) (new Packet3Chat(s)))) {
                                            icommandlistener.b("There\'s no player by that name online.");
                                        }
                                    }
                                } else if (s.toLowerCase().startsWith("whitelist ")) {
                                    this.a(s1, s, icommandlistener);
                                } else {
                                    icommandlistener.b("Unknown console command. Type \"help\" for help."); // CraftBukkit - Send to listener not log
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            this.a(icommandlistener);
        }

        return true;
    }

    private void a(String s, String s1, ICommandListener icommandlistener) {
        String[] astring = s1.split(" ");

        if (astring.length >= 2) {
            String s2 = astring[1].toLowerCase();

            if ("on".equals(s2)) {
                this.notify(icommandlistener, "Turned on white-listing"); // CraftBukkit - notify command sender
                this.b.d.b("white-list", true);
            } else if ("off".equals(s2)) {
                this.notify(icommandlistener, "Turned off white-listing"); // CraftBukkit - notify command sender
                this.b.d.b("white-list", false);
            } else if ("list".equals(s2)) {
                Set set = this.b.f.e();
                String s3 = "";

                String s4;

                for (Iterator iterator = set.iterator(); iterator.hasNext(); s3 = s3 + s4 + " ") {
                    s4 = (String) iterator.next();
                }

                icommandlistener.b("White-listed players: " + s3);
            } else {
                String s5;

                if ("add".equals(s2) && astring.length == 3) {
                    s5 = astring[2].toLowerCase();
                    this.b.f.k(s5);
                    this.notify(icommandlistener, "Added " + s5 + " to white-list"); // CraftBukkit - notify command sender
                } else if ("remove".equals(s2) && astring.length == 3) {
                    s5 = astring[2].toLowerCase();
                    this.b.f.l(s5);
                    this.notify(icommandlistener, "Removed " + s5 + " from white-list"); // CraftBukkit - notify command sender
                } else if ("reload".equals(s2)) {
                    this.b.f.f();
                    this.notify(icommandlistener, "Reloaded white-list from file"); // CraftBukkit - notify command sender
                }
            }
        }
    }

    private void a(ICommandListener icommandlistener) {
        icommandlistener.b("To run the server without a gui, start it like this:");
        icommandlistener.b("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
        icommandlistener.b("Console commands:");
        icommandlistener.b("   help  or  ?               shows this message");
        icommandlistener.b("   kick <player>             removes a player from the server");
        icommandlistener.b("   ban <player>              bans a player from the server");
        icommandlistener.b("   pardon <player>           pardons a banned player so that they can connect again");
        icommandlistener.b("   ban-ip <ip>               bans an IP address from the server");
        icommandlistener.b("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
        icommandlistener.b("   op <player>               turns a player into an op");
        icommandlistener.b("   deop <player>             removes op status from a player");
        icommandlistener.b("   tp <player1> <player2>    moves one player to the same location as another player");
        icommandlistener.b("   give <player> <id> [num]  gives a player a resource");
        icommandlistener.b("   tell <player> <message>   sends a private message to a player");
        icommandlistener.b("   stop                      gracefully stops the server");
        icommandlistener.b("   save-all                  forces a server-wide level save");
        icommandlistener.b("   save-off                  disables terrain saving (useful for backup scripts)");
        icommandlistener.b("   save-on                   re-enables terrain saving");
        icommandlistener.b("   list                      lists all currently connected players");
        icommandlistener.b("   say <message>             broadcasts a message to all players");
        icommandlistener.b("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
    }

    // CraftBukkit start
    // Notify sender and ops / log
    private void notify(ICommandListener commandListener, String msg ) {
        commandListener.b( msg );
        this.a( commandListener.c(), msg );
    }
    // CraftBukkit end

    private void a(String s, String s1) {
        String s2 = s + ": " + s1;

        // CraftBukkit - This notifies ops and logs
        this.b.f.j("\u00A77(" + s2 + ")");
        a.info(s2);
    }

    private int a(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException numberformatexception) {
            return i;
        }
    }
}
