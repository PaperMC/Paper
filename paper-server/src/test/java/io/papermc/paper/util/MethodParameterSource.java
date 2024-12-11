package io.papermc.paper.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.Language;
import org.junitpioneer.jupiter.cartesian.CartesianArgumentsSource;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@CartesianArgumentsSource(MethodParameterProvider.class)
public @interface MethodParameterSource {
    @Language("jvm-method-name")
    String[] value() default {};
}
