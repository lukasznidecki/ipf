/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti65;

import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.ITransactionTyped;
import org.hl7.fhir.dstu3.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.ClientRequestFactory;

import java.util.Map;

/**
 * Request Factory for ITI-65 requests
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti65ClientRequestFactory implements ClientRequestFactory<ITransactionTyped<Bundle>> {

    @Override
    public ITransactionTyped<Bundle> getClientExecutable(IGenericClient client, Object requestData, Map<String, Object> parameters) {
        return client.transaction().withBundle((Bundle)requestData);
    }

}
