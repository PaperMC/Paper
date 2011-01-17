package net.minecraft.server;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class EntityList {

    private static Map a = new HashMap();
    private static Map b = new HashMap();
    private static Map c = new HashMap();
    private static Map d = new HashMap();

    public EntityList() {}

    private static void a(Class class1, String s, int i) {
        a.put(((s)), ((class1)));
        b.put(((class1)), ((s)));
        c.put(((Integer.valueOf(i))), ((class1)));
        d.put(((class1)), ((Integer.valueOf(i))));
    }

    public static Entity a(String s, World world) {
        Entity entity = null;

        try {
            Class class1 = (Class) a.get(((s)));

            if (class1 != null) {
                entity = (Entity) class1.getConstructor(new Class[] {
                    net.minecraft.server.World.class
                }).newInstance(new Object[] {
                    world
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return entity;
    }

    public static Entity a(NBTTagCompound nbttagcompound, World world) {
        Entity entity = null;

        try {
            Class class1 = (Class) a.get(((nbttagcompound.h("id"))));

            if (class1 != null) {
                entity = (Entity) class1.getConstructor(new Class[] {
                    net.minecraft.server.World.class
                }).newInstance(new Object[] {
                    world
                });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (entity != null) {
            entity.e(nbttagcompound);
        } else {
            System.out.println((new StringBuilder()).append("Skipping Entity with id ").append(nbttagcompound.h("id")).toString());
        }
        return entity;
    }

    public static int a(Entity entity) {
        return ((Integer) d.get(((((entity)).getClass())))).intValue();
    }

    public static String b(Entity entity) {
        return (String) b.get(((((entity)).getClass())));
    }

    static {
        a(net.minecraft.server.EntityArrow.class, "Arrow", 10);
        a(net.minecraft.server.EntitySnowball.class, "Snowball", 11);
        a(net.minecraft.server.EntityItem.class, "Item", 1);
        a(net.minecraft.server.EntityPainting.class, "Painting", 9);
        a(net.minecraft.server.EntityLiving.class, "Mob", 48);
        a(net.minecraft.server.EntityMobs.class, "Monster", 49);
        a(net.minecraft.server.EntityCreeper.class, "Creeper", 50);
        a(net.minecraft.server.EntitySkeleton.class, "Skeleton", 51);
        a(net.minecraft.server.EntitySpider.class, "Spider", 52);
        a(net.minecraft.server.EntityZombieSimple.class, "Giant", 53);
        a(net.minecraft.server.EntityZombie.class, "Zombie", 54);
        a(net.minecraft.server.EntitySlime.class, "Slime", 55);
        a(net.minecraft.server.EntityGhast.class, "Ghast", 56);
        a(net.minecraft.server.EntityPigZombie.class, "PigZombie", 57);
        a(net.minecraft.server.EntityPig.class, "Pig", 90);
        a(net.minecraft.server.EntitySheep.class, "Sheep", 91);
        a(net.minecraft.server.EntityCow.class, "Cow", 92);
        a(net.minecraft.server.EntityChicken.class, "Chicken", 93);
        a(net.minecraft.server.EntitySquid.class, "Squid", 94);
        a(net.minecraft.server.EntityTNTPrimed.class, "PrimedTnt", 20);
        a(net.minecraft.server.EntityFallingSand.class, "FallingSand", 21);
        a(net.minecraft.server.EntityMinecart.class, "Minecart", 40);
        a(net.minecraft.server.EntityBoat.class, "Boat", 41);
    }
}
