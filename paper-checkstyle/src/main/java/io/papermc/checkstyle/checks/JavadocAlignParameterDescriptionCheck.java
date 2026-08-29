package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailNode;
import com.puppycrawl.tools.checkstyle.api.JavadocCommentsTokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.AbstractJavadocCheck;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtil;
import io.papermc.checkstyle.Util;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks that parameter descriptions in Javadoc are aligned.
 */
public final class JavadocAlignParameterDescriptionCheck extends AbstractJavadocCheck {

    static final Pattern SPACE_PREFIX = Pattern.compile("^\\s*");

    @Override
    public int[] getDefaultJavadocTokens() {
        return new int[]{JavadocCommentsTokenTypes.JAVADOC_CONTENT};
    }

    @Override
    public void visitJavadocToken(final DetailNode detailNode) {
        record ParamDesc(DetailNode node, int startCol) {
        }

        final List<ParamDesc> params = new ArrayList<>();
        int maxColumn = -1;
        DetailNode child = detailNode.getFirstChild();
        for (final DetailNode javadocBlock : JavadocUtil.getAllNodesOfType(detailNode, JavadocCommentsTokenTypes.JAVADOC_BLOCK_TAG)) {
            final DetailNode paramBlock = JavadocUtil.findFirstToken(javadocBlock, JavadocCommentsTokenTypes.PARAM_BLOCK_TAG);
            if (paramBlock == null) {
                continue;
            }
            final DetailNode paramDescription = JavadocUtil.findFirstToken(paramBlock, JavadocCommentsTokenTypes.DESCRIPTION);
            if (paramDescription == null) {
                continue;
            }
            // iterate over all text nodes (multiline)
            for (final DetailNode textNode : getFirstNodeOfTypePerLine(
                paramDescription, type -> type == JavadocCommentsTokenTypes.TEXT || type == JavadocCommentsTokenTypes.JAVADOC_INLINE_TAG
            )) {
                final Matcher matcher = SPACE_PREFIX.matcher(textNode.getText());
                int paramDescColNum = textNode.getColumnNumber();
                if (matcher.find()) {
                    paramDescColNum += matcher.group().length();
                }
                maxColumn = Math.max(maxColumn, paramDescColNum);
                params.add(new ParamDesc(textNode, paramDescColNum));
            }
            child = child.getNextSibling();
        }

        for (final ParamDesc param : params) {
            if (param.startCol() != maxColumn) {
                final DetailNode paramNameNode = Util.getPreviousSibling(param.node().getParent(), JavadocCommentsTokenTypes.PARAMETER_NAME);
                if (paramNameNode == null) {
                    continue;
                }
                this.log(
                    param.node().getLineNumber(),
                    param.startCol(),
                    "Param description for %s should start at column %d".formatted(paramNameNode.getText(), maxColumn + 1)
                );
            }
        }
    }

    // taken from getAllNodesOfType
    private static List<DetailNode> getFirstNodeOfTypePerLine(final DetailNode detailNode, final IntPredicate types) {
        final List<DetailNode> nodes = new ArrayList<>();
        final Set<Integer> seen = new HashSet<>();
        DetailNode node = detailNode.getFirstChild();
        while (node != null) {
            if (types.test(node.getType()) && seen.add(node.getLineNumber())) {
                nodes.add(node);
            }
            node = node.getNextSibling();
        }
        return nodes;
    }
}
