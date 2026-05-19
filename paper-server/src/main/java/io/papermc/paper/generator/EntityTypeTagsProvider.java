package io.papermc.paper.generator;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.EntityType;

public class EntityTypeTagsProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {

    public EntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.ENTITY_TYPE, provider, entityType -> entityType.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.HORSES)
            .add(
                EntityType.HORSE,
                EntityType.SKELETON_HORSE,
                EntityType.ZOMBIE_HORSE
            );
        this.tag(EntityTypeTags.MINECART)
            .add(
                EntityType.MINECART,
                EntityType.CHEST_MINECART,
                EntityType.FURNACE_MINECART,
                EntityType.HOPPER_MINECART,
                EntityType.SPAWNER_MINECART,
                EntityType.TNT_MINECART,
                EntityType.COMMAND_BLOCK_MINECART
            );
        this.tag(EntityTypeTags.SPLITTING_MOB)
            .add(
                EntityType.SLIME,
                EntityType.MAGMA_CUBE
            );
    }
}
