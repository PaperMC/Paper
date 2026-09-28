package io.papermc.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import io.papermc.checkstyle.Util;
import java.nio.file.Path;

public class RedundantNullabilityCheck extends AbstractCheck {

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
            TokenTypes.CLASS_DEF,
            TokenTypes.INTERFACE_DEF,
            TokenTypes.ANNOTATION_DEF,
            TokenTypes.RECORD_DEF,
            TokenTypes.ENUM_DEF,
        };
    }

    @Override
    public void visitToken(final DetailAST ast) {
        final boolean pkgIsNullMarked = Util.isPackageInfoAnnotated(Path.of(this.getFilePath()), NullabilityAnnotationsCheck::isNullMarkedAnnotation);
        final DetailAST nullMarkedAnnotation = NullabilityAnnotationsCheck.getNullMarkedAnnotation(ast);
        if (pkgIsNullMarked && nullMarkedAnnotation != null) {
            this.log(nullMarkedAnnotation.getLineNo(), ast.getColumnNo() - 1, "Redundant NullMarked annotation");
        }
    }
}
