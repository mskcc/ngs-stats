package org.mskcc.permissions;

import lombok.*;
import org.mskcc.permissions.model.dto.RequestPermissions;
import org.mskcc.permissions.repository.LabMemberRepository;
import org.mskcc.sequencer.SequencerDoneController;
import org.mskcc.sequencer.model.ArchivedFastq;
import org.mskcc.sequencer.repository.ArchivedFastqRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("permissions")
public class DeliveryPermissionsController {
    private static Logger log = LoggerFactory.getLogger(DeliveryPermissionsController.class);

    @Value("${lims.rest.url}")
    private String limsUrl;
    @Value("${lims.rest.username}")
    private String limsUser;
    @Value("${lims.rest.password}")
    private String limsPass;

    @Autowired
    private LabMemberRepository labMemberRepository;

    @Autowired
    private ArchivedFastqRepository archivedFastqRepository;

    protected static final String CMO_GROUP = "cmoigo";
    protected static final String BIC_GROUP = "bicigo";
    protected static final String ISABL_GROUP = "isabl";
    protected static final String ISABL_EMAIL = "zzPDL_SKI_ISABL@mskcc.org";

    @GetMapping(value = "/getRequestPermissions/{request}")
    public RequestPermissions getRequestPermissions(@PathVariable String request) {
        log.info("Querying request permissions for:" + request);
        if (request.length() < 5 || request.length() > 10)
            return null;

        // ask LIMS which lab and which request groups should have access ie cmoigo, bicigo, isabl
        RequestPermissionsLIMS lims = queryLIMSforLabNameAndGroupAccess(request);
        log.info("LIMS response:" + lims);

        // query lab members list from db
        List<String> labMembers = labMemberRepository.findByPi(lims.getLabName());

        List<String> groupReadAccess = buildGroupAccessList(lims.getIsCmoRequest(), lims.getIsBicRequest(), lims.dataAccessEmails);

        List<ArchivedFastq> fastqs = archivedFastqRepository.findByProject(request);
        if (request.startsWith("0")) {
            String requestFourDigit = getFourDigitRequest(request);
            List<ArchivedFastq> moreFastqs = archivedFastqRepository.findByProject(requestFourDigit);
            if (moreFastqs != null && moreFastqs.size() > 0)
                fastqs.addAll(moreFastqs);
        }
        List<String> fastqPaths = ArchivedFastq.toFastqPathOnly(fastqs);

        return new RequestPermissions(lims.getLabName(), labMembers, request, groupReadAccess, fastqPaths);
    }

    protected static String getFourDigitRequest(String request) {
        if (request.startsWith("0"))
            return request.substring(1);
        return request;
    }

    protected static List<String> buildGroupAccessList(Boolean isCmoRequest, Boolean bicAnalysis, String dataAccessEmails) {
        List<String> groups = new ArrayList<>();
        if (isCmoRequest)
            groups.add(CMO_GROUP);
        if (bicAnalysis)
            groups.add(BIC_GROUP);
        if (dataAccessEmails.contains(ISABL_EMAIL))
            groups.add(ISABL_GROUP);

        return groups;
    }

    protected RequestPermissionsLIMS queryLIMSforLabNameAndGroupAccess(String request) {
        String url = limsUrl + "/getRequestPermissions?request=" + request;
        log.info("Querying LIMS URL: " + url);

        RestTemplate rt = limsRestTemplate();
        ResponseEntity<RequestPermissionsLIMS> response = rt.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RequestPermissionsLIMS>(){});
        RequestPermissionsLIMS r = response.getBody();
        return r;
    }
    public RestTemplate limsRestTemplate() {
        RestTemplate restTemplate = SequencerDoneController.getInsecureRestTemplate();
        addBasicAuth(restTemplate, limsUser, limsPass);
        return restTemplate;
    }
    private void addBasicAuth(RestTemplate restTemplate, String username, String password) {
        List<ClientHttpRequestInterceptor> interceptors = Collections.singletonList(new BasicAuthorizationInterceptor(username, password));
        restTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(restTemplate.getRequestFactory(),
                interceptors));
    }

    /**
     * Class returned from the LIMS endpoint.
     */
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestPermissionsLIMS {
        private String requestId;
        private String labName;
        private String labHeadEmail;
        private Boolean isCmoRequest;
        private Boolean isBicRequest;
        private String dataAccessEmails;
    }
}