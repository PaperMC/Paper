package io.papermc.paper.connection;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.slf4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class PaperConfigurationTask implements ConfigurationTask {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    private static final ExecutorService CONFIGURATION_POOL = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Configuration Thread #%d")
        .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER)).build());

    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("paper_event_handling");

    private final ServerConfigurationPacketListenerImpl serverConfigurationPacketListener;

    public PaperConfigurationTask(ServerConfigurationPacketListenerImpl serverConfigurationPacketListener) {
        this.serverConfigurationPacketListener = serverConfigurationPacketListener;
    }

    @Override
    public void start(final Consumer<Packet<?>> task) {
        if (AsyncPlayerConnectionConfigureEvent.getHandlerList().getRegisteredListeners().length == 0) {
            this.serverConfigurationPacketListener.finishCurrentTask(TYPE);
            return;
        }
        CONFIGURATION_POOL.execute(() -> {
            AsyncPlayerConnectionConfigureEvent event = new AsyncPlayerConnectionConfigureEvent(this.serverConfigurationPacketListener.paperConnection);
            event.callEvent();
            this.serverConfigurationPacketListener.finishCurrentTask(TYPE);
        });
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
