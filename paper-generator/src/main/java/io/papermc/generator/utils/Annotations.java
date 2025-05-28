package io.papermc.generator.utils;

import com.squareup.javapoet.AnnotationSpec;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.List;
import net.minecraft.SharedConstants;
import org.bukkit.MinecraftExperimental;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Annotations {

    public static List<AnnotationSpec> experimentalAnnotations(SingleFlagHolder requiredFeature) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(MinecraftExperimental.class);
        builder.addMember("value", "$T.$L", MinecraftExperimental.Requires.class, requiredFeature.asAnnotationMember().name());

        return List.of(
            AnnotationSpec.builder(ApiStatus.Experimental.class).build(),
            builder.build()
        );
    }

    public static AnnotationSpec suppressWarnings(String... values) {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(SuppressWarnings.class);
        for (String value : values) {
            builder.addMember("value", "$S", value);
        }
        return builder.build();
    }

    @ApiStatus.Experimental
    public static final AnnotationSpec EXPERIMENTAL_API_ANNOTATION = AnnotationSpec.builder(ApiStatus.Experimental.class).build();
    public static final AnnotationSpec NULL_MARKED = AnnotationSpec.builder(NullMarked.class).build();
    public static final AnnotationSpec OVERRIDE = AnnotationSpec.builder(Override.class).build();
    public static final AnnotationSpec GENERATED_FROM = AnnotationSpec.builder(GeneratedFrom.class)
        .addMember("value", "$S", SharedConstants.getCurrentVersion().id())
        .build();
    public static final Iterable<AnnotationSpec> CLASS_HEADER = List.of(
        suppressWarnings("unused", "SpellCheckingInspection"),
        NULL_MARKED,
        GENERATED_FROM
    );

    private Annotations() {
    }
}
