package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.ItemStackTest.BukkitWrapper;
import org.bukkit.craftbukkit.inventory.ItemStackTest.CraftWrapper;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackProvider;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackWrapper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
public class ItemMetaTest {

    static final int MAX_FIREWORK_POWER = 255; // Please update ItemStackFireworkTest if/when this gets changed.

    @Test
    public void testPowerLimitExact() {
        assertThrows(IllegalArgumentException.class, () -> newFireworkMeta().setPower(MAX_FIREWORK_POWER + 1));
    }

    @Test
    public void testPowerLimitMax() {
        assertThrows(IllegalArgumentException.class, () -> newFireworkMeta().setPower(Integer.MAX_VALUE));
    }

    @Test
    public void testPowerLimitMin() {
        assertThrows(IllegalArgumentException.class, () -> newFireworkMeta().setPower(Integer.MIN_VALUE));
    }

    @Test
    public void testPowerLimitNegative() {
        assertThrows(IllegalArgumentException.class, () -> newFireworkMeta().setPower(-1));
    }

    @Test
    public void testPowers() {
        for (int i = 0; i <= ItemMetaTest.MAX_FIREWORK_POWER; i++) {
            FireworkMeta firework = ItemMetaTest.newFireworkMeta();
            firework.setPower(i);
            assertThat(firework.getPower(), is(i), String.valueOf(i));
        }
    }

    @Test
    public void testConflictingEnchantment() {
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.UNBREAKING), is(false));

        itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.UNBREAKING), is(false));
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.FORTUNE), is(true));
        assertThat(itemMeta.hasConflictingEnchant(null), is(false));
    }

    @Test
    public void testConflictingStoredEnchantment() {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.UNBREAKING), is(false));

        itemMeta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.UNBREAKING), is(false));
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.FORTUNE), is(true));
        assertThat(itemMeta.hasConflictingStoredEnchant(null), is(false));
    }

    @Test
    public void testConflictingEnchantments() {
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        itemMeta.addEnchant(Enchantment.UNBREAKING, 6, true);
        itemMeta.addEnchant(Enchantment.EFFICIENCY, 6, true);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.FORTUNE), is(false));

        itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingEnchant(Enchantment.FORTUNE), is(true));
        assertThat(itemMeta.hasConflictingEnchant(null), is(false));
    }

    @Test
    public void testConflictingStoredEnchantments() {
        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK);
        itemMeta.addStoredEnchant(Enchantment.UNBREAKING, 6, true);
        itemMeta.addStoredEnchant(Enchantment.EFFICIENCY, 6, true);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.FORTUNE), is(false));

        itemMeta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, false);
        assertThat(itemMeta.hasConflictingStoredEnchant(Enchantment.FORTUNE), is(true));
        assertThat(itemMeta.hasConflictingStoredEnchant(null), is(false));
    }

    private static FireworkMeta newFireworkMeta() {
        return ((FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET));
    }

    @Test
    public void testCrazyEquality() {
        CraftItemStack craft = CraftItemStack.asCraftCopy(new ItemStack(Material.STONE));
        craft.setItemMeta(craft.getItemMeta());
        ItemStack bukkit = new ItemStack(craft);
        assertThat(craft, is(bukkit));
        assertThat(bukkit, is(craft));
    }

    @Test
    public void testBlockStateMeta() {
        List<Block> queue = new ArrayList<>();

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BlockItem) {
                queue.add(((BlockItem) item).getBlock());
            }
            if (item instanceof StandingAndWallBlockItem) {
                queue.add(((StandingAndWallBlockItem) item).wallBlock);
            }
        }

        for (Block block : queue) {
            if (block != null) {
                ItemStack stack = CraftItemStack.asNewCraftStack(Item.byBlock(block));

                if (block instanceof AbstractSkullBlock || block instanceof AbstractBannerBlock) {
                    continue; // those blocks have a special meta
                }

                ItemMeta meta = stack.getItemMeta();
                if (block instanceof EntityBlock) {
                    assertTrue(meta instanceof BlockStateMeta, stack + " has meta of type " + meta + " expected BlockStateMeta");

                    BlockStateMeta blockState = (BlockStateMeta) meta;
                    assertNotNull(blockState.getBlockState(), stack + " has null block state");

                    blockState.setBlockState(blockState.getBlockState());
                } else {
                    assertFalse(meta instanceof BlockStateMeta, stack + " has unexpected meta of type BlockStateMeta (but is not a tile)");
                }
            }
        }
    }

    @Test
    public void testSpawnEggsHasMeta() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof net.minecraft.world.item.SpawnEggItem) {
                Material material = CraftItemType.minecraftToBukkit(item);
                CraftMetaItem baseMeta = (CraftMetaItem) Bukkit.getItemFactory().getItemMeta(material);
                ItemMeta baseMetaItem = CraftItemStack.getItemMeta(item.getDefaultInstance());

                assertTrue(baseMeta instanceof CraftMetaSpawnEgg, material + " is not handled in CraftItemFactory");
                assertTrue(baseMeta.applicableTo(material), material + " is not applicable to CraftMetaSpawnEgg");
                assertTrue(baseMetaItem instanceof SpawnEggMeta, material + " is not handled in CraftItemStack");
            }
        }
    }

    // Paper start - check entity tag metas
    private static final java.util.Set<Class<?>> ENTITY_TAG_METAS = java.util.Set.of(
        CraftMetaEntityTag.class,
        CraftMetaTropicalFishBucket.class,
        CraftMetaAxolotlBucket.class
    );
    @Test
    public void testEntityTagMeta() {
        for (final Item item : BuiltInRegistries.ITEM) {
            if (item instanceof net.minecraft.world.item.HangingEntityItem || item instanceof net.minecraft.world.item.MobBucketItem) {
                ItemStack stack = new ItemStack(CraftItemType.minecraftToBukkit(item));
                assertTrue(ENTITY_TAG_METAS.contains(stack.getItemMeta().getClass()), "missing entity tag meta handling for " + item);
                stack = CraftItemStack.asNewCraftStack(net.minecraft.world.item.Items.STONE);
                stack.editMeta(meta -> meta.displayName(net.kyori.adventure.text.Component.text("hello")));
                stack.setType(CraftItemType.minecraftToBukkit(item));
                assertTrue(ENTITY_TAG_METAS.contains(stack.getItemMeta().getClass()), "missing entity tag meta handling for " + item);
            }
        }
    }
    // Paper end

    @Test
    public void testEachExtraData() {
        final List<StackProvider> providers = Arrays.asList(
            new StackProvider(Material.WRITABLE_BOOK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                    meta.setAuthor("Some author");
                    meta.setPages("Page 1", "Page 2");
                    meta.setTitle("A title");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.WRITTEN_BOOK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                    meta.setAuthor("Some author");
                    meta.setPages("Page 1", "Page 2");
                    meta.setTitle("A title");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            /* Skulls rely on a running server instance
            new StackProvider(Material.SKULL_ITEM) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                    meta.setOwner("Notch");
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            */
            new StackProvider(Material.FILLED_MAP) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                    meta.setScaling(true);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.DIAMOND_CHESTPLATE) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final ArmorMeta meta = (ArmorMeta) cleanStack.getItemMeta();
                    meta.setTrim(new ArmorTrim(TrimMaterial.AMETHYST, TrimPattern.COAST));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.LEATHER_HORSE_ARMOR) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                    meta.setColor(Color.FUCHSIA);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.LEATHER_CHESTPLATE) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final ColorableArmorMeta meta = (ColorableArmorMeta) cleanStack.getItemMeta();
                    meta.setTrim(new ArmorTrim(TrimMaterial.COPPER, TrimPattern.DUNE));
                    meta.setColor(Color.MAROON);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.POTION) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                    meta.setBasePotionType(PotionType.WATER);
                    meta.addCustomEffect(PotionEffectType.NAUSEA.createEffect(1, 1), false);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.FIREWORK_ROCKET) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                    meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.OLIVE).with(Type.BALL_LARGE).build());
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.ENCHANTED_BOOK) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) cleanStack.getItemMeta();
                    meta.addStoredEnchant(Enchantment.FLAME, 1, true);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.FIREWORK_STAR) {
                @Override ItemStack operate(final ItemStack cleanStack) {
                    final FireworkEffectMeta meta = (FireworkEffectMeta) cleanStack.getItemMeta();
                    meta.setEffect(FireworkEffect.builder().withColor(Color.MAROON, Color.BLACK).with(Type.CREEPER).withFlicker().build());
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.WHITE_BANNER) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final BannerMeta meta = (BannerMeta) cleanStack.getItemMeta();
                    meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BRICKS));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            /* No distinguishing features, add back with virtual entity API
            new StackProvider(Material.ZOMBIE_SPAWN_EGG) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final SpawnEggMeta meta = (SpawnEggMeta) cleanStack.getItemMeta();
                    meta.setSpawnedType(EntityType.ZOMBIE);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            */
            new StackProvider(Material.KNOWLEDGE_BOOK) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final KnowledgeBookMeta meta = (KnowledgeBookMeta) cleanStack.getItemMeta();
                    meta.addRecipe(new NamespacedKey("minecraft", "test"), new NamespacedKey("plugin", "test"));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.TROPICAL_FISH_BUCKET) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final TropicalFishBucketMeta meta = (TropicalFishBucketMeta) cleanStack.getItemMeta();
                    meta.setBodyColor(DyeColor.ORANGE);
                    meta.setPatternColor(DyeColor.BLACK);
                    meta.setPattern(TropicalFish.Pattern.DASHER);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.AXOLOTL_BUCKET) {
                @Override ItemStack operate(ItemStack cleanStack) {
                     final AxolotlBucketMeta meta = (AxolotlBucketMeta) cleanStack.getItemMeta();
                     meta.setVariant(Axolotl.Variant.BLUE);
                     cleanStack.setItemMeta(meta);
                     return cleanStack;
                }
            },
            new StackProvider(Material.CROSSBOW) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CrossbowMeta meta = (CrossbowMeta) cleanStack.getItemMeta();
                    meta.addChargedProjectile(new ItemStack(Material.ARROW));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.ARMOR_STAND) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaArmorStand meta = (CraftMetaArmorStand) cleanStack.getItemMeta();
                    meta.entityTag = new CompoundTag();
                    meta.entityTag.putBoolean("Small", true);
                    meta.setInvisible(true); // Paper
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.SUSPICIOUS_STEW) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaSuspiciousStew meta = ((CraftMetaSuspiciousStew) cleanStack.getItemMeta());
                    meta.addCustomEffect(PotionEffectType.NAUSEA.createEffect(1, 0), false);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.ITEM_FRAME) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaEntityTag meta = ((CraftMetaEntityTag) cleanStack.getItemMeta());
                    meta.entityTag = new CompoundTag();
                    meta.entityTag.putBoolean("Invisible", true);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.COMPASS) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaCompass meta = ((CraftMetaCompass) cleanStack.getItemMeta());
                    meta.setLodestoneTracked(false);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.BUNDLE) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final BundleMeta meta = (BundleMeta) cleanStack.getItemMeta();
                    meta.addItem(new ItemStack(Material.STONE));
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.GOAT_HORN) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaMusicInstrument meta = (CraftMetaMusicInstrument) cleanStack.getItemMeta();
                    meta.setInstrument(MusicInstrument.ADMIRE_GOAT_HORN);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.OMINOUS_BOTTLE) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaOminousBottle meta = (CraftMetaOminousBottle) cleanStack.getItemMeta();
                    meta.setAmplifier(3);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            },
            new StackProvider(Material.SHIELD) {
                @Override ItemStack operate(ItemStack cleanStack) {
                    final CraftMetaShield meta = (CraftMetaShield) cleanStack.getItemMeta();
                    meta.setBaseColor(DyeColor.ORANGE);
                    cleanStack.setItemMeta(meta);
                    return cleanStack;
                }
            }
        );

        assertThat(providers, hasSize(ItemStackTest.COMPOUND_MATERIALS.length - 4/* Normal item meta, skulls, eggs and tile entities */), "Forgotten test?");

        for (final StackProvider provider : providers) {
            this.downCastTest(new BukkitWrapper(provider));
            this.downCastTest(new CraftWrapper(provider));
        }
    }

    @Test
    public void testAttributeModifiers() {
        UUID sameUUID = UUID.randomUUID();
        ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        itemMeta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(sameUUID, "Test Modifier", 10, AttributeModifier.Operation.ADD_NUMBER));

        ItemMeta equalMeta = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        equalMeta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(sameUUID, "Test Modifier", 10, AttributeModifier.Operation.ADD_NUMBER));

        assertThat(itemMeta.equals(equalMeta), is(true));

        ItemMeta itemMeta2 = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        itemMeta2.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(sameUUID, "Test Modifier", 10, AttributeModifier.Operation.ADD_NUMBER));

        ItemMeta notEqualMeta2 = Bukkit.getItemFactory().getItemMeta(Material.DIAMOND_PICKAXE);
        notEqualMeta2.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(sameUUID, "Test Modifier", 11, AttributeModifier.Operation.ADD_NUMBER));

        assertThat(itemMeta2.equals(notEqualMeta2), is(false));
    }

    @Test
    public void testBlockData() {
        BlockDataMeta itemMeta = (BlockDataMeta) Bukkit.getItemFactory().getItemMeta(Material.CHEST);
        itemMeta.setBlockData(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]"));
        assertThat(itemMeta.getBlockData(Material.CHEST), is(CraftBlockData.newData(null, "minecraft:chest[waterlogged=true]")));
    }

    @Test
    public void testMetaClasses() {
        Registry.ITEM.forEach(itemType -> {
            if (itemType == ItemType.AIR) {
                return;
            }

            ItemMeta meta = new ItemStack(itemType.asMaterial()).getItemMeta();
            Class<?> internal = meta == null ? CraftMetaItem.class : meta.getClass();
            Class<?>[] interfaces = internal.getInterfaces();
            Class<?> expected;
            if (interfaces.length > 0) {
                expected = interfaces[0];
            } else {
                expected = ItemMeta.class;
            }

            // Currently the expected and actual for AIR are ItemMeta rather than null
            Class<?> actual = itemType.getItemMetaClass();
            assertThat(actual, is(expected));
        });
    }

    private void downCastTest(final StackWrapper provider) {
        final String name = provider.toString();
        final ItemStack blank = new ItemStack(Material.STONE);
        final ItemStack craftBlank = CraftItemStack.asCraftCopy(blank);

        // Check that equality and similarity works for each meta implementation
        assertThat(provider.stack(), is(provider.stack()), name);
        assertThat(provider.stack().isSimilar(provider.stack()), is(true), name);

        this.downCastTest(name, provider.stack(), blank);
        blank.setItemMeta(blank.getItemMeta());
        this.downCastTest(name, provider.stack(), blank);

        this.downCastTest(name, provider.stack(), craftBlank);
        craftBlank.setItemMeta(craftBlank.getItemMeta());
        this.downCastTest(name, provider.stack(), craftBlank);
    }

    private void downCastTest(final String name, final ItemStack stack, final ItemStack blank) {
        assertThat(stack, is(not(blank)), name);
        assertThat(stack.getItemMeta(), is(not(blank.getItemMeta())), name);

        stack.setType(Material.STONE);

        assertThat(stack, is(blank), name);
    }
}
