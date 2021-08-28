package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftDragonBattle;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

    public CraftEnderDragon(CraftServer server, EntityEnderDragon entity) {
        super(server, entity);
    }

    @Override
    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (EntityComplexPart part : getHandle().subEntities) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public EntityEnderDragon getHandle() {
        return (EntityEnderDragon) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().getDataWatcher().get(EntityEnderDragon.DATA_PHASE)];
    }

    @Override
    public void setPhase(Phase phase) {
        getHandle().getDragonControllerManager().setControllerPhase(getMinecraftPhase(phase));
    }

    public static Phase getBukkitPhase(DragonControllerPhase phase) {
        return Phase.values()[phase.b()];
    }

    public static DragonControllerPhase getMinecraftPhase(Phase phase) {
        return DragonControllerPhase.getById(phase.ordinal());
    }

    @Override
    public BossBar getBossBar() {
        DragonBattle battle = getDragonBattle();
        return battle != null ? battle.getBossBar() : null;
    }

    @Override
    public DragonBattle getDragonBattle() {
        return getHandle().getEnderDragonBattle() != null ? new CraftDragonBattle(getHandle().getEnderDragonBattle()) : null;
    }

    @Override
    public int getDeathAnimationTicks() {
        return getHandle().dragonDeathTime;
    }
}
