package io.papermc.paper.datapack;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.bukkit.FeatureFlag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperDatapack implements Datapack {

    private static final Map<PackSource, DatapackSource> PACK_SOURCES = new ConcurrentHashMap<>();
    static {
        PACK_SOURCES.put(PackSource.DEFAULT, DatapackSource.DEFAULT);
        PACK_SOURCES.put(PackSource.BUILT_IN, DatapackSource.BUILT_IN);
        PACK_SOURCES.put(PackSource.FEATURE, DatapackSource.FEATURE);
        PACK_SOURCES.put(PackSource.WORLD, DatapackSource.WORLD);
        PACK_SOURCES.put(PackSource.SERVER, DatapackSource.SERVER);
    }

    private final Pack pack;
    private final boolean enabled;

    PaperDatapack(final Pack pack, final boolean enabled) {
        this.pack = pack;
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return this.pack.getId();
    }

    @Override
    public Component getTitle() {
        return PaperAdventure.asAdventure(this.pack.getTitle());
    }

    @Override
    public Component getDescription() {
        return PaperAdventure.asAdventure(this.pack.getDescription());
    }

    @Override
    public boolean isRequired() {
        return this.pack.isRequired();
    }

    @Override
    public Compatibility getCompatibility() {
        return Datapack.Compatibility.valueOf(this.pack.getCompatibility().name());
    }

    @Override
    public Set<FeatureFlag> getRequiredFeatures() {
        return PaperFeatureFlagProviderImpl.fromNms(this.pack.getRequestedFeatures());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        final MinecraftServer server = MinecraftServer.getServer();
        final List<Pack> enabledPacks = new ArrayList<>(server.getPackRepository().getSelectedPacks());
        final @Nullable Pack packToChange = server.getPackRepository().getPack(this.getName());
        if (packToChange == null) {
            throw new IllegalStateException("Cannot toggle state of pack that doesn't exist: " + this.getName());
        }
        if (enabled == enabledPacks.contains(packToChange)) {
            return;
        }
        if (enabled) {
            packToChange.getDefaultPosition().insert(enabledPacks, packToChange, Pack::selectionConfig, false); // modeled off the default /datapack enable logic
        } else {
            enabledPacks.remove(packToChange);
        }
        server.reloadResources(enabledPacks.stream().map(Pack::getId).toList(), ServerResourcesReloadedEvent.Cause.PLUGIN);
    }

    @Override
    public DatapackSource getSource() {
        return PACK_SOURCES.computeIfAbsent(this.pack.location().source(), source -> new DatapackSourceImpl(source.toString()));
    }

    @Override
    public Component computeDisplayName() {
        return PaperAdventure.asAdventure(this.pack.getChatLink(this.enabled));
    }
}
