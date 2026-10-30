package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailNode;
import com.puppycrawl.tools.checkstyle.api.JavadocCommentsTokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.AbstractJavadocCheck;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks that parameter descriptions in Javadoc are aligned.
 */
public final class JavadocAlignParameterDescriptionCheck extends AbstractJavadocCheck {

    static final Pattern SPACE_PREFIX = Pattern.compile("^\\s*");

    @Override
    public int[] getDefaultJavadocTokens() {
        return new int[] {JavadocCommentsTokenTypes.JAVADOC_CONTENT};
    }

    @Override
    public void visitJavadocToken(final DetailNode detailNode) {
        record ParamDesc(DetailNode node, DetailNode paramNameNode, int startCol) {
        }

        final List<ParamDesc> params = new ArrayList<>();
        int maxColumn = -1;
        final Set<Integer> visitedLines = new HashSet<>();
        for (final DetailNode javadocBlock : JavadocUtil.getAllNodesOfType(detailNode, JavadocCommentsTokenTypes.JAVADOC_BLOCK_TAG)) {
            final DetailNode paramBlock = JavadocUtil.findFirstToken(javadocBlock, JavadocCommentsTokenTypes.PARAM_BLOCK_TAG);
            if (paramBlock == null) {
                continue;
            }
            final DetailNode paramNameNode = JavadocUtil.findFirstToken(paramBlock, JavadocCommentsTokenTypes.PARAMETER_NAME);
            if (paramNameNode == null) {
                continue;
            }
            final DetailNode paramDescription = JavadocUtil.findFirstToken(paramBlock, JavadocCommentsTokenTypes.DESCRIPTION);
            if (paramDescription == null) {
                continue;
            }
            // iterate over all text nodes (multiline)
            for (final DetailNode textNode : JavadocUtil.getAllNodesOfType(paramDescription, JavadocCommentsTokenTypes.TEXT)) {
                if (!visitedLines.add(textNode.getLineNumber())) {
                    continue;
                }
                final Matcher matcher = SPACE_PREFIX.matcher(textNode.getText());
                int paramDescColNum = textNode.getColumnNumber();
                if (matcher.find()) {
                    paramDescColNum += matcher.group().length();
                }
                maxColumn = Math.max(maxColumn, paramDescColNum);
                params.add(new ParamDesc(textNode, paramNameNode, paramDescColNum));
            }
        }

        for (final ParamDesc param : params) {
            if (param.startCol() != maxColumn) {
                this.log(
                    param.node().getLineNumber(),
                    param.startCol(),
                    "Param description for %s should start at column %d".formatted(param.paramNameNode().getText(), maxColumn + 1)
                );
            }
        }
    }
}
