package com.destroystokyo.paper.event.executor.asm;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public interface ClassDefiner {

    /**
     * Returns if the defined classes can bypass access checks
     *
     * @return if classes bypass access checks
     */
    default boolean isBypassAccessChecks() {
        return false;
    }

    /**
     * Define a class
     *
     * @param parentLoader the parent classloader
     * @param name         the name of the class
     * @param data         the class data to load
     * @return the defined class
     * @throws ClassFormatError     if the class data is invalid
     * @throws NullPointerException if any of the arguments are null
     */
    Class<?> defineClass(ClassLoader parentLoader, String name, byte[] data);

    static ClassDefiner getInstance() {
        return SafeClassDefiner.INSTANCE;
    }

}
