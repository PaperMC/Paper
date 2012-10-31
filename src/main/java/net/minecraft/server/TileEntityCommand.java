package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.Arrays;
import com.google.common.base.Joiner;
// CraftBukkit end

public class TileEntityCommand extends TileEntity implements ICommandListener {

    private String a = "";
    private final org.bukkit.command.BlockCommandSender sender;

    public TileEntityCommand() {
        sender = new org.bukkit.craftbukkit.command.CraftBlockCommandSender(this);
    }

    public void b(String s) {
        this.a = s;
        this.update();
    }

    public void a(World world) {
        if (!world.isStatic) {
            MinecraftServer minecraftserver = MinecraftServer.getServer();

            if (minecraftserver != null && minecraftserver.getEnableCommandBlock()) {
                // CraftBukkit start - handle command block as console TODO: add new CommandSender for this
                org.bukkit.command.SimpleCommandMap commandMap = minecraftserver.server.getCommandMap();
                Joiner joiner = Joiner.on(" ");
                String command = this.a;
                if (this.a.startsWith("/")) {
                    command = this.a.substring(1);
                }
                String[] args = command.split(" ");
                ArrayList<String[]> commands = new ArrayList<String[]>();

                // block disallowed commands
                if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("op") ||
                        args[0].equalsIgnoreCase("deop") || args[0].equalsIgnoreCase("ban") || args[0].equalsIgnoreCase("ban-ip") ||
                        args[0].equalsIgnoreCase("pardon") || args[0].equalsIgnoreCase("pardon-ip") || args[0].equalsIgnoreCase("reload")) {
                    return;
                }

                // make sure this is a valid command
                if (commandMap.getCommand(args[0]) == null) {
                    return;
                }

                commands.add(args);

                // find positions of command block syntax, if any
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

                // now dispatch all of the commands we ended up with
                for (int i = 0; i < commands.size(); i++) {
                    commandMap.dispatch(sender, joiner.join(Arrays.asList(commands.get(i))));
                }
                // CraftBukkit end
            }
        }
    }

    // CraftBukkit start
    private ArrayList<String[]> buildCommands(String[] args, int pos) {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        EntityPlayer[] players = PlayerSelector.getPlayers(this, args[pos]);
        if (players != null) {
            for (EntityPlayer player : players) {
                String[] command = args.clone();
                command[pos] = player.getLocalizedName();
                commands.add(command);
            }
        }

        return commands;
    }
    // CraftBukkit end

    public String getName() {
        return "@";
    }

    public void sendMessage(String s) {}

    public boolean a(int i, String s) {
        return i <= 2;
    }

    public String a(String s, Object... aobject) {
        return s;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("Command", this.a);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.a = nbttagcompound.getString("Command");
    }

    public ChunkCoordinates b() {
        return new ChunkCoordinates(this.x, this.y, this.z);
    }

    public Packet l() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 2, nbttagcompound);
    }
}
