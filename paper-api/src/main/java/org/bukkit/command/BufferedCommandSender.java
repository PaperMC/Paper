package org.bukkit.command;

import org.jetbrains.annotations.NotNull;

/**
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
public class BufferedCommandSender implements MessageCommandSender {
    private final StringBuffer buffer = new StringBuffer();
    @Override
    public void sendMessage(@NotNull String message) {
        buffer.append(message);
        buffer.append("\n");
    }

    @NotNull
    public String getBuffer() {
        return buffer.toString();
    }

    public void reset() {
        this.buffer.setLength(0);
    }
}
