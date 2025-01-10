package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.JavaParser;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import io.papermc.checkstyle.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Checks that nullability annotations are present where required.
 */
public final class NullabilityAnnotationsCheck extends AbstractCheck {

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

    private static final Set<String> NULLABILITY_ANNOTATIONS = Set.of("Nullable", "NonNull");

    // for checking for annotations on nested types like "OuterType.@Nullable InnerType"
    private static boolean typeHasNullabilityAnnotation(final @Nullable DetailAST type) {
        final DetailAST dot;
        if (type == null || (dot = type.findFirstToken(TokenTypes.DOT)) == null) {
            return false;
        }
        return hasNullabilityAnnotationChild(dot.findFirstToken(TokenTypes.ANNOTATIONS));
    }

    private static boolean hasNullabilityAnnotationChild(final @Nullable DetailAST ast) {
        if (ast == null) {
            return false;
        }
        for (final DetailAST annotation : Util.childrenIterator(ast, TokenTypes.ANNOTATION)) {
            if (annotation.getChildCount(TokenTypes.IDENT) != 1) {
                // skip `.` annotations like ApiStatus.Internal as these aren't nullability annotations
                continue;
            }
            final String ident = annotation.findFirstToken(TokenTypes.IDENT).getText();
            if (NULLABILITY_ANNOTATIONS.contains(ident)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNullabilityAnnotation(final DetailAST inputAst) {
        if (inputAst.getType() == TokenTypes.RECORD_COMPONENT_DEF && hasNullabilityAnnotationChild(inputAst.findFirstToken(TokenTypes.ANNOTATIONS))) {
            return true;
        } else if (inputAst.getType() != TokenTypes.RECORD_COMPONENT_DEF && hasNullabilityAnnotationChild(inputAst.findFirstToken(TokenTypes.MODIFIERS))) {
            return true;
        } else {
            return typeHasNullabilityAnnotation(inputAst.findFirstToken(TokenTypes.TYPE));
        }
    }

    private static boolean isNullMarkedAnnotation(final DetailAST annotation) {
        if (annotation.getChildCount(TokenTypes.IDENT) != 1) {
            return false;
        }
        final String ident = annotation.findFirstToken(TokenTypes.IDENT).getText();
        return "NullMarked".equals(ident);
    }

    private static boolean isTypeDeclarationNullMarked(final DetailAST typeDeclaration) {
        final DetailAST modifiers = typeDeclaration.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return false;
        }
        for (final DetailAST annotation : Util.childrenIterator(modifiers, TokenTypes.ANNOTATION)) {
            if (isNullMarkedAnnotation(annotation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPackageInfoNullMarked() {
        final Path currentPath = Path.of(this.getFilePath());
        final Path packageInfo = currentPath.getParent().resolve("package-info.java");
        if (Files.notExists(packageInfo)) {
            return false;
        }
        final DetailAST packageInfoAst;
        try {
            packageInfoAst = JavaParser.parseFile(packageInfo.toFile(), JavaParser.Options.WITHOUT_COMMENTS);
        } catch (final IOException | CheckstyleException e) {
            throw new RuntimeException(e);
        }
        final DetailAST firstToken = packageInfoAst.findFirstToken(TokenTypes.PACKAGE_DEF);
        if (firstToken == null) {
            return false;
        }
        final DetailAST annotations = firstToken.findFirstToken(TokenTypes.ANNOTATIONS);
        for (final DetailAST annotation : Util.childrenIterator(annotations, TokenTypes.ANNOTATION)) {
            if (isNullMarkedAnnotation(annotation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visitToken(final DetailAST ast) {
        if (hasNullabilityAnnotation(ast)) {
            return;
        }
        for (DetailAST parentDef = Util.getEnclosingTypeDeclaration(ast); parentDef != null; parentDef = Util.getEnclosingTypeDeclaration(parentDef)) {
            if (isTypeDeclarationNullMarked(parentDef)) {
                return;
            }
        }
        if (this.isPackageInfoNullMarked()) {
            return;
        }
        this.recordViolation(ast);
    }

    private static DetailAST getTypeAstForViolation(final DetailAST inputAst) {
        final DetailAST typeToken = inputAst.findFirstToken(TokenTypes.TYPE);
        final DetailAST dot;
        if (typeToken != null && (dot = typeToken.findFirstToken(TokenTypes.DOT)) != null) {
            return Objects.requireNonNullElse(dot.getLastChild(), inputAst);
        }
        return Objects.requireNonNullElse(typeToken, inputAst);
    }

    private void recordViolation(final DetailAST ast) {
        final DetailAST typeAst = getTypeAstForViolation(ast);
        this.log(typeAst.getLineNo(), typeAst.getColumnNo() - 1, "Missing nullability annotation");
    }
}
