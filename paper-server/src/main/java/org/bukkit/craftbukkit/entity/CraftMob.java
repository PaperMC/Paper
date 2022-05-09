package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;

public abstract class CraftMob extends CraftLivingEntity implements Mob {
    public CraftMob(CraftServer server, EntityInsentient entity) {
        super(server, entity);
    }

    @Override
    public void setTarget(LivingEntity target) {
        Preconditions.checkState(!getHandle().generation, "Cannot set target during world generation");

        EntityInsentient entity = getHandle();
        if (target == null) {
            entity.setTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    @Override
    public CraftLivingEntity getTarget() {
        if (getHandle().getTarget() == null) return null;

        return (CraftLivingEntity) getHandle().getTarget().getBukkitEntity();
    }

    @Override
    public void setAware(boolean aware) {
        getHandle().aware = aware;
    }

    @Override
    public boolean isAware() {
        return getHandle().aware;
    }

    @Override
    public EntityInsentient getHandle() {
        return (EntityInsentient) entity;
    }

    @Override
    public String toString() {
        return "CraftMob";
    }

    @Override
    public void setLootTable(LootTable table) {
        getHandle().lootTable = (table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey());
    }

    @Override
    public LootTable getLootTable() {
        NamespacedKey key = CraftNamespacedKey.fromMinecraft(getHandle().getLootTable());
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        getHandle().lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return getHandle().lootTableSeed;
    }
}
