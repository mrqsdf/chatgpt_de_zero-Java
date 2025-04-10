from loguru import logger

from .dataset_loader import DatasetLoader
from .preprocesser import Preprocesser


def main():
    # STEP 1: Load the dataset
    logger.info("Loading dataset...")
    dataset = DatasetLoader().load()

    # STEP 2: Preprocess the dataset
    logger.info("Preprocessing dataset...")
    dataset = Preprocesser.preprocess(dataset)


if __name__ == "__main__":
    main()
    logger.info("Finished processing.")
