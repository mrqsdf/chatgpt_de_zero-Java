package fr.mrqsdf.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.resources.Pair;

public class BPETokenizer {
    private static final Logger logger = Logger.getLogger(BPETokenizer.class.getName());

    private int vocabSize;
    private int minFrequency;
    private Map<String, Integer> vocab;
    private Map<Pair, String> merges;

    public BPETokenizer() {
        this(10000, 2);
    }

    public BPETokenizer(int vocabSize, int minFrequency) {
        this.vocabSize = vocabSize;
        this.minFrequency = minFrequency;
        this.vocab = new LinkedHashMap<>();
        this.merges = new LinkedHashMap<>();
    }

    /**
     * Entraîne le tokenizer BPE sur le dataset fourni.
     * Le dataset doit être prétraité et contenir une liste de chaînes accessible par getData().
     *
     * @param dataset l'objet Dataset contenant les données textuelles.
     */
    public void train(Dataset dataset) {
        // Étape 1 : Initialisation du vocabulaire avec les tokens spéciaux
        vocab.put("<UNK>", 0);
        vocab.put("<PAD>", 1);
        vocab.put("<SPACE>", 2);
        int idx = vocab.size();

        // Étape 2 : Tokenisation des textes
        List<String> allTokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\w+|[^\\w\\s]|\\s");
        for (String text : dataset.getData()) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                allTokens.add(matcher.group());
            }
        }

        // Comptage de la fréquence de chaque token
        Map<String, Integer> tokenCounts = new HashMap<>();
        for (String token : allTokens) {
            tokenCounts.put(token, tokenCounts.getOrDefault(token, 0) + 1);
        }

        // Étape 3 : Initialisation de chaque token en séquence de caractères
        // tokenSplits associe chaque token à la liste de ses symboles (caractères)
        Map<String, List<String>> tokenSplits = new HashMap<>();
        for (Map.Entry<String, Integer> entry : tokenCounts.entrySet()) {
            String token = entry.getKey();
            int count = entry.getValue();
            if (count >= minFrequency) {
                if (token.equals(" ")) {
                    List<String> symbols = new ArrayList<>();
                    symbols.add("<SPACE>");
                    tokenSplits.put("<SPACE>", symbols);
                } else {
                    List<String> symbols = new ArrayList<>();
                    for (char c : token.toCharArray()) {
                        symbols.add(String.valueOf(c));
                    }
                    tokenSplits.put(token, symbols);
                }
                // Ajoute chaque caractère au vocabulaire s'il n'y est pas déjà
                for (char c : token.toCharArray()) {
                    String ch = String.valueOf(c);
                    if (!vocab.containsKey(ch)) {
                        vocab.put(ch, idx++);
                    }
                }
            }
        }

        // Étape 4 : Algorithme BPE – fusion des paires les plus fréquentes
        int numMerges = Math.min(vocabSize - vocab.size(), 10000);
        for (int mergeCount = 0; mergeCount < numMerges; mergeCount++) {
            // Compter toutes les paires de symboles dans tokenSplits
            Map<Pair, Integer> pairs = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : tokenSplits.entrySet()) {
                String token = entry.getKey();
                int freq = tokenCounts.getOrDefault(token, 0);
                List<String> symbols = entry.getValue();
                for (int i = 0; i < symbols.size() - 1; i++) {
                    Pair pair = new Pair(symbols.get(i), symbols.get(i + 1));
                    pairs.put(pair, pairs.getOrDefault(pair, 0) + freq);
                }
            }

            if (pairs.isEmpty()) {
                break;
            }

            // Recherche de la paire la plus fréquente
            Pair bestPair = null;
            int bestCount = -1;
            for (Map.Entry<Pair, Integer> entry : pairs.entrySet()) {
                if (entry.getValue() > bestCount) {
                    bestCount = entry.getValue();
                    bestPair = entry.getKey();
                }
            }

            if (bestCount < minFrequency) {
                break;
            }

            // Création du nouveau symbole en fusionnant la paire
            String newSymbol = bestPair.first + bestPair.second;
            vocab.put(newSymbol, idx++);
            merges.put(bestPair, newSymbol);

            // Application de la fusion sur toutes les séquences de tokens
            for (Map.Entry<String, List<String>> entry : tokenSplits.entrySet()) {
                List<String> symbols = entry.getValue();
                int i = 0;
                while (i < symbols.size() - 1) {
                    Pair currentPair = new Pair(symbols.get(i), symbols.get(i + 1));
                    if (currentPair.equals(bestPair)) {
                        symbols.set(i, newSymbol);
                        symbols.remove(i + 1);
                    } else {
                        i++;
                    }
                }
            }

            if (vocab.size() >= vocabSize) {
                break;
            }
        }
    }

    /**
     * Encode le texte en une liste d'identifiants de tokens en utilisant les fusions BPE.
     *
     * @param text Le texte à encoder.
     * @return Une liste d'identifiants correspondant aux tokens.
     */
    public List<Integer> encode(String text) {
        List<Integer> tokenIds = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\w+|[^\\w\\s]|\\s");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String token = matcher.group();
            if (token.equals(" ")) {
                tokenIds.add(vocab.getOrDefault("<SPACE>", vocab.get("<UNK>")));
                continue;
            }
            List<String> symbols = new ArrayList<>();
            for (char c : token.toCharArray()) {
                symbols.add(String.valueOf(c));
            }
            // Appliquer les fusions tant qu'il est possible d'en appliquer
            boolean merged = true;
            while (symbols.size() > 1 && merged) {
                merged = false;
                for (int i = 0; i < symbols.size() - 1; i++) {
                    Pair pair = new Pair(symbols.get(i), symbols.get(i + 1));
                    if (merges.containsKey(pair)) {
                        symbols.set(i, merges.get(pair));
                        symbols.remove(i + 1);
                        merged = true;
                        break;
                    }
                }
            }
            // Conversion des symboles en identifiants
            for (String symbol : symbols) {
                tokenIds.add(vocab.getOrDefault(symbol, vocab.get("<UNK>")));
            }
        }
        return tokenIds;
    }

    /**
     * Décode la liste d'identifiants de tokens en texte.
     *
     * @param tokenIds La liste d'identifiants.
     * @return Le texte correspondant.
     */
    public String decode(List<Integer> tokenIds) {
        // Crée une map inverse pour retrouver le token à partir de son identifiant
        Map<Integer, String> idToToken = new HashMap<>();
        for (Map.Entry<String, Integer> entry : vocab.entrySet()) {
            idToToken.put(entry.getValue(), entry.getKey());
        }
        StringBuilder sb = new StringBuilder();
        for (Integer id : tokenIds) {
            String token = idToToken.getOrDefault(id, "<UNK>");
            sb.append(token);
        }
        // Remplace le token spécial <SPACE> par un espace
        return sb.toString().replace("<SPACE>", " ");
    }

    /**
     * Sauvegarde l'état du tokenizer dans un fichier JSON.
     *
     * @param path Le chemin vers le fichier de sauvegarde.
     * @throws IOException en cas d'erreur d'écriture.
     */
    public void save(String path) throws IOException {
        // Prépare les données à sauvegarder : vocabulaire et fusions (merges).
        Map<String, Object> data = new HashMap<>();
        data.put("vocab", vocab);
        Map<String, String> mergesToSave = new LinkedHashMap<>();
        for (Map.Entry<Pair, String> entry : merges.entrySet()) {
            // La clé est créée sous la forme "first##second"
            String key = entry.getKey().first + "##" + entry.getKey().second;
            mergesToSave.put(key, entry.getValue());
        }
        data.put("merges", mergesToSave);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        Files.write(Paths.get(path), json.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Charge l'état du tokenizer à partir d'un fichier JSON.
     *
     * @param path Le chemin vers le fichier JSON.
     * @throws IOException en cas d'erreur de lecture.
     */
    public void load(String path) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());

        // Chargement du vocabulaire
        Map<String, Double> vocabMap = (Map<String, Double>) data.get("vocab");
        vocab = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : vocabMap.entrySet()) {
            vocab.put(entry.getKey(), entry.getValue().intValue());
        }

        // Chargement des merges et conversion des clés en objets Pair
        Map<String, String> mergesMap = (Map<String, String>) data.get("merges");
        merges = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : mergesMap.entrySet()) {
            String keyStr = entry.getKey();
            String[] parts = keyStr.split("##");
            if (parts.length == 2) {
                Pair pair = new Pair(parts[0], parts[1]);
                merges.put(pair, entry.getValue());
            }
        }
    }

}

