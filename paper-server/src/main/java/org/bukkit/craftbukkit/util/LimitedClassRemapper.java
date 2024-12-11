package org.bukkit.craftbukkit.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;

public class LimitedClassRemapper extends ClassRemapper {

    public LimitedClassRemapper(ClassVisitor classVisitor, Remapper remapper) {
        super(classVisitor, remapper);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        // We do not want to remap superName and interfaces for the enums
        this.cv.visit(version, access, this.remapper.mapType(name), this.remapper.mapSignature(signature, false), superName, interfaces);
    }

    @Override
    protected MethodVisitor createMethodRemapper(MethodVisitor methodVisitor) {
        return new LimitedMethodRemapper(this.api, methodVisitor, this.remapper);
    }

    private class LimitedMethodRemapper extends MethodRemapper {

        protected LimitedMethodRemapper(int api, MethodVisitor methodVisitor, Remapper remapper) {
            super(api, methodVisitor, remapper);
        }

        @Override
        public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface) {
            if (owner != null && owner.equals("java/lang/Enum") && name != null && name.equals("<init>")) {
                // We also do not want to remap the init method for enums
                this.mv.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
                return;
            }
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
        }
    }
}
