# Global-Alignment-Algorithm

Objective:

  - Program read two sequences from a single input file in FASTA format
  - The program should output the highest-scoring alignment along with the corresponding score.
  - The alignment should be shown as two sequences one above the other, as shown below, where in between the sequences there are the match-indicators, where  "|" indicates matched positions, ";" indicates mismatched positions, and "-" indicates a gap in one of the sequences.
  
  Seq 1    ACGTTTTGAAAGGTTATC ATGTAGCATGCATCAGTATCAGTACTTCATT
  
           ||||||||||||;|||||-|||||||||||||||||||||||||;|;-||
           
  Seq 2    ACGTTTTGAAAGTTTATCCATGTAGCATGCATCAGTATCAGTACCTT TT
