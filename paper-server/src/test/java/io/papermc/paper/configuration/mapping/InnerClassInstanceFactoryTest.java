package io.papermc.paper.configuration.mapping;

import io.papermc.paper.configuration.ConfigurationPart;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

import static io.leangen.geantyref.GenericTypeReflector.erase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Normal
public class InnerClassInstanceFactoryTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Rejected {
    }

    private static final Definition<Rejected, String, FieldProcessor.Factory<Rejected, String>> REJECTED = new Definition<>(
        Rejected.class,
        String.class,
        (data, annotatedType) -> (target, deserializedValue, valueInField) -> {
            throw new SerializationException(target.getType(), "rejected " + deserializedValue);
        }
    );

    public static class Part extends ConfigurationPart {
        @Rejected
        public String first = "a";
        @Rejected
        public String second = "b";
        public String plain = "c";
    }

    @Test
    public void processorFailuresAreThrown() throws Exception {
        final List<Definition<?, ?, ? extends FieldProcessor.Factory<?, ?>>> processors = List.of(REJECTED);
        final ObjectMapper.Factory factory = ObjectMapper.factoryBuilder()
            .addDiscoverer(InnerClassFieldDiscoverer.globalConfig(processors))
            .build();
        final ConfigurationNode node = BasicConfigurationNode.root(ConfigurationOptions.defaults()
            .serializers(builder -> builder.register(type -> ConfigurationPart.class.isAssignableFrom(erase(type)), factory.asTypeSerializer())));
        node.node("first").raw("x");
        node.node("second").raw("y");

        final SerializationException exception = assertThrows(SerializationException.class, () -> node.get(Part.class));
        assertEquals(1, exception.getSuppressed().length, "every failing field must be reported");
    }
}
