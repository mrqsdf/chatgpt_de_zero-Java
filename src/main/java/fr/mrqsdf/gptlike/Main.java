package fr.mrqsdf.gptlike;

import fr.mrqsdf.gptlike.utils.BPETokenizer;
import fr.mrqsdf.gptlike.utils.Dataset;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            // STEP 1: Load the dataset
            logger.info("Loading dataset...");
            Dataset dataset = new Dataset();

            // STEP 2: Preprocess the dataset
            logger.info("Preprocessing dataset...");
            dataset.preprocess();

            // STEP 3: Train the tokenizer
            logger.info("Training tokenizer...");
            BPETokenizer tokenizer = new BPETokenizer();
            tokenizer.train(dataset);

            // STEP 4: Test the tokenizer
            logger.info("Testing tokenizer...");
            String testText = "Bonjour, comment ça va?";
            List<Integer> encodedText = tokenizer.encode(testText);
            logger.info("Encoded text " + testText + ": " + encodedText);
            String decodedText = tokenizer.decode(encodedText);
            logger.info("Decoded text: " + decodedText);

            // STEP 5: Save the tokenizer
            logger.info("Saving tokenizer...");
            tokenizer.save("assets/data/tokenizer.json");
            logger.info("Tokenizer saved successfully.");
        } catch (IOException e) {
            logger.severe("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}