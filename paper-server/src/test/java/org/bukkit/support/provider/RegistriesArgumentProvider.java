package org.bukkit.support.provider;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectList;
import net.minecraft.world.item.Instrument;
import org.bukkit.GameEvent;
import org.bukkit.MusicInstrument;
import org.bukkit.craftbukkit.CraftGameEvent;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructureType;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class RegistriesArgumentProvider implements ArgumentsProvider {

    private static final List<Arguments> DATA = Lists.newArrayList();

    static {
        // Order: Bukkit class, Minecraft Registry key, CraftBukkit class, Minecraft class
        DATA.add(Arguments.of(Enchantment.class, Registries.ENCHANTMENT, CraftEnchantment.class, net.minecraft.world.item.enchantment.Enchantment.class));
        DATA.add(Arguments.of(GameEvent.class, Registries.GAME_EVENT, CraftGameEvent.class, net.minecraft.world.level.gameevent.GameEvent.class));
        DATA.add(Arguments.of(MusicInstrument.class, Registries.INSTRUMENT, CraftMusicInstrument.class, Instrument.class));
        DATA.add(Arguments.of(PotionEffectType.class, Registries.MOB_EFFECT, CraftPotionEffectType.class, MobEffectList.class));
        DATA.add(Arguments.of(Structure.class, Registries.STRUCTURE, CraftStructure.class, net.minecraft.world.level.levelgen.structure.Structure.class));
        DATA.add(Arguments.of(StructureType.class, Registries.STRUCTURE_TYPE, CraftStructureType.class, net.minecraft.world.level.levelgen.structure.StructureType.class));
        DATA.add(Arguments.of(TrimMaterial.class, Registries.TRIM_MATERIAL, CraftTrimMaterial.class, net.minecraft.world.item.armortrim.TrimMaterial.class));
        DATA.add(Arguments.of(TrimPattern.class, Registries.TRIM_PATTERN, CraftTrimPattern.class, net.minecraft.world.item.armortrim.TrimPattern.class));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return getData();
    }

    public static Stream<? extends Arguments> getData() {
        return DATA.stream();
    }
}
