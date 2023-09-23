package org.bukkit.util;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StringUtilTest {

    @Test
    public void nullPrefixTest() {
        assertThrows(NullPointerException.class, () -> StringUtil.startsWithIgnoreCase("String", null));
    }

    @Test
    public void nullStringTest() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.startsWithIgnoreCase(null, "String"));
    }

    @Test
    public void nullCollectionTest() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.copyPartialMatches("Token", ImmutableList.<String>of(), null));
    }

    @Test
    public void nullIterableTest() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.copyPartialMatches("Token", null, new ArrayList<String>()));
    }

    @Test
    public void nullTokenTest() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.copyPartialMatches(null, ImmutableList.<String>of(), new ArrayList<String>()));
    }

    @Test
    public void copyTokenTest() {
        String token = "ab";
        List<String> original = ImmutableList.of("ab12", "aC561", "AB5195", "Ab76", "", "a");
        List<String> expected = ImmutableList.of("ab12", "AB5195", "Ab76");
        List<String> list = new ArrayList<String>();
        assertThat(StringUtil.copyPartialMatches(token, original, list), is(expected));
        assertThat(StringUtil.copyPartialMatches(token, original, list), is(sameInstance(list)));
        assertThat(list.size(), is(expected.size() * 2));
    }

    @Test
    public void copyUnsupportedTest() {
        assertThrows(UnsupportedOperationException.class, () -> StringUtil.copyPartialMatches("token", ImmutableList.of("token1", "token2"), ImmutableList.of()));
    }

    @Test
    public void copyNullTest() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.copyPartialMatches("token", Arrays.asList("token1", "token2", null), new ArrayList<String>()));
    }
}
