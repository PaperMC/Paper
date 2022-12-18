package org.bukkit.craftbukkit.boss;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.dimension.end.DragonRespawnAnimation;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;

public class CraftDragonBattle implements DragonBattle {

    private final EndDragonFight handle;

    public CraftDragonBattle(EndDragonFight handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        Entity entity = this.handle.level.getEntity(this.handle.dragonUUID);
        return (entity != null) ? (EnderDragon) entity.getBukkitEntity() : null;
    }

    @Override
    public BossBar getBossBar() {
        return new CraftBossBar(this.handle.dragonEvent);
    }

    @Override
    public Location getEndPortalLocation() {
        if (this.handle.portalLocation == null) {
            return null;
        }

        return CraftLocation.toBukkit(this.handle.portalLocation, this.handle.level.getWorld());
    }

    @Override
    public boolean generateEndPortal(boolean withPortals) {
        if (this.handle.portalLocation != null || this.handle.findExitPortal() != null) {
            return false;
        }

        this.handle.spawnExitPortal(withPortals);
        return true;
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return this.handle.hasPreviouslyKilledDragon();
    }

    @Override
    public void setPreviouslyKilled(boolean previouslyKilled) {
        this.handle.previouslyKilled = previouslyKilled;
    }

    @Override
    public void initiateRespawn() {
        this.handle.tryRespawn();
    }

    @Override
    public boolean initiateRespawn(Collection<EnderCrystal> list) {
        if (this.hasBeenPreviouslyKilled() && this.getRespawnPhase() == RespawnPhase.NONE) {
            // Copy from EnderDragonBattle#tryRespawn for generate exit portal if not exists
            if (this.handle.portalLocation == null) {
                BlockPattern.BlockPatternMatch shapedetector_shapedetectorcollection = this.handle.findExitPortal();
                if (shapedetector_shapedetectorcollection == null) {
                    this.handle.spawnExitPortal(true);
                }
            }

            list = (list != null) ? new ArrayList<>(list) : Collections.emptyList();
            list.removeIf(enderCrystal -> {
                if (enderCrystal == null) {
                    return true;
                }

                World world = enderCrystal.getWorld();
                return !((CraftWorld) world).getHandle().equals(this.handle.level);
            });

            return this.handle.respawnDragon(list.stream().map(enderCrystal -> ((CraftEnderCrystal) enderCrystal).getHandle()).collect(Collectors.toList()));
        }
        return false;
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return this.toBukkitRespawnPhase(this.handle.respawnStage);
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        Preconditions.checkArgument(phase != null && phase != RespawnPhase.NONE, "Invalid respawn phase provided: %s", phase);

        if (this.handle.respawnStage == null) {
            return false;
        }

        this.handle.setRespawnStage(this.toNMSRespawnPhase(phase));
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetSpikeCrystals();
    }

    @Override
    public int hashCode() {
        return this.handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftDragonBattle && ((CraftDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(DragonRespawnAnimation phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private DragonRespawnAnimation toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? DragonRespawnAnimation.values()[phase.ordinal()] : null;
    }
    // Paper start - More DragonBattle API
    @Override
    public int getGatewayCount() {
        return EndDragonFight.GATEWAY_COUNT - this.handle.gateways.size();
    }

    @Override
    public boolean spawnNewGateway() {
        return this.handle.spawnNewGatewayIfPossible();
    }

    @Override
    public void spawnNewGateway(final io.papermc.paper.math.Position position) {
        this.handle.spawnNewGateway(io.papermc.paper.util.MCUtil.toBlockPos(position));
    }

    @Override
    public java.util.List<org.bukkit.entity.EnderCrystal> getRespawnCrystals() {
        if (this.handle.respawnCrystals == null) {
            return java.util.Collections.emptyList();
        }

        final java.util.List<org.bukkit.entity.EnderCrystal> enderCrystals = new java.util.ArrayList<>();
        for (final net.minecraft.world.entity.boss.enderdragon.EndCrystal endCrystal : this.handle.respawnCrystals) {
            if (!endCrystal.isRemoved() && endCrystal.isAlive() && endCrystal.valid) {
                enderCrystals.add(((org.bukkit.entity.EnderCrystal) endCrystal.getBukkitEntity()));
            }
        }
        return java.util.Collections.unmodifiableList(enderCrystals);
    }

    @Override
    public java.util.List<org.bukkit.entity.EnderCrystal> getHealingCrystals() {
        final java.util.List<org.bukkit.entity.EnderCrystal> enderCrystals = new java.util.ArrayList<>();
        for (final net.minecraft.world.entity.boss.enderdragon.EndCrystal endCrystal : this.handle.getSpikeCrystals()) {
            if (!endCrystal.isRemoved() && endCrystal.isAlive() && endCrystal.valid) {
                enderCrystals.add(((org.bukkit.entity.EnderCrystal) endCrystal.getBukkitEntity()));
            }
        }
        return java.util.Collections.unmodifiableList(enderCrystals);
    }
    // Paper end - More DragonBattle API
}
