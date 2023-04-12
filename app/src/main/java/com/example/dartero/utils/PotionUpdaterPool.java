package com.example.dartero.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class that represents a pool of threads for running the potions.
 */
public class PotionUpdaterPool {

    final ExecutorService pool;

    public PotionUpdaterPool() {
        final int cpuCores = Math.max(Runtime.getRuntime().availableProcessors() - 1, 1);
        pool = Executors.newFixedThreadPool(cpuCores);
    }

    public void submit(final Runnable task) {
        pool.submit(task);
    }
}
