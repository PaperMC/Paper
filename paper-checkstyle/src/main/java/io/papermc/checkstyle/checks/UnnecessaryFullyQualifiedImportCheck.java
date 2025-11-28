package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import io.papermc.checkstyle.Util;
import io.papermc.checkstyle.checks.util.ImportAwareAbstractCheck;
import org.apache.commons.lang3.tuple.Pair;
import org.jspecify.annotations.Nullable;

/**
 * Requires that the codebase be set up to follow standard naming conventions.
 * <ul>
 *     <li>packages are always all lowercase</li>
 *     <li>class names start with a capital letter</li>
 * </ul>
 */
public class UnnecessaryFullyQualifiedImportCheck extends ImportAwareAbstractCheck {

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
            TokenTypes.TYPE,
            TokenTypes.TYPE_ARGUMENT,
            TokenTypes.TYPE_PARAMETER,
            TokenTypes.TYPE_UPPER_BOUNDS,
            TokenTypes.TYPE_LOWER_BOUNDS,
            TokenTypes.EXTENDS_CLAUSE,
            TokenTypes.PERMITS_CLAUSE,
            TokenTypes.IMPLEMENTS_CLAUSE,
            TokenTypes.LITERAL_NEW,
            TokenTypes.ANNOTATION,
            TokenTypes.METHOD_CALL,
            TokenTypes.LITERAL_CLASS,
        };
    }

    private static @Nullable DetailAST fromMethodCall(final DetailAST methodCall) {
        final DetailAST dot = methodCall.findFirstToken(TokenTypes.DOT);
        if (dot == null) {
            return null;
        }
        final DetailAST nextDot = dot.findFirstToken(TokenTypes.DOT);
        if (nextDot == null) {
            return null;
        }
        final DetailAST ident = nextDot.findFirstToken(TokenTypes.IDENT);
        if (ident == null || Character.isLowerCase(ident.getText().charAt(0))) {
            // method call not on a type
            return null;
        }
        return dot;
    }

    private static @Nullable DetailAST fromLiteralClass(final DetailAST literalClass) {
        final DetailAST dot = literalClass.getParent();
        if (dot.getType() != TokenTypes.DOT) {
            return null;
        }
        return dot;
    }

    @Override
    public void visitToken(final DetailAST ast) {
        final DetailAST actualAst = switch (ast.getType()) {
            case TokenTypes.METHOD_CALL -> fromMethodCall(ast);
            case TokenTypes.LITERAL_CLASS -> fromLiteralClass(ast);
            default -> ast;
        };
        if (actualAst == null) {
            return;
        }
        final DetailAST dot = actualAst.findFirstToken(TokenTypes.DOT);
        if (dot == null) {
            return;
        }
        final Pair<String, DetailAST> fullIdent = Util.extractFullIdent(dot);
        final DetailAST startNode = fullIdent.getRight();
        final String fullIdentName = fullIdent.getLeft();
        if (Character.isUpperCase(fullIdentName.charAt(0))) {
            // not a package
            return;
        }
        String previousTypeName;
        String typeName = fullIdentName;
        int lastDotIdx = typeName.lastIndexOf('.');
        do {
            // Check if we have an import for this exact type
            if (this.hasImportFor(typeName)) {
                this.log(startNode.getLineNo(), startNode.getColumnNo(), "Fully qualified import used: " + fullIdentName);
                return;
            }
            previousTypeName = typeName;
            typeName = typeName.substring(0, lastDotIdx);
        } while ((lastDotIdx = typeName.lastIndexOf('.')) != -1 && Character.isUpperCase(typeName.substring(0, lastDotIdx + 2).charAt(lastDotIdx + 1)));

        final String outerSimpleName = previousTypeName.substring(previousTypeName.lastIndexOf('.') + 1);
        final String fullImportBySimple = this.getFullImportBySimple(outerSimpleName);
        if (fullImportBySimple == null) {
            this.log(startNode.getLineNo(), startNode.getColumnNo(), "Unnecessary fully qualified import (no conflict): " + fullIdentName);
        }
    }
}
