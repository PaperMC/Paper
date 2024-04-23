package org.bukkit.craftbukkit.legacy.reroute;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.objectweb.asm.Type;

public class RerouteBuilder {

    public static Map<String, RerouteMethodData> buildFromClass(Class<?> clazz) {
        Preconditions.checkArgument(!clazz.isInterface(), "Interface Classes are currently not supported");

        Map<String, RerouteMethodData> result = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isBridge()) {
                continue;
            }

            if (method.isSynthetic()) {
                continue;
            }

            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            if (!Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            if (method.isAnnotationPresent(DoNotReroute.class)) {
                continue;
            }

            RerouteMethodData rerouteMethodData = buildFromMethod(method);
            result.put(rerouteMethodData.source(), rerouteMethodData);
        }

        return Collections.unmodifiableMap(result);
    }

    public static RerouteMethodData buildFromMethod(Method method) {
        RerouteReturn rerouteReturn = new RerouteReturn(Type.getReturnType(method));
        List<RerouteArgument> arguments = new ArrayList<>();
        List<RerouteArgument> sourceArguments = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            Type type = Type.getType(parameter.getType());
            boolean injectPluginName = false;
            boolean injectPluginVersion = false;
            if (parameter.isAnnotationPresent(InjectPluginName.class)) {
                if (parameter.getType() != String.class) {
                    throw new RuntimeException("Plugin name argument must be of type name, but got " + parameter.getType());
                }
                injectPluginName = true;
            }

            if (parameter.isAnnotationPresent(InjectPluginVersion.class)) {
                if (parameter.getType() != ApiVersion.class) {
                    throw new RuntimeException("Plugin version argument must be of type ApiVersion, but got " + parameter.getType());
                }
                injectPluginVersion = true;
            }

            if (injectPluginName && injectPluginVersion) {
                // This should not happen, since we check types,
                // and those two have different types -> it would already have failed
                throw new RuntimeException("Wtf?");
            }

            RerouteArgument argument = new RerouteArgument(type, injectPluginName, injectPluginVersion);
            arguments.add(argument);
            if (!injectPluginName && !injectPluginVersion) {
                sourceArguments.add(argument);
            }
        }

        RerouteStatic rerouteStatic = method.getAnnotation(RerouteStatic.class);
        Type sourceOwner;
        if (rerouteStatic != null) {
            sourceOwner = Type.getObjectType(rerouteStatic.value());
        } else {
            RerouteArgument argument = sourceArguments.get(0);
            sourceOwner = argument.type();
            sourceArguments.remove(argument);
        }
        Type sourceDesc = Type.getMethodType(rerouteReturn.type(), sourceArguments.stream().map(RerouteArgument::type).toArray(Type[]::new));

        RerouteMethodName rerouteMethodName = method.getAnnotation(RerouteMethodName.class);
        String methodName;
        if (rerouteMethodName != null) {
            methodName = rerouteMethodName.value();
        } else {
            methodName = method.getName();
        }

        String methodKey = sourceOwner.getInternalName()
                + " "
                + sourceDesc.getDescriptor()
                + " "
                + methodName;

        Type targetType = Type.getType(method);

        return new RerouteMethodData(methodKey, sourceDesc, sourceOwner, methodName, rerouteStatic != null, targetType, Type.getInternalName(method.getDeclaringClass()), method.getName(), arguments, rerouteReturn);
    }
}
