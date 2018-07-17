package io.papermc.paper;

import io.papermc.paper.tag.EntityTags;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

public class EntityTagsTest extends AbstractTestingBase {

    @Test
    public void testInitialize() {
        try {
            EntityTags.HORSES.getValues();
            assert true;
        } catch (Throwable e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            assert false;
        }
    }
}
