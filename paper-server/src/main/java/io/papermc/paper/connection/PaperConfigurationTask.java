package io.papermc.paper.connection;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import org.slf4j.Logger;

public class PaperConfigurationTask implements ConfigurationTask {
    private static final Logger LOGGER = LogUtils.getClassLogger();

    public static final ExecutorService CONFIGURATION_POOL = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Configuration Thread #%d")
        .setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER)).build());

    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("paper_event_handling");

    private final ServerConfigurationPacketListenerImpl packetListener;

    public PaperConfigurationTask(ServerConfigurationPacketListenerImpl packetListener) {
        this.packetListener = packetListener;
    }

    @Override
    public void start(final Consumer<Packet<?>> task) {
        if (AsyncPlayerConnectionConfigureEvent.getHandlerList().getRegisteredListeners().length == 0) {
            this.packetListener.finishCurrentTask(TYPE);
            return;
        }
        CONFIGURATION_POOL.execute(() -> {
            AsyncPlayerConnectionConfigureEvent event = new AsyncPlayerConnectionConfigureEvent(this.packetListener.paperConnection);
            event.callEvent();
            this.packetListener.finishCurrentTask(TYPE);
        });
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
