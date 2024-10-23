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

### Build the Project
bash
mvn clean install

###Usage
To run the compression algorithm, modify the DNAClassification class according to your input files and parameters.

Set Dataset: Modify the DS variable to select your dataset.
Adjust Parameters: Change parameters such as  generations, and topSubsequences (this is given as input when running the code) based on your requirements.
Run the Program: Execute the main method to start the compression process.
bash
Copy code
java -cp target/GA-MDL-DNA-Compression-1.0-SNAPSHOT.jar com.example.DNAClassification
Results
The performance of the GA-based MDL compression method is benchmarked against established reference-free compression methods:

Compression Ratios: Detailed comparisons with JARVIS2 and GeCo3 are provided.
Compression Time: Analysis of the time taken for compression processes.
Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Acknowledgments
The development of this compression algorithm was inspired by the need for efficient genomic data storage.
Thanks to the BioPython library for its tools and resources for handling biological data.
