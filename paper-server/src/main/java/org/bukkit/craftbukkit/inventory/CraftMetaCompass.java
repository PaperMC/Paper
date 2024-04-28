package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.component.LodestoneTracker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.inventory.meta.CompassMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaCompass extends CraftMetaItem implements CompassMeta {

    static final ItemMetaKeyType<LodestoneTracker> LODESTONE_TARGET = new ItemMetaKeyType<>(DataComponents.LODESTONE_TRACKER, "LodestoneDimension");
    static final ItemMetaKey LODESTONE_POS = new ItemMetaKey("lodestone");
    static final ItemMetaKey LODESTONE_POS_WORLD = new ItemMetaKey("LodestonePosWorld");
    static final ItemMetaKey LODESTONE_POS_X = new ItemMetaKey("LodestonePosX");
    static final ItemMetaKey LODESTONE_POS_Y = new ItemMetaKey("LodestonePosY");
    static final ItemMetaKey LODESTONE_POS_Z = new ItemMetaKey("LodestonePosZ");
    static final ItemMetaKey LODESTONE_TRACKED = new ItemMetaKey("LodestoneTracked");

    private LodestoneTracker tracker; // Paper - use LodestoneTracker type

    CraftMetaCompass(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaCompass)) {
            return;
        }
        CraftMetaCompass compassMeta = (CraftMetaCompass) meta;
        this.tracker = compassMeta.tracker; // Paper - use LodestoneTracker type
    }

    CraftMetaCompass(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) { // Paper
        super(tag, extraHandledDcts); // Paper
        getOrEmpty(tag, CraftMetaCompass.LODESTONE_TARGET).ifPresent((lodestoneTarget) -> {
            this.tracker = lodestoneTarget; // Paper - use LodestoneTracker type
        });
    }

    CraftMetaCompass(Map<String, Object> map) {
        super(map);
        String lodestoneWorldString = SerializableMeta.getString(map, CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, true);
        if (lodestoneWorldString != null) {
            // Paper start - use LodestoneTracker type
            ResourceKey<net.minecraft.world.level.Level> lodestoneWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(lodestoneWorldString));
            int lodestoneX = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_X.BUKKIT);
            int lodestoneY = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT);
            int lodestoneZ = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT);
            this.tracker = new LodestoneTracker(Optional.of(new GlobalPos(lodestoneWorld, new BlockPos(lodestoneX, lodestoneY, lodestoneZ))), true);
            // Paper end - use LodestoneTracker type
        } else {
            // legacy
            Location lodestone = SerializableMeta.getObject(Location.class, map, CraftMetaCompass.LODESTONE_POS.BUKKIT, true);
            if (lodestone != null && lodestone.getWorld() != null) {
                this.setLodestone(lodestone);
            }
        }
        // Paper start - use LodestoneTracker type
        final Optional<Boolean> tracked = SerializableMeta.getObjectOptionally(Boolean.class, map, CraftMetaCompass.LODESTONE_TRACKED.BUKKIT, true);
        final Optional<GlobalPos> trackedPos = this.tracker != null ? this.tracker.target() : Optional.empty();
        tracked.ifPresent(isTracked -> this.tracker = new LodestoneTracker(trackedPos, isTracked));
        // Paper end - use LodestoneTracker type
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        // Paper start - use LodestoneTracker type
        if (this.tracker != null) {
            tag.put(CraftMetaCompass.LODESTONE_TARGET, this.tracker);
        }
        // Paper end - use LodestoneTracker type
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isCompassEmpty();
    }

    boolean isCompassEmpty() {
        return this.tracker == null; // Paper - use LodestoneTracker type
    }

    @Override
    public CraftMetaCompass clone() {
        CraftMetaCompass clone = ((CraftMetaCompass) super.clone());
        return clone;
    }

    @Override
    public boolean hasLodestone() {
        return this.tracker != null && this.tracker.target().isPresent(); // Paper - use LodestoneTracker type
    }

    @Override
    public Location getLodestone() {
        if (this.tracker == null || this.tracker.target().isEmpty()) { // Paper - use LodestoneTracker type
            return null;
        }
        ServerLevel worldServer = MinecraftServer.getServer().getLevel(this.tracker.target().get().dimension()); // Paper - use LodestoneTracker type
        World world = worldServer != null ? worldServer.getWorld() : null;
        return org.bukkit.craftbukkit.util.CraftLocation.toBukkit(this.tracker.target().get().pos(), world); // world may be null here, if the referenced world is not loaded // Paper - use LodestoneTracker type
    }

    @Override
    public void setLodestone(Location lodestone) {
        Preconditions.checkArgument(lodestone == null || lodestone.getWorld() != null, "world is null");
        if (lodestone == null) {
            // Paper start - use LodestoneTracker type
            if (this.tracker != null) {
                this.tracker = new LodestoneTracker(java.util.Optional.empty(), this.tracker.tracked()); // Paper - use LodestoneTracker type
            }
            // Paper end - use LodestoneTracker type
        } else {
            // Paper start - use LodestoneTracker type
            GlobalPos pos = GlobalPos.of(
                ((CraftWorld) lodestone.getWorld()).getHandle().dimension(),
                io.papermc.paper.util.MCUtil.toBlockPosition(lodestone)
            );
            boolean tracked = this.tracker == null || this.tracker.tracked();
            this.tracker = new LodestoneTracker(Optional.of(pos), tracked);
            // Paper end - use LodestoneTracker type
        }
    }

    @Override
    public boolean isLodestoneTracked() {
        return this.tracker != null && this.tracker.tracked(); // Paper - use LodestoneTracker type
    }

    @Override
    public void setLodestoneTracked(boolean tracked) {
        final Optional<GlobalPos> trackedPos = this.tracker != null ? this.tracker.target() : Optional.empty(); // Paper - use LodestoneTracker type
        this.tracker = new LodestoneTracker(trackedPos, tracked); // Paper - use LodestoneTracker type
    }

    // Paper start - Add more lodestone compass methods
    @Override
    public boolean isLodestoneCompass() {
        return this.tracker != null;
    }

    @Override
    public void clearLodestone() {
        this.tracker = null;
    }
    // Paper end - Add more lodestone compass methods

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.isLodestoneCompass()) {
            hash = 73 * hash + this.tracker.hashCode(); // Paper - use LodestoneTracker type
        }

        return original != hash ? CraftMetaCompass.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCompass) {
            CraftMetaCompass that = (CraftMetaCompass) meta;

            return java.util.Objects.equals(this.tracker, that.tracker); // Paper - use LodestoneTracker type
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCompass || this.isCompassEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.isLodestoneCompass()) { // Paper - use LodestoneTracker type
            // Paper start - use LodestoneTracker type
            if (this.tracker.target().isPresent()) {
                builder.put(CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, this.tracker.target().get().dimension().location().toString());
                builder.put(CraftMetaCompass.LODESTONE_POS_X.BUKKIT, this.tracker.target().get().pos().getX());
                builder.put(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT, this.tracker.target().get().pos().getY());
                builder.put(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT, this.tracker.target().get().pos().getZ());
            }
            builder.put(CraftMetaCompass.LODESTONE_TRACKED.BUKKIT, this.tracker.tracked());
            // Paper end - use LodestoneTracker type
        }

        return builder;
    }
}
