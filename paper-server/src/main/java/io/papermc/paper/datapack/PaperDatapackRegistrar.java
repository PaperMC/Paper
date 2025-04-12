package io.papermc.paper.datapack;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackDetector;
import net.minecraft.util.FileSystemUtil;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.DirectoryValidator;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@NullMarked
public class PaperDatapackRegistrar implements PaperRegistrar<BootstrapContext>, DatapackRegistrar {

    private static final Logger LOGGER = LogUtils.getClassLogger();

    private final PackDetector<Pack.ResourcesSupplier> detector;
    public final Map<String, Pack> discoveredPacks;
    private @Nullable BootstrapContext owner;

    public PaperDatapackRegistrar(final DirectoryValidator symlinkValidator, final Map<String, Pack> discoveredPacks) {
        this.detector = new FolderRepositorySource.FolderPackDetector(symlinkValidator);
        this.discoveredPacks = discoveredPacks;
    }

    @Override
    public void setCurrentContext(final @Nullable BootstrapContext owner) {
        this.owner = owner;
    }

    @Override
    public boolean hasPackDiscovered(final String name) {
        return this.discoveredPacks.containsKey(name);
    }

    @Override
    public DiscoveredDatapack getDiscoveredPack(final String name) {
        if (!this.hasPackDiscovered(name)) {
            throw new NoSuchElementException("No pack with id " + name + " was discovered");
        }
        return new PaperDiscoveredDatapack(this.discoveredPacks.get(name));
    }

    @Override
    public boolean removeDiscoveredPack(final String name) {
        return this.discoveredPacks.remove(name) != null;
    }

    @Override
    public @Unmodifiable Map<String, DiscoveredDatapack> getDiscoveredPacks() {
        final ImmutableMap.Builder<String, DiscoveredDatapack> builder = ImmutableMap.builderWithExpectedSize(this.discoveredPacks.size());
        for (final Map.Entry<String, Pack> entry : this.discoveredPacks.entrySet()) {
            builder.put(entry.getKey(), new PaperDiscoveredDatapack(entry.getValue()));
        }
        return builder.buildOrThrow();
    }

    @Override
    public @Nullable DiscoveredDatapack discoverPack(final URI uri, final String id, final Consumer<Configurer> configurer) throws IOException {
        Preconditions.checkState(this.owner != null, "Discovering packs is not supported outside of lifecycle events");
        return this.discoverPack(this.owner.getPluginMeta(), uri, id, configurer);
    }

    @Override
    public @Nullable DiscoveredDatapack discoverPack(final Path path, final String id, final Consumer<Configurer> configurer) throws IOException {
        Preconditions.checkState(this.owner != null, "Discovering packs is not supported outside of lifecycle events");
        return this.discoverPack(this.owner.getPluginMeta(), path, id, configurer);
    }

    @Override
    public @Nullable DiscoveredDatapack discoverPack(final PluginMeta pluginMeta, final URI uri, final String id, final Consumer<Configurer> configurer) throws IOException {
        return this.discoverPack(pluginMeta, FileSystemUtil.safeGetPath(uri), id, configurer);
    }

    @Override
    public @Nullable DiscoveredDatapack discoverPack(final PluginMeta pluginMeta, final Path path, final String id, final Consumer<Configurer> configurer) throws IOException {
        Preconditions.checkState(this.owner != null, "Discovering packs is not supported outside of lifecycle events");
        final List<ForbiddenSymlinkInfo> badLinks = new ArrayList<>();
        final Pack.ResourcesSupplier resourcesSupplier = this.detector.detectPackResources(path, badLinks);
        if (!badLinks.isEmpty()) {
            LOGGER.warn("Ignoring potential pack entry: {}", ContentValidationException.getMessage(path, badLinks));
            return null;
        } else if (resourcesSupplier != null) {
            final String packId = pluginMeta.getName() + "/" + id;
            final ConfigurerImpl configurerImpl = new ConfigurerImpl(Component.text(packId));
            configurer.accept(configurerImpl);
            final PackLocationInfo locInfo = new PackLocationInfo(packId,
                PaperAdventure.asVanilla(configurerImpl.title),
                PluginPackSource.INSTANCE,
                Optional.empty()
            );
            final Pack pack = Pack.readMetaAndCreate(locInfo,
                resourcesSupplier,
                PackType.SERVER_DATA,
                new PackSelectionConfig(
                    configurerImpl.autoEnableOnServerStart,
                    configurerImpl.position,
                    configurerImpl.fixedPosition
                ));
            if (pack != null) {
                this.discoveredPacks.put(packId, pack);
                return new PaperDiscoveredDatapack(pack);
            }
            return null;
        } else {
            LOGGER.info("Found non-pack entry '{}', ignoring", path);
            return null;
        }
    }

    static final class ConfigurerImpl implements Configurer {

        private Component title;
        private boolean autoEnableOnServerStart = false;
        private boolean fixedPosition = false;
        private Pack.Position position = Pack.Position.TOP;

        ConfigurerImpl(final Component title) {
            this.title = title;
        }

        @Override
        public Configurer title(final Component title) {
            this.title = title;
            return this;
        }

        @Override
        public Configurer autoEnableOnServerStart(final boolean autoEnableOnServerStart) {
            this.autoEnableOnServerStart = autoEnableOnServerStart;
            return this;
        }

        @Override
        public Configurer position(final boolean fixed, final Datapack.Position position) {
            this.fixedPosition = fixed;
            this.position = switch (position) {
                case TOP -> Pack.Position.TOP;
                case BOTTOM -> Pack.Position.BOTTOM;
            };
            return this;
        }
    }
}
