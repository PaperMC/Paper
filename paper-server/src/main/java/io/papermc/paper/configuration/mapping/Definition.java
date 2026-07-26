package io.papermc.paper.configuration.mapping;

import io.leangen.geantyref.TypeToken;
import java.lang.annotation.Annotation;

public record Definition<A extends Annotation, T, F>(Class<A> annotation, TypeToken<T> type, F factory) {

    public Definition(final Class<A> annotation, final Class<T> type, final F factory) {
        this(annotation, TypeToken.get(type), factory);
    }
}
