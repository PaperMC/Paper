package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.asm.AbstractRewriteRuleVisitorFactory;
import io.papermc.asm.ClassInfoProvider;
import io.papermc.asm.rules.builder.RuleFactoryConfiguration;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.classloader.ClassloaderBytecodeModifier;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static io.papermc.asm.util.DescriptorUtils.desc;

public abstract class VersionedClassloaderBytecodeModifier extends AbstractRewriteRuleVisitorFactory implements ClassloaderBytecodeModifier, RuleFactoryConfiguration.Holder {

    protected VersionedClassloaderBytecodeModifier(final int api) {
        super(api, ClassInfoProvider.basic());
    }

    @Override
    public final byte[] modify(final PluginMeta config, final byte[] bytecode) {
        final ClassReader cr = new ClassReader(bytecode);
        final ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES); // need to compute frames because of instruction removal in ctor rewriting

        cr.accept(this.createVisitor(cw), 0);
        return cw.toByteArray();
    }

    @Override
    public final RuleFactoryConfiguration configuration() {
        return RuleFactoryConfiguration.create(desc(this.getClass()));
    }
}
