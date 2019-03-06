package utils.loggers;

import java.util.logging.Logger;

public final class PuzzleLogger {

    private static Logger logger = Logger.getGlobal();

    public static void info(String className , String message) {
        logger.info("===================" + className + "====================");
        logger.info(message);
    }

    public static void warn(String className , String message) {
        logger.info("===================" + className + "====================");
        logger.warning(message);
    }

    public static void error(String className , String message) {
        logger.info("===================" + className + "====================");
        logger.severe(message);
    }
}
