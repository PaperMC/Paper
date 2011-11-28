package org.bukkit.craftbukkit.entity;

import com.google.common.collect.MapMaker;
import net.minecraft.server.*;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.MagmaCube;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static final Map<String, CraftPlayer> players = new MapMaker().softValues().makeMap();
    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /**
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        if (entity instanceof EntityLiving) {
            // Players
            if (entity instanceof EntityHuman) {
                if (entity instanceof EntityPlayer) { return getPlayer((EntityPlayer)entity); }
                else { return new CraftHumanEntity(server, (EntityHuman) entity); }
            }
            else if (entity instanceof EntityCreature) {
                // Animals
                if (entity instanceof EntityAnimal) {
                    if (entity instanceof EntityChicken) { return new CraftChicken(server, (EntityChicken) entity); }
                    else if (entity instanceof EntityCow) {
                        if (entity instanceof EntityMushroomCow) { return new CraftMushroomCow(server, (EntityMushroomCow) entity); }
                        else { return new CraftCow(server, (EntityCow) entity); }
                    }
                    else if (entity instanceof EntityPig) { return new CraftPig(server, (EntityPig) entity); }
                    else if (entity instanceof EntityWolf) { return new CraftWolf(server, (EntityWolf) entity); }
                    else if (entity instanceof EntitySheep) { return new CraftSheep(server, (EntitySheep) entity); }
                    else  { return new CraftAnimals(server, (EntityAnimal) entity); }
                }
                // Monsters
                else if (entity instanceof EntityMonster) {
                    if (entity instanceof EntityZombie) {
                        if (entity instanceof EntityPigZombie) { return new CraftPigZombie(server, (EntityPigZombie) entity); }
                        else { return new CraftZombie(server, (EntityZombie) entity); }
                    }
                    else if (entity instanceof EntityCreeper) { return new CraftCreeper(server, (EntityCreeper) entity); }
                    else if (entity instanceof EntityEnderman) { return new CraftEnderman(server, (EntityEnderman) entity); }
                    else if (entity instanceof EntitySilverfish) { return new CraftSilverfish(server, (EntitySilverfish) entity); }
                    else if (entity instanceof EntityGiantZombie) { return new CraftGiant(server, (EntityGiantZombie) entity); }
                    else if (entity instanceof EntitySkeleton) { return new CraftSkeleton(server, (EntitySkeleton) entity); }
                    else if (entity instanceof EntityBlaze) { return new CraftBlaze(server, (EntityBlaze) entity); }
                    else if (entity instanceof EntitySpider) {
                        if (entity instanceof EntityCaveSpider) { return new CraftCaveSpider(server, (EntityCaveSpider) entity); }
                        else { return new CraftSpider(server, (EntitySpider) entity); }
                    }

                    else  { return new CraftMonster(server, (EntityMonster) entity); }
                }
                // Water Animals
                else if (entity instanceof EntityWaterAnimal) {
                    if (entity instanceof EntitySquid) { return new CraftSquid(server, (EntitySquid) entity); }
                    else { return new CraftWaterMob(server, (EntityWaterAnimal) entity); }
                }
                else if (entity instanceof EntitySnowman) { return new CraftSnowman(server, (EntityCreature) entity); }
                else if (entity instanceof EntityVillager) { return new CraftVillager(server, (EntityVillager) entity); }
                else { return new CraftCreature(server, (EntityCreature) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof EntitySlime) {
                if (entity instanceof EntityLavaSlime) { return new CraftMagmaCube(server, (EntityLavaSlime) entity); }
                else { return new CraftSlime(server, (EntitySlime) entity); }
            }
            // Flying
            else if (entity instanceof EntityFlying) {
                if (entity instanceof EntityGhast) { return new CraftGhast(server, (EntityGhast) entity); }
                else { return new CraftFlying(server, (EntityFlying) entity); }
            }
            else if (entity instanceof EntityComplex) {
                if (entity instanceof EntityEnderDragon) { return new CraftEnderDragon(server, (EntityEnderDragon) entity); }
            }
            else  { return new CraftLivingEntity(server, (EntityLiving) entity); }
        }
        else if (entity instanceof EntityComplexPart) {
            EntityComplexPart part = (EntityComplexPart)entity;
            if (part.a instanceof EntityEnderDragon) { return new CraftEnderDragonPart(server, (EntityComplexPart)entity); }    
            else { return new CraftComplexPart(server, (EntityComplexPart) entity); }
        }
        else if (entity instanceof EntityExperienceOrb) { return new CraftExperienceOrb(server, (EntityExperienceOrb) entity); }
        else if (entity instanceof EntityArrow) { return new CraftArrow(server, (EntityArrow) entity); }
        else if (entity instanceof EntityBoat) { return new CraftBoat(server, (EntityBoat) entity); }
        else if (entity instanceof EntityProjectile) {
            if (entity instanceof EntityEgg) { return new CraftEgg(server, (EntityEgg) entity); }
            else if (entity instanceof EntitySnowball) { return new CraftSnowball(server, (EntitySnowball) entity); }
            else if (entity instanceof EntityPotion) { return new CraftThrownPotion(server, (EntityPotion) entity); }
            else if (entity instanceof EntityEnderPearl) { return new CraftEnderPearl(server, (EntityEnderPearl) entity); }
        }
        else if (entity instanceof EntityFallingSand) { return new CraftFallingSand(server, (EntityFallingSand) entity); }
        else if (entity instanceof EntityFireball) {
            if (entity instanceof EntitySmallFireball) { return new CraftSmallFireball(server, (EntitySmallFireball) entity); }
            else { return new CraftFireball(server, (EntityFireball) entity); }
        }
        else if (entity instanceof EntityEnderSignal) { return new CraftEnderSignal(server, (EntityEnderSignal) entity); }
        else if (entity instanceof EntityEnderCrystal) { return new CraftEnderCrystal(server, (EntityEnderCrystal) entity); }
        else if (entity instanceof EntityFishingHook) { return new CraftFish(server, (EntityFishingHook) entity); }
        else if (entity instanceof EntityItem) { return new CraftItem(server, (EntityItem) entity); }
        else if (entity instanceof EntityWeather) {
            if (entity instanceof EntityWeatherStorm) {
                return new CraftLightningStrike(server, (EntityWeatherStorm)entity);
            } else {
                return new CraftWeather(server, (EntityWeather)entity);
            }
        }
        else if (entity instanceof EntityMinecart) {
            EntityMinecart mc = (EntityMinecart) entity;
            if (mc.type == CraftMinecart.Type.StorageMinecart.getId()) {
                return new CraftStorageMinecart(server, mc);
            } else if (mc.type == CraftMinecart.Type.PoweredMinecart.getId()) {
                return new CraftPoweredMinecart(server, mc);
            } else {
                return new CraftMinecart(server, mc);
            }
        }
        else if (entity instanceof EntityPainting) { return new CraftPainting(server, (EntityPainting) entity); }
        else if (entity instanceof EntityTNTPrimed) { return new CraftTNTPrimed(server, (EntityTNTPrimed) entity); }
        
        throw new IllegalArgumentException("Unknown entity");
    }

    public Location getLocation() {
        return new Location(getWorld(), entity.locX, entity.locY, entity.locZ, entity.yaw, entity.pitch);
    }

    public Vector getVelocity() {
        return new Vector(entity.motX, entity.motY, entity.motZ);
    }

    public void setVelocity(Vector vel) {
        entity.motX = vel.getX();
        entity.motY = vel.getY();
        entity.motZ = vel.getZ();
        entity.velocityChanged = true;
    }

    public World getWorld() {
        return ((WorldServer) entity.world).getWorld();
    }

    public boolean teleport(Location location) {
        entity.world = ((CraftWorld) location.getWorld()).getHandle();
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // entity.setLocation() throws no event, and so cannot be cancelled
        return true;
    }

    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        @SuppressWarnings("unchecked")
        List<Entity> notchEntityList = entity.world.b(entity, entity.boundingBox.b(x, y, z));
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e: notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public int getEntityId() {
        return entity.id;
    }

    public int getFireTicks() {
        return entity.fireTicks;
    }

    public int getMaxFireTicks() {
        return entity.maxFireTicks;
    }

    public void setFireTicks(int ticks) {
        entity.fireTicks = ticks;
    }

    public void remove() {
        entity.dead = true;
    }

    public boolean isDead() {
        return !entity.aj();
    }

    public Entity getHandle() {
        return entity;
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        if (this.server != other.server && (this.server == null || !this.server.equals(other.server))) {
            return false;
        }
        if (this.entity != other.entity && (this.entity == null || !this.entity.equals(other.entity))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.server != null ? this.server.hashCode() : 0);
        hash = 89 * hash + (this.entity != null ? this.entity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityId() + '}';
    }

    public Server getServer() {
        return server;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : (CraftEntity) getHandle().passenger.getBukkitEntity();
    }

    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        if (passenger instanceof CraftEntity) {
            ((CraftEntity) passenger).getHandle().setPassengerOf(getHandle());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return getHandle().passenger == null;
    }

    public boolean eject() {
        if (getHandle().passenger == null) {
            return false;
        }

        getHandle().passenger.setPassengerOf(null);
        return true;
    }

    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    public UUID getUniqueId() {
        return getHandle().uniqueId;
    }

    private static CraftPlayer getPlayer(EntityPlayer entity) {
        CraftPlayer result = players.get(entity.name);

        if (result == null) {
            result = new CraftPlayer((CraftServer)Bukkit.getServer(), entity);
            players.put(entity.name, result);
        } else {
            result.setHandle(entity);
        }

        return result;
    }
    
    public int getTicksLived() {
        return getHandle().ticksLived;
    }
    
    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().ticksLived = value;
    }
}
