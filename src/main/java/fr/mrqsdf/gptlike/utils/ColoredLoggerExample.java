package fr.mrqsdf.gptlike.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ColoredLoggerExample {

    // Codes ANSI pour la coloration
    public static final String RESET  = "\u001B[0m";
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    // Formatter personnalisé qui ajoute les couleurs selon le niveau du log
    public static class ColoredFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            String color;
            Level level = record.getLevel();

            if (level == Level.SEVERE) {
                color = RED;
            } else if (level == Level.WARNING) {
                color = YELLOW;
            } else if (level == Level.INFO) {
                color = GREEN;
            } else if (level == Level.CONFIG) {
                color = CYAN;
            } else {
                // Pour les niveaux FINE, FINER, FINEST ou autres, on peut choisir une couleur différente
                color = BLUE;
            }

            // On peut personnaliser la forme du message ici
            String message = String.format("[%s] %s", level.getName(), formatMessage(record));
            return color + message + RESET + "\n";
        }
    }
}
