package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
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
import org.bukkit.craftbukkit.util.CraftLocation;
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

    private LodestoneTracker tracker;

    CraftMetaCompass(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof final CraftMetaCompass compassMeta)) {
            return;
        }
        this.tracker = compassMeta.tracker;
    }

    CraftMetaCompass(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);
        getOrEmpty(tag, CraftMetaCompass.LODESTONE_TARGET).ifPresent((lodestoneTarget) -> {
            this.tracker = lodestoneTarget;
        });
    }

    CraftMetaCompass(Map<String, Object> map) {
        super(map);
        String lodestoneWorldKey = SerializableMeta.getString(map, CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, true);
        if (lodestoneWorldKey != null) {
            ResourceKey<net.minecraft.world.level.Level> lodestoneWorld = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryParse(lodestoneWorldKey));
            int lodestoneX = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_X.BUKKIT);
            int lodestoneY = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT);
            int lodestoneZ = (Integer) map.get(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT);
            this.tracker = new LodestoneTracker(Optional.of(new GlobalPos(lodestoneWorld, new BlockPos(lodestoneX, lodestoneY, lodestoneZ))), true);
        } else {
            // legacy
            Location lodestone = SerializableMeta.getObject(Location.class, map, CraftMetaCompass.LODESTONE_POS.BUKKIT, true);
            if (lodestone != null && lodestone.getWorld() != null) {
                this.setLodestone(lodestone);
            }
        }

        final Optional<Boolean> tracked = SerializableMeta.getObjectOptionally(Boolean.class, map, CraftMetaCompass.LODESTONE_TRACKED.BUKKIT, true);
        final Optional<GlobalPos> trackedPos = this.tracker != null ? this.tracker.target() : Optional.empty();
        tracked.ifPresent(isTracked -> this.tracker = new LodestoneTracker(trackedPos, isTracked));
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.tracker != null) {
            tag.put(CraftMetaCompass.LODESTONE_TARGET, this.tracker);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isCompassEmpty();
    }

    boolean isCompassEmpty() {
        return this.tracker == null;
    }

    @Override
    public CraftMetaCompass clone() {
        CraftMetaCompass clone = ((CraftMetaCompass) super.clone());
        return clone;
    }

    @Override
    public boolean hasLodestone() {
        return this.tracker != null && this.tracker.target().isPresent();
    }

    @Override
    public Location getLodestone() {
        if (this.tracker == null || this.tracker.target().isEmpty()) {
            return null;
        }
        ServerLevel level = MinecraftServer.getServer().getLevel(this.tracker.target().get().dimension());
        World world = level != null ? level.getWorld() : null;
        return org.bukkit.craftbukkit.util.CraftLocation.toBukkit(this.tracker.target().get().pos(), world); // world may be null here, if the referenced world is not loaded
    }

    @Override
    public void setLodestone(Location lodestone) {
        Preconditions.checkArgument(lodestone == null || lodestone.getWorld() != null, "world is null");
        if (lodestone == null) {
            if (this.tracker != null) {
                this.tracker = new LodestoneTracker(java.util.Optional.empty(), this.tracker.tracked());
            }
        } else {
            GlobalPos pos = GlobalPos.of(
                ((CraftWorld) lodestone.getWorld()).getHandle().dimension(),
                CraftLocation.toBlockPosition(lodestone)
            );
            boolean tracked = this.tracker == null || this.tracker.tracked();
            this.tracker = new LodestoneTracker(Optional.of(pos), tracked);
        }
    }

    @Override
    public boolean isLodestoneTracked() {
        return this.tracker != null && this.tracker.tracked();
    }

    @Override
    public void setLodestoneTracked(boolean tracked) {
        final Optional<GlobalPos> trackedPos = this.tracker != null ? this.tracker.target() : Optional.empty();
        this.tracker = new LodestoneTracker(trackedPos, tracked);
    }

    @Override
    public boolean isLodestoneCompass() {
        return this.tracker != null;
    }

    @Override
    public void clearLodestone() {
        this.tracker = null;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.isLodestoneCompass()) {
            hash = 73 * hash + this.tracker.hashCode();
        }

        return original != hash ? CraftMetaCompass.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaCompass other) {
            return Objects.equals(this.tracker, other.tracker);
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

        if (this.isLodestoneCompass()) {
            if (this.tracker.target().isPresent()) {
                builder.put(CraftMetaCompass.LODESTONE_POS_WORLD.BUKKIT, this.tracker.target().get().dimension().location().toString());
                builder.put(CraftMetaCompass.LODESTONE_POS_X.BUKKIT, this.tracker.target().get().pos().getX());
                builder.put(CraftMetaCompass.LODESTONE_POS_Y.BUKKIT, this.tracker.target().get().pos().getY());
                builder.put(CraftMetaCompass.LODESTONE_POS_Z.BUKKIT, this.tracker.target().get().pos().getZ());
            }
            builder.put(CraftMetaCompass.LODESTONE_TRACKED.BUKKIT, this.tracker.tracked());
        }

        return builder;
    }
}
