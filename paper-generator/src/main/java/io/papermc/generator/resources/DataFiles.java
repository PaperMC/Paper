package io.papermc.generator.resources;

import com.squareup.javapoet.ClassName;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.resources.data.EntityClassData;
import io.papermc.generator.resources.data.EntityTypeData;
import io.papermc.generator.resources.data.ItemMetaData;
import io.papermc.generator.resources.data.RegistryData;
import io.papermc.generator.resources.predicate.BlockPredicate;
import io.papermc.generator.resources.predicate.ItemPredicate;
import io.papermc.typewriter.ClassNamed;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public final class DataFiles {

    private static final ResourceKey<Registry<DataFile<?, ?, ?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("paper", "data_file"));

    public static final ResourceKey<DataFile.Map<String, List<String>>> BLOCK_STATE_AMBIGUOUS_NAMES = store("block_state/ambiguous_names");
    public static final ResourceKey<DataFile.Map<Class<? extends Enum<? extends StringRepresentable>>, ClassName>> BLOCK_STATE_ENUM_PROPERTY_TYPES = store("block_state/enum_property_types");
    public static final ResourceKey<DataFile.Map<ClassNamed, List<BlockPredicate>>> BLOCK_STATE_PREDICATES = store("block_state/predicates");

    public static final ResourceKey<DataFile.Map<ClassNamed, ItemMetaData>> ITEM_META_BRIDGE = store("item_meta/bridge");
    public static final ResourceKey<DataFile.Map<ClassNamed, List<ItemPredicate>>> ITEM_META_PREDICATES = store("item_meta/predicates");

    public static ResourceKey<DataFile.Map<ResourceKey<? extends Registry<?>>, RegistryData>> registry(RegistryEntry.Type type) {
        return store("registry/%s".formatted(type.getSerializedName()));
    }

    public static final ResourceKey<DataFile.Map<ResourceKey<EntityType<?>>, EntityTypeData>> ENTITY_TYPES = store("entity_types");
    public static final ResourceKey<DataFile.Map<Class<? extends Mob>, EntityClassData>> ENTITY_CLASS_NAMES = store("entity_class_names");

    private static <T extends DataFile<?, ?, ?>> ResourceKey<T> store(String name) {
        return (ResourceKey<T>) ResourceKey.create(REGISTRY_KEY, ResourceLocation.fromNamespaceAndPath("paper", name));
    }

    private DataFiles() {
    }
}
