package ca.spottedleaf.moonrise.common.util;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public final class JsonUtil {

    public static void writeJson(final JsonElement element, final File file) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent(" ");
        jsonWriter.setLenient(false);
        Streams.write(element, jsonWriter);

        final String jsonString = stringWriter.toString();

        final File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        file.createNewFile();
        try (final PrintStream out = new PrintStream(new FileOutputStream(file), false, StandardCharsets.UTF_8)) {
            out.print(jsonString);
        }
    }

}
