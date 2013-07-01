package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import com.google.common.base.Joiner;
// CraftBukkit end

public class TileEntityCommand extends TileEntity implements ICommandListener {

    private int a;
    public String b = ""; // CraftBukkit - private -> public
    private String c = "@";
    // CraftBukkit start
    private final org.bukkit.command.BlockCommandSender sender;

    public TileEntityCommand() {
        sender = new org.bukkit.craftbukkit.command.CraftBlockCommandSender(this);
    }
    // CraftBukkit end

    public void a(String s) {
        this.b = s;
        this.update();
    }

    public int a(World world) {
        if (world.isStatic) {
            return 0;
        } else {
            MinecraftServer minecraftserver = MinecraftServer.getServer();

            if (minecraftserver != null && minecraftserver.getEnableCommandBlock()) {
                // CraftBukkit start - Handle command block commands using Bukkit dispatcher
                org.bukkit.command.SimpleCommandMap commandMap = minecraftserver.server.getCommandMap();
                Joiner joiner = Joiner.on(" ");
                String command = this.b;
                if (this.b.startsWith("/")) {
                    command = this.b.substring(1);
                }
                String[] args = command.split(" ");
                ArrayList<String[]> commands = new ArrayList<String[]>();

                // Block disallowed commands
                if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("op") ||
                        args[0].equalsIgnoreCase("deop") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("ban-ip") ||
                        args[0].equalsIgnoreCase("pardon") || args[0].equalsIgnoreCase("pardon-ip") || args[0].equalsIgnoreCase("reload")) {
                    return 0;
                }

                // Make sure this is a valid command
                if (commandMap.getCommand(args[0]) == null) {
                    return 0;
                }

                // If the world has no players don't run
                if (this.world.players.isEmpty()) {
                    return 0;
                }

                // testfor command requires special handling
                if (args[0].equalsIgnoreCase("testfor")) {
                    if (args.length < 2) {
                        return 0;
                    }

                    EntityPlayer[] players = PlayerSelector.getPlayers(this, args[1]);

                    if (players != null && players.length > 0) {
                        return players.length;
                    } else {
                        EntityPlayer player = MinecraftServer.getServer().getPlayerList().getPlayer(args[1]);
                        if (player == null) {
                            return 0;
                        } else {
                            return 1;
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
                        minecraftserver.getLogger().warning(String.format("CommandBlock at (%d,%d,%d) failed to handle command", this.x, this.y, this.z), exception);
                    }
                }

                return completed;
                // CraftBukkit end
            } else {
                return 0;
            }
        }
    }

    // CraftBukkit start
    private ArrayList<String[]> buildCommands(String[] args, int pos) {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        EntityPlayer[] players = PlayerSelector.getPlayers(this, args[pos]);
        if (players != null) {
            for (EntityPlayer player : players) {
                if (player.world != this.world) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.getLocalizedName();
                commands.add(command);
            }
        }

        return commands;
    }
    // CraftBukkit end

    public String getName() {
        return this.c;
    }

    public void b(String s) {
        this.c = s;
    }

    public void sendMessage(ChatMessage chatmessage) {}

    public boolean a(int i, String s) {
        return i <= 2;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("Command", this.b);
        nbttagcompound.setInt("SuccessCount", this.a);
        nbttagcompound.setString("CustomName", this.c);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.b = nbttagcompound.getString("Command");
        this.a = nbttagcompound.getInt("SuccessCount");
        if (nbttagcompound.hasKey("CustomName")) {
            this.c = nbttagcompound.getString("CustomName");
        }
    }

    public ChunkCoordinates b() {
        return new ChunkCoordinates(this.x, this.y, this.z);
    }

    public World f_() {
        return this.getWorld();
    }

    public Packet getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 2, nbttagcompound);
    }

    public int f() {
        return this.a;
    }

    public void a(int i) {
        this.a = i;
    }
}
