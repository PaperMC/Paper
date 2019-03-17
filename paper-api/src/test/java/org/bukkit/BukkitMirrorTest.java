package org.bukkit;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@org.junit.jupiter.api.Disabled // Paper
public class BukkitMirrorTest {

    public static Stream<Arguments> data() {
        return Stream.of(Server.class.getDeclaredMethods())
                .map(method -> {
                    try {
                        return Arguments.of(
                                method,
                                method.toGenericString().substring("public abstract ".length()).replace("(", "{").replace(")", "}"),
                                Bukkit.class.getDeclaredMethod(method.getName(), method.getParameterTypes())
                        );
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @ParameterizedTest
    @MethodSource("data")
    public void isStatic(Method server, String name, Method bukkit) throws Throwable {
        assertThat(Modifier.isStatic(bukkit.getModifiers()), is(true));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void isDeprecated(Method server, String name, Method bukkit) throws Throwable {
        assertThat(bukkit.isAnnotationPresent(Deprecated.class), is(server.isAnnotationPresent(Deprecated.class)));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void returnType(Method server, String name, Method bukkit) throws Throwable {
        assertThat(bukkit.getReturnType(), is((Object) server.getReturnType()));
        // assertThat(bukkit.getGenericReturnType(), is(server.getGenericReturnType())); // too strict on <T> type generics
    }

    @ParameterizedTest
    @MethodSource("data")
    public void parameterTypes(Method server, String name, Method bukkit) throws Throwable {
        // assertThat(bukkit.getGenericParameterTypes(), is(server.getGenericParameterTypes())); // too strict on <T> type generics
    }

    @ParameterizedTest
    @MethodSource("data")
    public void declaredException(Method server, String name, Method bukkit) throws Throwable {
        assertThat(bukkit.getGenericExceptionTypes(), is(server.getGenericExceptionTypes()));
    }
}
