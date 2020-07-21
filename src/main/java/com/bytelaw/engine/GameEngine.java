package com.bytelaw.engine;

import com.bytelaw.engine.input.MouseInput;

public class GameEngine implements Runnable {
    public static final int TARGET_FPS = 75, TARGET_UPS = 30;

    private final Window window;
    private final IGameLogic gameLogic;
    private final Timer timer;
    private final MouseInput mouseInput;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic logic) {
        window = new Window(windowTitle, width, height, vSync);
        mouseInput = new MouseInput();
        gameLogic = logic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch(Exception exception) {
            exception.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        timer.init();
        mouseInput.init(window);
        gameLogic.init(window);
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while(running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while(accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!window.isvSync())
                sync();
        }
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }

    @SuppressWarnings("BusyWait")
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while(timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException ignored) {}
        }
    }

    protected void input() {
        mouseInput.input(window);
        gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
