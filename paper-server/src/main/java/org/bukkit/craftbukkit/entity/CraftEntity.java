package org.bukkit.craftbukkit.entity;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.entity.TeleportFlag;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import io.papermc.paper.entity.LookAnchor;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.util.TriState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftSpawnCategory;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.chat.BaseComponent; // Spigot
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    private static PermissibleBase perm;
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    protected final CraftServer server;
    protected Entity entity;
    private final EntityType entityType;
    private EntityDamageEvent lastDamageEvent;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(CraftEntity.DATA_TYPE_REGISTRY);
    protected net.kyori.adventure.pointer.Pointers adventure$pointers; // Paper - implement pointers
    // Paper start - Folia shedulers
    public final io.papermc.paper.threadedregions.EntityScheduler taskScheduler = new io.papermc.paper.threadedregions.EntityScheduler(this);
    private final io.papermc.paper.threadedregions.scheduler.FoliaEntityScheduler apiScheduler = new io.papermc.paper.threadedregions.scheduler.FoliaEntityScheduler(this);

    @Override
    public final io.papermc.paper.threadedregions.scheduler.EntityScheduler getScheduler() {
        return this.apiScheduler;
    };
    // Paper end - Folia schedulers

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
        this.entityType = CraftEntityType.minecraftToBukkit(entity.getType());
    }

    public static <T extends Entity> CraftEntity getEntity(CraftServer server, T entity) {
        Preconditions.checkArgument(entity != null, "Unknown entity");

        // Special case human, since bukkit use Player interface for ...
        if (entity instanceof net.minecraft.world.entity.player.Player && !(entity instanceof ServerPlayer)) {
            return new CraftHumanEntity(server, (net.minecraft.world.entity.player.Player) entity);
        }

        // Special case complex part, since there is no extra entity type for them
        if (entity instanceof EnderDragonPart complexPart) {
            if (complexPart.parentMob instanceof EnderDragon) {
                return new CraftEnderDragonPart(server, complexPart);
            } else {
                return new CraftComplexPart(server, complexPart);
            }
        }

        CraftEntityTypes.EntityTypeData<?, T> entityTypeData = CraftEntityTypes.getEntityTypeData(CraftEntityType.minecraftToBukkit(entity.getType()));

        if (entityTypeData != null) {
            return (CraftEntity) entityTypeData.convertFunction().apply(server, entity);
        }

        throw new AssertionError("Unknown entity " + (entity == null ? null : entity.getClass()));
    }

    public Entity getHandle() {
        return this.entity;
    }

    public Entity getHandleRaw() {
        return this.entity;
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{uuid=" + this.getUniqueId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final CraftEntity other = (CraftEntity) obj;
        return this.entity == other.entity; // There should never be duplicate entities with differing references
    }

    @Override
    public int hashCode() {
        // The UUID and thus hash code should never change (unlike the entity id)
        return this.getUniqueId().hashCode();
    }

    @Override
    public Location getLocation() {
        return CraftLocation.toBukkit(this.entity.position(), this.getWorld(), this.entity.getBukkitYaw(), this.entity.getXRot());
    }

    @Override
    public Location getLocation(Location loc) {
        if (loc != null) {
            loc.setWorld(this.getWorld());
            loc.setX(this.entity.getX());
            loc.setY(this.entity.getY());
            loc.setZ(this.entity.getZ());
            loc.setYaw(this.entity.getBukkitYaw());
            loc.setPitch(this.entity.getXRot());
        }

        return loc;
    }

    @Override
    public Vector getVelocity() {
        return CraftVector.toBukkit(this.entity.getDeltaMovement());
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        // Paper start - Warn server owners when plugins try to set super high velocities
        if (!(this instanceof org.bukkit.entity.Projectile || this instanceof org.bukkit.entity.Minecart) && isUnsafeVelocity(velocity)) {
            CraftServer.excessiveVelEx = new Exception("Excessive velocity set detected: tried to set velocity of entity " + entity.getScoreboardName() + " id #" + getEntityId() + " to (" + velocity.getX() + "," + velocity.getY() + "," + velocity.getZ() + ").");
        }
        // Paper end
        this.entity.setDeltaMovement(CraftVector.toVec3(velocity));
        this.entity.hurtMarked = true;
    }

    /**
     * Checks if the given velocity is not necessarily safe in all situations.
     * This function returning true does not mean the velocity is dangerous or to be avoided, only that it may be
     * a detriment to performance on the server.
     *
     * It is not to be used as a hard rule of any sort.
     * Paper only uses it to warn server owners in watchdog crashes.
     *
     * @param vel incoming velocity to check
     * @return if the velocity has the potential to be a performance detriment
     */
    private static boolean isUnsafeVelocity(Vector vel) {
        final double x = vel.getX();
        final double y = vel.getY();
        final double z = vel.getZ();

        if (x > 4 || x < -4 || y > 4 || y < -4 || z > 4 || z < -4) {
            return true;
        }

        return false;
    }

    @Override
    public double getHeight() {
        return this.getHandle().getBbHeight();
    }

    @Override
    public double getWidth() {
        return this.getHandle().getBbWidth();
    }

    @Override
    public BoundingBox getBoundingBox() {
        AABB bb = this.getHandle().getBoundingBox();
        return new BoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    @Override
    public boolean isOnGround() {
        if (this.entity instanceof AbstractArrow abstractArrow) {
            return abstractArrow.isInGround();
        }
        return this.entity.onGround();
    }

    @Override
    public boolean isInWater() {
        return this.entity.isInWater();
    }

    @Override
    public World getWorld() {
        return this.entity.level().getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        this.entity.setYRot(yaw);
        this.entity.setXRot(pitch);
        this.entity.yRotO = yaw;
        this.entity.xRotO = pitch;
        this.entity.setYHeadRot(yaw);
    }

    @Override
    public boolean teleport(Location location) {
        return this.teleport(location, TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        // Paper start - Teleport passenger API
        return teleport(location, cause, new io.papermc.paper.entity.TeleportFlag[0]);
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause, io.papermc.paper.entity.TeleportFlag... flags) {
        // Paper end
        Preconditions.checkArgument(location != null, "location cannot be null");
        location.checkFinite();
        // Paper start - Teleport passenger API
        Set<io.papermc.paper.entity.TeleportFlag> flagSet = new HashSet<>(List.of(flags)); // Wrap into list while multiple old flags link to the same new one
        boolean dismount = !flagSet.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_VEHICLE);
        boolean retainPassengers = flagSet.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_PASSENGERS);
        // Don't allow teleporting between worlds while keeping passengers
        if (flagSet.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_PASSENGERS) && this.entity.isVehicle() && location.getWorld() != this.getWorld()) {
            return false;
        }

        // Don't allow to teleport between worlds if remaining on vehicle
        if (!dismount && this.entity.isPassenger() && location.getWorld() != this.getWorld()) {
            return false;
        }
        // Paper end

        if ((!retainPassengers && this.entity.isVehicle()) || this.entity.isRemoved()) { // Paper - Teleport passenger API
            return false;
        }

        // Paper start - fix teleport event not being called
        org.bukkit.event.entity.EntityTeleportEvent event = new org.bukkit.event.entity.EntityTeleportEvent(
            this, this.getLocation(), location);
        // cancelling the event is handled differently for players and entities,
        // entities just stop teleporting, players will still teleport to the "from" location of the event
        if (!event.callEvent() || event.getTo() == null) {
            return false;
        }
        location = event.getTo();
        // Paper end

        // If this entity is riding another entity, we must dismount before teleporting.
        if (dismount) this.entity.stopRiding(); // Paper - Teleport passenger API

        // Let the server handle cross world teleports
        if (location.getWorld() != null && !location.getWorld().equals(this.getWorld())) {
            // Prevent teleportation to an other world during world generation
            Preconditions.checkState(!this.entity.generation, "Cannot teleport entity to an other world during world generation");
            this.entity.teleport(new TeleportTransition(((CraftWorld) location.getWorld()).getHandle(), CraftLocation.toVec3(location), Vec3.ZERO, location.getPitch(), location.getYaw(), Set.of(), TeleportTransition.DO_NOTHING, TeleportCause.PLUGIN));
            return true;
        }

        // entity.snapTo() throws no event, and so cannot be cancelled
        this.entity.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); // Paper - use proper moveTo, as per vanilla teleporting

        // Ensure passengers of entity are teleported
        if (retainPassengers && this.entity.isVehicle()) this.entity.teleportPassengers();

        return true;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        return this.teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return this.teleport(destination.getLocation(), cause);
    }

    @Override
    public void lookAt(double x, double y, double z, LookAnchor entityAnchor) {
        this.getHandle().lookAt(toNmsAnchor(entityAnchor), new net.minecraft.world.phys.Vec3(x, y, z));
    }

    public static net.minecraft.commands.arguments.EntityAnchorArgument.Anchor toNmsAnchor(LookAnchor nmsAnchor) {
        return switch (nmsAnchor) {
            case EYES -> net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.EYES;
            case FEET -> net.minecraft.commands.arguments.EntityAnchorArgument.Anchor.FEET;
        };
    }

    public static LookAnchor toApiAnchor(net.minecraft.commands.arguments.EntityAnchorArgument.Anchor playerAnchor) {
        return switch (playerAnchor) {
            case EYES -> LookAnchor.EYES;
            case FEET -> LookAnchor.FEET;
        };
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        Preconditions.checkState(!this.entity.generation, "Cannot get nearby entities during world generation");
        org.spigotmc.AsyncCatcher.catchOp("getNearbyEntities"); // Spigot

        List<Entity> entities = this.getHandle().level().getEntities(this.entity, this.entity.getBoundingBox().inflate(x, y, z), Predicates.alwaysTrue());
        List<org.bukkit.entity.Entity> result = new java.util.ArrayList<>(entities.size());

        for (Entity entity : entities) {
            result.add(entity.getBukkitEntity());
        }
        return result;
    }

    @Override
    public int getEntityId() {
        return this.getHandle().getId();
    }

    @Override
    public int getFireTicks() {
        return this.getHandle().getRemainingFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return this.getHandle().getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        this.getHandle().setRemainingFireTicks(ticks);
    }

    @Override
    @Deprecated
    public void setVisualFire(boolean fire) {
        setVisualFire(fire ? TriState.TRUE : TriState.NOT_SET);
    }

    @Override
    public void setVisualFire(final TriState fire) {
        Preconditions.checkArgument(fire != null, "TriState cannot be null");
        this.getHandle().visualFire = fire;
    }

    @Override
    public boolean isVisualFire() {
        return getVisualFire().toBooleanOrElse(false);
    }

    @Override
    public TriState getVisualFire() {
        return this.getHandle().visualFire;
    }

    @Override
    public int getFreezeTicks() {
        return this.getHandle().getTicksFrozen();
    }

    @Override
    public int getMaxFreezeTicks() {
        return this.getHandle().getTicksRequiredToFreeze();
    }

    @Override
    public void setFreezeTicks(int ticks) {
        Preconditions.checkArgument(0 <= ticks, "Ticks (%s) cannot be less than 0", ticks);

        this.getHandle().setTicksFrozen(ticks);
    }

    @Override
    public boolean isFrozen() {
        return this.getHandle().isFullyFrozen();
    }

    @Override
    public boolean isFreezeTickingLocked() {
        return this.entity.freezeLocked;
    }

    @Override
    public void lockFreezeTicks(boolean locked) {
        this.entity.freezeLocked = locked;
    }

    @Override
    public void remove() {
        this.entity.pluginRemoved = true;
        this.entity.discard(this.getHandle().generation ? null : EntityRemoveEvent.Cause.PLUGIN);
    }

    @Override
    public boolean isDead() {
        return !this.entity.isAlive();
    }

    @Override
    public boolean isValid() {
        return this.entity.isAlive() && this.entity.valid;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public boolean isPersistent() {
        return this.entity.persist;
    }

    @Override
    public void setPersistent(boolean persistent) {
        this.entity.persist = persistent;
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        return this.isEmpty() ? null : this.getHandle().getPassengers().getFirst().getBukkitEntity();
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof CraftEntity) {
            this.eject();
            return ((CraftEntity) passenger).getHandle().startRiding(this.getHandle());
        } else {
            return false;
        }
    }

    @Override
    public List<org.bukkit.entity.Entity> getPassengers() {
        return Lists.newArrayList(Lists.transform(this.getHandle().getPassengers(), (Function<Entity, org.bukkit.entity.Entity>) Entity::getBukkitEntity));
    }

    @Override
    public boolean addPassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");

        return ((CraftEntity) passenger).getHandle().startRiding(this.getHandle(), true);
    }

    @Override
    public boolean removePassenger(org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");

        ((CraftEntity) passenger).getHandle().stopRiding();
        return true;
    }

    @Override
    public boolean isEmpty() {
        return !this.getHandle().isVehicle();
    }

    @Override
    public boolean eject() {
        if (this.isEmpty()) {
            return false;
        }

        this.getHandle().ejectPassengers();
        return true;
    }

    @Override
    public ItemStack getPickItemStack() {
        net.minecraft.world.item.ItemStack stack = this.getHandle().getPickResult();
        return stack == null ? ItemStack.empty() : stack.asBukkitCopy();
    }

    @Override
    public float getFallDistance() {
        return (float) this.getHandle().fallDistance;
    }

    @Override
    public void setFallDistance(float distance) {
        this.getHandle().fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        this.lastDamageEvent = event;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        return this.lastDamageEvent;
    }

    @Override
    public UUID getUniqueId() {
        return this.entity.getUUID();
    }

    @Override
    public int getTicksLived() {
        return this.getHandle().totalEntityAge;
    }

    @Override
    public void setTicksLived(int value) {
        Preconditions.checkArgument(value > 0, "Age value (%s) must be greater than 0", value);
        this.getHandle().tickCount = value;
        this.getHandle().totalEntityAge = value;
    }

    @Override
    public final EntityType getType() {
        return this.entityType;
    }

    @Override
    public void playEffect(EntityEffect effect) {
        Preconditions.checkArgument(effect != null, "Entity effect cannot be null");
        Preconditions.checkState(!this.entity.generation, "Cannot play effect during world generation");
        Preconditions.checkArgument(effect.isApplicableTo(this), "Entity effect cannot apply to this entity");

        this.getHandle().level().broadcastEntityEvent(this.getHandle(), effect.getData());
    }

    @Override
    public Sound getSwimSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().getSwimSound());
    }

    @Override
    public Sound getSwimSplashSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().getSwimSplashSound());
    }

    @Override
    public Sound getSwimHighSpeedSplashSound() {
        return CraftSound.minecraftToBukkit(this.getHandle().getSwimHighSpeedSplashSound());
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isInsideVehicle() {
        return this.getHandle().isPassenger();
    }

    @Override
    public boolean leaveVehicle() {
        if (!this.isInsideVehicle()) {
            return false;
        }

        this.getHandle().stopRiding();
        return true;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
        if (!this.isInsideVehicle()) {
            return null;
        }

        return this.getHandle().getVehicle().getBukkitEntity();
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        final Component name = this.getHandle().getCustomName();
        return name != null ? io.papermc.paper.adventure.PaperAdventure.asAdventure(name) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getHandle().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }

    @Override
    public net.kyori.adventure.pointer.Pointers pointers() {
        if (this.adventure$pointers == null) {
            this.adventure$pointers = net.kyori.adventure.pointer.Pointers.builder()
                .withDynamic(net.kyori.adventure.identity.Identity.DISPLAY_NAME, this::name)
                .withDynamic(net.kyori.adventure.identity.Identity.UUID, this::getUniqueId)
                .withStatic(net.kyori.adventure.permission.PermissionChecker.POINTER, this::permissionValue)
                .build();
        }

        return this.adventure$pointers;
    }

    @Override
    public void setCustomName(String name) {
        // sane limit for name length
        if (name != null && name.length() > 256) {
            name = name.substring(0, 256);
        }

        this.getHandle().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public String getCustomName() {
        Component name = this.getHandle().getCustomName();

        if (name == null) {
            return null;
        }

        return CraftChatMessage.fromComponent(name);
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        this.getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.getHandle().isCustomNameVisible();
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        if (this.getHandle().visibleByDefault != visible) {
            if (visible) {
                // Making visible by default, reset and show to all players
                for (Player player : this.server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndShowEntity(this);
                }
            } else {
                // Hiding by default, reset and hide from all players
                for (Player player : this.server.getOnlinePlayers()) {
                    ((CraftPlayer) player).resetAndHideEntity(this);
                }
            }

            this.getHandle().visibleByDefault = visible;
        }
    }

    @Override
    public boolean isVisibleByDefault() {
        return this.getHandle().visibleByDefault;
    }

    @Override
    public Set<Player> getTrackedBy() {
        Preconditions.checkState(!this.entity.generation, "Cannot get tracking players during world generation");
        ImmutableSet.Builder<Player> players = ImmutableSet.builder();

        ServerLevel world = ((CraftWorld) this.getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(this.getEntityId());

        if (entityTracker != null) {
            for (ServerPlayerConnection connection : entityTracker.seenBy) {
                players.add(connection.getPlayer().getBukkitEntity());
            }
        }

        return players.build();
    }

    @Override
    public boolean isTrackedBy(final Player player) {
        Preconditions.checkState(!this.entity.generation, "Cannot check tracking players during world generation");
        Preconditions.checkArgument(player != null, "Player cannot be null");

        ServerLevel world = ((CraftWorld) this.getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(this.getEntityId());
        if (entityTracker == null) return false;

        return entityTracker.seenBy.contains(((CraftPlayer) player).getHandle().connection);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(String... messages) {

    }

    @Override
    public void sendMessage(UUID sender, String message) {
        this.sendMessage(message); // Most entities don't know about senders
    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        this.sendMessage(messages); // Most entities don't know about senders
    }

    @Override
    public String getName() {
        return CraftChatMessage.fromComponent(this.getHandle().getName());
    }

    @Override
    public net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component name() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getHandle().getName());
    }

    @Override
    public net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component teamDisplayName() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.getHandle().getDisplayName());
    }

    @Override
    public boolean isPermissionSet(String name) {
        return CraftEntity.getPermissibleBase().isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return CraftEntity.getPermissibleBase().isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return CraftEntity.getPermissibleBase().hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return CraftEntity.getPermissibleBase().hasPermission(perm);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, name, value);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return CraftEntity.getPermissibleBase().addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        CraftEntity.getPermissibleBase().removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        CraftEntity.getPermissibleBase().recalculatePermissions();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return CraftEntity.getPermissibleBase().getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return CraftEntity.getPermissibleBase().isOp();
    }

    @Override
    public void setOp(boolean value) {
        CraftEntity.getPermissibleBase().setOp(value);
    }

    @Override
    public void setGlowing(boolean flag) {
        this.getHandle().setGlowingTag(flag);
    }

    @Override
    public boolean isGlowing() {
        return this.getHandle().isCurrentlyGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        this.getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return this.getHandle().isInvulnerableToBase(this.getHandle().damageSources().generic());
    }

    @Override
    public boolean isSilent() {
        return this.getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        this.getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !this.getHandle().isNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        this.getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return this.getHandle().getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        this.getHandle().setPortalCooldown(cooldown);
    }

    @Override
    public Set<String> getScoreboardTags() {
        return this.getHandle().getTags();
    }

    @Override
    public boolean addScoreboardTag(String tag) {
        return this.getHandle().addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(String tag) {
        return this.getHandle().removeTag(tag);
    }

    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(this.getHandle().getPistonPushReaction().ordinal());
    }

    @Override
    public BlockFace getFacing() {
        // Use this method over getDirection because it handles boats and minecarts.
        return CraftBlock.notchToBlockFace(this.getHandle().getMotionDirection());
    }

    @Override
    public CraftPersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    @Override
    public Pose getPose() {
        return Pose.values()[this.getHandle().getPose().ordinal()];
    }

    @Override
    public void setSneaking(boolean sneak) {
        this.getHandle().setShiftKeyDown(sneak);
    }

    @Override
    public boolean isSneaking() {
        return this.getHandle().isShiftKeyDown();
    }

    @Override
    public void setPose(Pose pose, boolean fixed) {
        Preconditions.checkNotNull(pose, "Pose cannot be null");
        final Entity handle = this.getHandle();
        handle.fixedPose = false;
        handle.setPose(net.minecraft.world.entity.Pose.values()[pose.ordinal()]);
        handle.fixedPose = fixed;
    }

    @Override
    public boolean hasFixedPose() {
        return this.getHandle().fixedPose;
    }

    @Override
    public SpawnCategory getSpawnCategory() {
        return CraftSpawnCategory.toBukkit(this.getHandle().getType().getCategory());
    }

    @Override
    public boolean isInWorld() {
        return this.getHandle().inWorld;
    }

    @Override
    public String getAsString() {
        final TagValueOutput output = TagValueOutput.createDiscardingWithContext(this.getHandle().registryAccess());
        if (!this.getHandle().saveAsPassenger(output, false, true, true)) {
            return null;
        }

        return output.buildResult().toString();
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return CraftEntitySnapshot.create(this);
    }

    @Override
    public org.bukkit.entity.Entity copy() {
        Entity copy = this.copy(this.getHandle().level());
        Preconditions.checkArgument(copy != null, "Error creating new entity.");

        return copy.getBukkitEntity();
    }

    @Override
    public org.bukkit.entity.Entity copy(Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Location has no world");

        Entity copy = this.copy(((CraftWorld) location.getWorld()).getHandle());
        Preconditions.checkArgument(copy != null, "Error creating new entity.");

        copy.setPos(location.getX(), location.getY(), location.getZ());
        return location.getWorld().addEntity(copy.getBukkitEntity());
    }

    private Entity copy(net.minecraft.world.level.Level level) {
        final TagValueOutput output = TagValueOutput.createDiscardingWithContext(level.registryAccess());
        this.getHandle().saveAsPassenger(output, false, true, true);

        return net.minecraft.world.entity.EntityType.loadEntityRecursive(output.buildResult(), level, EntitySpawnReason.LOAD, java.util.function.Function.identity());
    }

    public void storeBukkitValues(ValueOutput c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.store("BukkitValues", CompoundTag.CODEC, this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(ValueInput c) {
        c.read("BukkitValues", CompoundTag.CODEC).ifPresent(this.persistentDataContainer::putAll);
    }

    protected CompoundTag save() {
        final TagValueOutput tagValueOutput = TagValueOutput.createDiscardingWithContext(this.getHandle().registryAccess());

        tagValueOutput.putString(Entity.TAG_ID, this.getHandle().getEncodeId()); // todo NPE?
        this.getHandle().saveWithoutId(tagValueOutput);

        return tagValueOutput.buildResult();
    }

    // re-sends the spawn entity packet to updated values which cannot be updated otherwise
    protected void update() {
        if (!this.getHandle().isAlive()) {
            return;
        }

        ServerLevel world = ((CraftWorld) this.getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(this.getEntityId());

        if (entityTracker == null) {
            return;
        }

        for (final ServerPlayerConnection connection : entityTracker.seenBy) {
            this.getHandle().resendPossiblyDesyncedEntityData(connection.getPlayer());
        }
    }

    public void update(ServerPlayer player) {
        if (!this.getHandle().isAlive()) {
            return;
        }

        ServerLevel world = ((CraftWorld) this.getWorld()).getHandle();
        ChunkMap.TrackedEntity entityTracker = world.getChunkSource().chunkMap.entityMap.get(this.getEntityId());

        if (entityTracker == null) {
            return;
        }

        player.connection.send(this.getHandle().getAddEntityPacket(entityTracker.serverEntity));
    }

    private static PermissibleBase getPermissibleBase() {
        if (CraftEntity.perm == null) {
            CraftEntity.perm = new PermissibleBase(new ServerOperator() {

                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {

                }
            });
        }
        return CraftEntity.perm;
    }

    // Paper start - more teleport API / async chunk API
    @Override
    public CompletableFuture<Boolean> teleportAsync(final Location location, final TeleportCause cause, final TeleportFlag... teleportFlags) {
        Preconditions.checkArgument(location != null, "location");
        location.checkFinite();
        Location locationClone = location.clone(); // clone so we don't need to worry about mutations after this call.

        ServerLevel world = ((CraftWorld)locationClone.getWorld()).getHandle();
        CompletableFuture<Boolean> ret = new java.util.concurrent.CompletableFuture<>();

        world.loadChunksForMoveAsync(this.getHandle().getBoundingBoxAt(locationClone.getX(), locationClone.getY(), locationClone.getZ()),
            this instanceof CraftPlayer ? ca.spottedleaf.concurrentutil.util.Priority.HIGHER : ca.spottedleaf.concurrentutil.util.Priority.NORMAL, (list) -> {
                MinecraftServer.getServer().scheduleOnMain(() -> {
                    final ServerChunkCache chunkCache = world.getChunkSource();
                    for (final net.minecraft.world.level.chunk.ChunkAccess chunk : list) {
                        chunkCache.addTicketAtLevel(TicketType.POST_TELEPORT, chunk.getPos(), 33);
                    }
                    try {
                        ret.complete(CraftEntity.this.teleport(locationClone, cause, teleportFlags) ? Boolean.TRUE : Boolean.FALSE);
                    } catch (Throwable throwable) {
                        if (throwable instanceof ThreadDeath) {
                            throw (ThreadDeath)throwable;
                        }
                        MinecraftServer.LOGGER.error("Failed to teleport entity " + CraftEntity.this, throwable);
                        ret.completeExceptionally(throwable);
                    }
                });
            });

        return ret;
    }
    // Paper end - more teleport API / async chunk API

    private final org.bukkit.entity.Entity.Spigot spigot = new org.bukkit.entity.Entity.Spigot() {

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent... components) {
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent component) {
        }
    };

    public org.bukkit.entity.Entity.Spigot spigot() {
        return this.spigot;
    }

    @Override
    public Location getOrigin() {
        if (this.getHandle().origin == null) {
            return null;
        }

        World world = this.getWorld();
        if (this.getHandle().originWorld != null) {
            world = org.bukkit.Bukkit.getWorld(this.getHandle().originWorld);
        }

        return CraftVector.toBukkit(this.getHandle().origin).toLocation(world);
    }

    @Override
    public boolean fromMobSpawner() {
        return this.getHandle().spawnedViaMobSpawner;
    }

    @Override
    public org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason getEntitySpawnReason() {
        return this.getHandle().spawnReason;
    }

    @Override
    public boolean isUnderWater() {
        return this.getHandle().isUnderWater();
    }

    @Override
    public boolean isInRain() {
        return this.getHandle().isInRain();
    }

    @Override
    public boolean isInLava() {
        return this.getHandle().isInLava();
    }

    @Override
    public boolean isTicking() {
        return this.getHandle().isTicking();
    }

    @Override
    public Set<org.bukkit.entity.Player> getTrackedPlayers() {
        ServerLevel world = (net.minecraft.server.level.ServerLevel)this.entity.level();
        ChunkMap.TrackedEntity tracker = world == null ? null : world.getChunkSource().chunkMap.entityMap.get(this.entity.getId());
        if (tracker == null) {
            return java.util.Collections.emptySet();
        }

        Set<org.bukkit.entity.Player> set = new java.util.HashSet<>(tracker.seenBy.size());
        for (net.minecraft.server.network.ServerPlayerConnection connection : tracker.seenBy) {
            set.add(connection.getPlayer().getBukkitEntity().getPlayer());
        }
        return set;
    }

    @Override
    public boolean spawnAt(Location location, CreatureSpawnEvent.SpawnReason reason) {
        Preconditions.checkNotNull(location, "location cannot be null");
        Preconditions.checkNotNull(reason, "reason cannot be null");
        this.entity.setLevel(((CraftWorld) location.getWorld()).getHandle());
        this.entity.setPos(location.getX(), location.getY(), location.getZ());
        this.entity.setRot(location.getYaw(), location.getPitch());
        final boolean spawned = !this.entity.valid && this.entity.level().addFreshEntity(this.entity, reason);
        if (!spawned) return false; // Do not attempt to spawn rest if root was not spawned in
        // Like net.minecraft.world.level.ServerLevelAccessor.addFreshEntityWithPassengers(net.minecraft.world.entity.Entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason)
        this.entity.getIndirectPassengers().forEach(e -> e.level().addFreshEntity(e, reason));
        return true;
    }

    @Override
    public boolean isInPowderedSnow() {
        return this.getHandle().isInPowderSnow || this.getHandle().wasInPowderSnow; // depending on the location in the entity "tick" either could be needed.
    }

    @Override
    public double getX() {
        return this.entity.getX();
    }

    @Override
    public double getY() {
        return this.entity.getY();
    }

    @Override
    public double getZ() {
        return this.entity.getZ();
    }

    @Override
    public float getPitch() {
        return this.entity.getXRot();
    }

    @Override
    public float getYaw() {
        return this.entity.getBukkitYaw();
    }

    @Override
    public boolean isInvisible() {
        return this.getHandle().isInvisible();
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.getHandle().persistentInvisibility = invisible;
        this.getHandle().setSharedFlag(Entity.FLAG_INVISIBLE, invisible);
    }

    @Override
    public void setNoPhysics(boolean noPhysics) {
        this.getHandle().noPhysics = noPhysics;
    }

    @Override
    public boolean hasNoPhysics() {
        return this.getHandle().noPhysics;
    }

    @Override
    public boolean collidesAt(@org.jetbrains.annotations.NotNull Location location) {
        net.minecraft.world.phys.AABB box = this.getHandle().getBoundingBoxAt(location.getX(), location.getY(), location.getZ());

        return !this.getHandle().level().noCollision(this.getHandle(), box);
    }

    @Override
    public boolean wouldCollideUsing(@org.jetbrains.annotations.NotNull BoundingBox boundingBox) {
        net.minecraft.world.phys.AABB box = new AABB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());

        return !this.getHandle().level().noCollision(this.getHandle(), box);
    }

    @Override
    public String getScoreboardEntryName() {
        return this.getHandle().getScoreboardName();
    }

    @Override
    public void broadcastHurtAnimation(java.util.Collection<Player> players) {
        //noinspection SuspiciousMethodCalls
        Preconditions.checkArgument(!players.contains(this), "Cannot broadcast hurt animation to self without a yaw");
        for (final org.bukkit.entity.Player player : players) {
            ((CraftPlayer) player).sendHurtAnimation(0, this);
        }
    }
    @Override
    public <T> @Nullable T getData(@NotNull final DataComponentType.Valued<T> type) {
        return this.entity.get(io.papermc.paper.datacomponent.PaperDataComponentType.bukkitToMinecraft(type));
    }

    @Override
    public <T> @Nullable T getDataOrDefault(@NotNull final DataComponentType.Valued<? extends T> type, @Nullable final T fallback) {
        return this.entity.getOrDefault(io.papermc.paper.datacomponent.PaperDataComponentType.bukkitToMinecraft(type), fallback);
    }

    @Override
    public boolean hasData(final @NotNull DataComponentType type) {
        return this.entity.get(io.papermc.paper.datacomponent.PaperDataComponentType.bukkitToMinecraft(type)) != null;
    }

}
