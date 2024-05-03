package org.bukkit.craftbukkit.damage;

import java.util.Objects;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
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
        org.bukkit.entity.Entity causingEntity = getCausingEntity();
        return causingEntity != null ? causingEntity.getWorld() : null;
    }

    public Block getDirectBlock() {
        return this.getHandle().getDirectBlock();
    }

    @Override
    public DamageType getDamageType() {
        return this.damageType;
    }

    @Override
    public org.bukkit.entity.Entity getCausingEntity() {
        net.minecraft.world.entity.Entity entity = this.getHandle().getEntity();
        return (entity != null) ? entity.getBukkitEntity() : null;
    }

    @Override
    public org.bukkit.entity.Entity getDirectEntity() {
        net.minecraft.world.entity.Entity entity = this.getHandle().getDamager();
        return (entity != null) ? entity.getBukkitEntity() : null;
    }

    @Override
    public Location getDamageLocation() {
        Vec3D vec3D = this.getHandle().sourcePositionRaw();
        return (vec3D != null) ? CraftLocation.toBukkit(vec3D, this.getCausingEntityWorld()) : null;
    }

    @Override
    public Location getSourceLocation() {
        Vec3D vec3D = this.getHandle().getSourcePosition();
        return (vec3D != null) ? CraftLocation.toBukkit(vec3D, this.getCausingEntityWorld()) : null;
    }

    @Override
    public boolean isIndirect() {
        return this.getHandle().getEntity() != this.getHandle().getDamager();
    }

    @Override
    public float getFoodExhaustion() {
        return this.damageType.getExhaustion();
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

        if (!(obj instanceof DamageSource)) {
            return false;
        }

        DamageSource other = (DamageSource) obj;
        return Objects.equals(this.getDamageType(), other.getDamageType()) && Objects.equals(this.getCausingEntity(), other.getCausingEntity())
                && Objects.equals(this.getDirectEntity(), other.getDirectEntity()) && Objects.equals(this.getDamageLocation(), other.getDamageLocation());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.damageType.hashCode();
        result = 31 * result + (this.getCausingEntity() != null ? this.getCausingEntity().hashCode() : 0);
        result = 31 * result + (this.getDirectEntity() != null ? this.getDirectEntity().hashCode() : 0);
        result = 31 * result + (this.getDamageLocation() != null ? this.getDamageLocation().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DamageSource{damageType=" + this.getDamageType() + ",causingEntity=" + this.getCausingEntity() + ",directEntity=" + this.getDirectEntity() + ",damageLocation=" + this.getDamageLocation() + "}";
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

        Vec3D vec3D = (damageLocation == null) ? null : CraftLocation.toVec3D(damageLocation);

        return new CraftDamageSource(new net.minecraft.world.damagesource.DamageSource(holderDamageType, nmsDirectEntity, nmsCausingEntity, vec3D));
    }
}
