package fr.mrqsdf.gptlike.utils;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ColoredLogger {

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

    public static boolean displayClassName = false; // Afficher le nom de la classe dans le log

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

            //get Name() pour le nom de la classe
            String className = record.getSourceClassName();
            String message = String.format("[%s] %s", level.getName(), formatMessage(record));
            String formattedMessage = displayClassName ? String.format("%s: %s", className, message) : message;
            return color + formattedMessage + RESET + "\n";
        }
    }
}
