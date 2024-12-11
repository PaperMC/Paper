package io.papermc.paper.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.meta.NodeResolver;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NestedSetting {

    String[] value();

    class Factory implements NodeResolver.Factory {
        @Override
        public @Nullable NodeResolver make(String name, AnnotatedElement element) {
            if (element.isAnnotationPresent(NestedSetting.class)) {
                Object[] path = element.getAnnotation(NestedSetting.class).value();
                if (path.length > 0) {
                    return node -> node.node(path);
                }
            }
            return null;
        }
    }
}
