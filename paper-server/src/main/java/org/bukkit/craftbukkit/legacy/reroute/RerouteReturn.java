package org.bukkit.craftbukkit.legacy.reroute;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public record RerouteReturn(Type type) {

    /**
     * Converts the type string to the correct return opcode.
     * <br>
     * References:
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.3.2-200">Interpretation of field descriptors</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.return">return Opcode</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.ireturn">ireturn Opcode</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.lreturn">lreturn Opcode</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.freturn">freturn Opcode</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.dreturn">dreturn Opcode</a>
     * <br>
     * <a href="https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-6.html#jvms-6.5.areturn">areturn Opcode</a>
     *
     * @return the opcode of the type
     */
    public int instruction() {
        return type.getOpcode(Opcodes.IRETURN);
    }
}
