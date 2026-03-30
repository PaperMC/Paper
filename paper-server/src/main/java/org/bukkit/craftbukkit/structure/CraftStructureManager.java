package org.bukkit.craftbukkit.structure;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

public class CraftStructureManager implements StructureManager {

    private final StructureTemplateManager structureManager;
    private final RegistryAccess registry;

    public CraftStructureManager(StructureTemplateManager structureManager, RegistryAccess registry) {
        this.structureManager = structureManager;
        this.registry = registry;
    }

    @Override
    public Map<NamespacedKey, Structure> getStructures() {
        Map<NamespacedKey, Structure> cachedStructures = new HashMap<>();
        for (Map.Entry<Identifier, Optional<StructureTemplate>> entry : this.structureManager.structureRepository.entrySet()) {
            entry.getValue().ifPresent(definedStructure -> cachedStructures.put(CraftNamespacedKey.fromMinecraft(entry.getKey()), new CraftStructure(definedStructure, this.registry)));
        }
        return Collections.unmodifiableMap(cachedStructures);
    }

    @Override
    public Structure getStructure(NamespacedKey structureKey) {
        Preconditions.checkArgument(structureKey != null, "NamespacedKey structureKey cannot be null");

        final Optional<StructureTemplate> definedStructure = this.structureManager.structureRepository.get(CraftNamespacedKey.toMinecraft(structureKey));
        if (definedStructure == null) {
            return null;
        }
        return definedStructure.map((s) -> new CraftStructure(s, this.registry)).orElse(null);
    }

    @Override
    public Structure loadStructure(NamespacedKey structureKey, boolean register) {
        Identifier id = this.createAndValidateStructureId(structureKey);

        Optional<StructureTemplate> structure = this.structureManager.structureRepository.getOrDefault(id, Optional.empty())
            .or(() -> this.structureManager.tryLoad(id));

        if (register) {
            this.structureManager.structureRepository.put(id, structure);
        }

        return structure.map((template) -> new CraftStructure(template, this.registry)).orElse(null);
    }

    @Override
    public Structure loadStructure(NamespacedKey structureKey) {
        return this.loadStructure(structureKey, true);
    }

    @Override
    public void saveStructure(NamespacedKey structureKey) {
        this.structureManager.save(this.createAndValidateStructureId(structureKey));
    }

    @Override
    public void saveStructure(NamespacedKey structureKey, Structure structure) throws IOException {
        Preconditions.checkArgument(structureKey != null, "NamespacedKey structure cannot be null");
        Preconditions.checkArgument(structure != null, "Structure cannot be null");

        File structureFile = this.getStructureFile(structureKey);
        Files.createDirectories(structureFile.toPath().getParent());
        this.saveStructure(structureFile, structure);
    }

    @Override
    public Structure registerStructure(NamespacedKey structureKey, Structure structure) {
        Preconditions.checkArgument(structureKey != null, "NamespacedKey structureKey cannot be null");
        Preconditions.checkArgument(structure != null, "Structure cannot be null");
        Identifier id = this.createAndValidateStructureId(structureKey);

        final Optional<StructureTemplate> optionalDefinedStructure = Optional.of(((CraftStructure) structure).getHandle());
        final Optional<StructureTemplate> previousStructure = this.structureManager.structureRepository.put(id, optionalDefinedStructure);
        return previousStructure == null ? null : previousStructure.map((s) -> new CraftStructure(s, this.registry)).orElse(null);
    }

    @Override
    public Structure unregisterStructure(NamespacedKey structureKey) {
        Preconditions.checkArgument(structureKey != null, "NamespacedKey structureKey cannot be null");
        Identifier id = this.createAndValidateStructureId(structureKey);

        final Optional<StructureTemplate> previousStructure = this.structureManager.structureRepository.remove(id);
        return previousStructure == null ? null : previousStructure.map((s) -> new CraftStructure(s, this.registry)).orElse(null);
    }

    @Override
    public void deleteStructure(NamespacedKey structureKey) throws IOException {
        this.deleteStructure(structureKey, true);
    }

    @Override
    public void deleteStructure(NamespacedKey structureKey, boolean unregister) throws IOException {
        Identifier id = CraftNamespacedKey.toMinecraft(structureKey);

        if (unregister) {
            this.structureManager.structureRepository.remove(id);
        }
        Path path = this.structureManager.worldTemplates().createAndValidatePathToStructure(id, StructureTemplateManager.WORLD_STRUCTURE_LISTER);
        Files.deleteIfExists(path);
    }

    @Override
    public File getStructureFile(NamespacedKey structureKey) {
        Identifier id = this.createAndValidateStructureId(structureKey);
        return this.structureManager.worldTemplates().createAndValidatePathToStructure(id, StructureTemplateManager.WORLD_STRUCTURE_LISTER).toFile();
    }

    @Override
    public Structure loadStructure(File file) throws IOException {
        Preconditions.checkArgument(file != null, "File cannot be null");

        FileInputStream fileinputstream = new FileInputStream(file);
        return this.loadStructure(fileinputstream);
    }

    @Override
    public Structure loadStructure(InputStream inputStream) throws IOException {
        Preconditions.checkArgument(inputStream != null, "inputStream cannot be null");

        return new CraftStructure(this.structureManager.resourceManagerSource.readStructure(TemplateSource.readStructure(inputStream)), this.registry);
    }

    @Override
    public void saveStructure(File file, Structure structure) throws IOException {
        Preconditions.checkArgument(file != null, "file cannot be null");
        Preconditions.checkArgument(structure != null, "structure cannot be null");

        FileOutputStream fileoutputstream = new FileOutputStream(file);
        this.saveStructure(fileoutputstream, structure);
    }

    @Override
    public void saveStructure(OutputStream outputStream, Structure structure) throws IOException {
        Preconditions.checkArgument(outputStream != null, "outputStream cannot be null");
        Preconditions.checkArgument(structure != null, "structure cannot be null");

        CompoundTag tag = ((CraftStructure) structure).getHandle().save(new CompoundTag());
        NbtIo.writeCompressed(tag, outputStream);
    }

    @Override
    public Structure createStructure() {
        return new CraftStructure(new StructureTemplate(), this.registry);
    }

    private Identifier createAndValidateStructureId(NamespacedKey structureKey) {
        Preconditions.checkArgument(structureKey != null, "NamespacedKey structureKey cannot be null");

        Identifier id = CraftNamespacedKey.toMinecraft(structureKey);
        Preconditions.checkArgument(!id.getPath().contains("//"), "Resource id for Structures cannot contain \"//\"");
        return id;
    }

    @Override
    public Structure copy(Structure structure) {
        Preconditions.checkArgument(structure != null, "Structure cannot be null");
        return new CraftStructure(this.structureManager.resourceManagerSource.readStructure(((CraftStructure) structure).getHandle().save(new CompoundTag())), this.registry);
    }
}
