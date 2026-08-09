package io.papermc.checkstyle.checks;

import io.papermc.checkstyle.CheckTest;
import io.papermc.checkstyle.CheckTestBuilder;

class TestUnnecessaryFullyQualifiedImportCheck {

    @CheckTest("testdata.imports.UnnecessaryFullyQualifiedImport")
    void test(final CheckTestBuilder builder) {
        builder
            // variable def
            .addViolation("20:15: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // new literal
            .addViolation("20:84: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // variable def
            .addViolation("21:15: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // new literal
            .addViolation("21:95: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // type parm def (extends)
            .addViolation("29:23: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            // type argument, type param super
            .addViolation("29:115: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("37:23: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("37:71: Unnecessary fully qualified import (no conflict): org.jspecify.annotations.Nullable")
            .addViolation("37:143: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerClass")
            .addViolation("46:41: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass.InnerInterface")
            .addViolation("50:30: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("54:9: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("54:61: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            .addViolation("55:9: Unnecessary fully qualified import (no conflict): testdata.imports.testclasses.subpkg.OtherInterface")
            // field def
            .addViolation("66:5: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            // class literal ".class"
            .addViolation("70:28: Fully qualified import used: testdata.imports.testclasses.subpkg.OtherClass")
            .addCheck(new UnnecessaryFullyQualifiedImportCheck());
    }
}
