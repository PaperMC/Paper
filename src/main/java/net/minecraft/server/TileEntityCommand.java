package net.minecraft.server;

public class TileEntityCommand extends TileEntity implements ICommandListener {

    private String a = "";

    public TileEntityCommand() {}

    public void b(String s) {
        this.a = s;
        this.update();
    }

    public void a(World world) {
        if (!world.isStatic) {
            MinecraftServer minecraftserver = MinecraftServer.getServer();

            if (minecraftserver != null && minecraftserver.getEnableCommandBlock()) {
                // CraftBukkit start - disable command block TODO: hook this up to bukkit API
                // ICommandHandler icommandhandler = minecraftserver.getCommandHandler();

                // icommandhandler.a(this, this.a);
                // CraftBukkit end
            }
        }
    }

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
