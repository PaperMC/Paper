package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LocaleLanguage {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson b = new Gson();
    private static final Pattern c = Pattern.compile("%(\\d+\\$)?[\\d.]*[df]");
    private static volatile LocaleLanguage d = c();

    public LocaleLanguage() {}

    private static LocaleLanguage c() {
        Builder<String, String> builder = ImmutableMap.builder();
        BiConsumer biconsumer = builder::put;

        try {
            InputStream inputstream = LocaleLanguage.class.getResourceAsStream("/assets/minecraft/lang/en_us.json");
            Throwable throwable = null;

            try {
                a(inputstream, biconsumer);
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (inputstream != null) {
                    if (throwable != null) {
                        try {
                            inputstream.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    } else {
                        inputstream.close();
                    }
                }

            }
        } catch (JsonParseException | IOException ioexception) {
            LocaleLanguage.LOGGER.error("Couldn't read strings from /assets/minecraft/lang/en_us.json", ioexception);
        }

        final Map<String, String> map = builder.build();

        return new LocaleLanguage() {
            @Override
            public String a(String s) {
                return (String) map.getOrDefault(s, s);
            }

            @Override
            public boolean b(String s) {
                return map.containsKey(s);
            }
        };
    }

    public static void a(InputStream inputstream, BiConsumer<String, String> biconsumer) {
        JsonObject jsonobject = (JsonObject) LocaleLanguage.b.fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonObject.class);
        Iterator iterator = jsonobject.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<String, JsonElement> entry = (Entry) iterator.next();
            String s = LocaleLanguage.c.matcher(ChatDeserializer.a((JsonElement) entry.getValue(), (String) entry.getKey())).replaceAll("%$1s");

            biconsumer.accept(entry.getKey(), s);
        }

    }

    public static LocaleLanguage a() {
        return LocaleLanguage.d;
    }

    public abstract String a(String s);

    public abstract boolean b(String s);
}
