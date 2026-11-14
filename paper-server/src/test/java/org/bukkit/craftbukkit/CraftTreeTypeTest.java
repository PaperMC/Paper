package org.bukkit.craftbukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.bukkit.TreeType;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
public class CraftTreeTypeTest {

    // grower features that intentionally share a tree type with another feature
    private static final Map<ResourceKey<Feature>, ResourceKey<Feature>> ALIASES = Map.of(
        TreeFeatures.OAK_BEES_005, TreeFeatures.OAK,
        TreeFeatures.FANCY_OAK_BEES_005, TreeFeatures.FANCY_OAK,
        TreeFeatures.BIRCH_BEES_005, TreeFeatures.BIRCH,
        TreeFeatures.CHERRY_BEES_005, TreeFeatures.CHERRY,
        TreeFeatures.PALE_OAK_BONEMEAL, TreeFeatures.PALE_OAK
    );

    @SuppressWarnings("unchecked")
    public static Stream<ResourceKey<Feature>> growerFeatures() throws ReflectiveOperationException {
        List<ResourceKey<Feature>> features = new ArrayList<>();
        for (Field growerField : TreeGrower.class.getDeclaredFields()) {
            if (!Modifier.isStatic(growerField.getModifiers()) || growerField.getType() != TreeGrower.class) {
                continue;
            }

            TreeGrower grower = (TreeGrower) growerField.get(null);
            for (Field listField : TreeGrower.class.getDeclaredFields()) {
                if (Modifier.isStatic(listField.getModifiers()) || listField.getType() != WeightedList.class) {
                    continue;
                }

                listField.setAccessible(true);
                for (Weighted<ResourceKey<Feature>> weighted : ((WeightedList<ResourceKey<Feature>>) listField.get(grower)).unwrap()) {
                    features.add(weighted.value());
                }
            }
        }
        return features.stream().distinct();
    }

    @ParameterizedTest
    @MethodSource("growerFeatures")
    public void growerFeatureRoundTrips(ResourceKey<Feature> feature) {
        TreeType treeType = assertDoesNotThrow(() -> CraftTreeType.fromFeature(feature), "No TreeType for grower feature " + feature);
        assertEquals(ALIASES.getOrDefault(feature, feature), CraftTreeType.toFeature(treeType, true), "TreeType " + treeType + " does not generate grower feature " + feature);
    }

    @ParameterizedTest
    @EnumSource(value = TreeType.class, mode = EnumSource.Mode.EXCLUDE, names = "CHORUS_PLANT")
    public void treeTypeRoundTrips(TreeType treeType) {
        for (boolean normalWorld : new boolean[] {true, false}) {
            assertEquals(treeType, CraftTreeType.fromFeature(CraftTreeType.toFeature(treeType, normalWorld)), "TreeType " + treeType + " does not map back from its feature");
        }
    }
}
