package io.papermc.paper.world.structure;

import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.configuration.WorldConfiguration;
import java.lang.reflect.Field;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllFeatures
public class StructureSeedConfigTest {

    @Test
    public void checkStructureSeedDefaults() throws ReflectiveOperationException {
        final WorldConfiguration.Seeds seeds = PaperConfigurations.createWorldDefaultsForTesting().new Seeds();

        final Registry<StructureSet> structureSets = RegistryHelper.registryAccess().lookupOrThrow(Registries.STRUCTURE_SET);
        for (final ResourceKey<StructureSet> setKey : structureSets.registryKeySet()) {
            assertEquals(Identifier.DEFAULT_NAMESPACE, setKey.identifier().getNamespace());
            final StructureSet set = structureSets.getValueOrThrow(setKey);
            if (setKey == BuiltinStructureSets.STRONGHOLDS) { // special case due to seed matching world seed
                assertEquals(0, set.placement().salt);
                continue;
            }
            int salt = switch (setKey.identifier().getPath()) {
                case "villages" -> seeds.village;
                case "desert_pyramids" -> seeds.desert;
                case "igloos" -> seeds.igloo;
                case "jungle_temples" -> seeds.jungle;
                case "swamp_huts" -> seeds.swamp;
                case "pillager_outposts" -> seeds.outpost;
                case "ocean_monuments" -> seeds.monument;
                case "woodland_mansions" -> seeds.mansion;
                case "buried_treasures" -> seeds.buriedTreasure;
                case "mineshafts" -> seeds.mineshaft.or(0); // mineshaft seed is set differently
                case "ruined_portals" -> seeds.portal;
                case "shipwrecks" -> seeds.shipwreck;
                case "ocean_ruins" -> seeds.ocean;
                case "nether_complexes" -> seeds.nether;
                case "nether_fossils" -> seeds.fossil;
                case "end_cities" -> seeds.endCity;
                case "ancient_cities" -> seeds.ancientCity;
                case "trail_ruins" -> seeds.trailRuins;
                case "trial_chambers" -> seeds.trialChambers;
                default -> throw new AssertionError("Missing structure set seed in WorldConfiguration.Seeds for " + setKey);
            };
            if (setKey == BuiltinStructureSets.BURIED_TREASURES) {
                final Field field = StructurePlacement.class.getDeclaredField("HIGHLY_ARBITRARY_RANDOM_SALT");
                field.trySetAccessible();
                assertEquals(0, set.placement().salt);
                assertEquals(field.get(null), salt, "Mismatched default seed for " + setKey + ". Should be " + field.get(null));
                continue;
            }
            assertEquals(set.placement().salt, salt, "Mismatched default seed for " + setKey + ". Should be " + set.placement().salt);
        }
    }
}
