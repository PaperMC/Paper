package io.papermc.paper.connection;

import com.destroystokyo.paper.ClientOption;
import java.util.Locale;
import java.util.Set;
import net.minecraft.world.inventory.ContainerInput;
import org.bukkit.entity.Player;

public final class BedrockClientUtils {

    private BedrockClientUtils() {}

    public static boolean isBedrockClient(final Player player) {
        String brand = player.getClientBrandName();
        if (brand != null && brand.toLowerCase(Locale.ROOT).contains("geyser")) {
            return true;
        }

        Set<String> channels = player.getListeningPluginChannels();
        return channels.contains("geyser") || channels.contains("floodgate") || channels.contains("floodgate:brand");
    }

    public static boolean isJapaneseClient(final Player player) {
        return isJapaneseLocale(player.getClientOption(ClientOption.LOCALE));
    }

    public static boolean isJapaneseLocale(final String locale) {
        return locale != null && locale.toLowerCase(Locale.ROOT).startsWith("ja");
    }

    public static Locale parsePlayerLocale(final String locale) {
        Locale parsedLocale = net.kyori.adventure.translation.Translator.parseLocale(locale);
        if (parsedLocale != null) {
            return parsedLocale;
        }
        if (isJapaneseLocale(locale)) {
            return Locale.JAPANESE;
        }
        return Locale.US;
    }

    public static int remapBedrockContainerButton(final ContainerInput input, final int button, final Player player) {
        return remapBedrockContainerButton(input, button, isBedrockClient(player));
    }

    public static int remapBedrockContainerButton(final ContainerInput input, final int button, final boolean isBedrockClient) {
        if (!isBedrockClient) {
            return button;
        }

        if (input == ContainerInput.PICKUP || input == ContainerInput.QUICK_MOVE || input == ContainerInput.THROW) {
            return mapBedrockButton(button);
        }

        return button;
    }

    private static int mapBedrockButton(final int button) {
        return button == 0 ? 1 : button == 1 ? 0 : button;
    }
}
