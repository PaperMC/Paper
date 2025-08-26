package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestJavadocAlignParameterDescriptionCheck {

    @CheckTest("testdata.JavadocParamAlignment")
    void test(final CheckTestBuilder builder) {
        builder
            .addCheck(new JavadocAlignParameterDescriptionCheck())
            .addViolation("10:16: Param description for a should start at column 33");
    }
}
