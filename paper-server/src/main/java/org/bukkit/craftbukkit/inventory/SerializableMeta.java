package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.bukkit.block.Banner;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.meta.ItemMeta;

@SerializableAs("ItemMeta")
public final class SerializableMeta implements ConfigurationSerializable {
    static final String TYPE_FIELD = "meta-type";

    static final ImmutableMap<Class<? extends CraftMetaItem>, String> classMap;
    static final ImmutableMap<String, Constructor<? extends CraftMetaItem>> constructorMap;

    static {
        classMap = ImmutableMap.<Class<? extends CraftMetaItem>, String>builder()
                .put(CraftMetaArmor.class, "ARMOR")
                .put(CraftMetaArmorStand.class, "ARMOR_STAND")
                .put(CraftMetaBanner.class, "BANNER")
                .put(CraftMetaBlockState.class, "TILE_ENTITY")
                .put(CraftMetaBook.class, "BOOK")
                .put(CraftMetaBookSigned.class, "BOOK_SIGNED")
                .put(CraftMetaSkull.class, "SKULL")
                .put(CraftMetaLeatherArmor.class, "LEATHER_ARMOR")
                .put(CraftMetaColorableArmor.class, "COLORABLE_ARMOR")
                .put(CraftMetaMap.class, "MAP")
                .put(CraftMetaPotion.class, "POTION")
                .put(CraftMetaShield.class, "SHIELD")
                .put(CraftMetaSpawnEgg.class, "SPAWN_EGG")
                .put(CraftMetaEnchantedBook.class, "ENCHANTED")
                .put(CraftMetaFirework.class, "FIREWORK")
                .put(CraftMetaCharge.class, "FIREWORK_EFFECT")
                .put(CraftMetaKnowledgeBook.class, "KNOWLEDGE_BOOK")
                .put(CraftMetaTropicalFishBucket.class, "TROPICAL_FISH_BUCKET")
                .put(CraftMetaAxolotlBucket.class, "AXOLOTL_BUCKET")
                .put(CraftMetaCrossbow.class, "CROSSBOW")
                .put(CraftMetaSuspiciousStew.class, "SUSPICIOUS_STEW")
                .put(CraftMetaEntityTag.class, "ENTITY_TAG")
                .put(CraftMetaCompass.class, "COMPASS")
                .put(CraftMetaBundle.class, "BUNDLE")
                .put(CraftMetaMusicInstrument.class, "MUSIC_INSTRUMENT")
                .put(CraftMetaOminousBottle.class, "OMINOUS_BOTTLE")
                .put(CraftMetaItem.class, "UNSPECIFIC")
                .build();

        final ImmutableMap.Builder<String, Constructor<? extends CraftMetaItem>> classConstructorBuilder = ImmutableMap.builder();
        for (Map.Entry<Class<? extends CraftMetaItem>, String> mapping : classMap.entrySet()) {
            try {
                classConstructorBuilder.put(mapping.getValue(), mapping.getKey().getDeclaredConstructor(Map.class));
            } catch (NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }
        constructorMap = classConstructorBuilder.build();
    }

    private SerializableMeta() {
    }

    public static ItemMeta deserialize(Map<String, Object> map) throws Throwable {
        Preconditions.checkArgument(map != null, "Cannot deserialize null map");

        String type = SerializableMeta.getString(map, SerializableMeta.TYPE_FIELD, false);
        Constructor<? extends CraftMetaItem> constructor = SerializableMeta.constructorMap.get(type);

        if (constructor == null) {
            throw new IllegalArgumentException(type + " is not a valid " + SerializableMeta.TYPE_FIELD);
        }

        try {
            CraftMetaItem meta = constructor.newInstance(map);

            // Convert Shield CraftMetaBlockState to CraftMetaShield
            if (meta instanceof CraftMetaBlockState state && state.hasBlockState() && state.getBlockState() instanceof Banner) {
                meta = new CraftMetaShield(meta);
                meta.unhandledTags.clear(CraftMetaShield.BASE_COLOR.TYPE);
            }

            return meta;
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (final InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        throw new AssertionError();
    }

    public static String getString(Map<?, ?> map, Object field, boolean nullable) {
        return SerializableMeta.getObject(String.class, map, field, nullable);
    }

    public static boolean getBoolean(Map<?, ?> map, Object field) {
        Boolean value = SerializableMeta.getObject(Boolean.class, map, field, true);
        return value != null && value;
    }

    public static int getInteger(Map<?, ?> map, Object field) {
        Integer value = SerializableMeta.getObject(Integer.class, map, field, true);
        return value != null ? value : 0;
    }

    public static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
        final Object object = map.get(field);

        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }

        // SPIGOT-7675 - More lenient conversion of floating point numbers from other number types:
        if (clazz == Float.class || clazz == Double.class) {
            if (Number.class.isInstance(object)) {
                Number number = Number.class.cast(object);
                if (clazz == Float.class) {
                    return clazz.cast(number.floatValue());
                } else {
                    return clazz.cast(number.doubleValue());
                }
            }
        }

        if (object == null) {
            if (!nullable) {
                throw new NoSuchElementException(map + " does not contain " + field);
            }
            return null;
        }
        throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
    }

    public static <T> java.util.Optional<T> getObjectOptionally(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
        return Optional.ofNullable(getObject(clazz, map, field, nullable));
    }

    public static <T> List<T> getList(Class<T> clazz, Map<?, ?> map, Object field) {
        List<T> result = new ArrayList<>();

        List<?> list = SerializableMeta.getObject(List.class, map, field, true);
        if (list == null || list.isEmpty()) {
            return result;
        }

        for (Object object : list) {
            T cast = null;

            if (clazz.isInstance(object)) {
                cast = clazz.cast(object);
            }

            // SPIGOT-7675 - More lenient conversion of floating point numbers from other number types:
            if (clazz == Float.class || clazz == Double.class) {
                if (Number.class.isInstance(object)) {
                    Number number = Number.class.cast(object);
                    if (clazz == Float.class) {
                        cast = clazz.cast(number.floatValue());
                    } else {
                        cast = clazz.cast(number.doubleValue());
                    }
                }
            }

            if (cast != null) {
                result.add(cast);
            }
        }

        return result;
    }
}
