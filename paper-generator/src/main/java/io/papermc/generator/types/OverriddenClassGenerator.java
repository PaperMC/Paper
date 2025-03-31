package io.papermc.generator.types;

import com.mojang.logging.LogUtils;
import com.squareup.javapoet.MethodSpec;
import io.papermc.generator.utils.Annotations;
import io.papermc.typewriter.ClassNamed;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

import static javax.lang.model.element.Modifier.PUBLIC;

@NullMarked
public abstract class OverriddenClassGenerator extends SimpleGenerator {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final ClassNamed baseClass;
    protected boolean printWarningOnMissingOverride;

    protected OverriddenClassGenerator(ClassNamed baseClass, String className, String packageName) {
        super(className, packageName);
        this.baseClass = baseClass;
    }

    public ClassNamed getBaseClass() {
        return this.baseClass;
    }

    public MethodSpec.Builder createMethod(String name, Class<?>... parameterTypes) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(name)
            .addModifiers(PUBLIC);
        if (this.baseClass.knownClass() == null || methodExists(name, parameterTypes)) {
            methodBuilder.addAnnotation(Annotations.OVERRIDE);
        } else {
            if (this.printWarningOnMissingOverride) {
                LOGGER.warn("Method {}#{}{} didn't override a known api method!", this.className, name, LogUtils.defer(() -> Arrays.toString(parameterTypes)));
            }
        }
        return methodBuilder;
    }

    public MethodSpec.Builder createUnsafeMethod(String name) {
        return MethodSpec.methodBuilder(name)
            .addModifiers(PUBLIC)
            .addAnnotation(Annotations.OVERRIDE);
    }

    protected boolean methodExists(String name, Class<?>... parameterTypes) {
        try {
            this.baseClass.knownClass().getMethod(name, parameterTypes);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
