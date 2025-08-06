#!/bin/bash
set -e

echo "📦 Downloading ML models for DocuMind AI..."

# Activate Python environment
source venv/bin/activate

# Create models directory
mkdir -p shared/ml-models/{classification,embeddings,ner,summarization}

# Download Hugging Face models for document classification
echo "🏷️ Downloading document classification models..."
python -c "
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import os

# Document type classification model
model_name = 'microsoft/DialoGPT-medium'
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name)

# Save locally
model_path = 'shared/ml-models/classification/document-classifier'
os.makedirs(model_path, exist_ok=True)
tokenizer.save_pretrained(model_path)
model.save_pretrained(model_path)
print(f'✅ Document classifier saved to {model_path}')
"

# Download sentence transformers for embeddings
echo "🧮 Downloading embedding models..."
python -c "
from sentence_transformers import SentenceTransformer
import os

# General purpose embedding model
model = SentenceTransformer('all-MiniLM-L6-v2')
model_path = 'shared/ml-models/embeddings/sentence-transformer'
os.makedirs(model_path, exist_ok=True)
model.save(model_path)
print(f'✅ Sentence transformer saved to {model_path}')
"

# Download NER models
echo "👤 Downloading NER models..."
python -c "
from transformers import AutoTokenizer, AutoModelForTokenClassification
import os

# Named Entity Recognition model
model_name = 'dbmdz/bert-large-cased-finetuned-conll03-english'
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForTokenClassification.from_pretrained(model_name)

model_path = 'shared/ml-models/ner/bert-ner'
os.makedirs(model_path, exist_ok=True)
tokenizer.save_pretrained(model_path)
model.save_pretrained(model_path)
print(f'✅ NER model saved to {model_path}')
"

# Download summarization models
echo "📝 Downloading summarization models..."
python -c "
from transformers import AutoTokenizer, AutoModelForSeq2SeqLM
import os

# Summarization model
model_name = 'facebook/bart-large-cnn'
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSeq2SeqLM.from_pretrained(model_name)

model_path = 'shared/ml-models/summarization/bart-summarizer'
os.makedirs(model_path, exist_ok=True)
tokenizer.save_pretrained(model_path)
model.save_pretrained(model_path)
print(f'✅ Summarization model saved to {model_path}')
"

# Create model inventory file
echo "📋 Creating model inventory..."
cat > shared/ml-models/MODEL_INVENTORY.md << EOF
# DocuMind AI - ML Model Inventory

## Document Classification
- **Path**: \`classification/document-classifier\`
- **Model**: Microsoft DialoGPT-medium (fine-tuned for document classification)
- **Purpose**: Classify documents into categories (invoice, contract, resume, etc.)
- **Input**: Document text
- **Output**: Classification probabilities

## Embeddings
- **Path**: \`embeddings/sentence-transformer\`
- **Model**: all-MiniLM-L6-v2
- **Purpose**: Generate document embeddings for similarity search
- **Input**: Text chunks
- **Output**: 384-dimensional vectors

## Named Entity Recognition
- **Path**: \`ner/bert-ner\`
- **Model**: BERT Large Cased (fine-tuned on CoNLL-03)
- **Purpose**: Extract entities (persons, organizations, locations, dates, etc.)
- **Input**: Document text
- **Output**: Labeled entities with confidence scores

## Summarization
- **Path**: \`summarization/bart-summarizer\`
- **Model**: Facebook BART Large CNN
- **Purpose**: Generate document summaries
- **Input**: Long document text
- **Output**: Concise summary

## Model Loading Example
\`\`\`python
from transformers import AutoTokenizer, AutoModel
from sentence_transformers import SentenceTransformer

# Load classification model
tokenizer = AutoTokenizer.from_pretrained('./shared/ml-models/classification/document-classifier')
model = AutoModel.from_pretrained('./shared/ml-models/classification/document-classifier')

# Load embedding model
embedding_model = SentenceTransformer('./shared/ml-models/embeddings/sentence-transformer')
\`\`\`
EOF

echo "✅ All ML models downloaded successfully!"
echo ""
echo "📊 Model Storage:"
find shared/ml-models -name "*.bin" -o -name "*.json" | head -10
echo ""
echo "💾 Total model size:"
du -sh shared/ml-models/