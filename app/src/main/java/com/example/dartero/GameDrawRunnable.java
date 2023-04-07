package com.example.dartero;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameDrawRunnable implements Runnable {
    private Game game;
    private SurfaceHolder surfaceHolder;
    private boolean running = false;
    private Thread thread;

    private double averageFPS;
    private int frameCount = 0;
    private long startTime;
    private long elapsedTime;

    public GameDrawRunnable(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (running) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.draw(canvas);

                    // Calculate and draw FPS
                    elapsedTime = System.currentTimeMillis() - startTime;
                    if (elapsedTime >= 1000) {
                        averageFPS = frameCount / (1E-3 * elapsedTime);
                        frameCount = 0;
                        startTime = System.currentTimeMillis();
                    }
                    frameCount++;

                    game.drawFPS(canvas, averageFPS); // Pass the averageFPS to the drawFPS method
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
