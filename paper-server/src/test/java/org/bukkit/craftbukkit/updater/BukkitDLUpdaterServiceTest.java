package org.bukkit.craftbukkit.updater;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

@Ignore ("useful tests, but not necessary to run on each compile")
public class BukkitDLUpdaterServiceTest {
    @Test(expected=IOException.class)
    public void testHostNotFound() throws UnsupportedEncodingException, IOException {
        BukkitDLUpdaterService service = new BukkitDLUpdaterService("404.example.org");

        service.fetchArtifact("rb");
    }

    @Test(expected=FileNotFoundException.class)
    public void testArtifactNotFound() throws UnsupportedEncodingException, IOException {
        BukkitDLUpdaterService service = new BukkitDLUpdaterService("dl.bukkit.org");

        service.fetchArtifact("meep");
    }

    @Test
    public void testArtifactExists() throws UnsupportedEncodingException, IOException {
        BukkitDLUpdaterService service = new BukkitDLUpdaterService("dl.bukkit.org");

        assertNotNull(service.fetchArtifact("latest-dev"));
    }
}
