package net.minecraft.server;

import java.util.UUID;

public interface ICommandListener {

    ICommandListener DUMMY = new ICommandListener() {
        @Override
        public void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid) {}

        @Override
        public boolean shouldSendSuccess() {
            return false;
        }

        @Override
        public boolean shouldSendFailure() {
            return false;
        }

        @Override
        public boolean shouldBroadcastCommands() {
            return false;
        }
    };

    void sendMessage(IChatBaseComponent ichatbasecomponent, UUID uuid);

    boolean shouldSendSuccess();

    boolean shouldSendFailure();

    boolean shouldBroadcastCommands();
}
