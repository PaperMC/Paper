package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.DetailNode;
import com.puppycrawl.tools.checkstyle.api.JavadocCommentsTokenTypes;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.AbstractJavadocCheck;
import com.puppycrawl.tools.checkstyle.utils.BlockCommentPosition;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtil;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;
import io.papermc.checkstyle.Util;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * Checks that {@code @param} tags are in the same order as the parameters they document,
 * with type parameters after regular parameters (the order IntelliJ generates them in).
 * Only tags that move backward in the expected order are reported, unknown names are ignored.
 */
public final class JavadocParamTagOrderCheck extends AbstractJavadocCheck {

    private static final int[] DECLARATIONS = {
        TokenTypes.METHOD_DEF,
        TokenTypes.CTOR_DEF,
        TokenTypes.COMPACT_CTOR_DEF,
        TokenTypes.CLASS_DEF,
        TokenTypes.INTERFACE_DEF,
        TokenTypes.RECORD_DEF,
    };

    @Override
    public int[] getDefaultJavadocTokens() {
        return new int[] {JavadocCommentsTokenTypes.JAVADOC_CONTENT};
    }

    @Override
    public void visitJavadocToken(final DetailNode detailNode) {
        final DetailAST declaration = documentedDeclaration(this.getBlockCommentAst());
        if (declaration == null) {
            return;
        }
        final List<String> expectedOrder = expectedParamOrder(declaration);
        int furthestIndex = -1;
        for (final DetailNode javadocBlock : JavadocUtil.getAllNodesOfType(detailNode, JavadocCommentsTokenTypes.JAVADOC_BLOCK_TAG)) {
            final DetailNode paramBlock = JavadocUtil.findFirstToken(javadocBlock, JavadocCommentsTokenTypes.PARAM_BLOCK_TAG);
            if (paramBlock == null) {
                continue;
            }
            final DetailNode paramName = JavadocUtil.findFirstToken(paramBlock, JavadocCommentsTokenTypes.PARAMETER_NAME);
            if (paramName == null) {
                continue;
            }
            final int index = expectedOrder.indexOf(paramName.getText());
            if (index == -1) {
                continue;
            }
            if (index < furthestIndex) {
                this.log(
                    paramName.getLineNumber(),
                    paramName.getColumnNumber(),
                    "@param tag for %s should be in declaration order, with type parameters last".formatted(paramName.getText())
                );
            } else {
                furthestIndex = index;
            }
        }
    }

    private static @Nullable DetailAST documentedDeclaration(final DetailAST blockComment) {
        if (!BlockCommentPosition.isOnMethod(blockComment)
            && !BlockCommentPosition.isOnConstructor(blockComment)
            && !BlockCommentPosition.isOnCompactConstructor(blockComment)
            && !BlockCommentPosition.isOnClass(blockComment)
            && !BlockCommentPosition.isOnInterface(blockComment)
            && !BlockCommentPosition.isOnRecord(blockComment)) {
            return null;
        }
        DetailAST parent = blockComment.getParent();
        while (parent != null && !TokenUtil.isOfType(parent, DECLARATIONS)) {
            parent = parent.getParent();
        }
        return parent;
    }

    private static List<String> expectedParamOrder(final DetailAST declaration) {
        final List<String> names = new ArrayList<>();
        if (declaration.getType() == TokenTypes.COMPACT_CTOR_DEF) {
            // compact constructors document the components of the enclosing record
            addRecordComponentNames(names, declaration.getParent().getParent());
            return names;
        }
        final DetailAST parameters = declaration.findFirstToken(TokenTypes.PARAMETERS);
        if (parameters != null) {
            for (final DetailAST parameter : Util.childrenIterator(parameters, TokenTypes.PARAMETER_DEF)) {
                names.add(identText(parameter));
            }
        }
        addRecordComponentNames(names, declaration);
        final DetailAST typeParameters = declaration.findFirstToken(TokenTypes.TYPE_PARAMETERS);
        if (typeParameters != null) {
            for (final DetailAST typeParameter : Util.childrenIterator(typeParameters, TokenTypes.TYPE_PARAMETER)) {
                names.add("<" + identText(typeParameter) + ">");
            }
        }
        return names;
    }

    private static void addRecordComponentNames(final List<String> names, final DetailAST declaration) {
        final DetailAST recordComponents = declaration.findFirstToken(TokenTypes.RECORD_COMPONENTS);
        if (recordComponents == null) {
            return;
        }
        for (final DetailAST component : Util.childrenIterator(recordComponents, TokenTypes.RECORD_COMPONENT_DEF)) {
            names.add(identText(component));
        }
    }

    private static String identText(final DetailAST ast) {
        final DetailAST ident = ast.findFirstToken(TokenTypes.IDENT);
        return ident == null ? "" : ident.getText();
    }
}
