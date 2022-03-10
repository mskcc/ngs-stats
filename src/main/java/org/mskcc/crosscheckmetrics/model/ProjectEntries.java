package org.mskcc.crosscheckmetrics.model;

import lombok.Getter;

import java.util.*;

/**
 * Class that formats database entries for API response
 */
@Getter
public class ProjectEntries {
    private String project;
    private List<Map<String,Object>> entries;       // Processed DB entries to return in service call
    private Set<String> results = new HashSet<>();  // Set of all results in the status of the projects
    private Boolean pass;                           // Flag for unexpected/inconclusive results in project
    private String flag;

    public enum ProjectStatus { PASS, WARNING, FAIL }

    // Any entry in the project w/ a result not in passingResults is considered failed
    private static Set<String> passingResults = new HashSet<>();
    static {
        passingResults.add(FingerprintResult.EXPECTED_MATCH.toString());
        passingResults.add(FingerprintResult.EXPECTED_MISMATCH.toString());
        passingResults.add(FingerprintResult.INCONCLUSIVE.toString());
    }

    /**
     * Model for JSON response object for the project w/ all entries for that project
     *         {
     *             "project": String
     *             "entries": Map[],
     *             "results": String[],
     *             "pass": boolean,
     *             "flag": String
     *        }
     * @param project, String
     * @param entry, CrosscheckMetrics
     */
    public ProjectEntries(String project, CrosscheckMetrics entry){
        this.project = project;
        this.entries = new ArrayList<>();

        // Initialized to passing w/ blank flag
        this.pass = true;
        this.flag = ProjectStatus.PASS.toString();

        addEntry(entry);
    }

    /**
     * Adds entry to list of entries for the project
     *
     * @param entry
     */
    public void addEntry(CrosscheckMetrics entry){
        final Map<String,Object> processed = processEntry(entry);
        this.entries.add(processed);
        updateStatus(entry);
    }

    /**
     * Updates status of crosscheck_metrics results for project based on the results for each of the entries.
     */
    public void updateStatus(CrosscheckMetrics entry){
        final String result = entry.getResult();
        final boolean entryPasses = passingResults.contains(result);

        // todo - this deserves a test
        this.pass = this.pass && entryPasses;
        if(this.pass){
            if(result.equals(FingerprintResult.INCONCLUSIVE.toString())){
                this.flag = ProjectStatus.WARNING.toString();
            }
        } else {
            this.flag = ProjectStatus.FAIL.toString();
        }

        this.results.add(result);
    }

    /**
     * Parses out values into cleaner API response
     *
     * @param entry, CrosscheckMetrics - DB entry
     * @return, Map to be converted into JSON object
     */
    private Map<String,Object> processEntry(CrosscheckMetrics entry){
        final Map<String, Object> processed = new HashMap<>();
        processed.put("lodScore", entry.getLodScore());
        processed.put("lodScoreTumorNormal",entry.getLodScoreTumorNormal());
        processed.put("lodScoreNormalTumor",entry.getLodScoreNormalTumor());
        processed.put("result",entry.getResult());
        processed.put("igoIdA",entry.getCrosscheckMetricsId().getIgoIdA());
        processed.put("igoIdB",entry.getCrosscheckMetricsId().getIgoIdB());
        processed.put("project", entry.getCrosscheckMetricsId().getProject());
        processed.put("tumorNormalA", entry.getTumorNormalA());
        processed.put("tumorNormalB", entry.getTumorNormalB());
        processed.put("patientIdA", entry.getPatientIdA());
        processed.put("patientIdB", entry.getPatientIdB());

        return processed;
    }
}
