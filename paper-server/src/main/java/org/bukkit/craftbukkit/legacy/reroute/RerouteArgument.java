package org.bukkit.craftbukkit.legacy.reroute;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public record RerouteArgument(Type type, Type sourceType, boolean injectPluginName, boolean injectPluginVersion, @Nullable String injectCompatibility) {

    /**
     * Converts the type string to the correct load opcode.
     * <br>
     * References:
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.3.2-200">Interpretation of field descriptors</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.iload">iload Opcode</a> /
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.iload_n">{@literal iload_<n> Opcode}</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.lload">lload Opcode</a> /
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.lload_n">{@literal lload_<n> Opcode}</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.fload">fload Opcode</a> /
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.fload_n">{@literal fload_<n> Opcode}</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.dload">dload Opcode</a> /
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.dload_n">{@literal dload_<n> Opcode}</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.aload">aload Opcode</a> /
     * <a href="https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-6.html#jvms-6.5.aload_n">{@literal aload_<n> Opcode}</a>
     *
     * @return the opcode of the type
     */
    public int instruction() {
        if (this.injectPluginName() || this.injectPluginVersion() || this.injectCompatibility() != null) {
            throw new IllegalStateException(String.format("Cannot get instruction for plugin name / version argument / compatibility: %s", this));
        }

        return this.type.getOpcode(Opcodes.ILOAD);
    }
}
