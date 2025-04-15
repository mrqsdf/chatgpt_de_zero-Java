package fr.mrqsdf.gptlike.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import com.google.gson.Gson;

public class DatasetLoader {
    private static final Logger logger = Logger.getLogger(DatasetLoader.class.getName());

    // Chemin par défaut vers le fichier JSON
    private String datasetFilePath;

    /**
     * Constructeur sans argument qui initialise le chemin par défaut : assets/dataset/dataset.json
     */
    public DatasetLoader() {
        this("assets/dataset/dataset.json");
    }

    /**
     * Constructeur avec spécification du chemin du fichier dataset.
     *
     * @param datasetFilePath Le chemin vers le fichier JSON du dataset.
     */
    public DatasetLoader(String datasetFilePath) {
        this.datasetFilePath = datasetFilePath;
    }

    /**
     * Charge le dataset à partir du fichier JSON et retourne la liste de maps contenant les paires clé/phrase.
     *
     * @return Liste de Map<String, String> représentant le dataset.
     * @throws IOException en cas d'erreur de lecture ou de parsing du fichier.
     */
    public List<Map<String, String>> loadDataset() throws IOException {
        logger.info("Chargement du dataset depuis : " + datasetFilePath);
        // Lecture du fichier JSON en UTF-8
        String content = new String(Files.readAllBytes(Paths.get(datasetFilePath)), StandardCharsets.UTF_8);

        // Parsing du JSON
        Gson gson = new Gson();
        Dataset dataset = gson.fromJson(content, Dataset.class);

        if (dataset == null || dataset.getData() == null) {
            logger.warning("Le dataset est vide ou n'a pas pu être parsé.");
            throw new IOException("Le dataset est vide ou n'a pas pu être parsé.");
        }
        return dataset.getData();
    }

    /**
     * Classe interne représentant la structure du fichier JSON.
     */
    public static class Dataset {
        private List<Map<String, String>> data;

        public List<Map<String, String>> getData() {
            return data;
        }

        public void setData(List<Map<String, String>> data) {
            this.data = data;
        }
    }
}
