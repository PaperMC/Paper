package io.papermc.paper.configuration.constraint;

import io.papermc.paper.configuration.type.number.DoubleOr;
import io.papermc.paper.configuration.type.number.IntOr;
import io.papermc.paper.configuration.type.number.LongOr;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Normal
public class ConstraintsTest {

    @ConfigSerializable
    static class Ranged {
        @Constraints.Min(1)
        @Constraints.Max(10)
        int primitiveInt = 5;
        @Constraints.Min(0.5)
        double primitiveDouble = 1;
        @Constraints.Min(1)
        IntOr.Default intOr = IntOr.Default.USE_DEFAULT;
        @Constraints.Max(10)
        LongOr.Default longOr = LongOr.Default.USE_DEFAULT;
        @Constraints.Min(0.5)
        DoubleOr.Default doubleOr = DoubleOr.Default.USE_DEFAULT;
    }

    @ConfigSerializable
    static class Unsupported {
        @Constraints.Min(1)
        String value = "a";
    }

    private static ConfigurationNode node() {
        final ObjectMapper.Factory factory = ObjectMapper.factoryBuilder()
            .addConstraint(Constraints.Min.class, new Constraints.Min.Factory())
            .addConstraint(Constraints.Max.class, new Constraints.Max.Factory())
            .build();
        return BasicConfigurationNode.root(ConfigurationOptions.defaults().serializers(builder -> builder
            .registerAnnotatedObjects(factory)
            .register(IntOr.Default.SERIALIZER)
            .register(LongOr.Default.SERIALIZER)
            .register(DoubleOr.Default.SERIALIZER)));
    }

    private static void assertRejected(final String key, final Object value) throws SerializationException {
        final ConfigurationNode node = node();
        node.node(key).set(value);
        assertThrows(SerializationException.class, () -> node.get(Ranged.class), key + "=" + value + " should be rejected");
    }

    @Test
    public void inRangeValuesAreAccepted() throws SerializationException {
        final ConfigurationNode node = node();
        node.node("primitive-int").set(10);
        node.node("primitive-double").set(0.5);
        node.node("int-or").set(1);
        node.node("long-or").set(10L);
        node.node("double-or").set(0.5);
        assertDoesNotThrow(() -> node.get(Ranged.class));
    }

    @Test
    public void defaultValuesAreNotRangeChecked() {
        final ConfigurationNode node = node();
        assertDoesNotThrow(() -> node.get(Ranged.class));
    }

    @Test
    public void outOfRangeValuesAreRejected() throws SerializationException {
        assertRejected("primitive-int", 0);
        assertRejected("primitive-int", 11);
        assertRejected("primitive-double", 0.4);
        assertRejected("int-or", 0);
        assertRejected("long-or", 11L);
        assertRejected("double-or", 0.4);
    }

    @Test
    public void unsupportedTypesFailWhenMapperIsCreated() {
        final ConfigurationNode node = node();
        assertThrows(Exception.class, () -> node.get(Unsupported.class));
    }
}
