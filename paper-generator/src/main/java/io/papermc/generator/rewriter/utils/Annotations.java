package io.papermc.generator.rewriter.utils;

import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.context.ImportCollector;
import java.lang.annotation.Annotation;
import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Annotations {

    public static String annotation(Class<? extends Annotation> clazz, ImportCollector collector) {
        return "@%s".formatted(collector.getShortName(clazz));
    }

    public static String annotation(Class<? extends Annotation> clazz, ImportCollector collector, String param, String value) {
        String annotation = annotation(clazz, collector);
        if (value.isEmpty()) {
            return annotation;
        }
        return "%s(%s = %s)".formatted(annotation, param, value);
    }

    public static String annotation(Class<? extends Annotation> clazz, ImportCollector collector, String value) {
        String annotation = annotation(clazz, collector);
        if (value.isEmpty()) {
            return annotation;
        }
        return "%s(%s)".formatted(annotation, value);
    }

    public static void experimentalAnnotations(StringBuilder builder, String indent, ImportCollector importCollector, SingleFlagHolder requiredFeature) {
        builder.append(indent).append(annotation(MinecraftExperimental.class, importCollector, "%s.%s".formatted(
            importCollector.getShortName(MinecraftExperimental.Requires.class, false), requiredFeature.asAnnotationMember().name()
        ))).append('\n');

        builder.append(indent).append(annotation(ApiStatus.Experimental.class, importCollector)).append('\n');
    }

    private Annotations() {
    }
}
