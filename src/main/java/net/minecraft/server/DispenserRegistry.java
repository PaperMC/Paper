package net.minecraft.server;

import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.DummyGeneratorAccess;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class DispenserRegistry {

    public static final PrintStream a = System.out;
    private static boolean b;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        if (!DispenserRegistry.b) {
            DispenserRegistry.b = true;
            if (IRegistry.f.keySet().isEmpty()) {
                throw new IllegalStateException("Unable to load registries");
            } else {
                BlockFire.c();
                BlockComposter.c();
                if (EntityTypes.getName(EntityTypes.PLAYER) == null) {
                    throw new IllegalStateException("Failed loading EntityTypes");
                } else {
                    PotionBrewer.a();
                    PlayerSelector.a();
                    IDispenseBehavior.c();
                    ArgumentRegistry.a();
                    TagStatic.b();
                    d();
                }
                // CraftBukkit start - easier than fixing the decompile
                DataConverterFlattenData.map(1008, "{Name:'minecraft:oak_sign',Properties:{rotation:'0'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'0'}}");
                DataConverterFlattenData.map(1009, "{Name:'minecraft:oak_sign',Properties:{rotation:'1'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'1'}}");
                DataConverterFlattenData.map(1010, "{Name:'minecraft:oak_sign',Properties:{rotation:'2'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'2'}}");
                DataConverterFlattenData.map(1011, "{Name:'minecraft:oak_sign',Properties:{rotation:'3'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'3'}}");
                DataConverterFlattenData.map(1012, "{Name:'minecraft:oak_sign',Properties:{rotation:'4'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'4'}}");
                DataConverterFlattenData.map(1013, "{Name:'minecraft:oak_sign',Properties:{rotation:'5'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'5'}}");
                DataConverterFlattenData.map(1014, "{Name:'minecraft:oak_sign',Properties:{rotation:'6'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'6'}}");
                DataConverterFlattenData.map(1015, "{Name:'minecraft:oak_sign',Properties:{rotation:'7'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'7'}}");
                DataConverterFlattenData.map(1016, "{Name:'minecraft:oak_sign',Properties:{rotation:'8'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'8'}}");
                DataConverterFlattenData.map(1017, "{Name:'minecraft:oak_sign',Properties:{rotation:'9'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'9'}}");
                DataConverterFlattenData.map(1018, "{Name:'minecraft:oak_sign',Properties:{rotation:'10'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'10'}}");
                DataConverterFlattenData.map(1019, "{Name:'minecraft:oak_sign',Properties:{rotation:'11'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'11'}}");
                DataConverterFlattenData.map(1020, "{Name:'minecraft:oak_sign',Properties:{rotation:'12'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'12'}}");
                DataConverterFlattenData.map(1021, "{Name:'minecraft:oak_sign',Properties:{rotation:'13'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'13'}}");
                DataConverterFlattenData.map(1022, "{Name:'minecraft:oak_sign',Properties:{rotation:'14'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'14'}}");
                DataConverterFlattenData.map(1023, "{Name:'minecraft:oak_sign',Properties:{rotation:'15'}}", "{Name:'minecraft:standing_sign',Properties:{rotation:'15'}}");
                DataConverterMaterialId.ID_MAPPING.put(323, "minecraft:oak_sign");

                DataConverterFlattenData.map(1440, "{Name:\'minecraft:portal\',Properties:{axis:\'x\'}}", new String[]{"{Name:\'minecraft:portal\',Properties:{axis:\'x\'}}"});

                DataConverterMaterialId.ID_MAPPING.put(409, "minecraft:prismarine_shard");
                DataConverterMaterialId.ID_MAPPING.put(410, "minecraft:prismarine_crystals");
                DataConverterMaterialId.ID_MAPPING.put(411, "minecraft:rabbit");
                DataConverterMaterialId.ID_MAPPING.put(412, "minecraft:cooked_rabbit");
                DataConverterMaterialId.ID_MAPPING.put(413, "minecraft:rabbit_stew");
                DataConverterMaterialId.ID_MAPPING.put(414, "minecraft:rabbit_foot");
                DataConverterMaterialId.ID_MAPPING.put(415, "minecraft:rabbit_hide");
                DataConverterMaterialId.ID_MAPPING.put(416, "minecraft:armor_stand");

                DataConverterMaterialId.ID_MAPPING.put(423, "minecraft:mutton");
                DataConverterMaterialId.ID_MAPPING.put(424, "minecraft:cooked_mutton");
                DataConverterMaterialId.ID_MAPPING.put(425, "minecraft:banner");
                DataConverterMaterialId.ID_MAPPING.put(426, "minecraft:end_crystal");
                DataConverterMaterialId.ID_MAPPING.put(427, "minecraft:spruce_door");
                DataConverterMaterialId.ID_MAPPING.put(428, "minecraft:birch_door");
                DataConverterMaterialId.ID_MAPPING.put(429, "minecraft:jungle_door");
                DataConverterMaterialId.ID_MAPPING.put(430, "minecraft:acacia_door");
                DataConverterMaterialId.ID_MAPPING.put(431, "minecraft:dark_oak_door");
                DataConverterMaterialId.ID_MAPPING.put(432, "minecraft:chorus_fruit");
                DataConverterMaterialId.ID_MAPPING.put(433, "minecraft:chorus_fruit_popped");
                DataConverterMaterialId.ID_MAPPING.put(434, "minecraft:beetroot");
                DataConverterMaterialId.ID_MAPPING.put(435, "minecraft:beetroot_seeds");
                DataConverterMaterialId.ID_MAPPING.put(436, "minecraft:beetroot_soup");
                DataConverterMaterialId.ID_MAPPING.put(437, "minecraft:dragon_breath");
                DataConverterMaterialId.ID_MAPPING.put(438, "minecraft:splash_potion");
                DataConverterMaterialId.ID_MAPPING.put(439, "minecraft:spectral_arrow");
                DataConverterMaterialId.ID_MAPPING.put(440, "minecraft:tipped_arrow");
                DataConverterMaterialId.ID_MAPPING.put(441, "minecraft:lingering_potion");
                DataConverterMaterialId.ID_MAPPING.put(442, "minecraft:shield");
                DataConverterMaterialId.ID_MAPPING.put(443, "minecraft:elytra");
                DataConverterMaterialId.ID_MAPPING.put(444, "minecraft:spruce_boat");
                DataConverterMaterialId.ID_MAPPING.put(445, "minecraft:birch_boat");
                DataConverterMaterialId.ID_MAPPING.put(446, "minecraft:jungle_boat");
                DataConverterMaterialId.ID_MAPPING.put(447, "minecraft:acacia_boat");
                DataConverterMaterialId.ID_MAPPING.put(448, "minecraft:dark_oak_boat");
                DataConverterMaterialId.ID_MAPPING.put(449, "minecraft:totem_of_undying");
                DataConverterMaterialId.ID_MAPPING.put(450, "minecraft:shulker_shell");
                DataConverterMaterialId.ID_MAPPING.put(452, "minecraft:iron_nugget");
                DataConverterMaterialId.ID_MAPPING.put(453, "minecraft:knowledge_book");

                DataConverterSpawnEgg.ID_MAPPING[23] = "Arrow";
                // CraftBukkit end
            }
        }
    }

    private static <T> void a(Iterable<T> iterable, Function<T, String> function, Set<String> set) {
        LocaleLanguage localelanguage = LocaleLanguage.a();

        iterable.forEach((object) -> {
            String s = (String) function.apply(object);

            if (!localelanguage.b(s)) {
                set.add(s);
            }

        });
    }

    private static void a(final Set<String> set) {
        final LocaleLanguage localelanguage = LocaleLanguage.a();

        GameRules.a(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
                if (!localelanguage.b(gamerules_gamerulekey.b())) {
                    set.add(gamerules_gamerulekey.a());
                }

            }
        });
    }

    public static Set<String> b() {
        Set<String> set = new TreeSet();

        a(IRegistry.ATTRIBUTE, AttributeBase::getName, set);
        a(IRegistry.ENTITY_TYPE, EntityTypes::f, set);
        a(IRegistry.MOB_EFFECT, MobEffectList::c, set);
        a(IRegistry.ITEM, Item::getName, set);
        a(IRegistry.ENCHANTMENT, Enchantment::g, set);
        a(IRegistry.BLOCK, Block::i, set);
        a(IRegistry.CUSTOM_STAT, (minecraftkey) -> {
            return "stat." + minecraftkey.toString().replace(':', '.');
        }, set);
        a((Set) set);
        return set;
    }

    public static void c() {
        if (!DispenserRegistry.b) {
            throw new IllegalArgumentException("Not bootstrapped");
        } else {
            if (SharedConstants.d) {
                b().forEach((s) -> {
                    DispenserRegistry.LOGGER.error("Missing translations: " + s);
                });
                CommandDispatcher.b();
            }

            AttributeDefaults.a();
        }
    }

    private static void d() {
        if (DispenserRegistry.LOGGER.isDebugEnabled()) {
            System.setErr(new DebugOutputStream("STDERR", System.err));
            System.setOut(new DebugOutputStream("STDOUT", DispenserRegistry.a));
        } else {
            System.setErr(new RedirectStream("STDERR", System.err));
            System.setOut(new RedirectStream("STDOUT", DispenserRegistry.a));
        }

    }

    public static void a(String s) {
        DispenserRegistry.a.println(s);
    }
}
