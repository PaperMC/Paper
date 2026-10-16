package io.papermc.paper.plugin;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.util.List;
import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@Normal
class DeprecationWarningsTest { // require on any deprecated events for clarity (especially given the default behavior)

    static List<Class<Event>> eventClasses() {
        try (final ScanResult result = new ClassGraph().enableClassInfo().acceptPackages(
            Event.class.getPackageName(),
            "io.papermc.paper.event",
            "com.destroystokyo.paper.event",
            "org.spigotmc.event"
        ).scan()) {
            return result.getSubclasses(Event.class.getName()).loadClasses(Event.class);
        }
    }

    @ParameterizedTest
    @MethodSource("eventClasses")
    void ensureAnnotationPresent(final Class<? extends Event> eventClass) {
        if (eventClass.isAnnotationPresent(Deprecated.class)) {
            Assertions.assertTrue(eventClass.isAnnotationPresent(Warning.class), eventClass.getName() + " should be annotated with @Warning(propagate = false, ...)");
        }
    }
}
