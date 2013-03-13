package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMinecartTNT;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.MinecartTNT;

final class CraftMinecartTNT extends CraftMinecart implements MinecartTNT {
    CraftMinecartTNT(CraftServer server, EntityMinecartTNT entity) {
        super(server, entity);
    }
}
