package org.bukkit.craftbukkit.entity;

import net.minecraft.server.World;
import net.minecraft.server.EntityRabbit;
import net.minecraft.server.PathfinderGoalSelector;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.craftbukkit.CraftWorld;

public class CraftRabbit extends CraftAnimals implements Rabbit {

    public CraftRabbit(CraftServer server, EntityRabbit entity) {
        super(server, entity);
    }

    @Override
    public EntityRabbit getHandle() {
        return (EntityRabbit) entity;
    }

    @Override
    public String toString() {
        return "CraftRabbit{RabbitType=" + getRabbitType() + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.RABBIT;
    }

    @Override
    public Type getRabbitType() {
        int type = getHandle().getRabbitType(); 
        return CraftMagicMapping.fromMagic(type);
    }

    @Override
    public void setRabbitType(Type type) {
        EntityRabbit entity = getHandle();
        if (getRabbitType() == Type.THE_KILLER_BUNNY) {
            // Reset goals and target finders.
            World world = ((CraftWorld) this.getWorld()).getHandle();
            entity.goalSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
            entity.targetSelector = new PathfinderGoalSelector(world != null && world.methodProfiler != null ? world.methodProfiler : null);
            entity.initializePathFinderGoals();
        }

        entity.setRabbitType(CraftMagicMapping.toMagic(type)); 
    }

    private static class CraftMagicMapping {

        private static final int[] types = new int[Type.values().length];
        private static final Type[] reverse = new Type[Type.values().length];

        static {
            set(Type.BROWN, 0);
            set(Type.WHITE, 1);
            set(Type.BLACK, 2);
            set(Type.BLACK_AND_WHITE, 3);
            set(Type.GOLD, 4);
            set(Type.SALT_AND_PEPPER, 5);
            set(Type.THE_KILLER_BUNNY, 99);
        }

        private static void set(Type type, int value) {
            types[type.ordinal()] = value;
            if (value < reverse.length) {
                reverse[value] = type;
            }
        }

        public static Type fromMagic(int magic) {
            if (magic >= 0 && magic < reverse.length) {
                return reverse[magic];
            } else if (magic == 99) {
                return Type.THE_KILLER_BUNNY;
            } else {
                return null;
            }
        }

        public static int toMagic(Type type) {
            return types[type.ordinal()];
        }
    }
}
