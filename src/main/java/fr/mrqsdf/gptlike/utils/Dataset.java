package fr.mrqsdf.gptlike.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Dataset {
    private static final Logger logger = Logger.getLogger(Dataset.class.getName());

    // Les données brutes sous la forme d'une liste de Map (chaque map représente une ligne du dataset)
    private List<Map<String, String>> rawData;
    // Le résultat du prétraitement sera stocké dans cette liste
    private List<String> data;
    // Nombre maximum d'éléments à conserver après prétraitement
    private int maxLength;

    /**
     * Constructeur par défaut qui charge le dataset depuis le chemin par défaut
     * "assets/dataset/dataset.json" et limite le nombre d'éléments à 10000.
     *
     * @throws IOException en cas d'erreur lors du chargement du fichier JSON.
     */
    public Dataset() throws IOException {
        this("assets/dataset/dataset.json", 10000);
    }

    /**
     * Constructeur avec spécification du chemin du fichier JSON et du nombre maximum d'éléments.
     *
     * @param datasetFilePath Le chemin vers le fichier JSON du dataset.
     * @param maxLength Nombre maximum d'éléments à conserver après prétraitement.
     * @throws IOException en cas d'erreur lors du chargement du fichier.
     */
    public Dataset(String datasetFilePath, int maxLength) throws IOException {
        DatasetLoader loader = new DatasetLoader(datasetFilePath);
        // Charge le dataset sous forme de List<Map<String, String>>
        this.rawData = loader.loadDataset();
        this.maxLength = maxLength;
        this.data = null;
    }

    /**
     * Méthode de prétraitement du dataset.
     * <ul>
     *   <li>Extraction de la valeur associée à la clé "utt" pour chaque entrée.</li>
     *   <li>Suppression des entrées vides ou composées uniquement d'espaces.</li>
     *   <li>Suppression des entrées dont la longueur est inférieure ou égale à 5 caractères.</li>
     *   <li>Conservation d'un maximum de maxLength éléments.</li>
     * </ul>
     */
    public void preprocess() {
        logger.info("Début du prétraitement du dataset");
        List<String> processedData = new ArrayList<>();

        for (Map<String, String> entry : rawData) {
            String text = entry.get("utt");
            if (text != null) {
                text = text.trim();
                if (!text.isEmpty() && text.length() > 5) {
                    processedData.add(text);
                }
            }
        }

        if (processedData.size() > maxLength) {
            processedData = processedData.subList(0, maxLength);
        }
        this.data = processedData;
        logger.info("Prétraitement terminé, nombre d'éléments traités : " + data.size());
    }

    /**
     * Getter pour récupérer le dataset prétraité.
     *
     * @return une liste de chaînes contenant les données prétraitées.
     */
    public List<String> getData() {
        return data;
    }
}

