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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.io.IOException;

/**
 * The producer implementation for the ITI-17 component.
 */
public class Iti17Producer extends DefaultProducer {
    private final Iti17Endpoint endpoint;
    
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     */
    public Iti17Producer(Iti17Endpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String documentSpecifyingPart = exchange.getIn().getBody(String.class);
        String uri = endpoint.getServiceUrl() + documentSpecifyingPart;

        final HttpClient httpClient = HttpClients.createDefault();
        final HttpGet get = new HttpGet(uri);
        boolean keepConnection = false;
        try {
            HttpResponse response = httpClient.execute(get);
            keepConnection = handleResponse(exchange, response);
        }
        finally {
            if (!keepConnection) {
                get.releaseConnection();
            }
            
            // audit
            // TODO: finer mapping between HTTP response codes and event outcome codes
            RFC3881EventOutcomeCodes eventOutcome = 
                keepConnection ? 
                    RFC3881EventOutcomeCodes.SUCCESS : 
                    RFC3881EventOutcomeCodes.MAJOR_FAILURE;
            
            AuditorManager.getConsumerAuditor().auditRetrieveDocumentEvent(
                    eventOutcome, 
                    uri,
                    /*userName*/ null,
                    /*documentUniqueId*/ null, 
                    /*patientId*/ null);
        }
    }

    private boolean handleResponse(Exchange exchange, final HttpResponse response) throws IOException {
        Message out = Exchanges.resultMessage(exchange);
        if (response.getStatusLine().getStatusCode() == 200) {
            out.setBody(response.getEntity().getContent());
            return true;
        }

        out.setBody(response.getStatusLine().getStatusCode());
        out.setFault(true);
        
        return false;
    }

    /*
    private InputStream createWrappedStream(final GetMethod get) throws IOException {
        return new FilterInputStream(get.getResponseBodyAsStream()) {
            @Override
            public void close() throws IOException {
                super.close();
            }
        };
    }
    */
}
