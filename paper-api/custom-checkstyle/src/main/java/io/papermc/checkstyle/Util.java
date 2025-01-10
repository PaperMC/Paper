package io.papermc.checkstyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.DetailNode;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtil;
import com.puppycrawl.tools.checkstyle.utils.TokenUtil;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jspecify.annotations.Nullable;

/**
 * Utility class containing utility methods for custom checkstyle checks.
 */
public final class Util {

    private Util() {
    }

    /**
     * Gets the previous sibling of the given node with the given type.
     *
     * @param node the node
     * @param type the type
     * @return the previous sibling with the given type, or {@code null} if not found
     */
    public static @Nullable DetailNode getPreviousSibling(final DetailNode node, final int type) {
        DetailNode sibling = JavadocUtil.getPreviousSibling(node);
        while (sibling != null && sibling.getType() != type) {
            sibling = JavadocUtil.getPreviousSibling(sibling);
        }
        return sibling;
    }

    /**
     * Gets the next sibling of the given node with the given type.
     *
     * @param node the node
     * @param type the type
     * @return the next sibling with the given type, or {@code null} if not found
     */
    public static @Nullable DetailAST getNextSibling(final DetailAST node, final int type) {
        DetailAST sibling = node.getNextSibling();
        while (sibling != null && sibling.getType() != type) {
            sibling = sibling.getNextSibling();
        }
        return sibling;
    }

    /**
     * Gets the enclosing type declaration of the given node.
     *
     * @param node the node
     * @return the enclosing type declaration, or {@code null} if not found
     */
    public static @Nullable DetailAST getEnclosingTypeDeclaration(final DetailAST node) {
        DetailAST parent = node.getParent();
        while (parent != null && !TokenUtil.isTypeDeclaration(parent.getType())) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Gets an iterator over the children of the given node with the given type.
     *
     * @param ast the node
     * @param type the type
     * @return the iterator
     */
    public static Iterable<DetailAST> childrenIterator(final DetailAST ast, final int type) {
        return () -> new Iterator<>() {
            private @Nullable DetailAST current = TokenUtil.findFirstTokenByPredicate(ast, child -> child.getType() == type).orElse(null);

            @Override
            public boolean hasNext() {
                return this.current != null;
            }

            @Override
            public DetailAST next() {
                if (this.current == null) {
                    throw new NoSuchElementException();
                }
                final DetailAST result = this.current;
                this.current = getNextSibling(this.current, type);
                return result;
            }
        };
    }
}
