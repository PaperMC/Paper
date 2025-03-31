package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.data.EntityTypeData;
import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.typewriter.preset.model.EnumConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;

import static io.papermc.generator.utils.Formatting.quoted;

public class EntityTypeRewriter extends EnumRegistryRewriter<EntityType<?>> {

    public EntityTypeRewriter() {
        super(Registries.ENTITY_TYPE);
    }

    @Override
    protected void rewriteConstant(EnumConstant.Builder builder, Holder.Reference<EntityType<?>> reference) {
        EntityTypeData data = Objects.requireNonNull(DataFileLoader.get(DataFiles.ENTITY_TYPES).get(reference.key()), () -> "Missing entity type data for " + reference);
        String path = reference.key().location().getPath();
        List<String> arguments = new ArrayList<>(4);
        arguments.add(quoted(path));
        arguments.add(this.importCollector.getShortName(data.api()).concat(".class"));
        arguments.add(Integer.toString(data.legacyId()));

        if (!reference.value().canSummon()) {
            arguments.add(Boolean.FALSE.toString());
        }
        builder.arguments(arguments);
    }
}
