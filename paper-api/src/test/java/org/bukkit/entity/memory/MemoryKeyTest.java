package org.bukkit.entity.memory;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.Test;

public class MemoryKeyTest {

    @Test
    public void shouldContainAllMemories() {
        List<MemoryKey> memories = Arrays.asList(MemoryKey.HOME, MemoryKey.JOB_SITE, MemoryKey.MEETING_POINT);
        assertTrue(MemoryKey.values().containsAll(memories));
    }

    @Test
    public void shouldGetMemoryKeyHomeByNamespacedKey() {
        assertEquals(MemoryKey.HOME, MemoryKey.getByKey(NamespacedKey.minecraft("home")));
    }

    @Test
    public void shouldGetMemoryKeyJobSiteByNamespacedKey() {
        assertEquals(MemoryKey.JOB_SITE, MemoryKey.getByKey(NamespacedKey.minecraft("job_site")));
    }

    @Test
    public void shouldGetMemoryKeyMeetingPointByNamespacedKey() {
        assertEquals(MemoryKey.MEETING_POINT, MemoryKey.getByKey(NamespacedKey.minecraft("meeting_point")));
    }

    @Test
    public void shouldReturnNullWhenNamespacedKeyisNotPresentAsMemoryKey() {
        assertNull(MemoryKey.getByKey(NamespacedKey.minecraft("not_present")));
    }

    @Test
    public void shouldReturnNullWhenNamespacedKeyisNull() {
        assertNull(MemoryKey.getByKey(null));
    }
}
