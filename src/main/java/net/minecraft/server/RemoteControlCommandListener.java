package net.minecraft.server;

public class RemoteControlCommandListener implements ICommandListener {

    public static final RemoteControlCommandListener instance = new RemoteControlCommandListener();
    private StringBuffer b = new StringBuffer();

    public RemoteControlCommandListener() {}

    public void e() {
        this.b.setLength(0);
    }

    public String f() {
        return this.b.toString();
    }

    public String getName() {
        return "Rcon";
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatComponentText(this.getName());
    }

    // CraftBukkit start - Send a String
    public void sendMessage(String message) {
        this.b.append(message);
    }
    // CraftBukkit end

    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        this.b.append(ichatbasecomponent.c());
    }

    public boolean a(int i, String s) {
        return true;
    }

    public ChunkCoordinates getChunkCoordinates() {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World getWorld() {
        return MinecraftServer.getServer().getWorld();
    }
}
