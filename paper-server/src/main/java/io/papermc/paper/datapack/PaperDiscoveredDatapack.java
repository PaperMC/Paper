package io.papermc.paper.datapack;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.bukkit.FeatureFlag;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperDiscoveredDatapack implements DiscoveredDatapack {

    private static final Map<PackSource, DatapackSource> PACK_SOURCES;
    static {
        PACK_SOURCES = ImmutableMap.<PackSource, DatapackSource>builder()
            .put(PackSource.DEFAULT, DatapackSource.DEFAULT)
            .put(PackSource.BUILT_IN, DatapackSource.BUILT_IN)
            .put(PackSource.FEATURE, DatapackSource.FEATURE)
            .put(PackSource.WORLD, DatapackSource.WORLD)
            .put(PackSource.SERVER, DatapackSource.SERVER)
            .put(PluginPackSource.INSTANCE, DatapackSource.PLUGIN)
            .buildOrThrow();
    }

    private final Pack pack;

    PaperDiscoveredDatapack(final Pack pack) {
        this.pack = pack;
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
    public Datapack.Compatibility getCompatibility() {
        return Datapack.Compatibility.valueOf(this.pack.getCompatibility().name());
    }

    @Override
    public Set<FeatureFlag> getRequiredFeatures() {
        return PaperFeatureFlagProviderImpl.fromNms(this.pack.getRequestedFeatures());
    }

    @Override
    public DatapackSource getSource() {
        return PACK_SOURCES.get(this.pack.location().source());
    }
}
