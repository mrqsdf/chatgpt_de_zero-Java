from loguru import logger

from .dataset import Dataset
from .tokenizer import BPETokenizer


def main():
    # STEP 1: Load the dataset
    logger.info("Loading dataset...")
    dataset = Dataset()

    # STEP 2: Preprocess the dataset
    logger.info("Preprocessing dataset...")
    dataset.preprocess()

    # STEP 3: Train the tokenizer
    logger.info("Training tokenizer...")
    tokenizer = BPETokenizer()
    tokenizer.train(dataset)

    # STEP 4: Test the tokenizer
    logger.info("Testing tokenizer...")
    test_text = "Bonjour, comment ça va?"
    encoded_text = tokenizer.encode(test_text)
    logger.info(f"Encoded text {test_text}: {encoded_text}")
    decoded_text = tokenizer.decode(encoded_text)
    logger.info(f"Decoded text: {decoded_text}")


if __name__ == "__main__":
    main()
    logger.info("Finished processing.")
