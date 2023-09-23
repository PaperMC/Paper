package org.bukkit.entity.memory;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.memory.CraftMemoryKey;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CraftMemoryKeyTest extends AbstractTestingBase {

    @Test
    public void shouldConvertBukkitHomeKeyToNMSRepresentation() {
        MemoryModuleType<GlobalPos> nmsHomeKey = CraftMemoryKey.bukkitToMinecraft(MemoryKey.HOME);
        assertEquals(MemoryModuleType.HOME, nmsHomeKey, "MemoryModuleType should be HOME");
    }

    @Test
    public void shouldConvertBukkitJobSiteKeyToNMSRepresentation() {
        MemoryModuleType<GlobalPos> nmsHomeKey = CraftMemoryKey.bukkitToMinecraft(MemoryKey.JOB_SITE);
        assertEquals(MemoryModuleType.JOB_SITE, nmsHomeKey, "MemoryModuleType should be JOB_SITE");
    }

    @Test
    public void shouldConvertBukkitMeetingPointKeyToNMSRepresentation() {
        MemoryModuleType<GlobalPos> nmsHomeKey = CraftMemoryKey.bukkitToMinecraft(MemoryKey.MEETING_POINT);
        assertEquals(MemoryModuleType.MEETING_POINT, nmsHomeKey, "MemoryModuleType should be MEETING_POINT");
    }

    @Test
    public void shouldConvertNMSHomeKeyToBukkitRepresentation() {
        MemoryKey<Location> bukkitHomeKey = CraftMemoryKey.minecraftToBukkit(MemoryModuleType.HOME);
        assertEquals(MemoryKey.HOME, bukkitHomeKey, "MemoryModuleType should be HOME");
    }

    @Test
    public void shouldConvertNMSJobSiteKeyToBukkitRepresentation() {
        MemoryKey<Location> bukkitJobSiteKey = CraftMemoryKey.minecraftToBukkit(MemoryModuleType.JOB_SITE);
        assertEquals(MemoryKey.JOB_SITE, bukkitJobSiteKey, "MemoryKey should be JOB_SITE");
    }

    @Test
    public void shouldConvertNMSMeetingPointKeyToBukkitRepresentation() {
        MemoryKey<Location> bukkitHomeKey = CraftMemoryKey.minecraftToBukkit(MemoryModuleType.MEETING_POINT);
        assertEquals(MemoryKey.MEETING_POINT, bukkitHomeKey, "MemoryKey should be MEETING_POINT");
    }

    @Test
    public void shouldReturnNullWhenBukkitRepresentationOfKeyisNotAvailable() {
        MemoryKey bukkitNoKey = CraftMemoryKey.minecraftToBukkit(MemoryModuleType.NEAREST_LIVING_ENTITIES);
        assertNull(bukkitNoKey, "MemoryModuleType should be null");
    }

    @Test
    public void shouldReturnNullWhenBukkitRepresentationOfKeyisNotAvailableAndSerializerIsNotPresent() {
        for (MemoryModuleType<?> memoryModuleType : BuiltInRegistries.MEMORY_MODULE_TYPE) {
            if (!memoryModuleType.getCodec().isPresent()) {
                MemoryKey bukkitNoKey = CraftMemoryKey.minecraftToBukkit(memoryModuleType);
                assertNull(bukkitNoKey, "MemoryModuleType should be null");
            }
        }
    }

    @Test
    @Disabled("Unit type not yet implemented")
    public void shouldReturnAnInstanceOfMemoryKeyWhenBukkitRepresentationOfKeyisAvailableAndSerializerIsPresent() {
        for (MemoryModuleType<?> memoryModuleType : BuiltInRegistries.MEMORY_MODULE_TYPE) {
            if (memoryModuleType.getCodec().isPresent()) {
                MemoryKey bukkitNoKey = CraftMemoryKey.minecraftToBukkit(memoryModuleType);
                assertNotNull(bukkitNoKey, "MemoryModuleType should not be null " + BuiltInRegistries.MEMORY_MODULE_TYPE.getKey(memoryModuleType));
            }
        }
    }
}
