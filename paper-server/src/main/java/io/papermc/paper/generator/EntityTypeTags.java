package io.papermc.paper.generator;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

interface EntityTypeTags {

    TagKey<EntityType<?>> HORSES = create("horses");
    TagKey<EntityType<?>> MINECART = create("minecart");
    TagKey<EntityType<?>> SPLITTING_MOB = create("splitting_mob");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, name));
    }
}
