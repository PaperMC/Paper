package io.papermc.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import java.util.ArrayList;
import java.util.List;

public class CheckTestBuilder {

    final List<AbstractCheck> checks = new ArrayList<>();
    final List<String> expectedViolations = new ArrayList<>();

    public CheckTestBuilder addCheck(final AbstractCheck check) {
        this.checks.add(check);
        return this;
    }

    public CheckTestBuilder addViolation(final String expectedViolation) {
        this.expectedViolations.add(expectedViolation);
        return this;
    }

}
