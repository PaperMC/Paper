package io.papermc.paper.pluginremap.reflect;

import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.RewriteRuleVisitorFactory;
import io.papermc.paper.util.MappingEnvironment;
import io.papermc.reflectionrewriter.BaseReflectionRules;
import io.papermc.reflectionrewriter.DefineClassRule;
import io.papermc.reflectionrewriter.proxygenerator.ProxyGenerator;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

@DefaultQualifier(NonNull.class)
public final class ReflectionRemapper {
    private static final String PAPER_REFLECTION_HOLDER = "io.papermc.paper.pluginremap.reflect.PaperReflectionHolder";
    private static final String PAPER_REFLECTION_HOLDER_DESC = PAPER_REFLECTION_HOLDER.replace('.', '/');
    private static final RewriteRuleVisitorFactory VISITOR_FACTORY = RewriteRuleVisitorFactory.create(
        Opcodes.ASM9,
        chain -> chain.then(new BaseReflectionRules(PAPER_REFLECTION_HOLDER).rules())
            .then(DefineClassRule.create(PAPER_REFLECTION_HOLDER_DESC, true)),
        ClassInfoProvider.basic()
    );

    static {
        if (!MappingEnvironment.reobf()) {
            setupProxy();
        }
    }

    private ReflectionRemapper() {
    }

    public static ClassVisitor visitor(final ClassVisitor parent) {
        if (MappingEnvironment.reobf() || MappingEnvironment.DISABLE_PLUGIN_REMAPPING) {
            return parent;
        }
        return VISITOR_FACTORY.createVisitor(parent);
    }

    public static byte[] processClass(final byte[] bytes) {
        if (MappingEnvironment.DISABLE_PLUGIN_REMAPPING) {
            return bytes;
        }
        final ClassReader classReader = new ClassReader(bytes);
        final ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(ReflectionRemapper.visitor(classWriter), 0);
        return classWriter.toByteArray();
    }

    private static void setupProxy() {
        try {
            final byte[] bytes = ProxyGenerator.generateProxy(PaperReflection.class, PAPER_REFLECTION_HOLDER_DESC);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final Class<?> generated = lookup.defineClass(bytes);
            final Method init = generated.getDeclaredMethod("init", PaperReflection.class);
            init.invoke(null, new PaperReflection());
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
