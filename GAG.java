/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dna;

/**
 *
 * @author zohaib
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author zohaib
 */
public class GMG {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Define file path to read sequences

        int longestPattern = 5;
        Map<String, Character> sequenceToCodeMap = new HashMap<>();
        String DS = "BuEb";//enter dataset name here
        List<String> chromosomeFiles = null;
        String folderPath = null;
        List<String> dnaSequences = new ArrayList<>();
        folderPath = "Data/";
        chromosomeFiles = Arrays.asList(DS);

        int maxEntryLength = 1;
        boolean probBasedOccurances = true;
        // Read each chromosome file and combine lines into a single sequence (ignoring the first line)
        // Read each chromosome file and combine lines into a single sequence (ignoring the first line)
        for (String fileName : chromosomeFiles) {
            String filePath = folderPath + "/" + fileName;

            try {
                List<String> lines = Files.readAllLines(Paths.get(filePath));

                // Check if the file has .fasta or .fa extension
                if (fileName.endsWith(".fasta") || fileName.endsWith(".fa")) {
                    // Process FASTA format
                    StringBuilder chromosomeSequence = new StringBuilder();
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i).trim();

                        // Skip header line that starts with '>'
                        if (line.startsWith(">")) {
                            // If there is an accumulated sequence in the StringBuilder, add it to the list
                            if (chromosomeSequence.length() > 0) {
                                dnaSequences.add(chromosomeSequence.toString()); // Add sequence to list
                                chromosomeSequence.setLength(0); // Reset StringBuilder for the next sequence
                            }
                            continue; // Skip the header line
                        }

                        // Append the sequence lines (ignoring any headers)
                        chromosomeSequence.append(line);
                    }
                    // Add the last sequence after the loop
                    if (chromosomeSequence.length() > 0) {
                        dnaSequences.add(chromosomeSequence.toString());
                    }
                } else {
                    // Use the original processing for non-FASTA files
                    StringBuilder chromosomeSequence = new StringBuilder();
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i).trim();

                        // Ignore the first line if it starts with '>'
                        if (i == 0 && line.startsWith(">")) {
                            continue;
                        }

                        // Combine the remaining lines into one sequence
                        chromosomeSequence.append(line);
                    }

                    // Add the sequence to the list
                    if (chromosomeSequence.length() > 0) {
                        dnaSequences.add(chromosomeSequence.toString());
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading the file: " + fileName + " - " + e.getMessage());
            }
        }
        int totalBases = 0;
        int totalBits = 0;

        int bitsPerBase = 2;  // Each base requires 2 bits

        for (String sequence : dnaSequences) {
            sequence = sequence.trim();
            int sequenceLength = sequence.length();
            totalBases += sequenceLength;
            totalBits += sequenceLength * bitsPerBase;
        }

        // Calculate BPB for original data
        double bpbB = (double) totalBits / totalBases;
        System.out.println("Total bases of original data: " + totalBases);
        System.out.println("Total bits of original data: " + totalBits);

        // Take inputs for the number of generations and the number of top subsequences (m)
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the number of generations (n): ");
//        int generations = scanner.nextInt();
        int generations = 10;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of top subsequences (m): ");
        int topSubsequences = scanner.nextInt();
        long startTime = System.currentTimeMillis(); // Get the start time
        // Set to store the unique best sequences (Ensuring no duplicates)
        System.out.println("Started");
        String COV = null;
        String MV = null;

        for (int combination = 1; combination <= 1; combination++) {
            int codeIndex = 0;
            List<String> dnaSequencesDummy = dnaSequences;
            Set<String> bestSequences = new HashSet<>();
            List<Integer> bestOccurrences = new ArrayList<>();  // To store corresponding occurrences

            String dna1 = null;
            String dna2 = null;

            switch (combination) {
                case 1:
                    COV = "single";
                    MV = "single";
                    break;

                default:
                    System.out.println("Invalid combination.");
                    continue;
            }
            int pss = 0;
            // Loop to run the GA code m times, ensuring we collect unique sequences
            while (bestSequences.size() < topSubsequences) {
                pss = bestSequences.size();

                String bestSequence = "";
                int maxOverallOccurrences = 0;
                String bestDnaString = "";  // To hold the actual DNA sequence with the most occurrences

                dna1 = generateRandomDNA();

            // Generate dna2 and check if it's the same as dna1
                do {
                    dna2 = generateRandomDNA();
                } while (dna2.equals(dna1));

                String[] crossoverResult = null;
                String mutatedDna1 = null;
                String mutatedDna2 = null;
                // Store occurrences for each sequence along with the actual sequence
                Map<String, Integer> sequenceOccurrences = new HashMap<>();
                Map<String, String> sequenceStrings = new HashMap<>();
                sequenceOccurrences.put(dna1, countOccurrences(dnaSequencesDummy, dna1));

                sequenceStrings.put("DNA1", dna1);

                sequenceOccurrences.put(dna2, countOccurrences(dnaSequencesDummy, dna2));
                sequenceStrings.put("DNA2", dna2);
                // Loop for each generation (n generations)
                for (int gen = 1; gen <= generations; gen++) {
                    System.out.println("Pat: " + (bestSequences.size()+1) + "\nGeneration " + gen + ":");
                    if (COV.equals("single") && MV.equals("single")) {

                        crossoverResult = applySinglePointCrossover(dna1, dna2);
                        if (crossoverResult[0].length() == 0 || crossoverResult[1].length() == 0) {
                            break;
                        }
                        mutatedDna1 = applySinglePointMutation(dna1);

                        // Generate dna2 and check if it's the same as dna1
                        do {
                            mutatedDna2 = applySinglePointMutation(dna2);
                        } while (mutatedDna2.equals(mutatedDna1));
                    }
                    if (probBasedOccurances) {
                        // 50-50 chance to run either the first two lines or the last two lines
                        if (Math.random() < 0.5) {
                            // Run the first two lines
                            sequenceOccurrences.put(mutatedDna1, countOccurrences(dnaSequencesDummy, mutatedDna1));
                            sequenceStrings.put("Mutated DNA1", mutatedDna1);
                        } else {
                            // Run the last two lines
                            sequenceOccurrences.put(mutatedDna2, countOccurrences(dnaSequencesDummy, mutatedDna2));
                            sequenceStrings.put("Mutated DNA2", mutatedDna2);
                        }
                    } else {
                        // If probBasedOccurrences is false, run all four lines
                        sequenceOccurrences.put(mutatedDna1, countOccurrences(dnaSequencesDummy, mutatedDna1));
                        sequenceStrings.put("Mutated DNA1", mutatedDna1);

                        sequenceOccurrences.put(mutatedDna2, countOccurrences(dnaSequencesDummy, mutatedDna2));
                        sequenceStrings.put("Mutated DNA2", mutatedDna2);
                    }
                    // Find the best sequence for this generation
                    String bestSequenceInGeneration = "";
                    String bestDnaInGeneration = "";
                    int maxOccurrencesInGeneration = 0;
                    //System.out.println("Here2.5");
                    for (Map.Entry<String, Integer> entry : sequenceOccurrences.entrySet()) {
                        if (entry.getValue() > maxOccurrencesInGeneration) {
                            maxOccurrencesInGeneration = entry.getValue();
                            bestSequenceInGeneration = entry.getKey();
                            bestDnaInGeneration = entry.getKey();
                        }
                    }
                    // Track the overall best sequence across generations
                    if (maxOccurrencesInGeneration > maxOverallOccurrences) {
                        maxOverallOccurrences = maxOccurrencesInGeneration;
                        bestSequence = bestSequenceInGeneration;
                        bestDnaString = bestDnaInGeneration;
                    }
                    String firstString = null;
                    String secondString = null;
                    int firstCount = -1;
                    int secondCount = -1;

                    // Iterate through the map to find the top two occurrences
                    for (Map.Entry<String, Integer> entry : sequenceOccurrences.entrySet()) {
                        String currentString = entry.getKey();
                        int currentCount = entry.getValue();
                        if (currentCount > firstCount) {
                            // Update second place before first
                            secondString = firstString;
                            secondCount = firstCount;

                            // Update first place
                            firstString = currentString;
                            firstCount = currentCount;
                        } else if (currentCount >= secondCount) {
                            // Update second place only
                            secondString = currentString;
                            secondCount = currentCount;
                        }
                    }
                    dna1 = firstString;
                    dna2 = secondString;
                }

                // Assign characters other than A, C, T, G for the best sequences
                // Create a StringBuilder to hold characters
                StringBuilder codeBuilder = new StringBuilder();

                // Iterate through all printable ASCII characters
                for (char c = 32; c < 127; c++) { // From ASCII 32 to 126
                    // Append characters except for A, C, T, and G
                    if (c != 'A' && c != 'C' && c != 'T' && c != 'G') {
                        codeBuilder.append(c);
                    }
                }
                // Convert the StringBuilder to a char array
                char[] codes = codeBuilder.toString().toCharArray();

                // Ensure the best sequence is unique and does not overlap with already stored sequences
                if (maxOverallOccurrences != 0 && bestDnaString.length() > maxEntryLength) {
                    bestSequences.add(bestDnaString);
                    bestOccurrences.add(maxOverallOccurrences);

                    // Assign a unique code character for this bestDnaString
                    if (!sequenceToCodeMap.containsKey(bestDnaString)) {
                        sequenceToCodeMap.put(bestDnaString, codes[bestSequences.size()]);
                        codeIndex++;  // Increment to assign next character for next sequence
                    }

                    // Replace this bestDnaString in dnaSequencesDummy with its code character
                    for (int i = 0; i < dnaSequencesDummy.size(); i++) {
                        String sequence = dnaSequencesDummy.get(i);

                        // Replace the bestDnaString with the assigned code
                        if (sequence.contains(bestDnaString)) {
                            String updatedSequence = sequence.replace(bestDnaString, String.valueOf(sequenceToCodeMap.get(bestDnaString)));
                            dnaSequencesDummy.set(i, updatedSequence);
                        }
                    }

                }
                if (pss == bestSequences.size()) {
                    System.out.println("No more patterns can be found, stopping at CTL : " + bestSequences.size());
                    break;
                }
            }

            if (COV.equals("single") && MV.equals("single")) {
                System.out.println("Result of Crossover: Single, and Mutation : Single");

                // Print the final result - top m sequences
                System.out.println("\n=== Final Best Sequences ===");
                int i = 1;
                for (String bestSequenc : bestSequences) {
                    System.out.println("Top sequence " + i + ": " + bestSequenc + " with " + bestOccurrences.get(i - 1) + " occurrences.");
                    i++;
                }

                int totalBasesA = 0;
                int totalBitsA = 0;

                for (String sequence : dnaSequencesDummy) {
                    sequence = sequence.trim();
                    int sequenceLength = sequence.length();
                    totalBasesA += sequenceLength;
                    totalBitsA += sequenceLength * bitsPerBase;
                }
                long endTime = System.currentTimeMillis(); // Get the end time
                long duration = endTime - startTime; // Calculate duration in milliseconds
                double seconds = duration / 1000.0; // Convert to seconds
                System.out.println("Time taken: " + seconds + " seconds");
                double CTZ = calculateBPBUsingShannonEntropy(bestSequences, bestOccurrences);

                // Calculate BPB for original data
                System.out.println("Total bases of compressed data: " + totalBasesA);
                System.out.println("Total bits of compressed data: " + totalBitsA);

                int totalBasesAA = 0;
                int totalBitsAA = 0;

                for (String sequence : dnaSequencesDummy) {
                    sequence = sequence.trim();
                    int sequenceLength = sequence.length();
                    totalBasesAA += sequenceLength;
                    totalBitsAA += sequenceLength * bitsPerBase;
                }

                System.out.println("Bits per base (BPB) of compressed data: " + (double) (totalBitsAA) / totalBases);

            }

        }
    }

    // The rest of the methods like countOccurrences, generateRandomDNA, applySinglePointCrossover, applySinglePointMutation remain the same as before...
    public static int countOccurrences(List<String> sequences, String target) {
        int totalOccurrences = 0;
//System.out.println(sequences);
        // Loop over all sequences
        for (String sequence : sequences) {
            sequence = sequence.trim();  // Clean up leading/trailing spaces if any
            int sequenceLength = sequence.length();
            int targetLength = target.length();

            // Traverse the sequence character by character for overlapping matches
            for (int i = 0; i <= sequenceLength - targetLength; i++) {
                // Check if the target matches starting from the current position
                if (sequence.substring(i, i + targetLength).equals(target)) {
                    //             System.out.print(target+", ");
                    //           System.out.println(i);
                    totalOccurrences++;
                    i = i + targetLength - 1;
                }
            }
        }

        return totalOccurrences;
    }

    // Function to generate random DNA sequence of length between 2 and 6
    public static String generateRandomDNA() {
        double probabilityOfSize1 = 0.0000000010;
        Random rand = new Random();
        char[] nucleotides = {'A', 'C', 'T', 'G'};

        // Check if the probability is valid (between 0 and 1)
        if (probabilityOfSize1 < 0 || probabilityOfSize1 > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }

        // Generate length: size 1 with given probability, or size 2-6 otherwise
        int length;
        if (rand.nextDouble() < probabilityOfSize1) {
            length = 1;  // Probability dictates we create a sequence of size 1
        } else {
            length = rand.nextInt(6) + 2;  // Random size between 2 and 6
        }

        // Generate the DNA sequence
        StringBuilder dna = new StringBuilder();
        for (int i = 0; i < length; i++) {
            dna.append(nucleotides[rand.nextInt(4)]);  // Randomly select a nucleotide
        }

        //System.out.println("Generated DNA sequence: " + dna.toString());
        //System.out.println(dna.toString());
        return dna.toString();
    }

    // Function to apply single-point mutation
    public static String applySinglePointMutation(String dna) {
        if (dna.length() == 0) {
            return dna;  // Return the original sequence
        }
        Random rand = new Random();
        char[] nucleotides = {'A', 'C', 'T', 'G'};

        //System.out.println(dna.length());
        // Select a random position to mutate
        int mutationPosition = rand.nextInt(dna.length());
        //System.out.println(mutationPosition);
        // Find a new nucleotide different from the current one at the mutation position
        char currentBase = dna.charAt(mutationPosition);
        char newBase;
        do {
            newBase = nucleotides[rand.nextInt(4)];
        } while (newBase == currentBase);  // Ensure the new base is different

        // Create the mutated DNA sequence
        StringBuilder mutatedDna = new StringBuilder(dna);
        mutatedDna.setCharAt(mutationPosition, newBase);

        return mutatedDna.toString();
    }

    // Function to apply single-point crossover between two DNA sequences
    public static String[] applySinglePointCrossover(String dna1, String dna2) {
//System.out.println(dna1);
//                    System.out.println(dna2);
        Random rand = new Random();

        // Ensure both sequences have the same length before crossover
        int minLength = Math.min(dna1.length(), dna2.length());
        if (minLength < 2) {
            System.out.println("Single Crossover cannot happen. One or both sequences are too short. Trying again on another sequence");
            return new String[]{"", dna2};  // Return the original sequences
        }

        // Select a random crossover point
        int crossoverPoint = rand.nextInt(minLength);

        //System.out.println("CP: " + crossoverPoint);
        //System.out.println(dna1);
        //System.out.println(dna2);
        // Create offspring by combining the DNA from both parents at the crossover point
        String offspring1 = dna1.substring(0, crossoverPoint) + dna2.substring(crossoverPoint);
        String offspring2 = dna2.substring(0, crossoverPoint) + dna1.substring(crossoverPoint);

        //System.out.println(offspring1);
        //System.out.println(offspring2);
        // Return the two offspring
        return new String[]{offspring1, offspring2};
    }

    public static boolean isOverlapping(Set<String> bestSequences, String newSequence) {
        for (String existingSequence : bestSequences) {
            // Check if new sequence is a substring of an existing sequence, or vice versa
            if (existingSequence.contains(newSequence) || newSequence.contains(existingSequence)) {
                return true;  // Overlapping detected
            }
        }
        return false;  // No overlap
    }

    // Function to calculate compression size based on Shannon entropy
    public static double calculateBPBUsingShannonEntropy(Set<String> bestSequences, List<Integer> bestOccurrences) {
        // Total number of occurrences of all top subsequences
        int totalOccurrences = bestOccurrences.stream().mapToInt(Integer::intValue).sum();

        // Calculate the size of the encoded subsequences
        double totalEncodedSize = 0.0;

        // Loop through all best sequences
        for (int i = 0; i < bestSequences.size(); i++) {
            // Get the number of occurrences for the current sequence
            int occurrences = bestOccurrences.get(i);

            // Calculate the probability of the subsequence occurring
            double probability = (double) occurrences / totalOccurrences;

            // Calculate the bit length based on Shannon entropy (-log2(p))
            double bitLength = -Math.log(probability) / Math.log(2);
            //System.out.println("Bit length of " + bestOccurrences.get(i) + " is :" + bitLength);
            // Total encoded size is the number of bits assigned to this subsequence times its occurrences
            totalEncodedSize += occurrences * bitLength;
        }
        return totalEncodedSize;
    }

    // Function to apply multi-point mutation
    private static char mutateNucleotide(char nucleotide) {
        Random rand = new Random();
        char[] possibleMutations = {'A', 'T', 'C', 'G'};
        char newNucleotide;

        // Ensure the new nucleotide is different from the original one
        do {
            newNucleotide = possibleMutations[rand.nextInt(4)];  // Pick a random nucleotide
        } while (newNucleotide == nucleotide);

        return newNucleotide;
    }

}
