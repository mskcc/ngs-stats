package org.mskcc.picardstats.repository;

import org.mskcc.picardstats.model.PicardFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PicardFileRepository extends CrudRepository<PicardFile, String> {

    List<PicardFile> findByRunAndRequest(String run, String request);

    @Query(value = "SELECT DISTINCT FILETYPE FROM PICARDFILE WHERE RUN = :run AND REQUEST = :request", nativeQuery =
            true)
    List<String> findFiletypeByRunAndRequest(@Param("run") String run, @Param("request") String request);

    // Note that the MD file is not always generated for non pooled normals - 116k AM files vs 111k MD files
    @Query(value = "SELECT DISTINCT PF.RUN, PF.REQUEST, PF.SAMPLE, PF.REFERENCEGENOME, PF.MD5RRS, AM.PCT_ADAPTER, AM" +
            ".CATEGORY, MD.READ_PAIRS_EXAMINED, MD.UNMAPPED_READS, MD.PERCENT_DUPLICATION, WGS.MEAN_COVERAGE, WGS" +
            ".PCT_10X, WGS.PCT_30X, WGS.PCT_100X \n" +
            "FROM  PICARDFILE PF, ALIGNMENTSUMMARYMETRICS AM, DUPLICATIONMETRICS MD, WGSMETRICS WGS  \n" +
            "WHERE PF.MD5RRS = MD.MD5RRS AND PF.MD5RRS = AM.MD5RRS AND PF.MD5RRS = WGS.MD5RRS AND PF.RUN = :run AND " +
            "PF.REQUEST = 'POOLEDNORMALS'",
            nativeQuery = true)
    List<Object[]> findWGSStatsForPooledNormals(@Param("run") String run);

    @Query(value = "select " +
            "pf.filename," +
            "pf.fileImported," +
            "pf.fileType," +
            "pf.lastModified," +
            "pf.md5RRS," +
            "pf.parseOK," +
            "pf.referenceGenome," +
            "pf.request," +
            "pf.run," +
            "pf.sample," +
            "hm.filename," +
            "hm.AT_DROPOUT," +
            "hm.BAIT_DESIGN_EFFICIENCY," +
            "hm.BAIT_SET," +
            "hm.BAIT_TERRITORY," +
            "hm.FOLD_80_BASE_PENALTY," +
            "hm.FOLD_ENRICHMENT," +
            "hm.GC_DROPOUT," +
            "hm.GENOME_SIZE," +
            "hm.HET_SNP_Q," +
            "hm.HET_SNP_SENSITIVITY," +
            "hm.HS_LIBRARY_SIZE," +
            "hm.HS_PENALTY_100X," +
            "hm.HS_PENALTY_10X," +
            "hm.HS_PENALTY_20X," +
            "hm.HS_PENALTY_30X," +
            "hm.HS_PENALTY_40X," +
            "hm.HS_PENALTY_50X," +
            "hm.MAX_TARGET_COVERAGE," +
            "hm.MEAN_BAIT_COVERAGE," +
            "hm.MEAN_TARGET_COVERAGE," +
            "hm.MEDIAN_TARGET_COVERAGE," +
            "hm.NEAR_BAIT_BASES," +
            "hm.OFF_BAIT_BASES," +
            "hm.ON_BAIT_BASES," +
            "hm.ON_BAIT_VS_SELECTED," +
            "hm.ON_TARGET_BASES," +
            "hm.PCT_EXC_BASEQ," +
            "hm.PCT_EXC_DUPE," +
            "hm.PCT_EXC_MAPQ," +
            "hm.PCT_EXC_OFF_TARGET," +
            "hm.PCT_EXC_OVERLAP," +
            "hm.PCT_OFF_BAIT," +
            "hm.PCT_PF_READS," +
            "hm.PCT_PF_UQ_READS," +
            "hm.PCT_PF_UQ_READS_ALIGNED," +
            "hm.PCT_SELECTED_BASES," +
            "hm.PCT_TARGET_BASES_100X," +
            "hm.PCT_TARGET_BASES_10X," +
            "hm.PCT_TARGET_BASES_1X," +
            "hm.PCT_TARGET_BASES_20X," +
            "hm.PCT_TARGET_BASES_2X," +
            "hm.PCT_TARGET_BASES_30X," +
            "hm.PCT_TARGET_BASES_40X," +
            "hm.PCT_TARGET_BASES_50X," +
            "hm.PCT_USABLE_BASES_ON_BAIT," +
            "hm.PCT_USABLE_BASES_ON_TARGET," +
            "hm.PF_BASES_ALIGNED," +
            "hm.PF_READS," +
            "hm.PF_UNIQUE_READS," +
            "hm.PF_UQ_BASES_ALIGNED," +
            "hm.PF_UQ_READS_ALIGNED," +
            "hm.TARGET_TERRITORY," +
            "hm.TOTAL_READS," +
            "hm.ZERO_CVG_TARGETS_PCT," +
            "hm.md5RRS," +
            "am.filename," +
            "am.BAD_CYCLES,am.CATEGORY," +
            "am.MEAN_READ_LENGTH," +
            "am.PCT_ADAPTER," +
            "am.PCT_CHIMERAS," +
            "am.PCT_PF_READS," +
            "am.PCT_PF_READS_ALIGNED," +
            "am.PCT_PF_READS_IMPROPER_PAIRS," +
            "am.PCT_READS_ALIGNED_IN_PAIRS," +
            "am.PF_ALIGNED_BASES," +
            "am.PF_HQ_ALIGNED_BASES," +
            "am.PF_HQ_ALIGNED_Q20_BASES," +
            "am.PF_HQ_ALIGNED_READS," +
            "am.PF_HQ_ERROR_RATE," +
            "am.PF_HQ_MEDIAN_MISMATCHES," +
            "am.PF_INDEL_RATE," +
            "am.PF_MISMATCH_RATE," +
            "am.PF_NOISE_READS," +
            "am.PF_READS," +
            "am.PF_READS_ALIGNED," +
            "am.PF_READS_IMPROPER_PAIRS," +
            "am.READS_ALIGNED_IN_PAIRS," +
            "am.STRAND_BALANCE," +
            "am.TOTAL_READS," +
            "am.md5RRS," +
            "cm.filename," +
            "cm.ALT_NONOXO_BASES," +
            "cm.ALT_OXO_BASES," +
            "cm.CONTEXT," +
            "cm.C_REF_ALT_BASES," +
            "cm.C_REF_OXO_ERROR_RATE," +
            "cm.C_REF_OXO_Q," +
            "cm.C_REF_REF_BASES," +
            "cm.G_REF_ALT_BASES," +
            "cm.G_REF_OXO_ERROR_RATE," +
            "cm.G_REF_OXO_Q," +
            "cm.G_REF_REF_BASES," +
            "cm.LIBRARY," +
            "cm.OXIDATION_ERROR_RATE," +
            "cm.OXIDATION_Q," +
            "cm.REF_NONOXO_BASES," +
            "cm.REF_OXO_BASES," +
            "cm.REF_TOTAL_BASES," +
            "cm.SAMPLE_ALIAS," +
            "cm.TOTAL_BASES," +
            "cm.TOTAL_SITES," +
            "cm.md5RRS," +
            "dm.filename," +
            "dm.ESTIMATED_LIBRARY_SIZE," +
            "dm.LIBRARY," +
            "dm.PERCENT_DUPLICATION," +
            "dm.READ_PAIRS_EXAMINED," +
            "dm.READ_PAIR_DUPLICATES," +
            "dm.READ_PAIR_OPTICAL_DUPLICATES," +
            "dm.SECONDARY_OR_SUPPLEMENTARY_RDS," +
            "dm.UNMAPPED_READS," +
            "dm.UNPAIRED_READS_EXAMINED," +
            "dm.UNPAIRED_READ_DUPLICATES," +
            "qm.filename," +
            "qm.md5RRS," +
            "qm.mskQ," +
            "rm.filename," +
            "rm.CODING_BASES," +
            "rm.CORRECT_STRAND_READS," +
            "rm.IGNORED_READS," +
            "rm.INCORRECT_STRAND_READS," +
            "rm.INTERGENIC_BASES," +
            "rm.INTRONIC_BASES," +
            "rm.MEDIAN_3PRIME_BIAS," +
            "rm.MEDIAN_5PRIME_BIAS," +
            "rm.MEDIAN_5PRIME_TO_3PRIME_BIAS," +
            "rm.MEDIAN_CV_COVERAGE," +
            "rm.NUM_R1_TRANSCRIPT_STRAND_READS," +
            "rm.NUM_R2_TRANSCRIPT_STRAND_READS," +
            "rm.NUM_UNEXPLAINED_READS," +
            "rm.PCT_CODING_BASES," +
            "rm.PCT_CORRECT_STRAND_READS," +
            "rm.PCT_INTERGENIC_BASES," +
            "rm.PCT_INTRONIC_BASES," +
            "rm.PCT_MRNA_BASES," +
            "rm.PCT_R1_TRANSCRIPT_STRAND_READS," +
            "rm.PCT_R2_TRANSCRIPT_STRAND_READS," +
            "rm.PCT_RIBOSOMAL_BASES," +
            "rm.PCT_USABLE_BASES," +
            "rm.PCT_UTR_BASES," +
            "rm.PF_ALIGNED_BASES," +
            "rm.PF_BASES," +
            "rm.RIBOSOMAL_BASES," +
            "rm.UTR_BASES," +
            "rm.md5RRS," +
            "wm.filename," +
            "wm.GENOME_TERRITORY," +
            "wm.HET_SNP_Q," +
            "wm.HET_SNP_SENSITIVITY," +
            "wm.MAD_COVERAGE," +
            "wm.MEAN_COVERAGE," +
            "wm.MEDIAN_COVERAGE," +
            "wm.PCT_100X," +
            "wm.PCT_10X," +
            "wm.PCT_15X," +
            "wm.PCT_1X," +
            "wm.PCT_20X," +
            "wm.PCT_25X," +
            "wm.PCT_30X," +
            "wm.PCT_40X," +
            "wm.PCT_50X," +
            "wm.PCT_5X," +
            "wm.PCT_60X," +
            "wm.PCT_70X," +
            "wm.PCT_80X," +
            "wm.PCT_90X," +
            "wm.PCT_EXC_BASEQ," +
            "wm.PCT_EXC_CAPPED," +
            "wm.PCT_EXC_DUPE," +
            "wm.PCT_EXC_MAPQ," +
            "wm.PCT_EXC_OVERLAP," +
            "wm.PCT_EXC_TOTAL," +
            "wm.PCT_EXC_UNPAIRED," +
            "wm.SD_COVERAGE," +
            "wm.md5RRS" +
            " from picardfile as pf" +
            " left outer join alignmentsummarymetrics as am on am.filename = pf.filename \n" +
            " left outer join cpcgmetrics cm on cm.filename = pf.filename\n" +
            " left outer join duplicationmetrics dm on dm.filename = pf.filename\n" +
            " left outer join hsmetrics hm on hm.filename = pf.filename\n" +
            " left outer join qmetric qm on qm.filename = pf.filename\n" +
            " left outer join rnaseqmetrics rm on rm.filename = pf.filename\n" +
            " left outer join wgsmetrics wm on wm.filename = pf.filename\n" +
            " where run = :run", nativeQuery = true)
    List<PicardFile> findStatsByRun(@Param("run") String run);


    @Query(value = "select " +
            "pf.filename," +
            "pf.fileImported," +
            "pf.fileType," +
            "pf.lastModified," +
            "pf.md5RRS," +
            "pf.parseOK," +
            "pf.referenceGenome," +
            "pf.request," +
            "pf.run," +
            "pf.sample," +
            "hm.filename," +
            "hm.AT_DROPOUT," +
            "hm.BAIT_DESIGN_EFFICIENCY," +
            "hm.BAIT_SET," +
            "hm.BAIT_TERRITORY," +
            "hm.FOLD_80_BASE_PENALTY," +
            "hm.FOLD_ENRICHMENT," +
            "hm.GC_DROPOUT," +
            "hm.GENOME_SIZE," +
            "hm.HET_SNP_Q," +
            "hm.HET_SNP_SENSITIVITY," +
            "hm.HS_LIBRARY_SIZE," +
            "hm.HS_PENALTY_100X," +
            "hm.HS_PENALTY_10X," +
            "hm.HS_PENALTY_20X," +
            "hm.HS_PENALTY_30X," +
            "hm.HS_PENALTY_40X," +
            "hm.HS_PENALTY_50X," +
            "hm.MAX_TARGET_COVERAGE," +
            "hm.MEAN_BAIT_COVERAGE," +
            "hm.MEAN_TARGET_COVERAGE," +
            "hm.MEDIAN_TARGET_COVERAGE," +
            "hm.NEAR_BAIT_BASES," +
            "hm.OFF_BAIT_BASES," +
            "hm.ON_BAIT_BASES," +
            "hm.ON_BAIT_VS_SELECTED," +
            "hm.ON_TARGET_BASES," +
            "hm.PCT_EXC_BASEQ," +
            "hm.PCT_EXC_DUPE," +
            "hm.PCT_EXC_MAPQ," +
            "hm.PCT_EXC_OFF_TARGET," +
            "hm.PCT_EXC_OVERLAP," +
            "hm.PCT_OFF_BAIT," +
            "hm.PCT_PF_READS," +
            "hm.PCT_PF_UQ_READS," +
            "hm.PCT_PF_UQ_READS_ALIGNED," +
            "hm.PCT_SELECTED_BASES," +
            "hm.PCT_TARGET_BASES_100X," +
            "hm.PCT_TARGET_BASES_10X," +
            "hm.PCT_TARGET_BASES_1X," +
            "hm.PCT_TARGET_BASES_20X," +
            "hm.PCT_TARGET_BASES_2X," +
            "hm.PCT_TARGET_BASES_30X," +
            "hm.PCT_TARGET_BASES_40X," +
            "hm.PCT_TARGET_BASES_50X," +
            "hm.PCT_USABLE_BASES_ON_BAIT," +
            "hm.PCT_USABLE_BASES_ON_TARGET," +
            "hm.PF_BASES_ALIGNED," +
            "hm.PF_READS," +
            "hm.PF_UNIQUE_READS," +
            "hm.PF_UQ_BASES_ALIGNED," +
            "hm.PF_UQ_READS_ALIGNED," +
            "hm.TARGET_TERRITORY," +
            "hm.TOTAL_READS," +
            "hm.ZERO_CVG_TARGETS_PCT," +
            "hm.md5RRS," +
            "am.filename," +
            "am.BAD_CYCLES,am.CATEGORY," +
            "am.MEAN_READ_LENGTH," +
            "am.PCT_ADAPTER," +
            "am.PCT_CHIMERAS," +
            "am.PCT_PF_READS," +
            "am.PCT_PF_READS_ALIGNED," +
            "am.PCT_PF_READS_IMPROPER_PAIRS," +
            "am.PCT_READS_ALIGNED_IN_PAIRS," +
            "am.PF_ALIGNED_BASES," +
            "am.PF_HQ_ALIGNED_BASES," +
            "am.PF_HQ_ALIGNED_Q20_BASES," +
            "am.PF_HQ_ALIGNED_READS," +
            "am.PF_HQ_ERROR_RATE," +
            "am.PF_HQ_MEDIAN_MISMATCHES," +
            "am.PF_INDEL_RATE," +
            "am.PF_MISMATCH_RATE," +
            "am.PF_NOISE_READS," +
            "am.PF_READS," +
            "am.PF_READS_ALIGNED," +
            "am.PF_READS_IMPROPER_PAIRS," +
            "am.READS_ALIGNED_IN_PAIRS," +
            "am.STRAND_BALANCE," +
            "am.TOTAL_READS," +
            "am.md5RRS," +
            "cm.filename," +
            "cm.ALT_NONOXO_BASES," +
            "cm.ALT_OXO_BASES," +
            "cm.CONTEXT," +
            "cm.C_REF_ALT_BASES," +
            "cm.C_REF_OXO_ERROR_RATE," +
            "cm.C_REF_OXO_Q," +
            "cm.C_REF_REF_BASES," +
            "cm.G_REF_ALT_BASES," +
            "cm.G_REF_OXO_ERROR_RATE," +
            "cm.G_REF_OXO_Q," +
            "cm.G_REF_REF_BASES," +
            "cm.LIBRARY," +
            "cm.OXIDATION_ERROR_RATE," +
            "cm.OXIDATION_Q," +
            "cm.REF_NONOXO_BASES," +
            "cm.REF_OXO_BASES," +
            "cm.REF_TOTAL_BASES," +
            "cm.SAMPLE_ALIAS," +
            "cm.TOTAL_BASES," +
            "cm.TOTAL_SITES," +
            "cm.md5RRS," +
            "dm.filename," +
            "dm.ESTIMATED_LIBRARY_SIZE," +
            "dm.LIBRARY," +
            "dm.PERCENT_DUPLICATION," +
            "dm.READ_PAIRS_EXAMINED," +
            "dm.READ_PAIR_DUPLICATES," +
            "dm.READ_PAIR_OPTICAL_DUPLICATES," +
            "dm.SECONDARY_OR_SUPPLEMENTARY_RDS," +
            "dm.UNMAPPED_READS," +
            "dm.UNPAIRED_READS_EXAMINED," +
            "dm.UNPAIRED_READ_DUPLICATES," +
            "qm.filename," +
            "qm.md5RRS," +
            "qm.mskQ," +
            "rm.filename," +
            "rm.CODING_BASES," +
            "rm.CORRECT_STRAND_READS," +
            "rm.IGNORED_READS," +
            "rm.INCORRECT_STRAND_READS," +
            "rm.INTERGENIC_BASES," +
            "rm.INTRONIC_BASES," +
            "rm.MEDIAN_3PRIME_BIAS," +
            "rm.MEDIAN_5PRIME_BIAS," +
            "rm.MEDIAN_5PRIME_TO_3PRIME_BIAS," +
            "rm.MEDIAN_CV_COVERAGE," +
            "rm.NUM_R1_TRANSCRIPT_STRAND_READS," +
            "rm.NUM_R2_TRANSCRIPT_STRAND_READS," +
            "rm.NUM_UNEXPLAINED_READS," +
            "rm.PCT_CODING_BASES," +
            "rm.PCT_CORRECT_STRAND_READS," +
            "rm.PCT_INTERGENIC_BASES," +
            "rm.PCT_INTRONIC_BASES," +
            "rm.PCT_MRNA_BASES," +
            "rm.PCT_R1_TRANSCRIPT_STRAND_READS," +
            "rm.PCT_R2_TRANSCRIPT_STRAND_READS," +
            "rm.PCT_RIBOSOMAL_BASES," +
            "rm.PCT_USABLE_BASES," +
            "rm.PCT_UTR_BASES," +
            "rm.PF_ALIGNED_BASES," +
            "rm.PF_BASES," +
            "rm.RIBOSOMAL_BASES," +
            "rm.UTR_BASES," +
            "rm.md5RRS," +
            "wm.filename," +
            "wm.GENOME_TERRITORY," +
            "wm.HET_SNP_Q," +
            "wm.HET_SNP_SENSITIVITY," +
            "wm.MAD_COVERAGE," +
            "wm.MEAN_COVERAGE," +
            "wm.MEDIAN_COVERAGE," +
            "wm.PCT_100X," +
            "wm.PCT_10X," +
            "wm.PCT_15X," +
            "wm.PCT_1X," +
            "wm.PCT_20X," +
            "wm.PCT_25X," +
            "wm.PCT_30X," +
            "wm.PCT_40X," +
            "wm.PCT_50X," +
            "wm.PCT_5X," +
            "wm.PCT_60X," +
            "wm.PCT_70X," +
            "wm.PCT_80X," +
            "wm.PCT_90X," +
            "wm.PCT_EXC_BASEQ," +
            "wm.PCT_EXC_CAPPED," +
            "wm.PCT_EXC_DUPE," +
            "wm.PCT_EXC_MAPQ," +
            "wm.PCT_EXC_OVERLAP," +
            "wm.PCT_EXC_TOTAL," +
            "wm.PCT_EXC_UNPAIRED," +
            "wm.SD_COVERAGE," +
            "wm.md5RRS" +
            " from picardfile as pf" +
            " left outer join alignmentsummarymetrics as am on am.filename = pf.filename \n" +
            " left outer join cpcgmetrics cm on cm.filename = pf.filename\n" +
            " left outer join duplicationmetrics dm on dm.filename = pf.filename\n" +
            " left outer join hsmetrics hm on hm.filename = pf.filename\n" +
            " left outer join qmetric qm on qm.filename = pf.filename\n" +
            " left outer join rnaseqmetrics rm on rm.filename = pf.filename\n" +
            " left outer join wgsmetrics wm on wm.filename = pf.filename\n" +
            " where pf.lastModified > :runDate", nativeQuery = true)
    List<PicardFile> findStatsByRunDate(@Param("runDate") String runDate);
}


