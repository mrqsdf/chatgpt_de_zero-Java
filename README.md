![License](https://img.shields.io/badge/license-MIT-green)

A custom implementation of a GPT model built from scratch. This project demonstrates the fundamental concepts behind large language models like ChatGPT by implementing each component step by step.

## 🌟 Features

- **BPE Tokenizer**: Custom implementation of Byte Pair Encoding for text tokenization
- **Dataset Handling**: Preprocessing and management of text datasets
- **Model Training**: Train your own GPT model on custom data
- **Clean Architecture**: Modular design for easy understanding and extension

## 📊 Project Structure

```
chatgpt_from_scratch/
├── assets/
│   ├── data/
│   │   └── tokenizer.json  # Trained tokenizer
│   └── dataset/
│       └── dataset.json    # Preprocessed dataset
│    
├── fr/mrqsdf/gptlike/
│   ├── resource/
│   │   └── Pair.java       # Pair Class
│   ├── utils/
│   │   ├── ColoredLogger   # Custom Logger with color
│   │   ├── Dataset.java    # dataset Class
│   │   ├── BPETokenizer    # BPE tokenizer implementation
│   │   └── DatasetLoader   # Dataset loading utilities
│   └── Main.java           # Main Class for Train Tokenisation.
```

## 🔍 Components

### Dataset

The dataset module handles loading and preprocessing text data. By default, it uses a French discussion dataset.

### Tokenizer

A custom implementation of Byte Pair Encoding (BPE) tokenization, similar to what's used in models like GPT. The tokenizer:

- Splits text into initial tokens
- Iteratively merges the most frequent adjacent token pairs
- Builds a vocabulary of subword units
- Provides encoding and decoding functionality

### Model (Coming Soon)

The GPT model architecture implementation.

## 📚 References

- [Attention Is All You Need](https://arxiv.org/abs/1706.03762) - The original Transformer paper
- [Improving Language Understanding with Unsupervised Learning](https://openai.com/research/language-unsupervised) - OpenAI's GPT approach
- [Original Creator](https://www.youtube.com/watch?v=VrZTHO1E76w) - The original Creator

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

Created by PixelCrafted

Translated to Java By MrQsdf
