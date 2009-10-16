/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds;

import org.openehealth.ipf.commons.ihe.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.audit.ItiAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Client audit strategy for ITI-41.
 * 
 * @author Dmytro Rud
 */
class Iti41ClientAuditStrategy extends Iti41AuditStrategy {

    private static final String[] NECESSARY_AUDIT_FIELDS = new String[] {
        "ServiceEndpointUrl",
        "SubmissionSetUuid",
        "PatientId"};

    
    /**
     * Constructs the audit strategy.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    Iti41ClientAuditStrategy(boolean allowIncompleteAudit) {
        super(false, allowIncompleteAudit);
    }

    @Override
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, ItiAuditDataset auditDataset) {
        AuditorManager.getSourceAuditor().auditProvideAndRegisterDocumentSetBEvent(
                eventOutcome,
                auditDataset.getServiceEndpointUrl(),
                auditDataset.getSubmissionSetUuid(),
                auditDataset.getPatientId());
    }

    @Override
    public String[] getNecessaryAuditFieldNames() {
        return NECESSARY_AUDIT_FIELDS;
    }
}
