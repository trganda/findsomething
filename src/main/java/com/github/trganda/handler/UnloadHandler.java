package com.github.trganda.handler;

import burp.api.montoya.extension.ExtensionUnloadingHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class UnloadHandler implements ExtensionUnloadingHandler {

    private final ExecutorService pool;

    public UnloadHandler(ExecutorService pool) {
        this.pool = pool;
    }

    @Override
    public void extensionUnloaded() {
        shutdown();
    }

    private void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
    }
}
