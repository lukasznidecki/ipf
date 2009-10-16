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
package org.openehealth.ipf.commons.ihe.xds.core.validate.responses;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

/**
 * Validates {@link EbXMLRetrieveDocumentSetResponse}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseValidator implements Validator<EbXMLRetrieveDocumentSetResponse, ValidationProfile>{
    private final RegistryResponseValidator regResponseValidator = new RegistryResponseValidator();
    
    @Override
    public void validate(EbXMLRetrieveDocumentSetResponse response, ValidationProfile profile) {
        notNull(response, "response cannot be null");
        
        regResponseValidator.validate(response, profile);
        
        for (RetrievedDocument doc : response.getDocuments()) {
            String repoId = doc.getRequestData().getRepositoryUniqueId();
            metaDataAssert(repoId != null && !repoId.isEmpty(), REPO_ID_MUST_BE_SPECIFIED);
            
            String docId = doc.getRequestData().getDocumentUniqueId();
            metaDataAssert(docId != null && !docId.isEmpty(), DOC_ID_MUST_BE_SPECIFIED);
            
            metaDataAssert(doc.getDataHandler() != null, MISSING_DOCUMENT_FOR_DOC_ENTRY, docId);
        }
    }
}
