package com.example.dartero;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread{

//    private Game game;
//    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private boolean isPaused = false;

    private GameUpdateRunnable updateThread;
    private GameDrawRunnable drawThread;
//    private boolean isPaused;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
//        this.game = game;
//        this.surfaceHolder = surfaceHolder;
        this.updateThread = new GameUpdateRunnable(game);
        this.drawThread = new GameDrawRunnable(game, surfaceHolder);
    }

    public void startLoop() {
        start();
    }

    @Override
    public void run() {
        isRunning = true;
        super.run();

        // Start rendering the screen
        drawThread.start();

        // Start the update thread
        updateThread.start();

        while (isRunning) {
            if (isPaused) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        drawThread.join();
        drawThread.stop();

        // Stop the update thread
        updateThread.join();
        updateThread.stop();
    }

    public void pauseGame() {
        isPaused = true;
        updateThread.pause();
        drawThread.pause();
    }

    public void resumeGame() {
        isPaused = false;
        updateThread.resume();
        drawThread.resume();
        synchronized (this) {
            notify();
        }
    }


    public void stopLoop() {
        Log.d("GameLoop.java", "stopLoop()");
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}