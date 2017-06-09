package io.papermc.paper.console;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.util.List;
import java.util.regex.Pattern;

@Plugin(name = "stripAnsi", category = PatternConverter.CATEGORY)
@ConverterKeys({"stripAnsi"})
public final class StripANSIConverter extends LogEventPatternConverter {
    final private Pattern ANSI_PATTERN = Pattern.compile("\\e\\[[\\d;]*[^\\d;]");

    private final List<PatternFormatter> formatters;

    private StripANSIConverter(List<PatternFormatter> formatters) {
        super("stripAnsi", null);
        this.formatters = formatters;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        int start = toAppendTo.length();
        for (PatternFormatter formatter : formatters) {
            formatter.format(event, toAppendTo);
        }
        String content = toAppendTo.substring(start);
        content = ANSI_PATTERN.matcher(content).replaceAll("");

        toAppendTo.setLength(start);
        toAppendTo.append(content);
    }

    public static StripANSIConverter newInstance(Configuration config, String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on stripAnsi. Expected exactly 1, received " + options.length);
            return null;
        }

        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new StripANSIConverter(formatters);
    }
}
