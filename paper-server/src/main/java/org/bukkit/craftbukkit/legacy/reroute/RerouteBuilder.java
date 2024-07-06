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
            int count = 0;
            boolean injectPluginName = false;
            boolean injectPluginVersion = false;
            String injectCompatibility = null;
            if (parameter.isAnnotationPresent(InjectPluginName.class)) {
                if (parameter.getType() != String.class) {
                    throw new RuntimeException("Plugin name argument must be of type name, but got " + parameter.getType());
                }
                injectPluginName = true;
                count++;
            }

            if (parameter.isAnnotationPresent(InjectPluginVersion.class)) {
                if (parameter.getType() != ApiVersion.class) {
                    throw new RuntimeException("Plugin version argument must be of type ApiVersion, but got " + parameter.getType());
                }
                injectPluginVersion = true;
                count++;
            }

            if (parameter.isAnnotationPresent(InjectCompatibility.class)) {
                if (parameter.getType() != boolean.class) {
                    throw new RuntimeException("Compatibility argument must be of type boolean, but got " + parameter.getType());
                }
                injectCompatibility = parameter.getAnnotation(InjectCompatibility.class).value();
                count++;
            }

            if (count > 1) {
                // This should not happen, since we check types,
                // and those two have different types -> it would already have failed
                throw new RuntimeException("Wtf?");
            }

            RerouteArgumentType rerouteArgumentType = parameter.getAnnotation(RerouteArgumentType.class);
            if (count == 1 && rerouteArgumentType != null) {
                // Why would you do this?
                throw new RuntimeException("Wtf?");
            }

            Type sourceType;
            if (rerouteArgumentType != null) {
                sourceType = Type.getObjectType(rerouteArgumentType.value());
            } else {
                sourceType = type;
            }

            RerouteArgument argument = new RerouteArgument(type, sourceType, injectPluginName, injectPluginVersion, injectCompatibility);
            arguments.add(argument);
            if (count == 0) {
                sourceArguments.add(argument);
            }
        }

        RerouteStatic rerouteStatic = method.getAnnotation(RerouteStatic.class);
        Type sourceOwner;
        if (rerouteStatic != null) {
            sourceOwner = Type.getObjectType(rerouteStatic.value());
        } else {
            RerouteArgument argument = sourceArguments.get(0);
            sourceOwner = argument.sourceType();
            sourceArguments.remove(argument);
        }

        RerouteReturnType rerouteReturnType = method.getAnnotation(RerouteReturnType.class);
        Type returnType;
        if (rerouteReturnType != null) {
            returnType = Type.getObjectType(rerouteReturnType.value());
        } else {
            returnType = rerouteReturn.type();
        }
        Type sourceDesc = Type.getMethodType(returnType, sourceArguments.stream().map(RerouteArgument::sourceType).toArray(Type[]::new));

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

        boolean inBukkit = !method.isAnnotationPresent(NotInBukkit.class) && !method.getDeclaringClass().isAnnotationPresent(NotInBukkit.class);

        String requiredCompatibility = null;
        if (method.isAnnotationPresent(RequireCompatibility.class)) {
            requiredCompatibility = method.getAnnotation(RequireCompatibility.class).value();
        } else if (method.getDeclaringClass().isAnnotationPresent(RequireCompatibility.class)) {
            requiredCompatibility = method.getDeclaringClass().getAnnotation(RequireCompatibility.class).value();
        }

        RequirePluginVersionData requiredPluginVersion = null;
        if (method.isAnnotationPresent(RequirePluginVersion.class)) {
            requiredPluginVersion = RequirePluginVersionData.create(method.getAnnotation(RequirePluginVersion.class));
        } else if (method.getDeclaringClass().isAnnotationPresent(RequirePluginVersion.class)) {
            requiredPluginVersion = RequirePluginVersionData.create(method.getDeclaringClass().getAnnotation(RequirePluginVersion.class));
        }

        return new RerouteMethodData(methodKey, sourceDesc, sourceOwner, methodName, rerouteStatic != null, targetType, Type.getInternalName(method.getDeclaringClass()), method.getName(), arguments, rerouteReturn, inBukkit, requiredCompatibility, requiredPluginVersion);
    }
}
