package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.support.test.ClassNodeTest;
import org.junit.jupiter.api.Disabled;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.RecordComponentNode;

@Disabled
public class DeprecatedTest {

    private static final String DEPRECATED_DESC = Type.getDescriptor(Deprecated.class);

    @ClassNodeTest
    public void testIfSinceIsPresent(ClassNode classNode) {
        List<String> missingReason = new ArrayList<>();

        // Check class annotation
        checkAnnotation(missingReason, classNode.invisibleAnnotations, "Class");
        checkAnnotation(missingReason, classNode.visibleAnnotations, "Class");
        checkAnnotation(missingReason, classNode.invisibleTypeAnnotations, "Class");
        checkAnnotation(missingReason, classNode.visibleTypeAnnotations, "Class");

        if (classNode.recordComponents != null) {
            for (RecordComponentNode recordComponentNode : classNode.recordComponents) {
                checkAnnotation(missingReason, recordComponentNode.invisibleAnnotations, "RecordComponent '%s'".formatted(recordComponentNode.name));
                checkAnnotation(missingReason, recordComponentNode.visibleAnnotations, "RecordComponent '%s'".formatted(recordComponentNode.name));
                checkAnnotation(missingReason, recordComponentNode.invisibleTypeAnnotations, "RecordComponent '%s'".formatted(recordComponentNode.name));
                checkAnnotation(missingReason, recordComponentNode.visibleTypeAnnotations, "RecordComponent '%s'".formatted(recordComponentNode.name));
            }
        }

        if (classNode.fields != null) {
            for (FieldNode fieldNode : classNode.fields) {
                checkAnnotation(missingReason, fieldNode.invisibleAnnotations, "Field '%s'".formatted(fieldNode.name));
                checkAnnotation(missingReason, fieldNode.visibleAnnotations, "Field '%s'".formatted(fieldNode.name));
                checkAnnotation(missingReason, fieldNode.invisibleTypeAnnotations, "Field '%s'".formatted(fieldNode.name));
                checkAnnotation(missingReason, fieldNode.visibleTypeAnnotations, "Field '%s'".formatted(fieldNode.name));
            }
        }

        if (classNode.methods != null) {
            for (MethodNode methodNode : classNode.methods) {
                checkAnnotation(missingReason, methodNode.invisibleAnnotations, "Method '%s'".formatted(methodNode.name));
                checkAnnotation(missingReason, methodNode.visibleAnnotations, "Method '%s'".formatted(methodNode.name));
                checkAnnotation(missingReason, methodNode.invisibleTypeAnnotations, "Method '%s'".formatted(methodNode.name));
                checkAnnotation(missingReason, methodNode.visibleTypeAnnotations, "Method '%s'".formatted(methodNode.name));

                if (methodNode.visibleParameterAnnotations != null) {
                    for (int i = 0; i < methodNode.visibleParameterAnnotations.length; i++) {
                        checkAnnotation(missingReason, methodNode.visibleParameterAnnotations[i], "Method Parameter '%d' for Method '%s'".formatted(i, methodNode.name));
                    }
                }

                if (methodNode.invisibleParameterAnnotations != null) {
                    for (int i = 0; i < methodNode.invisibleParameterAnnotations.length; i++) {
                        checkAnnotation(missingReason, methodNode.invisibleParameterAnnotations[i], "Method Parameter '%d' for Method '%s'".formatted(i, methodNode.name));
                    }
                }

                checkAnnotation(missingReason, methodNode.visibleLocalVariableAnnotations, "Local variable in Method '%s'".formatted(methodNode.name));
                checkAnnotation(missingReason, methodNode.invisibleLocalVariableAnnotations, "Local variable in Method '%s'".formatted(methodNode.name));
            }
        }

        assertTrue(missingReason.isEmpty(), """
                Missing or wrongly formatted (only format 'number.number[.number]' is supported) 'since' value in 'Deprecated' annotation found.
                In Class '%s'.

                Following places where found:
                %s""".formatted(classNode.name, Joiner.on('\n').join(missingReason)));
    }

    private void checkAnnotation(List<String> missingReason, List<? extends AnnotationNode> annotationNodes, String where) {
        if (annotationNodes == null || annotationNodes.isEmpty()) {
            return;
        }

        for (AnnotationNode annotationNode : annotationNodes) {
            if (!annotationNode.desc.equals(DEPRECATED_DESC)) {
                continue;
            }

            if (!hasSince(annotationNode)) {
                missingReason.add(where);
            }
        }
    }

    private boolean hasSince(AnnotationNode annotationNode) {
        if (annotationNode.values == null || annotationNode.values.isEmpty()) {
            return false;
        }

        for (int i = 0; i < annotationNode.values.size(); i = i + 2) {
            if ("since".equals(annotationNode.values.get(i))) {
                String other = (String) annotationNode.values.get(i + 1);

                if (other == null || other.isEmpty()) {
                    return false;
                }

                if (!isValidVersion(other)) {
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    private boolean isValidVersion(String version) {
        String[] versionParts = version.split("\\.");

        if (versionParts.length != 2 && versionParts.length != 3) {
            return false;
        }

        if (!isNumber(versionParts[0])) {
            return false;
        }
        if (!isNumber(versionParts[1])) {
            return false;
        }
        if (versionParts.length == 3 && !isNumber(versionParts[2])) {
            return false;
        }

        return true;
    }

    private boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
