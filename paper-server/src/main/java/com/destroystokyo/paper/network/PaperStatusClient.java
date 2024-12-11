package com.destroystokyo.paper.network;

import net.minecraft.network.Connection;

class PaperStatusClient extends PaperNetworkClient implements StatusClient {

    PaperStatusClient(Connection networkManager) {
        super(networkManager);
    }

}
