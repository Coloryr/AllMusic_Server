package com.coloryr.allmusic.server.side.forge;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final PackData data, final PlayPayloadContext context) {
        // Do something with the data, on the main thread
        context.workHandler().submitAsync(() -> {

                })
                .exceptionally(e -> {
                    // Handle exception

                    return null;
                });
    }
}
