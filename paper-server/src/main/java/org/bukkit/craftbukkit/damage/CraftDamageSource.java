package org.bukkit.craftbukkit.damage;

import java.util.Objects;
import net.minecraft.Optionull;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;

public class CraftDamageSource implements DamageSource {

    private final net.minecraft.world.damagesource.DamageSource damageSource;
    private final DamageType damageType;

    public CraftDamageSource(net.minecraft.world.damagesource.DamageSource damageSource) {
        this.damageSource = damageSource;
        this.damageType = CraftDamageType.minecraftHolderToBukkit(damageSource.typeHolder());
    }

    public net.minecraft.world.damagesource.DamageSource getHandle() {
        return this.damageSource;
    }

    public World getCausingEntityWorld() {
        return Optionull.map(this.getCausingEntity(), Entity::getWorld);
    }

    @Override
    public DamageType getDamageType() {
        return this.damageType;
    }

    @Override
    public org.bukkit.entity.Entity getCausingEntity() {
        return Optionull.map(this.getHandle().getEntity(), net.minecraft.world.entity.Entity::getBukkitEntity);
    }

    @Override
    public org.bukkit.entity.Entity getDirectEntity() {
        return Optionull.map(this.getHandle().getDirectEntity(), net.minecraft.world.entity.Entity::getBukkitEntity);
    }

    @Override
    public Location getDamageLocation() {
        return Optionull.map(this.getHandle().sourcePositionRaw(), sourcePos -> CraftLocation.toBukkit(sourcePos, this.getCausingEntityWorld()));
    }

    @Override
    public Location getSourceLocation() {
        return Optionull.map(this.getHandle().getSourcePosition(), sourcePos -> CraftLocation.toBukkit(sourcePos, this.getCausingEntityWorld()));
    }

    @Override
    public boolean isIndirect() {
        return !this.getHandle().isDirect(); // Paper - fix DamageSource API
    }

    @Override
    public float getFoodExhaustion() {
        return this.getHandle().getFoodExhaustion();
    }

    @Override
    public boolean scalesWithDifficulty() {
        return this.getHandle().scalesWithDifficulty();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof DamageSource other)) {
            return false;
        }

        return Objects.equals(this.getDamageType(), other.getDamageType()) && Objects.equals(this.getCausingEntity(), other.getCausingEntity())
            && Objects.equals(this.getDirectEntity(), other.getDirectEntity()) && Objects.equals(this.getDamageLocation(), other.getDamageLocation());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.damageType.hashCode();
        result = 31 * result + Objects.hashCode(this.getCausingEntity());
        result = 31 * result + Objects.hashCode(this.getDirectEntity());
        result = 31 * result + Objects.hashCode(this.getDamageLocation());
        return result;
    }

    @Override
    public String toString() {
        return "DamageSource{damageType=" + this.getDamageType() + ", causingEntity=" + this.getCausingEntity() + ", directEntity=" + this.getDirectEntity() + ", damageLocation=" + this.getDamageLocation() + "}";
    }

    public static DamageSource buildFromBukkit(DamageType damageType, Entity causingEntity, Entity directEntity, Location damageLocation) {
        net.minecraft.core.Holder<net.minecraft.world.damagesource.DamageType> holderDamageType = CraftDamageType.bukkitToMinecraftHolder(damageType);

        net.minecraft.world.entity.Entity nmsCausingEntity = null;
        if (causingEntity instanceof CraftEntity craftCausingEntity) {
            nmsCausingEntity = craftCausingEntity.getHandle();
        }

        net.minecraft.world.entity.Entity nmsDirectEntity = null;
        if (directEntity instanceof CraftEntity craftDirectEntity) {
            nmsDirectEntity = craftDirectEntity.getHandle();
        }

        Vec3 sourcePos = (damageLocation == null) ? null : CraftLocation.toVec3(damageLocation);

        return new CraftDamageSource(new net.minecraft.world.damagesource.DamageSource(holderDamageType, nmsDirectEntity, nmsCausingEntity, sourcePos));
    }
}
