package io.papermc.paper.network;

import com.destroystokyo.paper.network.StatusClient;
import net.minecraft.network.Connection;

public class PaperStatusClient extends PaperNetworkClient implements StatusClient {

    public PaperStatusClient(final Connection connection) {
        super(connection);
    }
}
