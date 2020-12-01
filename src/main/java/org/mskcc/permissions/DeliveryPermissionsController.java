package org.mskcc.permissions;

import org.mskcc.permissions.model.dto.RequestPermissions;
import org.mskcc.permissions.repository.LabMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping(value = "/getRequestPermissions/{request}")
    public RequestPermissions getPooledNormals(@PathVariable String request) {
        log.info("Querying request permissions for:" + request);
        if (request.length() < 5 || request.length() > 10)
            return null;

        // ask LIMS which lab and which request groups should have access ie cmoigo, bicigo, isabl
        RequestPermissions permissions = queryLIMSforLabNameAndGroupAccess(request);

        // query lab members list from db
        List<String> labMembers = labMemberRepository.findByPi(permissions.getLabName());
        permissions.setLabMembers(labMembers);

        return permissions;
    }

    protected RequestPermissions queryLIMSforLabNameAndGroupAccess(String request) {
        return null;
    }
}