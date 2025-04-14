package io.papermc.paper.item;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ChargedProjectiles;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.Fireworks;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.JukeboxPlayable;
import io.papermc.paper.datacomponent.item.MapId;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.datacomponent.item.PotDecorations;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSongs;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.JukeboxSong;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.DecoratedPot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@AllFeatures
class ItemStackDataComponentTest {

    @Test
    void testMaxStackSize() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.MAX_STACK_SIZE, 32, ItemMeta.class, ItemMeta::getMaxStackSize, ItemMeta::setMaxStackSize);
    }

    @Test
    void testMaxDamage() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.MAX_DAMAGE, 120, Damageable.class, Damageable::getMaxDamage, Damageable::setMaxDamage);
    }

    @Test
    void testDamage() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.DAMAGE, 120, Damageable.class, Damageable::getDamage, Damageable::setDamage);
    }

    @Test
    void testUnbreakable() {
        final ItemStack stack = new ItemStack(Material.STONE);
        stack.setData(DataComponentTypes.UNBREAKABLE);
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.UNBREAKABLE).build());

        Assertions.assertTrue(stack.getItemMeta().isUnbreakable());
        Assertions.assertTrue(stack.getItemMeta().hasItemFlag(ItemFlag.HIDE_UNBREAKABLE));
        stack.unsetData(DataComponentTypes.UNBREAKABLE);
        Assertions.assertFalse(stack.getItemMeta().isUnbreakable());
    }

    // @Test
    // void testHideAdditionalTooltip() {
    //     final ItemStack stack = new ItemStack(Material.STONE);
    //     stack.setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
    //
    //     Assertions.assertTrue(stack.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ADDITIONAL_TOOLTIP));
    //     stack.unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);
    //     Assertions.assertFalse(stack.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ADDITIONAL_TOOLTIP));
    // }

    @Test
    void testHideTooltip() {
        ItemStack stack = new ItemStack(Material.STONE);
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true).build());

        Assertions.assertEquals(stack.getItemMeta().isHideTooltip(), stack.hasData(DataComponentTypes.TOOLTIP_DISPLAY) && stack.getData(DataComponentTypes.TOOLTIP_DISPLAY).hideTooltip());
        Assertions.assertTrue(stack.getItemMeta().isHideTooltip());
        stack.unsetData(DataComponentTypes.TOOLTIP_DISPLAY);
        Assertions.assertFalse(stack.getItemMeta().isHideTooltip());
        stack = new ItemStack(Material.STONE);

        stack.unsetData(DataComponentTypes.TOOLTIP_DISPLAY);
        Assertions.assertFalse(stack.getItemMeta().isHideTooltip());
        Assertions.assertEquals(stack.getItemMeta().isHideTooltip(), stack.hasData(DataComponentTypes.TOOLTIP_DISPLAY));
    }

    @Test
    void testRepairCost() {
        final ItemStack stack = new ItemStack(Material.STONE);
        testWithMeta(stack, DataComponentTypes.REPAIR_COST, 120, Repairable.class, Repairable::getRepairCost, Repairable::setRepairCost);
    }

    @Test
    void testCustomName() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.CUSTOM_NAME, Component.text("HELLO!!!!!!"), ItemMeta.class, ItemMeta::displayName, ItemMeta::displayName);
    }

    @Test
    void testItemName() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.ITEM_NAME, Component.text("HELLO!!!!!! ITEM NAME"), ItemMeta.class, ItemMeta::itemName, ItemMeta::itemName);
    }

    @Test
    void testItemLore() {
        List<Component> list = List.of(Component.text("1"), Component.text("2"));
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.LORE, ItemLore.lore().lines(list).build(), ItemLore::lines, ItemMeta.class, ItemMeta::lore, ItemMeta::lore);
    }

    @Test
    void testItemRarity() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.RARITY, ItemRarity.RARE, ItemMeta.class, ItemMeta::getRarity, ItemMeta::setRarity);
    }

    @Test
    void testItemEnchantments() {
        final ItemStack stack = new ItemStack(Material.STONE);
        Map<Enchantment, Integer> enchantmentIntegerMap = Map.of(Enchantment.SOUL_SPEED, 1);
        stack.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments(enchantmentIntegerMap));
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ENCHANTMENTS).build());

        Assertions.assertTrue(stack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS));
        Assertions.assertEquals(1, stack.getItemMeta().getEnchantLevel(Enchantment.SOUL_SPEED));
        Assertions.assertEquals(stack.getItemMeta().getEnchants(), enchantmentIntegerMap);
        stack.unsetData(DataComponentTypes.ENCHANTMENTS);
        Assertions.assertTrue(stack.getItemMeta().getEnchants().isEmpty());
    }

    @Test
    void testItemAttributes() {
        final ItemStack stack = new ItemStack(Material.STONE);
        AttributeModifier modifier = new AttributeModifier(NamespacedKey.minecraft("test"), 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        stack.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes().addModifier(Attribute.ATTACK_DAMAGE, modifier).build());
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS).build());

        Assertions.assertTrue(stack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES));
        Assertions.assertEquals(modifier, ((List<AttributeModifier>) stack.getItemMeta().getAttributeModifiers(Attribute.ATTACK_DAMAGE)).getFirst());
        stack.unsetData(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        Assertions.assertNull(stack.getItemMeta().getAttributeModifiers());
    }

    @Test
    void testLegacyCustomModelData() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(1).build(), customModelData -> customModelData.floats().get(0).intValue(), ItemMeta.class, ItemMeta::getCustomModelData, ItemMeta::setCustomModelData);
    }

    @Test
    void testEnchantmentGlintOverride() {
        testWithMeta(new ItemStack(Material.STONE), DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true, ItemMeta.class, ItemMeta::getEnchantmentGlintOverride, ItemMeta::setEnchantmentGlintOverride);
    }

    @Test
    void testFood() {
        FoodProperties properties = FoodProperties.food()
            .canAlwaysEat(true)
            .saturation(1.3F)
            .nutrition(1)
            .build();

        final ItemStack stack = new ItemStack(Material.CROSSBOW);
        stack.setData(DataComponentTypes.FOOD, properties);

        ItemMeta meta = stack.getItemMeta();
        FoodComponent component = meta.getFood();
        Assertions.assertEquals(properties.canAlwaysEat(), component.canAlwaysEat());
        Assertions.assertEquals(properties.saturation(), component.getSaturation());
        Assertions.assertEquals(properties.nutrition(), component.getNutrition());

        stack.unsetData(DataComponentTypes.FOOD);
        meta = stack.getItemMeta();
        Assertions.assertFalse(meta.hasFood());
    }

    @Test
    void testTool() {
        Tool properties = Tool.tool()
            .damagePerBlock(1)
            .defaultMiningSpeed(2F)
            .addRules(List.of(
                Tool.rule(
                    RegistrySet.keySetFromValues(RegistryKey.BLOCK, List.of(BlockType.STONE, BlockType.GRAVEL)),
                    2F,
                    TriState.TRUE
                ),
                Tool.rule(
                    RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK).getTag(TagKey.create(RegistryKey.BLOCK, NamespacedKey.minecraft("bamboo_blocks"))),
                    2F,
                    TriState.TRUE
                )
            ))
            .build();

        final ItemStack stack = new ItemStack(Material.CROSSBOW);
        stack.setData(DataComponentTypes.TOOL, properties);

        ItemMeta meta = stack.getItemMeta();
        ToolComponent component = meta.getTool();
        Assertions.assertEquals(properties.damagePerBlock(), component.getDamagePerBlock());
        Assertions.assertEquals(properties.defaultMiningSpeed(), component.getDefaultMiningSpeed());

        int idx = 0;
        for (ToolComponent.ToolRule effect : component.getRules()) {
            Assertions.assertEquals(properties.rules().get(idx).speed(), effect.getSpeed());
            Assertions.assertEquals(properties.rules().get(idx).correctForDrops().toBoolean(), effect.isCorrectForDrops());
            Assertions.assertEquals(properties.rules().get(idx).blocks().resolve(Registry.BLOCK), effect.getBlocks().stream().map(Material::asBlockType).toList());
            idx++;
        }

        stack.unsetData(DataComponentTypes.TOOL);
        meta = stack.getItemMeta();
        Assertions.assertFalse(meta.hasTool());
    }

    @Test
    void testJukeboxPlayable() {
        JukeboxPlayable properties = JukeboxPlayable.jukeboxPlayable(JukeboxSong.MALL).build();

        final ItemStack stack = new ItemStack(Material.BEEF);
        stack.setData(DataComponentTypes.JUKEBOX_PLAYABLE, properties);

        ItemMeta meta = stack.getItemMeta();
        JukeboxPlayableComponent component = meta.getJukeboxPlayable();
        Assertions.assertEquals(properties.jukeboxSong(), component.getSong());

        stack.unsetData(DataComponentTypes.JUKEBOX_PLAYABLE);
        meta = stack.getItemMeta();
        Assertions.assertFalse(meta.hasJukeboxPlayable());
    }

    @Test
    void testDyedColor() {
        final ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE);
        Color color = Color.BLUE;
        stack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(color));
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.DYED_COLOR).build());

        Assertions.assertTrue(stack.getItemMeta().hasItemFlag(ItemFlag.HIDE_DYE));
        Assertions.assertEquals(color, ((LeatherArmorMeta) stack.getItemMeta()).getColor());
        stack.unsetData(DataComponentTypes.DYED_COLOR);
        Assertions.assertFalse(((LeatherArmorMeta) stack.getItemMeta()).isDyed());
    }

    @Test
    void testMapColor() {
        testWithMeta(new ItemStack(Material.FILLED_MAP), DataComponentTypes.MAP_COLOR, MapItemColor.mapItemColor().color(Color.BLUE).build(), MapItemColor::color, MapMeta.class, MapMeta::getColor, MapMeta::setColor);
    }

    @Test
    void testMapId() {
        testWithMeta(new ItemStack(Material.FILLED_MAP), DataComponentTypes.MAP_ID, MapId.mapId(1), MapId::id, MapMeta.class, MapMeta::getMapId, MapMeta::setMapId);
    }

    @Test
    void testFireworks() {
        testWithMeta(new ItemStack(Material.FIREWORK_ROCKET), DataComponentTypes.FIREWORKS, Fireworks.fireworks(List.of(FireworkEffect.builder().build()), 1), Fireworks::effects, FireworkMeta.class, FireworkMeta::getEffects, (fireworkMeta, effects) -> {
            fireworkMeta.clearEffects();
            fireworkMeta.addEffects(effects);
        });

        testWithMeta(new ItemStack(Material.FIREWORK_ROCKET), DataComponentTypes.FIREWORKS, Fireworks.fireworks(List.of(FireworkEffect.builder().build()), 1), Fireworks::flightDuration, FireworkMeta.class, FireworkMeta::getPower, FireworkMeta::setPower);
    }

    @Test
    void testTrim() {
        final ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemArmorTrim armorTrim = ItemArmorTrim.itemArmorTrim(new ArmorTrim(TrimMaterial.AMETHYST, TrimPattern.BOLT)).build();
        stack.setData(DataComponentTypes.TRIM, armorTrim);
        stack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.TRIM).build());

        Assertions.assertTrue(stack.getItemMeta().hasItemFlag(ItemFlag.HIDE_ARMOR_TRIM));
        Assertions.assertEquals(armorTrim.armorTrim(), ((ArmorMeta) stack.getItemMeta()).getTrim());
        stack.unsetData(DataComponentTypes.TRIM);
        Assertions.assertFalse(((ArmorMeta) stack.getItemMeta()).hasTrim());
    }

    @Test
    void testChargedProjectiles() {
        final ItemStack stack = new ItemStack(Material.CROSSBOW);
        ItemStack projectile = new ItemStack(Material.FIREWORK_ROCKET);
        stack.setData(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectiles.chargedProjectiles().add(projectile).build());

        CrossbowMeta meta = (CrossbowMeta) stack.getItemMeta();
        Assertions.assertEquals(meta.getChargedProjectiles().getFirst(), projectile);

        stack.unsetData(DataComponentTypes.CHARGED_PROJECTILES);
        meta = (CrossbowMeta) stack.getItemMeta();
        Assertions.assertTrue(meta.getChargedProjectiles().isEmpty());
    }

    @Test
    void testPot() {
        final ItemStack stack = new ItemStack(Material.DECORATED_POT);
        stack.setData(DataComponentTypes.POT_DECORATIONS, PotDecorations.potDecorations().back(ItemType.DANGER_POTTERY_SHERD).build());

        BlockState state = ((BlockStateMeta) stack.getItemMeta()).getBlockState();
        DecoratedPot decoratedPot = (DecoratedPot) state;

        Assertions.assertEquals(decoratedPot.getSherd(DecoratedPot.Side.BACK), Material.DANGER_POTTERY_SHERD);
        stack.unsetData(DataComponentTypes.POT_DECORATIONS);
        decoratedPot = (DecoratedPot) ((BlockStateMeta) stack.getItemMeta()).getBlockState();
        Assertions.assertTrue(decoratedPot.getSherds().values().stream().allMatch((m) -> m.asItemType() == ItemType.BRICK));
    }

    @Test
    void testRecipes() {
        final ItemStack stack = new ItemStack(Material.KNOWLEDGE_BOOK);
        stack.setData(DataComponentTypes.RECIPES, List.of(Key.key("paper:fun_recipe")));

        final ItemMeta itemMeta = stack.getItemMeta();
        Assertions.assertInstanceOf(KnowledgeBookMeta.class, itemMeta);

        final List<NamespacedKey> recipes = ((KnowledgeBookMeta) itemMeta).getRecipes();
        Assertions.assertEquals(recipes, List.of(new NamespacedKey("paper", "fun_recipe")));
    }

    @Test
    void testJukeboxWithEitherKey() {
        final ItemStack apiStack = CraftItemStack.asBukkitCopy(new net.minecraft.world.item.ItemStack(Items.MUSIC_DISC_5));
        final JukeboxPlayable data = apiStack.getData(DataComponentTypes.JUKEBOX_PLAYABLE);

        Assertions.assertNotNull(data);
        Assertions.assertEquals(JukeboxSong.FIVE, data.jukeboxSong());
    }

    @Test
    void testJukeboxWithEitherHolder() {
        final net.minecraft.world.item.ItemStack internalStack = new net.minecraft.world.item.ItemStack(Items.STONE);
        internalStack.set(DataComponents.JUKEBOX_PLAYABLE, new net.minecraft.world.item.JukeboxPlayable(
            new EitherHolder<>(RegistryHelper.getRegistry().lookupOrThrow(Registries.JUKEBOX_SONG).getOrThrow(JukeboxSongs.FIVE))
        ));

        final ItemStack apiStack = CraftItemStack.asBukkitCopy(internalStack);
        final JukeboxPlayable data = apiStack.getData(DataComponentTypes.JUKEBOX_PLAYABLE);

        Assertions.assertNotNull(data);
        Assertions.assertEquals(JukeboxSong.FIVE, data.jukeboxSong());
    }

    private static <T, M extends ItemMeta> void testWithMeta(final ItemStack stack, final DataComponentType.Valued<T> type, final T value, final Class<M> metaType, final Function<M, T> metaGetter, final BiConsumer<M, T> metaSetter) {
        testWithMeta(stack, type, value, Function.identity(), metaType, metaGetter, metaSetter);
    }

    private static <T, M extends ItemMeta, R> void testWithMeta(final ItemStack stack, final DataComponentType.Valued<T> type, final T value, Function<T, R> mapper, final Class<M> metaType, final Function<M, R> metaGetter, final BiConsumer<M, R> metaSetter) {
        ItemStack original = stack.clone();
        stack.setData(type, value);

        Assertions.assertEquals(value, stack.getData(type));

        final ItemMeta meta = stack.getItemMeta();
        final M typedMeta = Assertions.assertInstanceOf(metaType, meta);

        Assertions.assertEquals(metaGetter.apply(typedMeta), mapper.apply(value));

        // SETTING
        metaSetter.accept(typedMeta, mapper.apply(value));
        original.setItemMeta(typedMeta);
        Assertions.assertEquals(value, original.getData(type));
    }

    private static <M extends ItemMeta> void testWithMeta(final ItemStack stack, final DataComponentType.NonValued type, final boolean value, final Class<M> metaType, final Function<M, Boolean> metaGetter, final BiConsumer<M, Boolean> metaSetter) {
        ItemStack original = stack.clone();
        stack.setData(type);

        Assertions.assertEquals(value, stack.hasData(type));

        final ItemMeta meta = stack.getItemMeta();
        final M typedMeta = Assertions.assertInstanceOf(metaType, meta);

        Assertions.assertEquals(metaGetter.apply(typedMeta), value);

        // SETTING
        metaSetter.accept(typedMeta, value);
        original.setItemMeta(typedMeta);
        Assertions.assertEquals(value, original.hasData(type));
    }
}
