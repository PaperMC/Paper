package com.destroystokyo.paper.event.executor.asm;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import java.util.concurrent.ConcurrentMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public class SafeClassDefiner implements ClassDefiner {

    /* default */ static final SafeClassDefiner INSTANCE = new SafeClassDefiner();

    private SafeClassDefiner() {
    }

    private final ConcurrentMap<ClassLoader, GeneratedClassLoader> loaders = new MapMaker().weakKeys().makeMap();

    @Override
    public Class<?> defineClass(final ClassLoader parentLoader, final String name, final byte[] data) {
        final GeneratedClassLoader loader = this.loaders.computeIfAbsent(parentLoader, GeneratedClassLoader::new);
        synchronized (loader.getClassLoadingLock(name)) {
            Preconditions.checkState(!loader.hasClass(name), "%s already defined", name);
            final Class<?> c = loader.define(name, data);
            assert c.getName().equals(name);
            return c;
        }
    }

    private static class GeneratedClassLoader extends ClassLoader {

        static {
            ClassLoader.registerAsParallelCapable();
        }

        protected GeneratedClassLoader(final ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(final String name, final byte[] data) {
            synchronized (this.getClassLoadingLock(name)) {
                assert !this.hasClass(name);
                final Class<?> c = this.defineClass(name, data, 0, data.length);
                this.resolveClass(c);
                return c;
            }
        }

        @Override
        public Object getClassLoadingLock(final String name) {
            return super.getClassLoadingLock(name);
        }

        public boolean hasClass(final String name) {
            synchronized (this.getClassLoadingLock(name)) {
                try {
                    Class.forName(name);
                    return true;
                } catch (final ClassNotFoundException e) {
                    return false;
                }
            }
        }
    }
}
