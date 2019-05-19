package org.bukkit.entity.memory;

import java.util.Arrays;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.junit.Assert;
import org.junit.Test;

public class MemoryKeyTest {

    @Test
    public void shouldContainAllMemories() {
        List<MemoryKey> memories = Arrays.asList(MemoryKey.HOME, MemoryKey.JOB_SITE, MemoryKey.MEETING_POINT);
        Assert.assertTrue(MemoryKey.values().containsAll(memories));
    }

    @Test
    public void shouldGetMemoryKeyHomeByNamespacedKey() {
        Assert.assertEquals(MemoryKey.HOME, MemoryKey.getByKey(NamespacedKey.minecraft("home")));
    }

    @Test
    public void shouldGetMemoryKeyJobSiteByNamespacedKey() {
        Assert.assertEquals(MemoryKey.JOB_SITE, MemoryKey.getByKey(NamespacedKey.minecraft("job_site")));
    }

    @Test
    public void shouldGetMemoryKeyMeetingPointByNamespacedKey() {
        Assert.assertEquals(MemoryKey.MEETING_POINT, MemoryKey.getByKey(NamespacedKey.minecraft("meeting_point")));
    }

    @Test
    public void shouldReturnNullWhenNamespacedKeyisNotPresentAsMemoryKey() {
        Assert.assertEquals(null, MemoryKey.getByKey(NamespacedKey.minecraft("not_present")));
    }

    @Test
    public void shouldReturnNullWhenNamespacedKeyisNull() {
        Assert.assertNull(MemoryKey.getByKey(null));
    }
}
