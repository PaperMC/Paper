package io.papermc.generator;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.MossyCarpetBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.WallBlock;
import org.junit.jupiter.api.Test;

public class BlockStatePropertyTest extends BootstrapTest {

    @Test
    public void testReferences() throws NoSuchFieldException, IllegalAccessException {
        // if renamed should change DataPropertyWriter#FIELD_TO_BASE_NAME/FIELD_TO_BASE_NAME_SPECIFICS
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup.findStaticVarHandle(ChiseledBookShelfBlock.class, "SLOT_OCCUPIED_PROPERTIES", List.class);
        lookup.findStaticVarHandle(PipeBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
        lookup.findStaticVarHandle(WallBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
        lookup.findStaticVarHandle(MossyCarpetBlock.class, "PROPERTY_BY_DIRECTION", Map.class);
    }
}
