package io.papermc.paper.block;

import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.bukkit.Instrument;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AllFeatures
class InstrumentSoundTest {

    static Stream<Instrument> bukkitInstruments() {
        return Arrays.stream(Instrument.values()).filter(i -> i.getSound() != null);
    }

    @ParameterizedTest
    @MethodSource("bukkitInstruments")
    void checkInstrumentSound(final Instrument bukkit) {
        final NoteBlockInstrument nms = CraftBlockData.toNMS(bukkit, NoteBlockInstrument.class);
        assertEquals(nms.getSoundEvent(), CraftSound.bukkitToMinecraftHolder(bukkit.getSound()));
    }
}
