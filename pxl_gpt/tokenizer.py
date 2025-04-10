from .dataset import Dataset
from collections import Counter
import re
from tqdm import tqdm


class BPETokenizer:
    def __init__(self, vocab_size=10000, min_frequency=2):
        """Initialize the BPE tokenizer."""
        self.vocab_size = vocab_size
        self.min_frequency = min_frequency
        self.vocab = {}
        self.merges = {}

    def train(self, dataset: Dataset):
        """Train the BPE tokenizer on the dataset."""
        # Step 1: Create initial vocabulary with special tokens
        vocab = {"<UNK>": 0, "<PAD>": 1}
        idx = len(vocab)

        # Step 2: Tokenize text properly, keeping punctuation as separate tokens
        all_tokens = []
        for text in dataset.data:
            # This regex splits on word boundaries but keeps punctuation as separate tokens
            tokens = re.findall(r"\w+|[^\w\s]", text)
            all_tokens.extend(tokens)

        # Count token frequencies
        token_counts = Counter(all_tokens)

        # Step 3: Initialize each token as character sequence
        token_splits = {}
        for token, count in token_counts.items():
            if count >= self.min_frequency:
                token_splits[token] = list(token)
                # Add each character to vocab if not already there
                for char in token:
                    if char not in vocab:
                        vocab[char] = idx
                        idx += 1

        # Step 4: BPE algorithm - merge most frequent pairs
        num_merges = min(self.vocab_size - len(vocab), 10000)

        for _ in tqdm(range(num_merges)):
            # Count all symbol pairs
            pairs = Counter()
            for token, freq in token_counts.items():
                if token in token_splits:
                    symbols = token_splits[token]
                    for i in range(len(symbols) - 1):
                        pairs[symbols[i], symbols[i + 1]] += freq

            # Break if no more pairs
            if not pairs:
                break

            # Get most frequent pair
            best_pair = max(pairs, key=pairs.get)
            if pairs[best_pair] < self.min_frequency:
                break

            # Create new symbol
            new_symbol = "".join(best_pair)

            # Add to vocabulary
            vocab[new_symbol] = idx
            idx += 1

            # Save merge operation
            self.merges[best_pair] = new_symbol

            # Apply merge to all splits
            for token in token_splits:
                symbols = token_splits[token]
                i = 0
                while i < len(symbols) - 1:
                    if (symbols[i], symbols[i + 1]) == best_pair:
                        symbols[i] = new_symbol
                        symbols.pop(i + 1)
                    else:
                        i += 1

            # Check if we've reached vocab size
            if len(vocab) >= self.vocab_size:
                break

        self.vocab = vocab

    def encode(self, text):
        """Encode text using BPE."""
        # Tokenize the text properly
        tokens = re.findall(r"\w+|[^\w\s]", text)
        token_ids = []

        for token in tokens:
            # Apply merges in sequence
            symbols = list(token)

            # Apply merges until no more can be applied
            while len(symbols) > 1:
                # Find the first pair that can be merged
                merged = False
                for i in range(len(symbols) - 1):
                    pair = (symbols[i], symbols[i + 1])
                    if pair in self.merges:
                        symbols[i] = self.merges[pair]
                        symbols.pop(i + 1)
                        merged = True
                        break

                # If no merge was possible, we're done
                if not merged:
                    break

            # Convert symbols to IDs
            for symbol in symbols:
                token_ids.append(self.vocab.get(symbol, self.vocab["<UNK>"]))

        return token_ids

    def decode(self, token_ids):
        """Decode token IDs back to text."""
        # Create reverse vocabulary for lookup
        id_to_token = {v: k for k, v in self.vocab.items()}

        # Convert IDs to tokens
        tokens = [id_to_token.get(id, "<UNK>") for id in token_ids]

        # Join tokens - this is simplistic and might need refinement
        # for proper handling of whitespace around punctuation
        text = "".join(tokens)
        return text

    def save(self, path):
        """Save the tokenizer to a file."""
        import json

        data = {"vocab": self.vocab, "merges": {str(k): v for k, v in self.merges.items()}}
        with open(path, "w") as f:
            json.dump(data, f)

    def load(self, path):
        """Load the tokenizer from a file."""
        import json
        import ast

        with open(path, "r") as f:
            data = json.load(f)
        self.vocab = data["vocab"]
        # Convert string keys back to tuples
        self.merges = {ast.literal_eval(k): v for k, v in data["merges"].items()}
