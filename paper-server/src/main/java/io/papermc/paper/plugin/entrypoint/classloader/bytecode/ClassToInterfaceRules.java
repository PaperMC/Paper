package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.RewriteRuleVisitorFactory;
import io.papermc.asm.rules.classes.EnumToInterfaceRule;
import java.lang.constant.ClassDesc;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.potion.PotionType;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public final class ClassToInterfaceRules {

    private static final RewriteRuleVisitorFactory VISITOR_FACTORY = RewriteRuleVisitorFactory.create(
        Opcodes.ASM9,
        chain -> {
            Map<ClassDesc, ClassDesc> enums = enums().entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().describeConstable().orElseThrow(),
                entry -> entry.getValue().describeConstable().orElseThrow()
            ));
            chain.then(new EnumToInterfaceRule(enums));
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
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classReader.accept(visitor(classWriter), 0);
        return classWriter.toByteArray();
    }

    private static Map<Class<?>, Class<?>> enums() {
        return Map.of(
            PotionType.class, CraftPotionType.class
        );
    }
}
