package io.papermc.generator.utils;

import com.squareup.javapoet.AnnotationSpec;
import java.util.List;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.SharedConstants;
import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Annotations {

    public static List<AnnotationSpec> experimentalAnnotations(final String version) {
        return List.of(
            AnnotationSpec.builder(ApiStatus.Experimental.class).build(),
            AnnotationSpec.builder(MinecraftExperimental.class)
                .addMember("value", "$S", version)
                .build()
        );
    }

    public static AnnotationSpec deprecatedVersioned(final @Nullable String version, boolean forRemoval) {
        AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(Deprecated.class);
        if (forRemoval) {
            annotationSpec.addMember("forRemoval", "$L", forRemoval);
        }
        if (version != null) {
            annotationSpec.addMember("since", "$S", version);
        }

        return annotationSpec.build();
    }

    public static AnnotationSpec scheduledRemoval(final @Nullable String version) {
        return AnnotationSpec.builder(ApiStatus.ScheduledForRemoval.class)
            .addMember("inVersion", "$S", version)
            .build();
    }

    @ApiStatus.Experimental
    public static final AnnotationSpec EXPERIMENTAL_API_ANNOTATION = AnnotationSpec.builder(ApiStatus.Experimental.class).build();
    public static final AnnotationSpec NOT_NULL = AnnotationSpec.builder(NotNull.class).build();
    private static final AnnotationSpec SUPPRESS_WARNINGS = AnnotationSpec.builder(SuppressWarnings.class)
        .addMember("value", "$S", "unused")
        .addMember("value", "$S", "SpellCheckingInspection")
        .build();
    private static final AnnotationSpec GENERATED_FROM = AnnotationSpec.builder(GeneratedFrom.class)
        .addMember("value", "$S", SharedConstants.getCurrentVersion().getName())
        .build();
    public static final Iterable<AnnotationSpec> CLASS_HEADER = List.of(
        SUPPRESS_WARNINGS,
        GENERATED_FROM
    );

    private Annotations() {
    }
}
