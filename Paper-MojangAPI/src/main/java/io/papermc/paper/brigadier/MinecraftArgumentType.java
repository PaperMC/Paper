package io.papermc.paper.brigadier;

import com.mojang.brigadier.arguments.ArgumentType;
import io.papermc.paper.brigadier.arguments.SimpleArgument;
import io.papermc.paper.brigadier.types.GameProfileResult;
import io.papermc.paper.brigadier.types.Position;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class MinecraftArgumentType implements Keyed {

    protected static final Map<NamespacedKey, MinecraftArgumentType> ARGUMENT_TYPE_MAP = new HashMap<>();

    public static final SimpleArgument<GameProfileResult> GAME_PROFILES = SimpleArgument.wrapper("game_profile", GameProfileResult.class);

    public static final SimpleArgument<Position> BLOCK_POSITION = SimpleArgument.wrapper("block_pos", Position.class);

    public static final SimpleArgument<Position> VEC3 = SimpleArgument.wrapper("vec3", Position.class);

    public static final SimpleArgument<Position> VEC2 = SimpleArgument.wrapper("vec2", Position.class);

    
    
    public static final SimpleArgument<TextColor> COLOR = SimpleArgument.wrapper("color", TextColor.class);
    
    
    
    public static final SimpleArgument<Component> COMPONENT = SimpleArgument.wrapper("component", Component.class);
    
    public static final SimpleArgument<BinaryTagHolder> NBT_COMPOUND_TAG = SimpleArgument.wrapper("nbt_compound_tag", BinaryTagHolder.class);
    
    
    
    public static final SimpleArgument<Integer> INVENTORY_SLOT = SimpleArgument.wrapper("item_slot", Integer.class);
    
    public static final SimpleArgument<NamespacedKey> RESOURCE_LOCATION = SimpleArgument.wrapper("resource_location", NamespacedKey.class);
    
    public static final SimpleArgument<PotionEffectType> MOB_EFFECT = SimpleArgument.wrapper("mob_effect", PotionEffectType.class);
    
    
    
    public static final SimpleArgument<Enchantment> ENCHANTMENT = SimpleArgument.wrapper("item_enchantment", Enchantment.class);
    
    public static final SimpleArgument<EntityType> ENTITY_TYPE = SimpleArgument.wrapper("entity_summon", EntityType.class);
    
    
    
    public static final SimpleArgument<Position> ROTATION = SimpleArgument.wrapper("rotation", Position.class);


    private final NamespacedKey key;

    protected MinecraftArgumentType(@NotNull String id) {
        this.key = NamespacedKey.minecraft(id);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }
}
