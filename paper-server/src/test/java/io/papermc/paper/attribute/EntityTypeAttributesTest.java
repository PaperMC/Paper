package io.papermc.paper.attribute;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AllFeatures
public class EntityTypeAttributesTest {

    @Test
    public void testIllegalEntity() {
        assertFalse(EntityType.EGG.hasDefaultAttributes());
        assertThrows(IllegalArgumentException.class, EntityType.EGG::getDefaultAttributes);
    }

    @Test
    public void testLegalEntity() {
        assertTrue(EntityType.ZOMBIE.hasDefaultAttributes());
        EntityType.ZOMBIE.getDefaultAttributes();
    }

    @Test
    public void testUnmodifiabilityOfAttributable() {
        Attributable attributable = EntityType.ZOMBIE.getDefaultAttributes();
        assertThrows(UnsupportedOperationException.class, () -> attributable.registerAttribute(Attribute.ATTACK_DAMAGE));
        AttributeInstance instance = attributable.getAttribute(Attribute.FOLLOW_RANGE);
        assertNotNull(instance);
        assertThrows(UnsupportedOperationException.class, () -> instance.addModifier(new AttributeModifier("test", 3, AttributeModifier.Operation.ADD_NUMBER)));
        assertThrows(UnsupportedOperationException.class, () -> instance.setBaseValue(3.2));
    }
}
