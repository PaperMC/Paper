package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestJavadocAlignParameterDescriptionCheck {

    @CheckTest("testdata.JavadocParamAlignment")
    void test(final CheckTestBuilder builder) {
        builder
            .addCheck(new JavadocAlignParameterDescriptionCheck())
            .addViolation("10:17: Param description for a should start at column 34")
            .addViolation("30:23: Param description for a should start at column 34")
            .addViolation("32:30: Param description for superLongParamName should start at column 34");
    }
}
