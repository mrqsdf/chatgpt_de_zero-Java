from .dataset_loader import DatasetLoader


class Dataset:
    def __init__(self, dataset_ref="breandan/french-reddit-discussion", download_dir="data"):
        """
        Initialize the Dataset class.
        :param dataset_ref: Reference to the dataset.
        :param download_dir: Directory to download the dataset.
        """
        self.raw_data = DatasetLoader(dataset_ref, download_dir).load()
        self.data = None

    def preprocess(self):
        """
        Preprocess the dataset by removing unnecessary columns and rows.
        :param dataset: The dataset to preprocess.
        :return: Preprocessed dataset.
        """
        # We want to only keep raw text and remove any other columns.

        text_only = self.raw_data["utt"]

        # Remove any rows that are empty or contain only whitespace
        text_only = text_only[text_only.str.strip() != ""]
        # Remove any rows that are too short
        text_only = text_only[text_only.str.len() > 5]

        self.data = text_only.to_list()[:1000]
