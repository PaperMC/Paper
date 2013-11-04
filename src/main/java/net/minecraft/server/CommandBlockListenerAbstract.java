package net.minecraft.server;

import java.text.SimpleDateFormat;
import java.util.Date;

// CraftBukkit start
import java.util.ArrayList;
import org.apache.logging.log4j.Level;
import com.google.common.base.Joiner;
// CraftBukkit end

public abstract class CommandBlockListenerAbstract implements ICommandListener {

    private static final SimpleDateFormat a = new SimpleDateFormat("HH:mm:ss");
    private int b;
    private boolean c = true;
    private IChatBaseComponent d = null;
    public String e = ""; // CraftBukkit - private -> public
    private String f = "@";
    protected org.bukkit.command.CommandSender sender; // CraftBukkit - add sender;

    public CommandBlockListenerAbstract() {}

    public int g() {
        return this.b;
    }

    public IChatBaseComponent h() {
        return this.d;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Command", this.e);
        nbttagcompound.setInt("SuccessCount", this.b);
        nbttagcompound.setString("CustomName", this.f);
        if (this.d != null) {
            nbttagcompound.setString("LastOutput", ChatSerializer.a(this.d));
        }

        nbttagcompound.setBoolean("TrackOutput", this.c);
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getString("Command");
        this.b = nbttagcompound.getInt("SuccessCount");
        if (nbttagcompound.hasKeyOfType("CustomName", 8)) {
            this.f = nbttagcompound.getString("CustomName");
        }

        if (nbttagcompound.hasKeyOfType("LastOutput", 8)) {
            this.d = ChatSerializer.a(nbttagcompound.getString("LastOutput"));
        }

        if (nbttagcompound.hasKeyOfType("TrackOutput", 1)) {
            this.c = nbttagcompound.getBoolean("TrackOutput");
        }
    }

    public boolean a(int i, String s) {
        return i <= 2;
    }

    public void a(String s) {
        this.e = s;
    }

    public String i() {
        return this.e;
    }

    public void a(World world) {
        if (world.isStatic) {
            this.b = 0;
        }

        MinecraftServer minecraftserver = MinecraftServer.getServer();

        if (minecraftserver != null && minecraftserver.getEnableCommandBlock()) {
            // CraftBukkit start - Handle command block commands using Bukkit dispatcher
            org.bukkit.command.SimpleCommandMap commandMap = minecraftserver.server.getCommandMap();
            Joiner joiner = Joiner.on(" ");
            String command = this.e;
            if (this.e.startsWith("/")) {
                command = this.e.substring(1);
            }
            String[] args = command.split(" ");
            ArrayList<String[]> commands = new ArrayList<String[]>();

            // Block disallowed commands
            if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("op") ||
                    args[0].equalsIgnoreCase("deop") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("ban-ip") ||
                    args[0].equalsIgnoreCase("pardon") || args[0].equalsIgnoreCase("pardon-ip") || args[0].equalsIgnoreCase("reload")) {
                this.b = 0;
                return;
            }

            // Make sure this is a valid command
            if (commandMap.getCommand(args[0]) == null) {
                this.b = 0;
                return;
            }

            // If the world has no players don't run
            if (this.getWorld().players.isEmpty()) {
                this.b = 0;
                return;
            }

            // testfor command requires special handling
            if (args[0].equalsIgnoreCase("testfor")) {
                if (args.length < 2) {
                    this.b = 0;
                    return;
                }

                EntityPlayer[] players = PlayerSelector.getPlayers(this, args[1]);

                if (players != null && players.length > 0) {
                    this.b = players.length;
                    return;
                } else {
                    EntityPlayer player = MinecraftServer.getServer().getPlayerList().getPlayer(args[1]);
                    if (player == null) {
                        this.b = 0;
                        return;
                    } else {
                        this.b = 1;
                        return;
                    }
                }
            }

            commands.add(args);

            // Find positions of command block syntax, if any
            ArrayList<String[]> newCommands = new ArrayList<String[]>();
            for (int i = 0; i < args.length; i++) {
                if (PlayerSelector.isPattern(args[i])) {
                    for (int j = 0; j < commands.size(); j++) {
                        newCommands.addAll(this.buildCommands(commands.get(j), i));
                    }
                    ArrayList<String[]> temp = commands;
                    commands = newCommands;
                    newCommands = temp;
                    newCommands.clear();
                }
            }

            int completed = 0;

            // Now dispatch all of the commands we ended up with
            for (int i = 0; i < commands.size(); i++) {
                try {
                    if (commandMap.dispatch(sender, joiner.join(java.util.Arrays.asList(commands.get(i))))) {
                        completed++;
                    }
                } catch (Throwable exception) {
                    if(this instanceof TileEntityCommandListener) {
                        TileEntityCommandListener listener = (TileEntityCommandListener) this;
                        MinecraftServer.av().log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().x, listener.getChunkCoordinates().y, listener.getChunkCoordinates().z), exception);
                    } else if (this instanceof EntityMinecartCommandBlockListener) {
                        EntityMinecartCommandBlockListener listener = (EntityMinecartCommandBlockListener) this;
                        MinecraftServer.av().log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", listener.getChunkCoordinates().x, listener.getChunkCoordinates().y, listener.getChunkCoordinates().z), exception);
                    } else {
                        MinecraftServer.av().log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), exception);
                    }
                }
            }

            this.b = completed;
            // CraftBukkit end
        } else {
            this.b = 0;
        }
    }

    // CraftBukkit start
    private ArrayList<String[]> buildCommands(String[] args, int pos) {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        EntityPlayer[] players = PlayerSelector.getPlayers(this, args[pos]);
        if (players != null) {
            for (EntityPlayer player : players) {
                if (player.world != this.getWorld()) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.getName();
                commands.add(command);
            }
        }

        return commands;
    }
    // CraftBukkit end

    public String getName() {
        return this.f;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public void b(String s) {
        this.f = s;
    }

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        if (this.c && this.getWorld() != null && !this.getWorld().isStatic) {
            this.d = (new ChatComponentText("[" + a.format(new Date()) + "] ")).a(ichatbasecomponent);
            this.e();
        }
    }

    public abstract void e();

    public void b(IChatBaseComponent ichatbasecomponent) {
        this.d = ichatbasecomponent;
    }
}
