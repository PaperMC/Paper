package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;

public abstract class CraftMob extends CraftLivingEntity implements Mob {
    public CraftMob(CraftServer server, net.minecraft.world.entity.Mob entity) {
        super(server, entity);
    }

    @Override
    public void setTarget(LivingEntity target) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot set target during world generation");

        net.minecraft.world.entity.Mob entity = this.getHandle();
        if (target == null) {
            entity.setTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            entity.setTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    @Override
    public CraftLivingEntity getTarget() {
        if (this.getHandle().getTarget() == null) return null;

        return (CraftLivingEntity) this.getHandle().getTarget().getBukkitEntity();
    }

    @Override
    public void setAware(boolean aware) {
        this.getHandle().aware = aware;
    }

    @Override
    public boolean isAware() {
        return this.getHandle().aware;
    }

    @Override
    public Sound getAmbientSound() {
        SoundEvent sound = this.getHandle().getAmbientSound0();
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public net.minecraft.world.entity.Mob getHandle() {
        return (net.minecraft.world.entity.Mob) this.entity;
    }

    @Override
    public String toString() {
        return "CraftMob";
    }

    @Override
    public void setLootTable(LootTable table) {
        this.getHandle().lootTable = Optional.ofNullable(CraftLootTable.bukkitToMinecraft(table));
    }

    @Override
    public LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().getLootTable().orElse(null));
    }

    @Override
    public void setSeed(long seed) {
        this.getHandle().lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return this.getHandle().lootTableSeed;
    }
}
