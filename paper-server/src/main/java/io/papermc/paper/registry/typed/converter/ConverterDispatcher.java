package io.papermc.paper.registry.typed.converter;

public interface ConverterDispatcher<T> {

    void add(Iterable<T> types);

    void add(T type, T... types);
}
