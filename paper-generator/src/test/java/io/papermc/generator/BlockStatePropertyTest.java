package io.papermc.generator;

import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.ClassHelper;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.MossyCarpetBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BlockStatePropertyTest {

    private static Set<Class<? extends Comparable<?>>> ENUM_PROPERTY_VALUES;

    @BeforeAll
    public static void getAllProperties() {
        // bootstrap
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        // get all properties
        Set<Class<? extends Comparable<?>>> enumPropertyValues = Collections.newSetFromMap(new IdentityHashMap<>());
        try {
            for (Field field : BlockStateProperties.class.getDeclaredFields()) {
                if (ClassHelper.isStaticConstant(field, Modifier.PUBLIC)) {
                    if (!EnumProperty.class.isAssignableFrom(field.getType())) {
                        continue;
                    }

                    enumPropertyValues.add(((EnumProperty<?>) field.get(null)).getValueClass());
                }
            }
            ENUM_PROPERTY_VALUES = Collections.unmodifiableSet(enumPropertyValues);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    public void testReferences() throws NoSuchFieldException, IllegalAccessException {
        // if renamed should change DataPropertyWriter#FIELD_TO_BASE_NAME/FIELD_TO_BASE_NAME_SPECIFICS
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup.findStaticVarHandle(ChiseledBookShelfBlock.class, "SLOT_OCCUPIED_PROPERTIES", List.class);
        lookup.findStaticVarHandle(PipeBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
        lookup.findStaticVarHandle(WallBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
        lookup.findStaticVarHandle(MossyCarpetBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
    }

    @Test
    public void testBridge() {
        Set<String> missingApiEquivalents = new HashSet<>();
        for (Class<? extends Comparable<?>> value : ENUM_PROPERTY_VALUES) {
            if (!BlockStateMapping.ENUM_BRIDGE.containsKey(value)) {
                missingApiEquivalents.add(value.getCanonicalName());
            }
        }

        Assertions.assertTrue(missingApiEquivalents.isEmpty(), () -> "Missing some api equivalent in the block state mapping enum bridge (BlockStateMapping#ENUM_BRIDGE) : " + String.join(", ", missingApiEquivalents));
    }
}
