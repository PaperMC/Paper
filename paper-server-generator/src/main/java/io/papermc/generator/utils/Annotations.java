package io.papermc.generator.utils;

import com.squareup.javapoet.AnnotationSpec;
import java.util.ArrayList;
import java.util.List;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.SharedConstants;
import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

public final class Annotations {

    public static List<AnnotationSpec> experimentalAnnotations(final MinecraftExperimental.@Nullable Requires requiredFeatureFlag) {
        final List<AnnotationSpec> annotationSpecs = new ArrayList<>();
        annotationSpecs.add(AnnotationSpec.builder(ApiStatus.Experimental.class).build());
        if (requiredFeatureFlag != null) {
            annotationSpecs.add(AnnotationSpec.builder(MinecraftExperimental.class)
                .addMember("value", "$T.$L", MinecraftExperimental.Requires.class, requiredFeatureFlag.name())
                .build());
        } else {
            annotationSpecs.add(AnnotationSpec.builder(MinecraftExperimental.class).build());
        }
        return annotationSpecs;
    }

    public static AnnotationSpec deprecatedVersioned(final @Nullable String version, final boolean forRemoval) {
        final AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(Deprecated.class);
        if (forRemoval) {
            annotationSpec.addMember("forRemoval", "$L", true);
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
    public static final AnnotationSpec NULL_MARKED = AnnotationSpec.builder(NullMarked.class).build();
    private static final AnnotationSpec SUPPRESS_WARNINGS = AnnotationSpec.builder(SuppressWarnings.class)
        .addMember("value", "$S", "unused")
        .addMember("value", "$S", "SpellCheckingInspection")
        .build();
    private static final AnnotationSpec GENERATED_FROM = AnnotationSpec.builder(GeneratedFrom.class)
        .addMember("value", "$S", SharedConstants.getCurrentVersion().getName())
        .build();
    public static final Iterable<AnnotationSpec> CLASS_HEADER = List.of(
        SUPPRESS_WARNINGS,
        GENERATED_FROM,
        NULL_MARKED
    );

    private Annotations() {
    }
}
