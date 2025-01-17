package io.papermc.paper.adventure.providers;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.DataComponentValue;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.adventure.text.serializer.gson.GsonDataComponentValue;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import org.bukkit.craftbukkit.CraftRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.serializer.gson.GsonDataComponentValue.gsonDataComponentValue;

@DefaultQualifier(NonNull.class)
public class DataComponentValueConverterProviderImpl implements DataComponentValueConverterRegistry.Provider {

    static final Key ID = Key.key("adventure", "platform/paper");

    @Override
    public Key id() {
        return ID;
    }

    private static <T> RegistryOps<T> createOps(final DynamicOps<T> delegate) {
        return CraftRegistry.getMinecraftRegistry().createSerializationContext(delegate);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Iterable<DataComponentValueConverterRegistry.Conversion<?, ?>> conversions() {
        return List.of(
            DataComponentValueConverterRegistry.Conversion.convert(
                PaperAdventure.DataComponentValueImpl.class,
                GsonDataComponentValue.class,
                (key, dataComponentValue) -> gsonDataComponentValue((JsonElement) dataComponentValue.codec().encodeStart(createOps(JsonOps.INSTANCE), dataComponentValue.value()).getOrThrow())
            ),
            DataComponentValueConverterRegistry.Conversion.convert(
                GsonDataComponentValue.class,
                PaperAdventure.DataComponentValueImpl.class,
                (key, dataComponentValue) -> {
                    final @Nullable DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(PaperAdventure.asVanilla(key));
                    if (type == null) {
                        throw new IllegalArgumentException("Unknown data component type: " + key);
                    }
                    return new PaperAdventure.DataComponentValueImpl(type.codecOrThrow(), type.codecOrThrow().parse(createOps(JsonOps.INSTANCE), dataComponentValue.element()).getOrThrow(IllegalArgumentException::new));
                }
            ),
            DataComponentValueConverterRegistry.Conversion.convert(
                PaperAdventure.DataComponentValueImpl.class,
                DataComponentValue.TagSerializable.class,
                (key, dataComponentValue) -> BinaryTagHolder.encode((Tag) dataComponentValue.codec().encodeStart(createOps(NbtOps.INSTANCE), dataComponentValue.value()).getOrThrow(), PaperAdventure.NBT_CODEC)
            ),
            DataComponentValueConverterRegistry.Conversion.convert(
                DataComponentValue.TagSerializable.class,
                PaperAdventure.DataComponentValueImpl.class,
                (key, tagSerializable) -> {
                    final @Nullable DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(PaperAdventure.asVanilla(key));
                    if (type == null) {
                        throw new IllegalArgumentException("Unknown data component type: " + key);
                    }
                    try {
                        return new PaperAdventure.DataComponentValueImpl(type.codecOrThrow(), type.codecOrThrow().parse(createOps(NbtOps.INSTANCE), tagSerializable.asBinaryTag().get(PaperAdventure.NBT_CODEC)).getOrThrow(IllegalArgumentException::new));
                    } catch (final CommandSyntaxException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            ),
            DataComponentValueConverterRegistry.Conversion.convert(
                DataComponentValue.TagSerializable.class,
                GsonDataComponentValue.class,
                (key, tagSerializable) -> {
                    Tag decodedSnbt;
                    try {
                        decodedSnbt = tagSerializable.asBinaryTag().get(PaperAdventure.NBT_CODEC);
                    } catch (final CommandSyntaxException e) {
                        throw new IllegalArgumentException("Unable to parse SNBT value", e);
                    }

                    return GsonDataComponentValue.gsonDataComponentValue(NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, decodedSnbt));
                }
            )
        );
    }
}
