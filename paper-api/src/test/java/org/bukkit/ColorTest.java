package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Locale;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class ColorTest {
    static class TestColor {
        static int id = 0;
        final String name;
        final int argb;
        final int rgb;
        final int bgr;
        final int a;
        final int r;
        final int g;
        final int b;

        TestColor(int rgb, int bgr, int r, int g, int b) {
            this((255 << 24 | r << 16 | g << 8 | b), rgb, bgr, 255, r, g, b);
        }

        TestColor(int argb, int rgb, int bgr, int a, int r, int g, int b) {
            this.argb = argb;
            this.rgb = rgb;
            this.bgr = bgr;
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
            this.name = id + ":" + Integer.toHexString(argb).toUpperCase(Locale.ROOT) + "_" + Integer.toHexString(rgb).toUpperCase(Locale.ROOT) + "_" + Integer.toHexString(bgr).toUpperCase(Locale.ROOT) + "-a" + Integer.toHexString(a).toUpperCase(Locale.ROOT) + "-r" + Integer.toHexString(r).toUpperCase(Locale.ROOT) + "-g" + Integer.toHexString(g).toUpperCase(Locale.ROOT) + "-b" + Integer.toHexString(b).toUpperCase(Locale.ROOT);
        }
    }

    static TestColor[] examples = new TestColor[]{
        /*            0xRRGGBB, 0xBBGGRR, 0xRR, 0xGG, 0xBB */
        new TestColor(0xFFFFFF, 0xFFFFFF, 0xFF, 0xFF, 0xFF),
        new TestColor(0xFFFFAA, 0xAAFFFF, 0xFF, 0xFF, 0xAA),
        new TestColor(0xFF00FF, 0xFF00FF, 0xFF, 0x00, 0xFF),
        new TestColor(0x67FF22, 0x22FF67, 0x67, 0xFF, 0x22),
        new TestColor(0x000000, 0x000000, 0x00, 0x00, 0x00),
        /*            0xAARRGGBB, 0xRRGGBB, 0xBBGGRR, 0xAA, 0xRR, 0xGG, 0xBB */
        new TestColor(0xFF559922, 0x559922, 0x229955, 0xFF, 0x55, 0x99, 0x22),
        new TestColor(0x00000000, 0x000000, 0x000000, 0x00, 0x00, 0x00, 0x00)
    };

    @Test
    public void testSerialization() throws Throwable {
        for (TestColor testColor : examples) {
            Color base = Color.fromRGB(testColor.rgb);

            YamlConfiguration toSerialize = new YamlConfiguration();
            toSerialize.set("color", base);
            String serialized = toSerialize.saveToString();

            YamlConfiguration deserialized = new YamlConfiguration();
            deserialized.loadFromString(serialized);

            assertThat(base, is(deserialized.getColor("color")), testColor.name + " on " + serialized);
        }
    }

    // Equality tests
    @Test
    public void testEqualities() {
        for (TestColor testColor : examples) {
            Color fromARGB = Color.fromARGB(testColor.argb);
            Color fromARGBs = Color.fromARGB(testColor.a, testColor.r, testColor.g, testColor.b);
            Color fromRGB = Color.fromRGB(testColor.rgb);
            Color fromBGR = Color.fromBGR(testColor.bgr);
            Color fromRGBs = Color.fromRGB(testColor.r, testColor.g, testColor.b);
            Color fromBGRs = Color.fromBGR(testColor.b, testColor.g, testColor.r);

            assertThat(fromARGB, is(fromARGB), testColor.name);
            assertThat(fromARGBs, is(fromARGBs), testColor.name);
            assertThat(fromRGB, is(fromRGBs), testColor.name);
            assertThat(fromRGB, is(fromBGR), testColor.name);
            assertThat(fromRGB, is(fromBGRs), testColor.name);
            assertThat(fromRGBs, is(fromBGR), testColor.name);
            assertThat(fromRGBs, is(fromBGRs), testColor.name);
            assertThat(fromBGR, is(fromBGRs), testColor.name);
        }
    }

    @Test
    public void testInequalities() {
        for (int i = 1; i < examples.length; i++) {
            TestColor testFrom = examples[i];
            Color from = Color.fromARGB(testFrom.argb);
            for (int j = i - 1; j >= 0; j--) {
                TestColor testTo = examples[j];
                Color to = Color.fromARGB(testTo.argb);
                String name = testFrom.name + " to " + testTo.name;
                assertThat(from, is(not(to)), name);

                Color transform = from.setAlpha(testTo.a).setRed(testTo.r).setBlue(testTo.b).setGreen(testTo.g);
                assertThat(transform, is(not(sameInstance(from))), name);
                assertThat(transform, is(to), name);
            }
        }
    }

    // ARGB tests
    @Test
    public void testARGB() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromARGB(testColor.argb).asARGB(), is(testColor.argb), testColor.name);
            assertThat(Color.fromARGB(testColor.a, testColor.r, testColor.g, testColor.b).asARGB(), is(testColor.argb), testColor.name);
        }
    }

    // RGB tests
    @Test
    public void testRGB() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromRGB(testColor.rgb).asRGB(), is(testColor.rgb), testColor.name);
            assertThat(Color.fromBGR(testColor.bgr).asRGB(), is(testColor.rgb), testColor.name);
            assertThat(Color.fromRGB(testColor.r, testColor.g, testColor.b).asRGB(), is(testColor.rgb), testColor.name);
            assertThat(Color.fromBGR(testColor.b, testColor.g, testColor.r).asRGB(), is(testColor.rgb), testColor.name);
        }
    }

    @Test
    public void testInvalidRGB1() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x01000000));
    }

    @Test
    public void testInvalidRGB2() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidRGB3() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidRGB4() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(-1));
    }

    // BGR tests
    @Test
    public void testBGR() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromRGB(testColor.rgb).asBGR(), is(testColor.bgr), testColor.name);
            assertThat(Color.fromBGR(testColor.bgr).asBGR(), is(testColor.bgr), testColor.name);
            assertThat(Color.fromRGB(testColor.r, testColor.g, testColor.b).asBGR(), is(testColor.bgr), testColor.name);
            assertThat(Color.fromBGR(testColor.b, testColor.g, testColor.r).asBGR(), is(testColor.bgr), testColor.name);
        }
    }

    @Test
    public void testInvalidBGR1() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x01000000));
    }

    @Test
    public void testInvalidBGR2() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidBGR3() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidBGR4() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(-1));
    }

    // Alpha tests
    @Test
    public void testAlpha() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromARGB(testColor.argb).getAlpha(), is(testColor.a), testColor.name);
        }
    }

    @Test
    public void testInvalidA01() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromARGB(-1, 0x00, 0x00, 0x00));
    }

    @Test
    public void testInvalidA02() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromARGB(Integer.MAX_VALUE, 0x00, 0x00, 0x00));
    }

    @Test
    public void testInvalidA03() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromARGB(Integer.MIN_VALUE, 0x00, 0x00, 0x00));
    }

    @Test
    public void testInvalidA04() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromARGB(0x100, 0x00, 0x00, 0x00));
    }

    @Test
    public void testInvalidA05() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, 0x00).setAlpha(-1));
    }

    @Test
    public void testInvalidA06() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, 0x00).setAlpha(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidA07() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, 0x00).setAlpha(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidA08() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, 0x00).setAlpha(0x100));
    }

    @Test
    public void testInvalidA09() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setAlpha(-1));
    }

    @Test
    public void testInvalidA10() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setAlpha(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidA11() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setAlpha(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidA12() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setAlpha(0x100));
    }

    // Red tests
    @Test
    public void testRed() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromRGB(testColor.rgb).getRed(), is(testColor.r), testColor.name);
            assertThat(Color.fromBGR(testColor.bgr).getRed(), is(testColor.r), testColor.name);
            assertThat(Color.fromRGB(testColor.r, testColor.g, testColor.b).getRed(), is(testColor.r), testColor.name);
            assertThat(Color.fromBGR(testColor.b, testColor.g, testColor.r).getRed(), is(testColor.r), testColor.name);
        }
    }

    @Test
    public void testInvalidR01() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(-1, 0x00, 0x00));
    }

    @Test
    public void testInvalidR02() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(Integer.MAX_VALUE, 0x00, 0x00));
    }

    @Test
    public void testInvalidR03() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(Integer.MIN_VALUE, 0x00, 0x00));
    }

    @Test
    public void testInvalidR04() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x100, 0x00, 0x00));
    }

    @Test
    public void testInvalidR05() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, -1));
    }

    @Test
    public void testInvalidR06() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidR07() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidR08() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x00, 0x100));
    }

    @Test
    public void testInvalidR09() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setRed(-1));
    }

    @Test
    public void testInvalidR10() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setRed(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidR11() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setRed(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidR12() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setRed(0x100));
    }

    // Blue tests
    @Test
    public void testBlue() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromRGB(testColor.rgb).getBlue(), is(testColor.b), testColor.name);
            assertThat(Color.fromBGR(testColor.bgr).getBlue(), is(testColor.b), testColor.name);
            assertThat(Color.fromRGB(testColor.r, testColor.g, testColor.b).getBlue(), is(testColor.b), testColor.name);
            assertThat(Color.fromBGR(testColor.b, testColor.g, testColor.r).getBlue(), is(testColor.b), testColor.name);
        }
    }

    @Test
    public void testInvalidB01() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, 0x00, -1));
    }

    @Test
    public void testInvalidB02() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, 0x00, Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidB03() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, 0x00, Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidB04() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, 0x00, 0x100));
    }

    @Test
    public void testInvalidB05() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(-1, 0x00, 0x00));
    }

    @Test
    public void testInvalidB06() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(Integer.MAX_VALUE, 0x00, 0x00));
    }

    @Test
    public void testInvalidB07() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(Integer.MIN_VALUE, 0x00, 0x00));
    }

    @Test
    public void testInvalidB08() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x100, 0x00, 0x00));
    }

    @Test
    public void testInvalidB09() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setBlue(-1));
    }

    @Test
    public void testInvalidB10() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setBlue(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidB11() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setBlue(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidB12() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setBlue(0x100));
    }

    // Green tests
    @Test
    public void testGreen() {
        for (TestColor testColor : examples) {
            assertThat(Color.fromRGB(testColor.rgb).getGreen(), is(testColor.g), testColor.name);
            assertThat(Color.fromBGR(testColor.bgr).getGreen(), is(testColor.g), testColor.name);
            assertThat(Color.fromRGB(testColor.r, testColor.g, testColor.b).getGreen(), is(testColor.g), testColor.name);
            assertThat(Color.fromBGR(testColor.b, testColor.g, testColor.r).getGreen(), is(testColor.g), testColor.name);
        }
    }

    @Test
    public void testInvalidG01() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, -1, 0x00));
    }

    @Test
    public void testInvalidG02() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, Integer.MAX_VALUE, 0x00));
    }

    @Test
    public void testInvalidG03() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, Integer.MIN_VALUE, 0x00));
    }

    @Test
    public void testInvalidG04() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromRGB(0x00, 0x100, 0x00));
    }

    @Test
    public void testInvalidG05() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, -1, 0x00));
    }

    @Test
    public void testInvalidG06() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, Integer.MAX_VALUE, 0x00));
    }

    @Test
    public void testInvalidG07() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, Integer.MIN_VALUE, 0x00));
    }

    @Test
    public void testInvalidG08() {
        assertThrows(IllegalArgumentException.class, () -> Color.fromBGR(0x00, 0x100, 0x00));
    }

    @Test
    public void testInvalidG09() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setGreen(-1));
    }

    @Test
    public void testInvalidG10() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setGreen(Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidG11() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setGreen(Integer.MIN_VALUE));
    }

    @Test
    public void testInvalidG12() {
        assertThrows(IllegalArgumentException.class, () -> Color.WHITE.setGreen(0x100));
    }
}
