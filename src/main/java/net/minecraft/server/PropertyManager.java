package net.minecraft.server;

import com.google.common.base.MoreObjects;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import joptsimple.OptionSet; // CraftBukkit

public abstract class PropertyManager<T extends PropertyManager<T>> {

    private static final Logger LOGGER = LogManager.getLogger();
    public final Properties properties;
    // CraftBukkit start
    private OptionSet options = null;

    public PropertyManager(Properties properties, final OptionSet options) {
        this.properties = properties;

        this.options = options;
    }

    private String getOverride(String name, String value) {
        if ((this.options != null) && (this.options.has(name))) {
            return String.valueOf(this.options.valueOf(name));
        }

        return value;
    }
    // CraftBukkit end

    public static Properties loadPropertiesFile(java.nio.file.Path java_nio_file_path) {
        Properties properties = new Properties();

        try {
            InputStream inputstream = Files.newInputStream(java_nio_file_path);
            Throwable throwable = null;

            try {
                properties.load(inputstream);
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (inputstream != null) {
                    if (throwable != null) {
                        try {
                            inputstream.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        inputstream.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            PropertyManager.LOGGER.error("Failed to load properties from file: " + java_nio_file_path);
        }

        return properties;
    }

    public void savePropertiesFile(java.nio.file.Path java_nio_file_path) {
        try {
            // CraftBukkit start - Don't attempt writing to file if it's read only
            if (java_nio_file_path.toFile().exists() && !java_nio_file_path.toFile().canWrite()) {
                return;
            }
            // CraftBukkit end
            OutputStream outputstream = Files.newOutputStream(java_nio_file_path);
            Throwable throwable = null;

            try {
                this.properties.store(outputstream, "Minecraft server properties");
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (outputstream != null) {
                    if (throwable != null) {
                        try {
                            outputstream.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        outputstream.close();
                    }
                }

            }
        } catch (IOException ioexception) {
            PropertyManager.LOGGER.error("Failed to store properties to file: " + java_nio_file_path);
        }

    }

    private static <V extends Number> Function<String, V> a(Function<String, V> function) {
        return (s) -> {
            try {
                return (V) function.apply(s); // CraftBukkit - decompile error
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        };
    }

    protected static <V> Function<String, V> a(IntFunction<V> intfunction, Function<String, V> function) {
        return (s) -> {
            try {
                return intfunction.apply(Integer.parseInt(s));
            } catch (NumberFormatException numberformatexception) {
                return function.apply(s);
            }
        };
    }

    @Nullable
    private String c(String s) {
        return (String) getOverride(s, this.properties.getProperty(s)); // CraftBukkit
    }

    @Nullable
    protected <V> V a(String s, Function<String, V> function) {
        String s1 = this.c(s);

        if (s1 == null) {
            return null;
        } else {
            this.properties.remove(s);
            return function.apply(s1);
        }
    }

    protected <V> V a(String s, Function<String, V> function, Function<V, String> function1, V v0) {
        String s1 = this.c(s);
        V v1 = MoreObjects.firstNonNull(s1 != null ? function.apply(s1) : null, v0);

        this.properties.put(s, function1.apply(v1));
        return v1;
    }

    protected <V> PropertyManager<T>.EditableProperty<V> b(String s, Function<String, V> function, Function<V, String> function1, V v0) {
        String s1 = this.c(s);
        V v1 = MoreObjects.firstNonNull(s1 != null ? function.apply(s1) : null, v0);

        this.properties.put(s, function1.apply(v1));
        return new PropertyManager.EditableProperty(s, v1, function1); // CraftBukkit - decompile error
    }

    protected <V> V a(String s, Function<String, V> function, UnaryOperator<V> unaryoperator, Function<V, String> function1, V v0) {
        return this.a(s, (s1) -> {
            V v1 = function.apply(s1);

            return v1 != null ? unaryoperator.apply(v1) : null;
        }, function1, v0);
    }

    protected <V> V a(String s, Function<String, V> function, V v0) {
        return this.a(s, function, Objects::toString, v0);
    }

    protected <V> PropertyManager<T>.EditableProperty<V> b(String s, Function<String, V> function, V v0) {
        return this.b(s, function, Objects::toString, v0);
    }

    protected String getString(String s, String s1) {
        return (String) this.a(s, Function.identity(), Function.identity(), s1);
    }

    @Nullable
    protected String a(String s) {
        return (String) this.a(s, Function.identity());
    }

    protected int getInt(String s, int i) {
        return (Integer) this.a(s, a(Integer::parseInt), i); // CraftBukkit - decompile error
    }

    protected PropertyManager<T>.EditableProperty<Integer> b(String s, int i) {
        return this.b(s, a(Integer::parseInt), i);
    }

    protected int a(String s, UnaryOperator<Integer> unaryoperator, int i) {
        return (Integer) this.a(s, a(Integer::parseInt), unaryoperator, Objects::toString, i);
    }

    protected long getLong(String s, long i) {
        return (Long) this.a(s, a(Long::parseLong), i); // CraftBukkit - decompile error
    }

    protected boolean getBoolean(String s, boolean flag) {
        return (Boolean) this.a(s, Boolean::valueOf, (Object) flag);
    }

    protected PropertyManager<T>.EditableProperty<Boolean> b(String s, boolean flag) {
        return this.b(s, Boolean::valueOf, flag);
    }

    @Nullable
    protected Boolean b(String s) {
        return (Boolean) this.a(s, Boolean::valueOf);
    }

    protected Properties a() {
        Properties properties = new Properties();

        properties.putAll(this.properties);
        return properties;
    }

    protected abstract T reload(IRegistryCustom iregistrycustom, Properties properties, OptionSet optionset); // CraftBukkit

    public class EditableProperty<V> implements Supplier<V> {

        private final String b;
        private final V c;
        private final Function<V, String> d;

        private EditableProperty(String s, V object, Function function) { // CraftBukkit - decompile error
            this.b = s;
            this.c = object;
            this.d = function;
        }

        public V get() {
            return this.c;
        }

        public T set(IRegistryCustom iregistrycustom, V v0) {
            Properties properties = PropertyManager.this.a();

            properties.put(this.b, this.d.apply(v0));
            return PropertyManager.this.reload(iregistrycustom, properties, PropertyManager.this.options); // CraftBukkit
        }
    }
}
