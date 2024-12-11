package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import com.google.common.base.Predicates;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.bukkit.craftbukkit.util.Commodore;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.Type;

@Normal
public class RerouteValidationTest {

    public static Stream<Arguments> data() {
        Commodore commodore = new Commodore(Predicates.alwaysTrue());
        return commodore.getReroutes().stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testReroutes(Reroute reroute) {
        Map<String, String> wrongReroutes = new HashMap<>();
        String owner = null;

        for (Map.Entry<String, Reroute.RerouteDataHolder> value : reroute.rerouteDataMap.entrySet()) {
            for (Map.Entry<String, RerouteMethodData> entry : value.getValue().rerouteMethodDataMap.entrySet()) {
                owner = entry.getValue().targetOwner();
                if (!entry.getValue().isInBukkit()) {
                    continue;
                }

                String error = this.isValid(entry.getKey(), entry.getValue());

                if (error != null) {
                    wrongReroutes.put(entry.getKey(), error);
                }
            }
        }

        assertTrue(wrongReroutes.isEmpty(), String.format("""
                There are validation errors for method reroutes in Class: %s
                This means there is no methods in bukkit matching the reroute.
                If the method is no longer present in bukkit because it got removed, than mark the reroute method with @NotInBukkit

                Following Errors where found:
                %s
                """, owner, Joiner.on('\n').withKeyValueSeparator(": ").join(wrongReroutes)));
    }

    private String isValid(String key, RerouteMethodData rerouteMethodData) {
        try {
            Class<?> clazz = this.toClass(rerouteMethodData.sourceOwner());
            Class<?> returnClazz = this.toClass(rerouteMethodData.sourceDesc().getReturnType());
            Class<?>[] paras = new Class[rerouteMethodData.sourceDesc().getArgumentCount()];
            Type[] paraTypes = rerouteMethodData.sourceDesc().getArgumentTypes();

            for (int i = 0; i < paraTypes.length; i++) {
                paras[i] = this.toClass(paraTypes[i]);
            }

            Method method = clazz.getDeclaredMethod(rerouteMethodData.sourceName(), paras);

            if (method.getReturnType() != returnClazz) {
                return "Return type mismatch expected " + method.getReturnType().getName() + " got: " + returnClazz;
            }

            if (!Modifier.isPublic(method.getModifiers())) {
                return "Method is not public";
            }

            if (Modifier.isStatic(method.getModifiers()) && !rerouteMethodData.staticReroute()) {
                return "Method is static, but the reroute data is not marked as static reroute";
            }

            if (!Modifier.isStatic(method.getModifiers()) && rerouteMethodData.staticReroute()) {
                return "Method is not static, but the reroute data is marked as static reroute";
            }
        } catch (ClassNotFoundException e) {
            return "No such Class: " + e.getLocalizedMessage();
        } catch (NoSuchMethodException e) {
            return "No such method: " + e.getLocalizedMessage();
        }

        return null;
    }

    private Class<?> toClass(Type type) throws ClassNotFoundException {
        if (type.getSort() == Type.OBJECT) {
            return Class.forName(type.getClassName(), false, this.getClass().getClassLoader());
        } else if (type.getSort() == Type.ARRAY) {
            return Class.forName(type.getDescriptor().replace('/', '.'), false, this.getClass().getClassLoader());
        } else {
            return switch (type.getSort()) {
                case Type.VOID -> void.class;
                case Type.BOOLEAN -> boolean.class;
                case Type.CHAR -> char.class;
                case Type.BYTE -> byte.class;
                case Type.SHORT -> short.class;
                case Type.INT -> int.class;
                case Type.FLOAT -> float.class;
                case Type.LONG -> long.class;
                case Type.DOUBLE -> double.class;
                default -> throw new UnsupportedOperationException("Type not supported: " + type);
            };
        }
    }
}
