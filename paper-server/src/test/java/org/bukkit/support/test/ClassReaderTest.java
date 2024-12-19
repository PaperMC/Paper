package org.bukkit.support.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.support.provider.ClassReaderArgumentProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ClassReaderArgumentProvider.class)
@ParameterizedTest
public @interface ClassReaderTest {

    ClassType[] value() default {ClassType.BUKKIT, ClassType.CRAFT_BUKKIT};

    Class<?>[] excludedClasses() default {};

    String[] excludedPackages() default {};

    enum ClassType {
        BUKKIT,
        CRAFT_BUKKIT,
    }
}
