package org.mskcc.picardstats;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.mskcc.picardstats.model.AlignmentSummaryMetrics;
import org.mskcc.picardstats.model.DuplicationMetrics;
import org.mskcc.picardstats.model.HsMetrics;
import org.mskcc.picardstats.model.PicardFile;

import javax.persistence.Column;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PicardToExcel {

    private static final String[] titlesMD = {"Run", "Request", "Sample", "ReferenceGenome", "Last Modified Date",
            "LIBRARY", "UNPAIRED_READS_EXAMINED", "READ_PAIRS_EXAMINED", "SECONDARY_OR_SUPPLEMENTARY_RDS",
            "UNMAPPED_READS", "UNPAIRED_READ_DUPLICATES", "READ_PAIR_DUPLICATES", "READ_PAIR_OPTICAL_DUPLICATES",
            "PERCENT_DUPLICATION", "ESTIMATED_LIBRARY_SIZE"};

    private static final String[] titlesAM = {"Run", "Request", "Sample", "ReferenceGenome", "Last Modified Date",
            "CATEGORY", "TOTAL_READS", "PF_READS", "PCT_PF_READS", "PF_NOISE_READS", "PF_READS_ALIGNED", "PCT_PF_READS_ALIGNED",
            "PF_ALIGNED_BASES", "PF_HQ_ALIGNED_READS", "PF_HQ_ALIGNED_BASES", "PF_HQ_ALIGNED_Q20_BASES",
            "PF_HQ_MEDIAN_MISMATCHES", "PF_MISMATCH_RATE", "PF_HQ_ERROR_RATE", "PF_INDEL_RATE", "MEAN_READ_LENGTH",
            "READS_ALIGNED_IN_PAIRS", "PCT_READS_ALIGNED_IN_PAIRS", "BAD_CYCLES", "STRAND_BALANCE", "PCT_CHIMERAS", "PCT_ADAPTER"};

    private static final String[] titlesHS = {"Run", "Request", "Sample", "ReferenceGenome", "Last Modified Date",
            "BAIT_SET", "GENOME_SIZE","BAIT_TERRITORY","TARGET_TERRITORY", "BAIT_DESIGN_EFFICIENCY","TOTAL_READS",
            "PF_READS","PF_UNIQUE_READS","PCT_PF_READS","PCT_PF_UQ_READS", "PF_UQ_READS_ALIGNED",
            "PCT_PF_UQ_READS_ALIGNED","PF_BASES_ALIGNED","PF_UQ_BASES_ALIGNED","ON_BAIT_BASES", "NEAR_BAIT_BASES",
            "OFF_BAIT_BASES","ON_TARGET_BASES","PCT_SELECTED_BASES","PCT_OFF_BAIT", "ON_BAIT_VS_SELECTED",
            "MEAN_BAIT_COVERAGE","MEAN_TARGET_COVERAGE","MEDIAN_TARGET_COVERAGE", "MAX_TARGET_COVERAGE",
            "PCT_USABLE_BASES_ON_BAIT","PCT_USABLE_BASES_ON_TARGET","FOLD_ENRICHMENT", "ZERO_CVG_TARGETS_PCT",
            "PCT_EXC_DUPE","PCT_EXC_MAPQ","PCT_EXC_BASEQ","PCT_EXC_OVERLAP","PCT_EXC_OFF_TARGET", "FOLD_80_BASE_PENALTY",
            "PCT_TARGET_BASES_1X","PCT_TARGET_BASES_2X","PCT_TARGET_BASES_10X", "PCT_TARGET_BASES_20X",
            "PCT_TARGET_BASES_30X","PCT_TARGET_BASES_40X","PCT_TARGET_BASES_50X", "PCT_TARGET_BASES_100X",
            "HS_LIBRARY_SIZE","HS_PENALTY_10X","HS_PENALTY_20X","HS_PENALTY_30X", "HS_PENALTY_40X","HS_PENALTY_50X",
            "HS_PENALTY_100X","AT_DROPOUT","GC_DROPOUT","HET_SNP_SENSITIVITY", "HET_SNP_Q"};


    protected static void writeProjectExcel(File fileName, List<PicardFile> picardFiles) throws FileNotFoundException, IOException {
        Workbook wb = new HSSFWorkbook();
        Map<String, CellStyle> styles = createStyles(wb);
        CreationHelper createHelper = wb.getCreationHelper();

        Sheet mdMetrics = wb.createSheet("MD Metrics");
        Row mdHeaderRow = mdMetrics.createRow(0);
        for (int i = 0; i < titlesMD.length; i++) {
            Cell cell = mdHeaderRow.createCell(i);
            cell.setCellValue(titlesMD[i]);
            cell.setCellStyle(styles.get("header"));
        }

        Sheet amMetrics = wb.createSheet("AM Metrics (Paired)");
        Row amHeaderRow = amMetrics.createRow(0);
        for (int i = 0; i < titlesAM.length; i++) {
            Cell cell = amHeaderRow.createCell(i);
            cell.setCellValue(titlesAM[i]);
            cell.setCellStyle(styles.get("header"));
            amMetrics.autoSizeColumn(i);
        }

        Sheet hsMetrics = wb.createSheet("HS Metrics");
        Row hsHeaderRow = hsMetrics.createRow(0);
        for (int i = 0; i < titlesHS.length; i++) {
            Cell cell = hsHeaderRow.createCell(i);
            cell.setCellValue(titlesHS[i]);
            cell.setCellStyle(styles.get("header"));
            hsMetrics.autoSizeColumn(i);
        }

        int mdRow = 1;
        int amRow = 1;
        int hsRow = 1;
        for (PicardFile f: picardFiles) {
            if (!f.isParseOK())
                continue;
            if ("MD".equals(f.getFileType())) {
                System.out.println("Creating additional MD row." + mdRow);
                Row row = mdMetrics.createRow(mdRow++);
                writeRunRequestSampleGenomeDate(wb, createHelper, f, row);

                Cell cell;
                DuplicationMetrics dm = f.getDuplicationMetrics();
                cell = row.createCell(5);
                cell.setCellValue(dm.LIBRARY);
                cell = row.createCell(6);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.UNPAIRED_READS_EXAMINED);
                cell = row.createCell(7);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.READ_PAIRS_EXAMINED);
                cell = row.createCell(8);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.SECONDARY_OR_SUPPLEMENTARY_RDS);
                cell = row.createCell(9);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.UNMAPPED_READS);
                cell = row.createCell(10);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.UNPAIRED_READ_DUPLICATES);
                cell = row.createCell(11);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.READ_PAIR_DUPLICATES);
                cell = row.createCell(12);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.READ_PAIR_OPTICAL_DUPLICATES);
                cell = row.createCell(13);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(dm.PERCENT_DUPLICATION);
                cell = row.createCell(14);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(dm.ESTIMATED_LIBRARY_SIZE);
            } else if ("AM".equals(f.getFileType())) {
                Row row = amMetrics.createRow(amRow++);
                writeRunRequestSampleGenomeDate(wb, createHelper, f, row);

                Cell cell;
                AlignmentSummaryMetrics am = f.getAlignmentSummaryMetrics();
                int i=5;
                cell = row.createCell(i++);
                cell.setCellValue(am.CATEGORY.name());
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.TOTAL_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(am.PCT_PF_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_NOISE_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_READS_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(am.PCT_PF_READS_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_ALIGNED_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_HQ_ALIGNED_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_HQ_ALIGNED_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_HQ_ALIGNED_Q20_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_HQ_MEDIAN_MISMATCHES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_MISMATCH_RATE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_HQ_ERROR_RATE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.PF_INDEL_RATE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.MEAN_READ_LENGTH);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.READS_ALIGNED_IN_PAIRS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(am.PCT_READS_ALIGNED_IN_PAIRS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.BAD_CYCLES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(am.STRAND_BALANCE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(am.PCT_CHIMERAS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(am.PCT_ADAPTER);
            } else if ("HS".equals(f.getFileType())) {
                Row row = hsMetrics.createRow(hsRow++);
                writeRunRequestSampleGenomeDate(wb, createHelper, f, row);

                Cell cell;
                System.out.println("Getting: " + f.getFilename());
                HsMetrics hs = f.getHsMetrics();
                int i=5;
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("cell"));
                if (hs.BAIT_SET != null)
                    cell.setCellValue(hs.BAIT_SET);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.GENOME_SIZE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.BAIT_TERRITORY);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.TARGET_TERRITORY);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.BAIT_DESIGN_EFFICIENCY);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.TOTAL_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.PF_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.PF_UNIQUE_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_PF_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_PF_UQ_READS);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.PF_UQ_READS_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_PF_UQ_READS_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.PF_BASES_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.PF_UQ_BASES_ALIGNED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.ON_BAIT_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.NEAR_BAIT_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.OFF_BAIT_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.ON_TARGET_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_SELECTED_BASES);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_OFF_BAIT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.ON_BAIT_VS_SELECTED);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.MEAN_BAIT_COVERAGE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.MEAN_TARGET_COVERAGE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.MEDIAN_TARGET_COVERAGE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                cell.setCellValue(hs.MAX_TARGET_COVERAGE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_USABLE_BASES_ON_BAIT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_USABLE_BASES_ON_TARGET);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.FOLD_ENRICHMENT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.ZERO_CVG_TARGETS_PCT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_EXC_DUPE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_EXC_MAPQ);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_EXC_BASEQ);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_EXC_OVERLAP);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_EXC_OFF_TARGET);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.FOLD_80_BASE_PENALTY);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_1X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_2X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_10X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_20X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_30X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_40X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_50X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.PCT_TARGET_BASES_100X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("number"));
                if (hs.HS_LIBRARY_SIZE != null)
                    cell.setCellValue(hs.HS_LIBRARY_SIZE);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_10X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_20X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_30X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_40X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_50X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HS_PENALTY_100X);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.AT_DROPOUT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.GC_DROPOUT);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HET_SNP_SENSITIVITY);
                cell = row.createCell(i++);
                cell.setCellStyle(styles.get("percent"));
                cell.setCellValue(hs.HET_SNP_Q);
            }
        }

        for (int i=0; i < titlesMD.length; i++) {
            if (i != 5) // LIBRARY can be very long
                mdMetrics.autoSizeColumn(i);
        }

        FileOutputStream out = new FileOutputStream(fileName);
        wb.write(out);
        out.close();
    }

    private static void writeRunRequestSampleGenomeDate(Workbook wb, CreationHelper createHelper, PicardFile f, Row row) {
        row.createCell(0).setCellValue(f.getRun());
        row.createCell(1).setCellValue(f.getRequest());
        row.createCell(2).setCellValue(f.getSample());
        row.createCell(3).setCellValue(f.getReferenceGenome());
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy/mm/dd h:mm"));
        Cell cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(f.getLastModified());
    }

    /**
     * Create a library of cell styles
     */
    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0"));
        styles.put("number", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.000"));
        styles.put("percent", style);

        return styles;
    }
}
