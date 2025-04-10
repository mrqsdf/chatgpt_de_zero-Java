from .dataset_loader import DatasetLoader


class Preprocesser:
    @classmethod
    def preprocess(cls, dataset: DatasetLoader):
        """
        Preprocess the dataset by removing unnecessary columns and rows.
        :param dataset: The dataset to preprocess.
        :return: Preprocessed dataset.
        """
        # We want to only keep raw text and remove any other columns.

        text_only = dataset["utt"]

        # Remove any rows that are empty or contain only whitespace
        text_only = text_only[text_only.str.strip() != ""]
        # Remove any rows that are too short
        text_only = text_only[text_only.str.len() > 5]

        return text_only
