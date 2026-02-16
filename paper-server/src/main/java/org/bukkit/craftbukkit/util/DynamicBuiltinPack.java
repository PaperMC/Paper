package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.util.PaperCodecs;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.SharedConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.util.FileUtil;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;

public record DynamicBuiltinPack(String name, Component description) {
    public DynamicBuiltinPack {
        Preconditions.checkArgument(FileUtil.isValidPathSegment(name) && FileUtil.isPathPartPortable(name), "Invalid name %s", name);
    }

    private static final Codec<PackMetadataSection> CODEC = PaperCodecs.packMetaCodec(PackMetadataSection.SERVER_TYPE);

    void refreshMetadata(final LevelPathAccess access) throws IOException {
        final Path mcmetaFile = this.getDatapackDir(access).resolve(PackResources.PACK_META);
        if (Files.notExists(mcmetaFile)) {
            return;
        }

        final PackMetadataSection packMetadataSection = CODEC.parse(
            JsonOps.INSTANCE, StrictJsonParser.parse(Files.readString(mcmetaFile, StandardCharsets.UTF_8))
        ).getOrThrow(RuntimeException::new);

        final PackFormat currentVersion = SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA);
        if (!PackCompatibility.forVersion(packMetadataSection.supportedFormats(), currentVersion).isCompatible()) {
            if (!this.createMetadata(mcmetaFile, currentVersion, true)) {
                throw new IOException("Unable to update metadata for pack " + this.name);
            }
        }
    }

    public interface LevelPathAccess {

        LevelPathAccess SERVER = resource -> MinecraftServer.getServer().getWorldPath(resource);

        static LevelPathAccess fromStorageSource(final LevelStorageSource.LevelStorageAccess storage) {
            return storage::getLevelPath;
        }

        Path get(LevelResource resource);
    }

    public PackOutput data() {
        return new PackOutput(this.getDatapackDir(LevelPathAccess.SERVER));
    }

    private Path getDatapackDir(final LevelPathAccess access) {
        return access.get(LevelResource.DATAPACK_DIR).resolve(this.name);
    }

    public boolean createIfNeeded(final LevelPathAccess access) {
        final Path mcmetaFile = this.getDatapackDir(access).resolve(PackResources.PACK_META);
        if (Files.exists(mcmetaFile)) {
            return true;
        }
        return this.createMetadata(mcmetaFile, SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA), false);
    }

    private boolean createMetadata(final Path path, final PackFormat version, final boolean updating) { // inspired from the datapack command
        final PackMetadataSection packMetadataSection = new PackMetadataSection(this.description, version.minorRange());
        final JsonElement topMcmeta = CODEC.encodeStart(JsonOps.INSTANCE, packMetadataSection).getOrThrow(RuntimeException::new);

        try {
            if (!updating) {
                Files.createDirectory(path.getParent());
            }
            try (
                final BufferedWriter mcmetaFile = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
                final JsonWriter jsonWriter = new JsonWriter(mcmetaFile)
            ) {
                jsonWriter.setSerializeNulls(false);
                jsonWriter.setIndent("  ");
                GsonHelper.writeValue(jsonWriter, topMcmeta, null);
            }
            return true;
        } catch (final IOException ex) {
            return false;
        }
    }
}
