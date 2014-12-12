package org.bukkit.craftbukkit.command;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumCommandResult;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class ListenerWrapper implements ICommandListener {

    private final ICommandListener listener;

    public ListenerWrapper(ICommandListener listener) {
        this.listener = listener;
    }

    @Override
    public String getName() {
        return listener.getName();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return listener.getScoreboardDisplayName();
    }

    @Override
    public void sendMessage(IChatBaseComponent icbc) {
        listener.sendMessage(icbc);
    }

    @Override
    public boolean a(int i, String string) {
        return true; // Give access to all commands as we do our own perm check
    }

    @Override
    public BlockPosition getChunkCoordinates() {
        return listener.getChunkCoordinates();
    }

    @Override
    public Vec3D d() {
        return listener.d();
    }

    @Override
    public World getWorld() {
        return listener.getWorld();
    }

    @Override
    public Entity f() {
        return listener.f();
    }

    @Override
    public boolean getSendCommandFeedback() {
        return listener.getSendCommandFeedback();
    }

    @Override
    public void a(EnumCommandResult ecr, int i) {
        listener.a(ecr, i);
    }

}
