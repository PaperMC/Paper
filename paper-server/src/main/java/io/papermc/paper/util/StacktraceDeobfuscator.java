package io.papermc.paper.util;

import io.papermc.paper.configuration.GlobalConfiguration;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@DefaultQualifier(NonNull.class)
public enum StacktraceDeobfuscator {
    INSTANCE;

    private final Map<Class<?>, Int2ObjectMap<String>> lineMapCache = Collections.synchronizedMap(new LinkedHashMap<>(128, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(final Map.Entry<Class<?>, Int2ObjectMap<String>> eldest) {
            return this.size() > 127;
        }
    });

    public void deobfuscateThrowable(final Throwable throwable) {
        if (!MappingEnvironment.reobf()) {
            return;
        }
        if (GlobalConfiguration.get() != null && !GlobalConfiguration.get().logging.deobfuscateStacktraces) { // handle null as true
            return;
        }

        throwable.setStackTrace(this.deobfuscateStacktrace(throwable.getStackTrace()));
        final Throwable cause = throwable.getCause();
        if (cause != null) {
            this.deobfuscateThrowable(cause);
        }
        for (final Throwable suppressed : throwable.getSuppressed()) {
            this.deobfuscateThrowable(suppressed);
        }
    }

    public StackTraceElement[] deobfuscateStacktrace(final StackTraceElement[] traceElements) {
        if (!MappingEnvironment.reobf()) {
            return traceElements;
        }
        if (GlobalConfiguration.get() != null && !GlobalConfiguration.get().logging.deobfuscateStacktraces) { // handle null as true
            return traceElements;
        }

        final @Nullable Map<String, ObfHelper.ClassMapping> mappings = ObfHelper.INSTANCE.mappingsByObfName();
        if (mappings == null || traceElements.length == 0) {
            return traceElements;
        }
        final StackTraceElement[] result = new StackTraceElement[traceElements.length];
        for (int i = 0; i < traceElements.length; i++) {
            final StackTraceElement element = traceElements[i];

            final String className = element.getClassName();
            final String methodName = element.getMethodName();

            final ObfHelper.ClassMapping classMapping = mappings.get(className);
            if (classMapping == null) {
                result[i] = element;
                continue;
            }

            final Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (final ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            final @Nullable String methodKey = this.determineMethodForLine(clazz, element.getLineNumber());
            final @Nullable String mappedMethodName = methodKey == null ? null : classMapping.methodsByObf().get(methodKey);

            result[i] = new StackTraceElement(
                element.getClassLoaderName(),
                element.getModuleName(),
                element.getModuleVersion(),
                classMapping.mojangName(),
                mappedMethodName != null ? mappedMethodName : methodName,
                sourceFileName(classMapping.mojangName()),
                element.getLineNumber()
            );
        }
        return result;
    }

    private @Nullable String determineMethodForLine(final Class<?> clazz, final int lineNumber) {
        return this.lineMapCache.computeIfAbsent(clazz, StacktraceDeobfuscator::buildLineMap).get(lineNumber);
    }

    private static String sourceFileName(final String fullClassName) {
        final int dot = fullClassName.lastIndexOf('.');
        final String className = dot == -1
            ? fullClassName
            : fullClassName.substring(dot + 1);
        final String rootClassName = className.split("\\$")[0];
        return rootClassName + ".java";
    }

    private static Int2ObjectMap<String> buildLineMap(final Class<?> key) {
        final StringPool pool = new StringPool();
        final Int2ObjectMap<String> lineMap = new Int2ObjectOpenHashMap<>();
        final class LineCollectingMethodVisitor extends MethodVisitor {
            private final String name;
            private final String descriptor;

            LineCollectingMethodVisitor(final String name, final String descriptor) {
                super(Opcodes.ASM9);
                this.name = name;
                this.descriptor = descriptor;
            }

            @Override
            public void visitLineNumber(final int line, final Label start) {
                lineMap.put(line, pool.string(ObfHelper.methodKey(this.name, this.descriptor)));
            }
        }
        final ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
                return new LineCollectingMethodVisitor(name, descriptor);
            }
        };
        try {
            final @Nullable InputStream inputStream = StacktraceDeobfuscator.class.getClassLoader()
                .getResourceAsStream(key.getName().replace('.', '/') + ".class");
            if (inputStream == null) {
                throw new IllegalStateException("Could not find class file: " + key.getName());
            }
            final byte[] classData;
            try (inputStream) {
                classData = inputStream.readAllBytes();
            }
            final ClassReader reader = new ClassReader(classData);
            reader.accept(classVisitor, 0);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
        return lineMap;
    }
}
