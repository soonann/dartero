package com.example.dartero;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * GameDrawRunnable is a class responsible for rendering game on the screen
 * to ensure smooth and consistent rendering experience.
 * This class also calculates the FPS on screen.
 */
public class GameDrawRunnable implements Runnable {
    private Game game;
    private SurfaceHolder surfaceHolder;
    private boolean running = false;

    private boolean paused = false;
    private Thread thread;

    private double averageFPS;
    private int frameCount = 0;
    private long startTime;
    private long elapsedTime;

    /**
     * Constructs a new GameDrawRunnable object
     * @param game  The Game object that will be rendered
     * @param surfaceHolder The SurfaceHolder used for rendering the game
     */
    public GameDrawRunnable(Game game, SurfaceHolder surfaceHolder) {
        this.game = game;
        this.surfaceHolder = surfaceHolder;
    }

    /**
     * Start the rendering thread
     */
    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop the rendering thread
     */
    public void stop() {
        running = false;
    }

    /**
     * Wait for rendering thread to finish
     */
    public void join() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void pause() {
        paused = true;
    }
    public void resume() {
        paused = false;
    }

    /**
     * Returns the current average frames per second
     * @return The average FPS as a double
     */
    public double getAverageFPS() {
        return averageFPS;
    }

    /**
     * Main rendering loop.
     * Handles rendering of the game, calculating average FPS and display FPS.
     */
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
