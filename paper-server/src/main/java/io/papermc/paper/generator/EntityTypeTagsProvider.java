package io.papermc.paper.generator;

import io.papermc.paper.generator.references.EntityTypeIds;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsProvider extends TagsProvider<EntityType<?>> {

    public EntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.ENTITY_TYPE, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.HORSES)
            .add(
                EntityTypeIds.HORSE,
                EntityTypeIds.SKELETON_HORSE,
                EntityTypeIds.ZOMBIE_HORSE
            );
        this.tag(EntityTypeTags.MINECART)
            .add(
                EntityTypeIds.MINECART,
                EntityTypeIds.CHEST_MINECART,
                EntityTypeIds.FURNACE_MINECART,
                EntityTypeIds.HOPPER_MINECART,
                EntityTypeIds.SPAWNER_MINECART,
                EntityTypeIds.TNT_MINECART,
                EntityTypeIds.COMMAND_BLOCK_MINECART
            );
        this.tag(EntityTypeTags.SPLITTING_MOB)
            .add(
                EntityTypeIds.SLIME,
                EntityTypeIds.MAGMA_CUBE,
                EntityTypeIds.SULFUR_CUBE
            );
    }
}
