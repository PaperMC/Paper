package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import com.mojang.serialization.DataResult;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.environment.AllFeatures;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@AllFeatures
public class ParticleTest {

    public static Stream<Arguments> data() {
        return CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE).keySet().stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testBukkitValuesPresent(ResourceLocation minecraft) {
        // TODO: 10/19/23 Remove with enum PR, it is then no longer needed, since the enum PR has a extra test for this
        assertNotNull(Registry.PARTICLE_TYPE.get(CraftNamespacedKey.fromMinecraft(minecraft)), String.format("""
                No bukkit particle found for minecraft particle %s.
                Please add the particle to bukkit.
                """, minecraft));
    }

    @ParameterizedTest
    @EnumSource(Particle.class)
    public void testMinecraftValuesPresent(Particle bukkit) {
        // TODO: 10/19/23 Remove with enum PR, it is then no longer needed, since the enum PR has a extra test for this
        Particle finalBukkit = bukkit;
        assertDoesNotThrow(() -> CraftParticle.bukkitToMinecraft(finalBukkit), String.format("""
                No minecraft particle found for bukkit particle %s.
                Please map the bukkit particle to a minecraft particle.
                """, bukkit.getKey()));
    }

    @ParameterizedTest
    @EnumSource(Particle.class)
    public void testRightParticleParamCreation(Particle bukkit) {
        net.minecraft.core.particles.ParticleType<?> minecraft = CraftParticle.bukkitToMinecraft(bukkit);

        if (bukkit.getDataType().equals(Void.class)) {
            this.testEmptyData(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Particle.DustOptions.class)) {
            this.testDustOption(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(ItemStack.class)) {
            this.testItemStack(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(BlockData.class)) {
            this.testBlockData(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Particle.DustTransition.class)) {
            this.testDustTransition(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Vibration.class)) {
            this.testVibration(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Float.class)) {
            this.testFloat(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Integer.class)) {
            this.testInteger(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Color.class)) {
            this.testColor(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Particle.Trail.class)) {
            this.testTrail(bukkit, minecraft);
            return;
        }

        if (bukkit.getDataType().equals(Particle.Spell.class)) {
            this.testSpell(bukkit, minecraft);
            return;
        }

        fail(String.format("""
                No test found for particle %s.
                Please add a test case for it here.
                """, bukkit.getKey()));
    }

    private <T extends ParticleOptions> void testEmptyData(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        this.createAndTest(bukkit, minecraft, null, SimpleParticleType.class);
    }

    private <T extends ParticleOptions> void testDustOption(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(236, 28, 36), 0.1205f);
        DustParticleOptions param = this.createAndTest(bukkit, minecraft, dustOptions, DustParticleOptions.class);

        assertEquals(0.1205f, param.getScale(), 0.001, String.format("""
                Dust option scale for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));

        Vector3f expectedColor = new Vector3f(0.92549f, 0.1098f, 0.14117647f);
        assertTrue(expectedColor.equals(param.getColor(), 0.001f), String.format("""
                Dust option color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), expectedColor, param.getColor())); // Print expected and got since we use assert true
    }

    private <T extends ParticleOptions> void testItemStack(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemParticleOption param = this.createAndTest(bukkit, minecraft, itemStack, ItemParticleOption.class);

        assertEquals(itemStack, CraftItemStack.asBukkitCopy(param.getItem()), String.format("""
                ItemStack for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));
    }

    private <T extends ParticleOptions> void testBlockData(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        BlockData blockData = Bukkit.createBlockData(Material.STONE);
        BlockParticleOption param = this.createAndTest(bukkit, minecraft, blockData, BlockParticleOption.class);

        assertEquals(blockData, CraftBlockData.fromData(param.getState()), String.format("""
                Block data for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));
    }

    private <T extends ParticleOptions> void testDustTransition(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(236, 28, 36), Color.fromRGB(107, 159, 181), 0.1205f);
        DustColorTransitionOptions param = this.createAndTest(bukkit, minecraft, dustTransition, DustColorTransitionOptions.class);

        assertEquals(0.1205f, param.getScale(), 0.001, String.format("""
                Dust transition scale for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));

        Vector3f expectedFrom = new Vector3f(0.92549f, 0.1098f, 0.14117647f);
        assertTrue(expectedFrom.equals(param.getFromColor(), 0.001f), String.format("""
                Dust transition from color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), expectedFrom, param.getFromColor())); // Print expected and got since we use assert true

        Vector3f expectedTo = new Vector3f(0.4196f, 0.6235294f, 0.7098f);
        assertTrue(expectedTo.equals(param.getToColor(), 0.001f), String.format("""
                Dust transition to color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), expectedTo, param.getToColor())); // Print expected and got since we use assert true
    }

    private <T extends ParticleOptions> void testVibration(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Vibration vibration = new Vibration(new Vibration.Destination.BlockDestination(new Location(null, 1, 5, 9)), 265);
        VibrationParticleOption param = this.createAndTest(bukkit, minecraft, vibration, VibrationParticleOption.class);

        assertEquals(265, param.getArrivalInTicks(), String.format("""
                Vibration ticks for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));

        Optional<Vec3> pos = param.getDestination().getPosition(null);
        assertTrue(pos.isPresent(), String.format("""
                Vibration position for particle %s is not present.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));

        // Add 0.5 since it gets centered to the block
        assertEquals(new Vec3(1.5, 5.5, 9.5), pos.get(), String.format("""
                Vibration position for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));
    }

    private <T extends ParticleOptions> void testFloat(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        if (bukkit == Particle.SCULK_CHARGE) {
            float roll = 0.1205f;
            SculkChargeParticleOptions param = this.createAndTest(bukkit, minecraft, roll, SculkChargeParticleOptions.class);

            assertEquals(roll, param.roll(), 0.001, String.format("""
                    Float roll for particle %s do not match.
                    Did something change in the implementation or minecraft?
                    """, bukkit.getKey()));
        } else if (bukkit == Particle.DRAGON_BREATH) {
            float power = 3.1415f;
            PowerParticleOption param = this.createAndTest(bukkit, minecraft, power, PowerParticleOption.class);

            assertEquals(power, param.getPower(), 0.001, String.format("""
                    Float power for particle %s do not match.
                    Did something change in the implementation or minecraft?
                    """, bukkit.getKey()));
        } else {
            throw new IllegalArgumentException("Missing float particle options test mapping, please add it here.");
        }
    }

    private <T extends ParticleOptions> void testInteger(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        int delay = 1205;
        ShriekParticleOption param = this.createAndTest(bukkit, minecraft, delay, ShriekParticleOption.class);

        assertEquals(delay, param.getDelay(), String.format("""
                Integer delay for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));
    }

    private <T extends ParticleOptions> void testColor(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Color color = Color.fromARGB(107, 236, 28, 36);
        ColorParticleOption param = this.createAndTest(bukkit, minecraft, color, ColorParticleOption.class);

        Vector4f actual = new Vector4f(param.getAlpha(), param.getRed(), param.getGreen(), param.getBlue());
        Vector4f expected = new Vector4f(0.4196f, 0.92549f, 0.1098f, 0.14117647f);
        assertTrue(expected.equals(actual, 0.001f), String.format("""
                Dust transition to color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), expected, actual)); // Print expected and got since we use assert true
    }

    private <T extends ParticleOptions> void testTrail(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Color color = Color.fromRGB(236, 28, 36);
        Particle.Trail trail = new Particle.Trail(new Location(null, 1, 5, 9), color, 20);

        TrailParticleOption param = this.createAndTest(bukkit, minecraft, trail, TrailParticleOption.class);

        Vec3 pos = param.target();
        assertEquals(new Vec3(1.0, 5.0, 9.0), pos, String.format("""
                Vibration position for particle %s do not match.
                Did something change in the implementation or minecraft?
                """, bukkit.getKey()));

        int actual = param.color();
        int expected = color.asRGB();
        assertEquals(expected, actual, String.format("""
                Color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), expected, actual)); // Print expected and got since we use assert true
    }

    private <T extends ParticleOptions> void testSpell(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft) {
        Color color = Color.AQUA;
        float power = 3.1415f;
        Particle.Spell spell = new Particle.Spell(color, power);

        SpellParticleOption param = this.createAndTest(bukkit, minecraft, spell, SpellParticleOption.class);

        assertEquals(color.asARGB(), param.color, String.format("""
                Spell color for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), color.asARGB(), param.color));
        assertEquals(power, param.getPower(), 0.001, String.format("""
                Spell power for particle %s do not match.
                Did something change in the implementation or minecraft?
                Expected: %s.
                Got: %s.
                """, bukkit.getKey(), power, param.getPower()));
    }

    private <D extends ParticleOptions, T extends ParticleOptions> D createAndTest(Particle bukkit, net.minecraft.core.particles.ParticleType<T> minecraft, Object data, Class<D> paramClass) {
        @SuppressWarnings("unchecked")
        T particleParam = (T) assertDoesNotThrow(() -> CraftParticle.createParticleParam(bukkit, data), String.format("""
                Could not create particle param for particle %s.
                This can indicated, that the default particle param is used, but the particle requires extra data.
                Or that something is wrong with the logic which creates the particle param with extra data.
                Check in CraftParticle if the conversion is still correct.
                """, bukkit.getKey()));

        DataResult<Tag> encoded = assertDoesNotThrow(() -> minecraft.codec().codec().encodeStart(NbtOps.INSTANCE, particleParam), // Paper - remap fix
                String.format("""
                        Could not encoded particle param for particle %s.
                        This can indicated, that the wrong particle param is created in CraftParticle.
                        Particle param is of type %s.
                        """, bukkit.getKey(), particleParam.getClass()));

        Optional<DataResult.Error<Tag>> encodeError = encoded.error();
        assertTrue(encodeError.isEmpty(), () -> String.format("""
                Could not encoded particle param for particle %s.
                This is possible because the wrong particle param is created in CraftParticle.
                Particle param is of type %s.
                Error message: %s.
                """, bukkit.getKey(), particleParam.getClass(), encoded.error().get().message()));

        Optional<Tag> encodeResult = encoded.result();
        assertTrue(encodeResult.isPresent(), String.format("""
                Result is not present for particle %s.
                Even though there is also no error, this should not happen.
                Particle param is of type %s.
                """, bukkit.getKey(), particleParam.getClass()));

        DataResult<T> decoded = minecraft.codec().codec().parse(NbtOps.INSTANCE, encodeResult.get());

        Optional<DataResult.Error<T>> decodeError = decoded.error();
        assertTrue(decodeError.isEmpty(), () -> String.format("""
                Could not decoded particle param for particle %s.
                This is possible because the wrong particle param is created in CraftParticle.
                Particle param is of type %s.
                Error message: %s.


                NBT data: %s.
                """, bukkit.getKey(), particleParam.getClass(), decodeError.get().message(), encodeResult.get()));

        Optional<T> decodeResult = decoded.result();
        assertTrue(decodeResult.isPresent(), String.format("""
                Result is not present for particle %s.
                Even though there is also no error, this should not happen.
                Particle param is of type %s.
                """, bukkit.getKey(), particleParam.getClass()));

        return assertInstanceOf(paramClass, decodeResult.get(), String.format("""
                Result is not of the right type for particle %s.
                """, bukkit.getKey()));
    }
}
