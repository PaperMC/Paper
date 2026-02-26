package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.RewriteRuleVisitorFactory;
import io.papermc.asm.rules.classes.ClassToInterfaceRule;
import io.papermc.paper.util.OldEnumHolderable;
import java.util.Map;
import java.util.Set;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.OldEnum;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public final class ClassToInterfaceRules {

    private static final RewriteRuleVisitorFactory VISITOR_FACTORY = RewriteRuleVisitorFactory.create(
        Opcodes.ASM9,
        chain -> {
            for (final Class<?> klass : classes()) {
                chain.then(new ClassToInterfaceRule(klass.describeConstable().orElseThrow(), null));
            }

            for (final Map.Entry<Class<? extends OldEnum<?>>, Class<? extends OldEnumHolderable<?, ?>>> entry : enums()) {
                chain.then(new ClassToInterfaceRule(entry.getKey().describeConstable().orElseThrow(), null));
            }
            // todo later bump asm-utils and move static methods out
            /*
            chain.then(new EnumToInterfaceRule(enums().stream().collect(Collectors.toMap(entry -> {
                return entry.getKey().describeConstable().orElseThrow();
            }, entry -> {
                return entry.getValue().describeConstable().orElseThrow();
            }))));*/
        },
        ClassInfoProvider.basic()
    );

    private ClassToInterfaceRules() {
    }

    public static ClassVisitor visitor(final ClassVisitor parent) {
        return VISITOR_FACTORY.createVisitor(parent);
    }

    public static byte[] processClass(final byte[] bytes) {
        final ClassReader classReader = new ClassReader(bytes);
        final ClassWriter classWriter = new ClassWriter(classReader, 0);
        classReader.accept(visitor(classWriter), 0);
        return classWriter.toByteArray();
    }

    private static Set<Map.Entry<Class<? extends OldEnum<?>>, Class<? extends OldEnumHolderable<?, ?>>>> enums() {
        return Set.of(
            Map.entry(PotionType.class, CraftPotionType.class)
        );
    }

    private static Set<Class<?>> classes() {
        return Set.of();
    }
}
