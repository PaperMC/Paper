package org.bukkit.craftbukkit.packs;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.InclusiveRange;
import org.bukkit.Bukkit;
import org.bukkit.FeatureFlag;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftFeatureFlag;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.packs.DataPack;

public class CraftDataPack implements DataPack {

    private final Pack handle;
    private final PackMetadataSection resourcePackInfo;

    public CraftDataPack(Pack handler) {
        this.handle = handler;
        try (PackResources iresourcepack = this.handle.resources.openPrimary(this.handle.location())) {
            this.resourcePackInfo = iresourcepack.getMetadataSection(PackMetadataSection.TYPE);
        } catch (IOException e) { // This is already called in NMS then if in NMS not happen is secure this not throw here
            throw new RuntimeException(e);
        }
    }

    public Pack getHandle() {
        return this.handle;
    }

    public String getRawId() {
        return this.getHandle().getId();
    }

    @Override
    public String getTitle() {
        return CraftChatMessage.fromComponent(this.getHandle().getTitle());
    }

    @Override
    public String getDescription() {
        return CraftChatMessage.fromComponent(this.getHandle().getDescription());
    }

    @Override
    public int getPackFormat() {
        return this.resourcePackInfo.packFormat();
    }

    @Override
    public int getMinSupportedPackFormat() {
        return this.resourcePackInfo.supportedFormats().orElse(new InclusiveRange<>(this.getPackFormat())).minInclusive();
    }

    @Override
    public int getMaxSupportedPackFormat() {
        return this.resourcePackInfo.supportedFormats().orElse(new InclusiveRange<>(this.getPackFormat())).maxInclusive();
    }

    @Override
    public boolean isRequired() {
        return this.getHandle().isRequired();
    }

    @Override
    public Compatibility getCompatibility() {
        return switch (this.getHandle().getCompatibility()) {
            case COMPATIBLE -> Compatibility.COMPATIBLE;
            case TOO_NEW -> Compatibility.NEW;
            case TOO_OLD -> Compatibility.OLD;
        };
    }

    @Override
    public boolean isEnabled() {
        return ((CraftServer) Bukkit.getServer()).getServer().getPackRepository().getSelectedIds().contains(this.getRawId());
    }

    @Override
    public DataPack.Source getSource() {
        if (this.getHandle().getPackSource() == PackSource.BUILT_IN) {
            return Source.BUILT_IN;
        } else if (this.getHandle().getPackSource() == PackSource.FEATURE) {
            return Source.FEATURE;
        } else if (this.getHandle().getPackSource() == PackSource.WORLD) {
            return Source.WORLD;
        } else if (this.getHandle().getPackSource() == PackSource.SERVER) {
            return Source.SERVER;
        }
        return Source.DEFAULT;
    }

    @Override
    public Set<FeatureFlag> getRequestedFeatures() {
        return CraftFeatureFlag.getFromNMS(this.getHandle().getRequestedFeatures()).stream().map(FeatureFlag.class::cast).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public NamespacedKey getKey() {
        return NamespacedKey.fromString(this.getRawId());
    }

    @Override
    public String toString() {
        String requestedFeatures = this.getRequestedFeatures().stream().map(featureFlag -> featureFlag.getKey().toString()).collect(Collectors.joining(","));
        return "CraftDataPack{rawId=" + this.getRawId() + ",id=" + this.getKey() + ",title=" + this.getTitle() + ",description=" + this.getDescription() + ",packformat=" + this.getPackFormat() + ",minSupportedPackFormat=" + this.getMinSupportedPackFormat() + ",maxSupportedPackFormat=" + this.getMaxSupportedPackFormat() + ",compatibility=" + this.getCompatibility() + ",source=" + this.getSource() + ",enabled=" + this.isEnabled() + ",requestedFeatures=[" + requestedFeatures + "]}";
    }
}
