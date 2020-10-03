package net.minecraft.server;

import java.util.UUID;

public class RemoteControlCommandListener implements ICommandListener {

    private static final ChatComponentText b = new ChatComponentText("Rcon");
    private final StringBuffer buffer = new StringBuffer();
    private final MinecraftServer server;

    public RemoteControlCommandListener(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
    }

    public void clearMessages() {
        this.buffer.setLength(0);
    }

    public String getMessages() {
        return this.buffer.toString();
    }

    public CommandListenerWrapper getWrapper() {
        WorldServer worldserver = this.server.E();

        return new CommandListenerWrapper(this, Vec3D.b((BaseBlockPosition) worldserver.getSpawn()), Vec2F.a, worldserver, 4, "Rcon", RemoteControlCommandListener.b, this.server, (Entity) null);
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {
        this.buffer.append(ichatbasecomponent.getString());
    }

    @Override
    public boolean shouldSendSuccess() {
        return true;
    }

    @Override
    public boolean shouldSendFailure() {
        return true;
    }

    @Override
    public boolean shouldBroadcastCommands() {
        return this.server.i();
    }
}
