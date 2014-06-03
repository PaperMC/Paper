package org.bukkit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class BukkitMirrorTest {

    @Parameters(name="{index}: {1}")
    public static List<Object[]> data() {
        return Lists.transform(Arrays.asList(Server.class.getDeclaredMethods()), new Function<Method, Object[]>() {
            @Override
            public Object[] apply(Method input) {
                return new Object[] {
                    input,
                    input.toGenericString().substring("public abstract ".length()).replace("(", "{").replace(")", "}")
                    };
            }
        });
    }

    @Parameter(0)
    public Method server;

    @Parameter(1)
    public String name;

    private Method bukkit;

    @Before
    public void makeBukkit() throws Throwable {
        bukkit = Bukkit.class.getDeclaredMethod(server.getName(), server.getParameterTypes());
    }

    @Test
    public void isStatic() throws Throwable {
        assertThat(Modifier.isStatic(bukkit.getModifiers()), is(true));
    }

    @Test
    public void isDeprecated() throws Throwable {
        assertThat(bukkit.isAnnotationPresent(Deprecated.class), is(server.isAnnotationPresent(Deprecated.class)));
    }

    @Test
    public void returnType() throws Throwable {
        assertThat(bukkit.getReturnType(), is((Object) server.getReturnType()));
        assertThat(bukkit.getGenericReturnType(), is(server.getGenericReturnType()));
    }

    @Test
    public void parameterTypes() throws Throwable {
        assertThat(bukkit.getGenericParameterTypes(), is(server.getGenericParameterTypes()));
    }

    @Test
    public void declaredException() throws Throwable {
        assertThat(bukkit.getGenericExceptionTypes(), is(server.getGenericExceptionTypes()));
    }
}
