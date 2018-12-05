package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

import net.minecraft.server.DragonControllerPhase;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityEnderDragon;

import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {

    private BossBar bossBar;

    public CraftEnderDragon(CraftServer server, EntityEnderDragon entity) {
        super(server, entity);

        if (entity.getEnderDragonBattle() != null) {
            this.bossBar = new CraftBossBar(entity.getEnderDragonBattle().bossBattle);
        }
    }

    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (EntityComplexPart part : getHandle().children) {
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

    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().getDataWatcher().get(EntityEnderDragon.PHASE)];
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
        return bossBar;
    }
}
