package com.alrex.parcool.utilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

//by ruby :flushed:
public class FabricDistExecutor {
    public static <T> T unsafeRunForDist(Supplier<? extends T> client, Supplier<? extends T> server) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) {
            return client.get();
        } else {
            return server.get();
        }
    }
}
