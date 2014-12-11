package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityGuardian;
import net.minecraft.server.GenericAttributes;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return ((EntityGuardian)entity).cl();
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        EntityGuardian entityGuardian = (EntityGuardian) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.a( true );
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.a( false );

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.a(0.85F, 0.85F);
            entityGuardian.aW();

            // Update pathfinding (random stroll back to 80)
            entityGuardian.bq.b(80);

            // Tell minecraft that we need persistence since the guardian changed
            entityGuardian.bW();
        }
    }
}
