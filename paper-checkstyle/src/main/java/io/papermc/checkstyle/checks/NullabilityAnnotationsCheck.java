package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;
import io.papermc.checkstyle.Util;
import java.nio.file.Path;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Checks that nullability annotations are present where required.
 */
public final class NullabilityAnnotationsCheck extends AbstractCheck {

    private static final Set<String> NULLABILITY_ANNOTATIONS = Set.of("Nullable", "NonNull");

    @Override
    public int[] getDefaultTokens() {
        return this.getRequiredTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return this.getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[]{
            TokenTypes.METHOD_DEF,
            TokenTypes.PARAMETER_DEF,
            TokenTypes.ANNOTATION_FIELD_DEF,
            TokenTypes.RECORD_COMPONENT_DEF, // annotations are in ANNOTATIONS token block
        };
    }

    private static boolean hasNoNullabilityAnnotationChildren(final @Nullable DetailAST ast) {
        if (ast == null) {
            return true;
        }
        for (final DetailAST annotation : Util.childrenIterator(ast, TokenTypes.ANNOTATION)) {
            if (annotation.getChildCount(TokenTypes.IDENT) != 1) {
                // skip `.` annotations like ApiStatus.Internal as these aren't nullability annotations
                continue;
            }
            final String ident = annotation.findFirstToken(TokenTypes.IDENT).getText();
            if (NULLABILITY_ANNOTATIONS.contains(ident)) {
                return false;
            }
        }
        return true;
    }

    private void visitMethodDefOrParamDef(final DetailAST holderDef, final int baseAnnotationHolderType) {
        final DetailAST type = holderDef.findFirstToken(TokenTypes.TYPE);
        final DetailAST arrayTypeStart = type.findFirstToken(TokenTypes.ARRAY_DECLARATOR);
        if (arrayTypeStart != null) {
            final DetailAST arrayAnnotations = type.findFirstToken(TokenTypes.ANNOTATIONS);
            if (hasNoNullabilityAnnotationChildren(arrayAnnotations)) {
                this.log(arrayTypeStart.getLineNo(), arrayTypeStart.getColumnNo() - 1, "Array is missing nullability annotation");
            }
        }
        if (TokenUtil.findFirstTokenByPredicate(type, t -> Util.PRIMITIVE_TYPES.contains(t.getType())).isPresent()) {
            // skip primitive types
            return;
        }
        final DetailAST dot = type.findFirstToken(TokenTypes.DOT);
        final DetailAST annotationHolder;
        final DetailAST identLoc;
        if (dot != null) {
            annotationHolder = dot.findFirstToken(TokenTypes.ANNOTATIONS);
            identLoc = Util.getLastChild(dot, TokenTypes.IDENT);
        } else {
            annotationHolder = holderDef.findFirstToken(baseAnnotationHolderType);
            identLoc = type;
        }
        if (hasNoNullabilityAnnotationChildren(annotationHolder)) {
            this.log(identLoc.getLineNo(), identLoc.getColumnNo(), "Missing nullability annotation for '" + holderDef.findFirstToken(TokenTypes.IDENT).getText() + "'");
        }
    }

    public static boolean isNullMarkedAnnotation(final DetailAST annotation) {
        if (annotation.getChildCount(TokenTypes.IDENT) != 1) {
            return false;
        }
        final String ident = annotation.findFirstToken(TokenTypes.IDENT).getText();
        return "NullMarked".equals(ident);
    }

    public static @Nullable DetailAST getNullMarkedAnnotation(final DetailAST typeDeclaration) {
        final DetailAST modifiers = typeDeclaration.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return null;
        }
        for (final DetailAST annotation : Util.childrenIterator(modifiers, TokenTypes.ANNOTATION)) {
            if (isNullMarkedAnnotation(annotation)) {
                return annotation;
            }
        }
        return null;
    }

    @Override
    public void visitToken(final DetailAST ast) {
        if (Util.isPackageInfoAnnotated(Path.of(this.getFilePath()), NullabilityAnnotationsCheck::isNullMarkedAnnotation)) {
            return;
        }
        for (DetailAST parentDef = Util.getEnclosingTypeDeclaration(ast); parentDef != null; parentDef = Util.getEnclosingTypeDeclaration(parentDef)) {
            if (getNullMarkedAnnotation(parentDef) != null) {
                return;
            }
        }
        switch (ast.getType()) {
            case TokenTypes.METHOD_DEF, TokenTypes.PARAMETER_DEF, TokenTypes.ANNOTATION_FIELD_DEF -> this.visitMethodDefOrParamDef(ast, TokenTypes.MODIFIERS);
            case TokenTypes.RECORD_COMPONENT_DEF -> this.visitMethodDefOrParamDef(ast, TokenTypes.ANNOTATIONS);
        }
    }
}
