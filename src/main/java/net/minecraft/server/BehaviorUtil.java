package net.minecraft.server;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BehaviorUtil {

    public static void a(EntityLiving entityliving, EntityLiving entityliving1, float f) {
        d(entityliving, entityliving1);
        b(entityliving, entityliving1, f);
    }

    public static boolean a(BehaviorController<?> behaviorcontroller, EntityLiving entityliving) {
        return behaviorcontroller.getMemory(MemoryModuleType.VISIBLE_MOBS).filter((list) -> {
            return list.contains(entityliving);
        }).isPresent();
    }

    public static boolean a(BehaviorController<?> behaviorcontroller, MemoryModuleType<? extends EntityLiving> memorymoduletype, EntityTypes<?> entitytypes) {
        return a(behaviorcontroller, memorymoduletype, (entityliving) -> {
            return entityliving.getEntityType() == entitytypes;
        });
    }

    private static boolean a(BehaviorController<?> behaviorcontroller, MemoryModuleType<? extends EntityLiving> memorymoduletype, Predicate<EntityLiving> predicate) {
        return behaviorcontroller.getMemory(memorymoduletype).filter(predicate).filter(EntityLiving::isAlive).filter((entityliving) -> {
            return a(behaviorcontroller, entityliving);
        }).isPresent();
    }

    private static void d(EntityLiving entityliving, EntityLiving entityliving1) {
        a(entityliving, entityliving1);
        a(entityliving1, entityliving);
    }

    public static void a(EntityLiving entityliving, EntityLiving entityliving1) {
        entityliving.getBehaviorController().setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorPositionEntity(entityliving1, true))); // CraftBukkit - decompile error
    }

    private static void b(EntityLiving entityliving, EntityLiving entityliving1, float f) {
        boolean flag = true;

        a(entityliving, (Entity) entityliving1, f, 2);
        a(entityliving1, (Entity) entityliving, f, 2);
    }

    public static void a(EntityLiving entityliving, Entity entity, float f, int i) {
        MemoryTarget memorytarget = new MemoryTarget(new BehaviorPositionEntity(entity, false), f, i);

        entityliving.getBehaviorController().setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorPositionEntity(entity, true))); // CraftBukkit - decompile error
        entityliving.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, memorytarget); // CraftBukkit - decompile error
    }

    public static void a(EntityLiving entityliving, BlockPosition blockposition, float f, int i) {
        MemoryTarget memorytarget = new MemoryTarget(new BehaviorTarget(blockposition), f, i);

        entityliving.getBehaviorController().setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorTarget(blockposition))); // CraftBukkit - decompile error
        entityliving.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, memorytarget); // CraftBukkit - decompile error
    }

    public static void a(EntityLiving entityliving, ItemStack itemstack, Vec3D vec3d) {
        if (itemstack.isEmpty()) return; // CraftBukkit - SPIGOT-4940: no empty loot
        double d0 = entityliving.getHeadY() - 0.30000001192092896D;
        EntityItem entityitem = new EntityItem(entityliving.world, entityliving.locX(), d0, entityliving.locZ(), itemstack);
        float f = 0.3F;
        Vec3D vec3d1 = vec3d.d(entityliving.getPositionVector());

        vec3d1 = vec3d1.d().a(0.30000001192092896D);
        entityitem.setMot(vec3d1);
        entityitem.defaultPickupDelay();
        // CraftBukkit start
        org.bukkit.event.entity.EntityDropItemEvent event = new org.bukkit.event.entity.EntityDropItemEvent(entityliving.getBukkitEntity(), (org.bukkit.entity.Item) entityitem.getBukkitEntity());
        entityitem.world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        // CraftBukkit end
        entityliving.world.addEntity(entityitem);
    }

    public static SectionPosition a(WorldServer worldserver, SectionPosition sectionposition, int i) {
        int j = worldserver.b(sectionposition);
        Stream<SectionPosition> stream = SectionPosition.a(sectionposition, i).filter((sectionposition1) -> { // CraftBukkit - decompile error
            return worldserver.b(sectionposition1) < j;
        });

        worldserver.getClass();
        return (SectionPosition) stream.min(Comparator.comparingInt(worldserver::b)).orElse(sectionposition);
    }

    public static boolean a(EntityInsentient entityinsentient, EntityLiving entityliving, int i) {
        Item item = entityinsentient.getItemInMainHand().getItem();

        if (item instanceof ItemProjectileWeapon && entityinsentient.a((ItemProjectileWeapon) item)) {
            int j = ((ItemProjectileWeapon) item).d() - i;

            return entityinsentient.a((Entity) entityliving, (double) j);
        } else {
            return b((EntityLiving) entityinsentient, entityliving);
        }
    }

    public static boolean b(EntityLiving entityliving, EntityLiving entityliving1) {
        double d0 = entityliving.h(entityliving1.locX(), entityliving1.locY(), entityliving1.locZ());
        double d1 = (double) (entityliving.getWidth() * 2.0F * entityliving.getWidth() * 2.0F + entityliving1.getWidth());

        return d0 <= d1;
    }

    public static boolean a(EntityLiving entityliving, EntityLiving entityliving1, double d0) {
        Optional<EntityLiving> optional = entityliving.getBehaviorController().getMemory(MemoryModuleType.ATTACK_TARGET);

        if (!optional.isPresent()) {
            return false;
        } else {
            double d1 = entityliving.e(((EntityLiving) optional.get()).getPositionVector());
            double d2 = entityliving.e(entityliving1.getPositionVector());

            return d2 > d1 + d0 * d0;
        }
    }

    public static boolean c(EntityLiving entityliving, EntityLiving entityliving1) {
        BehaviorController<?> behaviorcontroller = entityliving.getBehaviorController();

        return !behaviorcontroller.hasMemory(MemoryModuleType.VISIBLE_MOBS) ? false : ((List) behaviorcontroller.getMemory(MemoryModuleType.VISIBLE_MOBS).get()).contains(entityliving1);
    }

    public static EntityLiving a(EntityLiving entityliving, Optional<EntityLiving> optional, EntityLiving entityliving1) {
        return !optional.isPresent() ? entityliving1 : a(entityliving, (EntityLiving) optional.get(), entityliving1);
    }

    public static EntityLiving a(EntityLiving entityliving, EntityLiving entityliving1, EntityLiving entityliving2) {
        Vec3D vec3d = entityliving1.getPositionVector();
        Vec3D vec3d1 = entityliving2.getPositionVector();

        return entityliving.e(vec3d) < entityliving.e(vec3d1) ? entityliving1 : entityliving2;
    }

    public static Optional<EntityLiving> a(EntityLiving entityliving, MemoryModuleType<UUID> memorymoduletype) {
        Optional<UUID> optional = entityliving.getBehaviorController().getMemory(memorymoduletype);

        return optional.map((uuid) -> {
            return (EntityLiving) ((WorldServer) entityliving.world).getEntity(uuid);
        });
    }

    public static Stream<EntityVillager> a(EntityVillager entityvillager, Predicate<EntityVillager> predicate) {
        return (Stream) entityvillager.getBehaviorController().getMemory(MemoryModuleType.MOBS).map((list) -> {
            return list.stream().filter((entityliving) -> {
                return entityliving instanceof EntityVillager && entityliving != entityvillager;
            }).map((entityliving) -> {
                return (EntityVillager) entityliving;
            }).filter(EntityLiving::isAlive).filter(predicate);
        }).orElseGet(Stream::empty);
    }
}
