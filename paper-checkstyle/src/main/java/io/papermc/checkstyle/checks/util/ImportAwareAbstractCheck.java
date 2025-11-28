package io.papermc.checkstyle.checks.util;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import io.papermc.checkstyle.Util;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.Nullable;

public abstract class ImportAwareAbstractCheck extends AbstractCheck {

    private final ThreadLocal<Set<String>> imports = ThreadLocal.withInitial(LinkedHashSet::new);
    private final ThreadLocal<Map<String, String>> simpleImports = ThreadLocal.withInitial(LinkedHashMap::new);

    @Override
    public void beginTree(final DetailAST rootAST) {
        final Set<String> imports = new LinkedHashSet<>();
        final Map<String, String> simpleImports = new LinkedHashMap<>();
        for (final DetailAST importAst : Util.childrenIterator(rootAST, TokenTypes.IMPORT)) {
            extractName(importAst, imports, simpleImports);
        }
        this.imports.set(imports);
        this.simpleImports.set(simpleImports);
    }

    private static void extractName(final DetailAST parent, final Set<String> fullImports, final Map<String, String> simpleImports) {
        final DetailAST lastDot = Objects.requireNonNull(parent.findFirstToken(TokenTypes.DOT));
        if (lastDot.getChildCount(TokenTypes.STAR) > 0) {
            // skip wildcard imports
            return;
        }
        final DetailAST ident = Objects.requireNonNull(lastDot.findFirstToken(TokenTypes.IDENT));
        final String fullImport = FullIdent.createFullIdent(lastDot).getText();
        fullImports.add(fullImport);
        simpleImports.put(ident.getText(), fullImport);
    }

    @Override
    public void finishTree(final DetailAST rootAST) {
        this.imports.remove();
    }

    protected boolean hasImportFor(final String type) {
        return this.imports.get().contains(type);
    }

    protected @Nullable String getFullImportBySimple(final String simpleName) {
        return this.simpleImports.get().get(simpleName);
    }
}
