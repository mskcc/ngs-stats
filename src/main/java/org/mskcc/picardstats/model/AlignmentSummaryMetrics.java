package org.mskcc.picardstats.model;


import lombok.ToString;

import javax.persistence.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Entity
/*
https://github.com/broadinstitute/picard/blob/master/src/main/java/picard/analysis/AlignmentSummaryMetrics.java
 */
@ToString
public class AlignmentSummaryMetrics {
    @Id
    @Column(length = 150)
    public String filename;
    @Column(length = 32)
    public String md5RRS;

    public enum Category { UNPAIRED, FIRST_OF_PAIR, SECOND_OF_PAIR, PAIR }

    /**
     * One of either UNPAIRED (for a fragment run), FIRST_OF_PAIR when metrics are for only the
     * first read in a paired run, SECOND_OF_PAIR when the metrics are for only the second read
     * in a paired run or PAIR when the metrics are aggregated for both first and second reads
     * in a pair.
     */
    public Category CATEGORY;

    /**
     * The total number of reads including all PF and non-PF reads. When CATEGORY equals PAIR
     * this value will be 2x the number of clusters.
     */
    public long TOTAL_READS;

    /** The number of PF reads where PF is defined as passing Illumina's filter. */
    public long PF_READS;

    /** The fraction of reads that are PF (PF_READS / TOTAL_READS) */
    public double PCT_PF_READS;

    /**
     * The number of PF reads that are marked as noise reads.  A noise read is one which is composed
     * entirely of A bases and/or N bases. These reads are marked as they are usually artifactual and
     * are of no use in downstream analysis.
     */
    public long PF_NOISE_READS;

    /**
     * The number of PF reads that were aligned to the reference sequence. This includes reads that
     * aligned with low quality (i.e. their alignments are ambiguous).
     */
    public long PF_READS_ALIGNED;

    /**
     * The percentage of PF reads that aligned to the reference sequence. PF_READS_ALIGNED / PF_READS
     */
    public double PCT_PF_READS_ALIGNED;

    /**
     * The total number of aligned bases, in all mapped PF reads, that are aligned to the reference sequence.
     */
    public long PF_ALIGNED_BASES;

    /**
     * The number of PF reads that were aligned to the reference sequence with a mapping quality of
     * Q20 or higher signifying that the aligner estimates a 1/100 (or smaller) chance that the
     * alignment is wrong.
     */
    public long PF_HQ_ALIGNED_READS;

    /**
     * The number of bases aligned to the reference sequence in reads that were mapped at high
     * quality.  Will usually approximate PF_HQ_ALIGNED_READS * READ_LENGTH but may differ when
     * either mixed read lengths are present or many reads are aligned with gaps.
     */
    public long PF_HQ_ALIGNED_BASES;

    /**
     * The subset of PF_HQ_ALIGNED_BASES where the base call quality was Q20 or higher.
     */
    public long PF_HQ_ALIGNED_Q20_BASES;

    /**
     * The median number of mismatches versus the reference sequence in reads that were aligned
     * to the reference at high quality (i.e. PF_HQ_ALIGNED READS).
     */
    public double PF_HQ_MEDIAN_MISMATCHES;

    /**
     * The rate of bases mismatching the reference for all bases aligned to the reference sequence.
     */
    public double PF_MISMATCH_RATE;

    /**
     * The fraction of bases that mismatch the reference in PF HQ aligned reads.
     */
    public double PF_HQ_ERROR_RATE;

    /**
     * The number of insertion and deletion events per 100 aligned bases.  Uses the number of events
     * as the numerator, not the number of inserted or deleted bases.
     */
    public double PF_INDEL_RATE;

    /**
     * The mean read length of the set of reads examined.  When looking at the data for a single lane with
     * equal length reads this number is just the read length.  When looking at data for merged lanes with
     * differing read lengths this is the mean read length of all reads.
     */
    public double MEAN_READ_LENGTH;

    /**
     * The number of aligned reads whose mate pair was also aligned to the reference.
     */
    public long READS_ALIGNED_IN_PAIRS;

    /**
     * The fraction of aligned reads whose mate pair was also aligned to the reference.
     * READS_ALIGNED_IN_PAIRS / PF_READS_ALIGNED
     */
    public double PCT_READS_ALIGNED_IN_PAIRS;

    /**
     * The number of (primary) aligned reads that are **not** "properly" aligned in pairs (as per SAM flag 0x2).
     */
    public long PF_READS_IMPROPER_PAIRS;

    /**
     * The fraction of (primary) reads that are *not* "properly" aligned in pairs (as per SAM flag 0x2).
     * PF_READS_IMPROPER_PAIRS / PF_READS_ALIGNED
     */
    public double PCT_PF_READS_IMPROPER_PAIRS;

    /**
     * The number of instrument cycles in which 80% or more of base calls were no-calls.
     */
    public long BAD_CYCLES;

    /**
     * The number of PF reads aligned to the positive strand of the genome divided by the number of
     * PF reads aligned to the genome.
     */
    public double STRAND_BALANCE;

    /**
     * The fraction of reads that map outside of a maximum insert size (usually 100kb) or that have
     * the two ends mapping to different chromosomes.
     */
    public double PCT_CHIMERAS;

    /**
     * The fraction of PF reads that are unaligned and match to a known adapter sequence right from the
     * start of the read.
     */
    public double PCT_ADAPTER;

    /**
     * UNPAIRED_READS_EXAMINED is a column in the mark duplicates stats file which is costly to run, the same result can
     * be derived from the AM.txt file for PAIRED end runs by the caldulation: (TOTAL_READS - READS_ALIGNED_IN_PAIRS) / 2
     * For UNPAIRED this function just returns TOTAL_READS.
     * @return UNPAIRED_READS
     */
    public Long getUnpairedReads() {
        if (CATEGORY.equals(Category.PAIR)) {
            return (TOTAL_READS - READS_ALIGNED_IN_PAIRS) / 2L;
        } else if (CATEGORY.equals(Category.UNPAIRED)) {
            return TOTAL_READS;
        }
        return null;
    }

    public static List<AlignmentSummaryMetrics> readFile(File file, String md5RRS) throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        for (int i=0;i < 7;i++) {
            if (scan.hasNext())
                scan.nextLine();
            else {
                System.err.println("File is not in the correct format, ignoring:" + file.getName());
                return null;
            }
        }

        scan.useDelimiter("\t");
        List<AlignmentSummaryMetrics> amList = new ArrayList<>();
        int lineCount = 0;
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.contains("PAIR")) {
                AlignmentSummaryMetrics am = parseLine(line, file.getName(), md5RRS);
                amList.add(am);
            }
        }
        return amList;
    }

    //CATEGORY	BAD_CYCLES	STRAND_BALANCE	PCT_CHIMERAS	PCT_ADAPTER	SAMPLE	LIBRARY	READ_GROUP
    //FIRST_OF_PAIR	99865176	99865176	1	0	99541201	0.996756	9944286068	90230194	9030494459	8874341128	0	0.002737	0.002499	0.000108	101	99261598	0.997191	00.500459	0.009383	0.000001
    protected static AlignmentSummaryMetrics parseLine(String line, String filename, String md5RRS) {
        String [] parts = line.split("\t");

        AlignmentSummaryMetrics am = new AlignmentSummaryMetrics();
        am.filename = filename;
        am.md5RRS = md5RRS;
        am.CATEGORY = Category.valueOf(parts[0]);
        am.TOTAL_READS = Long.parseLong(parts[1]);
        am.PF_READS = Long.parseLong(parts[2]);
        am.PCT_PF_READS = Double.parseDouble(parts[3]);
        am.PF_NOISE_READS = Long.parseLong(parts[4]);
        am.PF_READS_ALIGNED = Long.parseLong(parts[5]);
        am.PCT_PF_READS_ALIGNED = Double.parseDouble(parts[6]);
        am.PF_ALIGNED_BASES = Long.parseLong(parts[7]);
        am.PF_HQ_ALIGNED_READS = Long.parseLong(parts[8]);
        am.PF_HQ_ALIGNED_BASES = Long.parseLong(parts[9]);
        am.PF_HQ_ALIGNED_Q20_BASES = Long.parseLong(parts[10]);
        am.PF_HQ_MEDIAN_MISMATCHES = Double.parseDouble(parts[11]);
        am.PF_MISMATCH_RATE = Double.parseDouble(parts[12]);
        am.PF_HQ_ERROR_RATE = Double.parseDouble(parts[13]);
        am.PF_INDEL_RATE = Double.parseDouble(parts[14]);
        am.MEAN_READ_LENGTH = Double.parseDouble(parts[15]);
        am.READS_ALIGNED_IN_PAIRS = Long.parseLong(parts[16]);
        am.PCT_READS_ALIGNED_IN_PAIRS = Double.parseDouble(parts[17]);
        am.BAD_CYCLES = Long.parseLong(parts[18]);
        am.STRAND_BALANCE = Double.parseDouble(parts[19]);
        am.PCT_CHIMERAS = Double.parseDouble(parts[20]);
        am.PCT_ADAPTER = Double.parseDouble(parts[21]);

        return am;
    }
}