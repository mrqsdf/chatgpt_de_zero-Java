# PXL-GPT: Build ChatGPT From Scratch

![Python](https://img.shields.io/badge/python-3.12-blue)
![License](https://img.shields.io/badge/license-MIT-green)

A custom implementation of a GPT model built from scratch. This project demonstrates the fundamental concepts behind large language models like ChatGPT by implementing each component step by step.

## 🌟 Features

- **BPE Tokenizer**: Custom implementation of Byte Pair Encoding for text tokenization
- **Dataset Handling**: Preprocessing and management of text datasets
- **Model Training**: Train your own GPT model on custom data
- **Clean Architecture**: Modular design for easy understanding and extension

## 🛠️ Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/chatgpt_from_scratch.git
cd chatgpt_from_scratch

# Install the package in development mode
pip install -e .

# Install pre-commit hooks
pre-commit install --install-hooks
```

## 🚀 Quick Start

### Training a Tokenizer

```python
from pxl_gpt.dataset import Dataset
from pxl_gpt.tokenizer import BPETokenizer

# Load and preprocess dataset
dataset = Dataset()
dataset.preprocess()

# Train tokenizer
tokenizer = BPETokenizer()
tokenizer.train(dataset)

# Save tokenizer
tokenizer.save("data/tokenizer.json")
```

### Testing the Tokenizer

```python
# Load a saved tokenizer
tokenizer = BPETokenizer()
tokenizer.load("data/tokenizer.json")

# Test encoding and decoding
test_text = "Bonjour, comment ça va?"
encoded = tokenizer.encode(test_text)
decoded = tokenizer.decode(encoded)
print(f"Original: {test_text}")
print(f"Encoded: {encoded}")
print(f"Decoded: {decoded}")
```

## 📊 Project Structure

```
chatgpt_from_scratch/
├── data/
│   ├── cleaned_data.csv  # Preprocessed dataset
│   └── tokenizer.json    # Trained tokenizer
├── pxl_gpt/
│   ├── dataset.py        # Dataset handling
│   ├── dataset_loader.py # Dataset loading utilities
│   ├── model.py          # GPT model implementation
│   └── tokenizer.py      # BPE tokenizer implementation
├── scripts/
│   └── train_tokenizer.py # Script to train the tokenizer
└── pyproject.toml        # Project configuration
```

## 🔍 Components

### Dataset

The dataset module handles loading and preprocessing text data. By default, it uses a French Reddit discussion dataset.

### Tokenizer

A custom implementation of Byte Pair Encoding (BPE) tokenization, similar to what's used in models like GPT. The tokenizer:

- Splits text into initial tokens
- Iteratively merges the most frequent adjacent token pairs
- Builds a vocabulary of subword units
- Provides encoding and decoding functionality

### Model (Coming Soon)

The GPT model architecture implementation.

## Translated Code 

- [Code en Java by MrQsdf](https://github.com/mrqsdf/chatgpt_de_zero-Java/tree/main) - Code translated to java

## 📚 References

- [Attention Is All You Need](https://arxiv.org/abs/1706.03762) - The original Transformer paper
- [Improving Language Understanding with Unsupervised Learning](https://openai.com/research/language-unsupervised) - OpenAI's GPT approach

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

Created by PixelCrafted
