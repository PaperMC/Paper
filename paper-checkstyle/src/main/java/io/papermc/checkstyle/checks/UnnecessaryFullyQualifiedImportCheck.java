package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;
import io.papermc.checkstyle.Util;
import io.papermc.checkstyle.checks.util.ImportAwareAbstractCheck;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.jspecify.annotations.Nullable;

/**
 * Reports fully qualified type references that could be imported instead.
 * <p>
 * A reference is only reported if its simple name doesn't already refer to another type in the file,
 * either through an import, a type declared in the file, or an unqualified use of that name.
 * <p>
 * Requires that the codebase be set up to follow standard naming conventions.
 * <ul>
 *     <li>packages are always all lowercase</li>
 *     <li>class names start with a capital letter</li>
 * </ul>
 */
public class UnnecessaryFullyQualifiedImportCheck extends ImportAwareAbstractCheck {

    private static final String JAVA_LANG = "java.lang";

    private static final int[] TYPE_CONTEXTS = {
        TokenTypes.TYPE,
        TokenTypes.TYPE_ARGUMENT,
        TokenTypes.TYPE_PARAMETER,
        TokenTypes.TYPE_UPPER_BOUNDS,
        TokenTypes.TYPE_LOWER_BOUNDS,
        TokenTypes.EXTENDS_CLAUSE,
        TokenTypes.PERMITS_CLAUSE,
        TokenTypes.IMPLEMENTS_CLAUSE,
        TokenTypes.LITERAL_THROWS,
        TokenTypes.LITERAL_NEW,
        TokenTypes.ANNOTATION,
    };

    private final ThreadLocal<@Nullable FileScope> fileScope = new ThreadLocal<>();

    /**
     * Type names visible in the file being checked, other than imports.
     *
     * @param packageName      the package of the file
     * @param topLevelTypes    simple names of top level types declared in the file
     * @param nestedTypes      simple names of nested and local types declared in the file
     * @param unqualifiedTypes simple names of types referenced without qualification
     */
    private record FileScope(String packageName, Set<String> topLevelTypes, Set<String> nestedTypes, Set<String> unqualifiedTypes) {
    }

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
        return new int[] {
            TokenTypes.TYPE,
            TokenTypes.TYPE_ARGUMENT,
            TokenTypes.TYPE_PARAMETER,
            TokenTypes.TYPE_UPPER_BOUNDS,
            TokenTypes.TYPE_LOWER_BOUNDS,
            TokenTypes.EXTENDS_CLAUSE,
            TokenTypes.PERMITS_CLAUSE,
            TokenTypes.IMPLEMENTS_CLAUSE,
            TokenTypes.LITERAL_THROWS,
            TokenTypes.LITERAL_NEW,
            TokenTypes.ANNOTATION,
            TokenTypes.METHOD_CALL,
            TokenTypes.METHOD_REF,
            TokenTypes.LITERAL_CLASS,
            TokenTypes.DOT,
        };
    }

    @Override
    public void beginTree(final DetailAST rootAST) {
        super.beginTree(rootAST);
        String packageName = "";
        final Set<String> topLevelTypes = new HashSet<>();
        final Set<String> nestedTypes = new HashSet<>();
        final Set<String> unqualifiedTypes = new HashSet<>();
        final Deque<DetailAST> nodes = new ArrayDeque<>();
        for (DetailAST node = rootAST; node != null; node = node.getNextSibling()) {
            nodes.push(node);
        }
        while (!nodes.isEmpty()) {
            final DetailAST node = nodes.pop();
            for (DetailAST child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                nodes.push(child);
            }
            if (node.getType() == TokenTypes.PACKAGE_DEF) {
                packageName = FullIdent.createFullIdent(node.getLastChild().getPreviousSibling()).getText();
            } else if (node.getType() == TokenTypes.IDENT) {
                final DetailAST parent = node.getParent();
                if (TokenUtil.isTypeDeclaration(parent.getType())) {
                    final DetailAST declarationParent = parent.getParent();
                    if (declarationParent == null || declarationParent.getType() == TokenTypes.COMPILATION_UNIT) {
                        topLevelTypes.add(node.getText());
                    } else {
                        nestedTypes.add(node.getText());
                    }
                } else if (isUnqualifiedTypeReference(node, parent)) {
                    unqualifiedTypes.add(node.getText());
                }
            }
        }
        this.fileScope.set(new FileScope(packageName, topLevelTypes, nestedTypes, unqualifiedTypes));
    }

    @Override
    public void finishTree(final DetailAST rootAST) {
        super.finishTree(rootAST);
        this.fileScope.remove();
    }

    private static boolean isUnqualifiedTypeReference(final DetailAST ident, final DetailAST parent) {
        if (TokenUtil.isOfType(parent, TYPE_CONTEXTS)) {
            return true;
        }
        // first part of a dotted name or method reference, like Type.member or Type::member
        return Character.isUpperCase(ident.getText().charAt(0))
            && TokenUtil.isOfType(parent, TokenTypes.DOT, TokenTypes.METHOD_REF)
            && parent.getFirstChild() == ident;
    }

    @Override
    public void visitToken(final DetailAST ast) {
        switch (ast.getType()) {
            case TokenTypes.METHOD_CALL -> {
                final DetailAST callee = ast.findFirstToken(TokenTypes.DOT);
                if (callee != null) {
                    this.checkExpressionChain(callee, true);
                }
            }
            case TokenTypes.METHOD_REF -> this.checkExpressionChain(ast.getFirstChild(), false);
            case TokenTypes.DOT -> {
                if (isFieldAccess(ast)) {
                    this.checkExpressionChain(ast, true);
                }
            }
            case TokenTypes.LITERAL_CLASS -> {
                final DetailAST dot = ast.getParent();
                if (dot.getType() == TokenTypes.DOT) {
                    this.checkChildren(dot);
                }
            }
            default -> this.checkChildren(ast);
        }
    }

    private void checkChildren(final DetailAST ast) {
        // clauses like extends, implements, throws, and bounds can contain multiple types
        for (final DetailAST dot : Util.childrenIterator(ast, TokenTypes.DOT)) {
            this.checkQualifiedName(dot);
        }
    }

    /**
     * Checks the type qualifier in a dotted expression, like {@code java.util.Locale} in {@code java.util.Locale.ROOT}.
     *
     * @param chain        the outermost node of the dotted expression
     * @param lastIsMember whether the last part of the chain is a member (field or method) and not part of the type
     */
    private void checkExpressionChain(final DetailAST chain, final boolean lastIsMember) {
        final List<DetailAST> dots = new ArrayList<>();
        DetailAST node = chain;
        while (node.getType() == TokenTypes.DOT) {
            dots.addFirst(node);
            node = node.getFirstChild();
        }
        if (node.getType() != TokenTypes.IDENT) {
            // starts with this, super, a method call, etc.
            return;
        }
        final List<String> parts = new ArrayList<>();
        parts.add(node.getText());
        for (final DetailAST dot : dots) {
            final DetailAST name = Util.getNextSibling(dot.getFirstChild(), TokenTypes.IDENT);
            if (name == null) {
                // .class, .this, .new, etc.
                return;
            }
            parts.add(name.getText());
        }
        int packageEnd = 0;
        while (packageEnd < parts.size() && !Character.isUpperCase(parts.get(packageEnd).charAt(0))) {
            packageEnd++;
        }
        if (packageEnd == 0 || packageEnd == parts.size()) {
            // not qualified by a package, or no type at all
            return;
        }
        int typeEnd = packageEnd;
        while (typeEnd + 1 < parts.size() && Character.isUpperCase(parts.get(typeEnd + 1).charAt(0))) {
            typeEnd++;
        }
        if (lastIsMember && typeEnd == parts.size() - 1) {
            typeEnd--;
        }
        if (typeEnd < packageEnd) {
            // only a member, like local.inner.FIELD
            return;
        }
        this.checkQualifiedName(dots.get(typeEnd - 1));
    }

    private static boolean isFieldAccess(final DetailAST dot) {
        final DetailAST parent = dot.getParent();
        if (TokenUtil.isOfType(parent, TokenTypes.DOT, TokenTypes.METHOD_CALL, TokenTypes.METHOD_REF) || TokenUtil.isOfType(parent, TYPE_CONTEXTS)) {
            return false;
        }
        for (DetailAST ancestor = parent; ancestor != null; ancestor = ancestor.getParent()) {
            if (ancestor.getType() == TokenTypes.EXPR) {
                return true;
            }
            if (TokenUtil.isOfType(ancestor, TYPE_CONTEXTS)) {
                return false;
            }
        }
        return false;
    }

    private void checkQualifiedName(final DetailAST dot) {
        final Util.QualifiedName fullIdent = Util.extractFullIdent(dot);
        final DetailAST startNode = fullIdent.startNode();
        final String fullIdentName = fullIdent.name();
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
                this.log(startNode.getLineNo(), startNode.getColumnNo(), "Fully qualified import used: " + withoutConstants(fullIdentName));
                return;
            }
            previousTypeName = typeName;
            typeName = typeName.substring(0, lastDotIdx);
        } while ((lastDotIdx = typeName.lastIndexOf('.')) != -1 && Character.isUpperCase(typeName.substring(0, lastDotIdx + 2).charAt(lastDotIdx + 1)));

        final String outerSimpleName = previousTypeName.substring(previousTypeName.lastIndexOf('.') + 1);
        final String outerPackage = previousTypeName.substring(0, previousTypeName.lastIndexOf('.'));
        if (this.getFullImportBySimple(outerSimpleName) == null && !this.isNameTaken(outerSimpleName, outerPackage)) {
            this.log(startNode.getLineNo(), startNode.getColumnNo(), "Unnecessary fully qualified import (no conflict): " + withoutConstants(fullIdentName));
        }
    }

    /**
     * Checks if the simple name already refers to a different type in this file.
     *
     * @param simpleName  the simple name of the outermost type
     * @param packageName the package of the outermost type
     * @return true if the type can't be referenced by its simple name
     */
    private boolean isNameTaken(final String simpleName, final String packageName) {
        final FileScope scope = this.fileScope.get();
        if (scope == null) {
            return false;
        }
        final boolean samePackage = packageName.equals(scope.packageName());
        if (scope.nestedTypes().contains(simpleName)) {
            // a nested type shadows any other type with the same name
            return true;
        }
        if (scope.topLevelTypes().contains(simpleName)) {
            // a top level type with the same name is only the same type if it's in the same package
            return !samePackage;
        }
        if (scope.unqualifiedTypes().contains(simpleName)) {
            // unqualified names resolve to the same package or java.lang (imports are already handled)
            return !samePackage && !JAVA_LANG.equals(packageName);
        }
        return false;
    }

    /**
     * Removes trailing constants from a dotted name, like {@code ROOT} in {@code java.util.Locale.ROOT}.
     *
     * @param name the dotted name
     * @return the name without trailing constants
     */
    private static String withoutConstants(final String name) {
        String result = name;
        int lastDot;
        while ((lastDot = result.lastIndexOf('.')) != -1 && isConstantName(result.substring(lastDot + 1))) {
            final String qualifier = result.substring(0, lastDot);
            if (!Character.isUpperCase(qualifier.charAt(qualifier.lastIndexOf('.') + 1))) {
                // the constant-like part is the outermost type, like java.util.UUID
                break;
            }
            result = qualifier;
        }
        return result;
    }

    private static boolean isConstantName(final String name) {
        return name.length() > 1 && name.equals(name.toUpperCase(Locale.ROOT)) && name.chars().anyMatch(Character::isLetter);
    }
}
