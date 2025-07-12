package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailNode;
import com.puppycrawl.tools.checkstyle.api.JavadocTokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.AbstractJavadocCheck;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtil;
import io.papermc.checkstyle.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * Checks that parameter descriptions in Javadoc are aligned.
 */
public final class JavadocAlignParameterDescriptionCheck extends AbstractJavadocCheck {

    @Override
    public int[] getDefaultJavadocTokens() {
        return new int[]{JavadocTokenTypes.JAVADOC};
    }

    @Override
    public void visitJavadocToken(final DetailNode detailNode) {
        final List<DetailNode> params = new ArrayList<>();
        int maxColumn = -1;
        for (final DetailNode child : detailNode.getChildren()) {
            final DetailNode paramLiteralNode = JavadocUtil.findFirstToken(child, JavadocTokenTypes.PARAM_LITERAL);
            if (child.getType() != JavadocTokenTypes.JAVADOC_TAG || paramLiteralNode == null) {
                continue;
            }
            final DetailNode paramDescription = JavadocUtil.getNextSibling(paramLiteralNode, JavadocTokenTypes.DESCRIPTION);
            maxColumn = Math.max(maxColumn, paramDescription.getColumnNumber());
            params.add(paramDescription);
        }

        for (final DetailNode param : params) {
            if (param.getColumnNumber() != maxColumn) {
                final DetailNode paramNameNode = Util.getPreviousSibling(param, JavadocTokenTypes.PARAMETER_NAME);
                if (paramNameNode == null) {
                    continue;
                }
                this.log(
                    param.getLineNumber(),
                    param.getColumnNumber() - 1,
                    "Param description for %s should start at column %d".formatted(paramNameNode.getText(), maxColumn)
                );
            }
        }
    }
}
