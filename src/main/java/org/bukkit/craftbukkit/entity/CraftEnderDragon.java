package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityLiving;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {
    public CraftEnderDragon(CraftServer server, EntityLiving entity) {
        super(server, entity);
    }

    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();
        
        for (EntityComplexPart part : getHandle().f) {
            builder.add((ComplexEntityPart)part.getBukkitEntity());
        }
        
        return builder.build();
    }

    @Override
    public EntityEnderDragon getHandle() {
        return (EntityEnderDragon)super.getHandle();
    }
}
