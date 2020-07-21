package com.bytelaw;

import com.bytelaw.engine.DummyGame;
import com.bytelaw.engine.GameEngine;
import com.bytelaw.engine.IGameLogic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game3D {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting...");
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEngine = new GameEngine("3D Game", 750, 600, vSync, gameLogic);
            gameEngine.run();
            LOGGER.info("Closed");
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }
}
