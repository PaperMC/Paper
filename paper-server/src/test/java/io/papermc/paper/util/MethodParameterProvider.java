/*
 * Copyright 2015-2023 the original author or authors of https://github.com/junit-team/junit5/blob/6593317c15fb556febbde11914fa7afe00abf8cd/junit-jupiter-params/src/main/java/org/junit/jupiter/params/provider/MethodArgumentsProvider.java
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package io.papermc.paper.util;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.ClassLoaderUtils;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.StringUtils;
import org.junitpioneer.jupiter.cartesian.CartesianParameterArgumentsProvider;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;
import static org.junit.platform.commons.util.CollectionUtils.isConvertibleToStream;

public class MethodParameterProvider implements CartesianParameterArgumentsProvider<Object>, AnnotationConsumer<MethodParameterSource> {
    private MethodParameterSource source;

    MethodParameterProvider() {
    }

    @Override
    public void accept(final MethodParameterSource source) {
        this.source = source;
    }

    @Override
    public Stream<Object> provideArguments(ExtensionContext context, Parameter parameter) {
        return this.provideArguments(context, this.source);
    }

    // Below is mostly copied from MethodArgumentsProvider

    private static final Predicate<Method> isFactoryMethod = //
        method -> isConvertibleToStream(method.getReturnType()) && !isTestMethod(method);

    protected Stream<Object> provideArguments(ExtensionContext context, MethodParameterSource methodSource) {
        Class<?> testClass = context.getRequiredTestClass();
        Method testMethod = context.getRequiredTestMethod();
        Object testInstance = context.getTestInstance().orElse(null);
        String[] methodNames = methodSource.value();
        // @formatter:off
        return stream(methodNames)
            .map(factoryMethodName -> findFactoryMethod(testClass, testMethod, factoryMethodName))
            .map(factoryMethod -> validateFactoryMethod(factoryMethod, testInstance))
            .map(factoryMethod -> context.getExecutableInvoker().invoke(factoryMethod, testInstance))
            .flatMap(CollectionUtils::toStream);
        // @formatter:on
    }

    private static Method findFactoryMethod(Class<?> testClass, Method testMethod, String factoryMethodName) {
        String originalFactoryMethodName = factoryMethodName;

        // If the user did not provide a factory method name, find a "default" local
        // factory method with the same name as the parameterized test method.
        if (StringUtils.isBlank(factoryMethodName)) {
            factoryMethodName = testMethod.getName();
            return findFactoryMethodBySimpleName(testClass, testMethod, factoryMethodName);
        }

        // Convert local factory method name to fully-qualified method name.
        if (!looksLikeAFullyQualifiedMethodName(factoryMethodName)) {
            factoryMethodName = testClass.getName() + "#" + factoryMethodName;
        }

        // Find factory method using fully-qualified name.
        Method factoryMethod = findFactoryMethodByFullyQualifiedName(testClass, testMethod, factoryMethodName);

        // Ensure factory method has a valid return type and is not a test method.
        Preconditions.condition(isFactoryMethod.test(factoryMethod), () -> format(
            "Could not find valid factory method [%s] for test class [%s] but found the following invalid candidate: %s",
            originalFactoryMethodName, testClass.getName(), factoryMethod));

        return factoryMethod;
    }

    private static boolean looksLikeAFullyQualifiedMethodName(String factoryMethodName) {
        if (factoryMethodName.contains("#")) {
            return true;
        }
        int indexOfFirstDot = factoryMethodName.indexOf('.');
        if (indexOfFirstDot == -1) {
            return false;
        }
        int indexOfLastOpeningParenthesis = factoryMethodName.lastIndexOf('(');
        if (indexOfLastOpeningParenthesis > 0) {
            // Exclude simple/local method names with parameters
            return indexOfFirstDot < indexOfLastOpeningParenthesis;
        }
        // If we get this far, we conclude the supplied factory method name "looks"
        // like it was intended to be a fully-qualified method name, even if the
        // syntax is invalid. We do this in order to provide better diagnostics for
        // the user when a fully-qualified method name is in fact invalid.
        return true;
    }

    // package-private for testing
    static Method findFactoryMethodByFullyQualifiedName(
        Class<?> testClass, Method testMethod,
        String fullyQualifiedMethodName
    ) {
        String[] methodParts = ReflectionUtils.parseFullyQualifiedMethodName(fullyQualifiedMethodName);
        String className = methodParts[0];
        String methodName = methodParts[1];
        String methodParameters = methodParts[2];
        ClassLoader classLoader = ClassLoaderUtils.getClassLoader(testClass);
        Class<?> clazz = loadRequiredClass(className, classLoader);

        // Attempt to find an exact match first.
        Method factoryMethod = ReflectionUtils.findMethod(clazz, methodName, methodParameters).orElse(null);
        if (factoryMethod != null) {
            return factoryMethod;
        }

        boolean explicitParameterListSpecified = //
            StringUtils.isNotBlank(methodParameters) || fullyQualifiedMethodName.endsWith("()");

        // If we didn't find an exact match but an explicit parameter list was specified,
        // that's a user configuration error.
        Preconditions.condition(!explicitParameterListSpecified,
            () -> format("Could not find factory method [%s(%s)] in class [%s]", methodName, methodParameters,
                className));

        // Otherwise, fall back to the same lenient search semantics that are used
        // to locate a "default" local factory method.
        return findFactoryMethodBySimpleName(clazz, testMethod, methodName);
    }

    /**
     * Find the factory method by searching for all methods in the given {@code clazz}
     * with the desired {@code factoryMethodName} which have return types that can be
     * converted to a {@link Stream}, ignoring the {@code testMethod} itself as well
     * as any {@code @Test}, {@code @TestTemplate}, or {@code @TestFactory} methods
     * with the same name.
     *
     * @return the single factory method matching the search criteria
     * @throws PreconditionViolationException if the factory method was not found or
     *                                        multiple competing factory methods with the same name were found
     */
    private static Method findFactoryMethodBySimpleName(Class<?> clazz, Method testMethod, String factoryMethodName) {
        Predicate<Method> isCandidate = candidate -> factoryMethodName.equals(candidate.getName())
            && !testMethod.equals(candidate);
        List<Method> candidates = ReflectionUtils.findMethods(clazz, isCandidate);

        List<Method> factoryMethods = candidates.stream().filter(isFactoryMethod).collect(toList());

        Preconditions.condition(factoryMethods.size() > 0, () -> {
            // If we didn't find the factory method using the isFactoryMethod Predicate, perhaps
            // the specified factory method has an invalid return type or is a test method.
            // In that case, we report the invalid candidates that were found.
            if (candidates.size() > 0) {
                return format(
                    "Could not find valid factory method [%s] in class [%s] but found the following invalid candidates: %s",
                    factoryMethodName, clazz.getName(), candidates);
            }
            // Otherwise, report that we didn't find anything.
            return format("Could not find factory method [%s] in class [%s]", factoryMethodName, clazz.getName());
        });
        Preconditions.condition(factoryMethods.size() == 1,
            () -> format("%d factory methods named [%s] were found in class [%s]: %s", factoryMethods.size(),
                factoryMethodName, clazz.getName(), factoryMethods));
        return factoryMethods.get(0);
    }

    private static boolean isTestMethod(Method candidate) {
        return isAnnotated(candidate, Test.class) || isAnnotated(candidate, TestTemplate.class)
            || isAnnotated(candidate, TestFactory.class);
    }

    private static Class<?> loadRequiredClass(String className, ClassLoader classLoader) {
        return ReflectionUtils.tryToLoadClass(className, classLoader).getOrThrow(
            cause -> new JUnitException(format("Could not load class [%s]", className), cause));
    }

    private static Method validateFactoryMethod(Method factoryMethod, Object testInstance) {
        Preconditions.condition(
            factoryMethod.getDeclaringClass().isInstance(testInstance) || ReflectionUtils.isStatic(factoryMethod),
            () -> format("Method '%s' must be static: local factory methods must be static "
                    + "unless the PER_CLASS @TestInstance lifecycle mode is used; "
                    + "external factory methods must always be static.",
                factoryMethod.toGenericString()));
        return factoryMethod;
    }
}
