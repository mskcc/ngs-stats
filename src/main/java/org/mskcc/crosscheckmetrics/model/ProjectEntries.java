package org.mskcc.crosscheckmetrics.model;

import lombok.Getter;

import java.util.*;

/**
 * Class that formats database entries for API response
 */
@Getter
public class ProjectEntries {
    private String project;
    private List<CrosscheckMetrics> entries;
    private Set<String> results = new HashSet<>();  // Set of all results in the status of the projects
    private boolean pass;                           // Flag for unexpected/inconclusive results in project
    private String flag;

    /**
     * {
     *     project: "PROJECT_ID",
     *     entries: CrosscheckMetrics[],
     *     status: Set<UNEXPECTED_MATCH, UNEXPECTED_MISMATCH, INCONCLUSIVE, ...>
     * }
     *
     * @param project
     * @param entry
     */
    public ProjectEntries(String project, CrosscheckMetrics entry){
        this.project = project;
        this.entries = new ArrayList<>(Arrays.asList(entry));
        this.pass = true;
        updateStatus(entry);
    }

    /**
     * Adds entry to list of entries for the project
     *
     * @param entry
     */
    public void addEntry(CrosscheckMetrics entry){
        this.entries.add(entry);
        updateStatus(entry);
    }

    /**
     * Updates status of crosscheck_metrics results for project based on the results for each of the entries.
     */
    public void updateStatus(CrosscheckMetrics entry){
        final String result = entry.getResult();
        final boolean entryPasses = result.equals(FingerprintResult.EXPECTED_MATCH.toString()) || result.equals(FingerprintResult.EXPECTED_MISMATCH.toString());

        this.pass = this.pass && entryPasses;
        if(!entryPasses){
            if(this.flag == null){
                // Initialize message
                this.flag = String.format("Invalid results: %s", result);
            } else if (! this.results.contains(result)){
                // Concatenate if flag has already been initialized and there is a new invalid result
                this.flag = String.format("%s, %s", this.flag, result);
            }
        }
        this.results.add(result);
    }
}
