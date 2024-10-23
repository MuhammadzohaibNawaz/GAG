# GA-MDL DNA Compression

## Overview

This repository contains a Genetic Algorithm (GA) based approach for DNA compression, leveraging the Minimum Description Length (MDL) principle. Our method aims to efficiently compress genomic sequences by identifying optimal k-mers (patterns) that provide the best compression performance. This project addresses the challenges associated with the exponential growth of genomic data, offering a robust solution for effective data management.

## Features

- **Genetic Algorithm Optimization**: Utilizes a genetic algorithm to optimize k-mer selection, enhancing compression efficiency.
- **Minimum Description Length Principle**: Applies the MDL principle to identify the most compact representation of genomic sequences.
- **Flexible Input**: Supports various genomic datasets in FASTA format.
- **Performance Benchmarking**: Evaluates compression ratios and time against state-of-the-art methods such as JARVIS2 and GeCo3.

## Installation

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- [Maven](https://maven.apache.org/download.cgi) (for building the project)

### Clone the Repository

```bash
git clone https://github.com/yourusername/GA-MDL-DNA-Compression.git
cd GA-MDL-DNA-Compression
