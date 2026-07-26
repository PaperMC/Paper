package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftRaid;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Raider;

public abstract class CraftRaider extends CraftMonster implements Raider {

    public CraftRaider(CraftServer server, net.minecraft.world.entity.raid.Raider entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.raid.Raider getHandle() {
        return (net.minecraft.world.entity.raid.Raider) this.entity;
    }

    @Override
    public void setRaid(Raid raid) {
        this.getHandle().setCurrentRaid(raid != null ? ((CraftRaid) raid).getHandle() : null);
    }

    @Override
    public Raid getRaid() {
        return this.getHandle().getCurrentRaid() == null ? null : new CraftRaid(this.getHandle().getCurrentRaid(), this.getHandle().level());
    }

    @Override
    public void setWave(int wave) {
        Preconditions.checkArgument(wave >= 0, "wave must be >= 0");
        this.getHandle().setWave(wave);
    }

    @Override
    public int getWave() {
        return this.getHandle().getWave();
    }

    @Override
    public Block getPatrolTarget() {
        return this.getHandle().getPatrolTarget() == null ? null : CraftBlock.at(this.getHandle().level(), this.getHandle().getPatrolTarget());
    }

    @Override
    public void setPatrolTarget(Block block) {
        if (block == null) {
            this.getHandle().setPatrolTarget(null);
        } else {
            Preconditions.checkArgument(block.getWorld().equals(this.getWorld()), "Block must be in same world");
            this.getHandle().setPatrolTarget(((CraftBlock) block).getPosition());
        }
    }

    @Override
    public boolean isPatrolLeader() {
        return this.getHandle().isPatrolLeader();
    }

    @Override
    public void setPatrolLeader(boolean leader) {
        this.getHandle().setPatrolLeader(leader);
    }

    @Override
    public boolean isCanJoinRaid() {
        return this.getHandle().canJoinRaid();
    }

    @Override
    public void setCanJoinRaid(boolean join) {
        this.getHandle().setCanJoinRaid(join);
    }

    @Override
    public boolean isCelebrating() {
        return this.getHandle().isCelebrating();
    }

    @Override
    public void setCelebrating(boolean celebrating) {
        this.getHandle().setCelebrating(celebrating);
    }

    @Override
    public int getTicksOutsideRaid() {
        return this.getHandle().getTicksOutsideRaid();
    }

    @Override
    public void setTicksOutsideRaid(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        this.getHandle().setTicksOutsideRaid(ticks);
    }

    @Override
    public Sound getCelebrationSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().getCelebrateSound());
    }
}
