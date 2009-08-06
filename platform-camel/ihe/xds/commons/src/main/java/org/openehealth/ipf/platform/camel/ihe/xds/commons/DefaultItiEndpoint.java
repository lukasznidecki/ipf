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
package org.openehealth.ipf.platform.camel.ihe.xds.commons;

import org.apache.camel.Component;
import org.apache.camel.impl.DefaultEndpoint;
import java.net.URISyntaxException;

/**
 * Camel endpoint used to create producers and consumers based on webservice calls.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public abstract class DefaultItiEndpoint extends DefaultEndpoint {
    private static final String ENDPOINT_PROTOCOL = "http://";
    private static final String ENDPOINT_PROTOCOL_SECURE = "https://";

    private final String address;
    
    private String serviceAddress;
    private String serviceUrl;    
    private boolean secure;
    
    /**
     * Whether we should audit or not -- an URL parameter.  
     * Equals to <code>true</code> per default.
     */
    private boolean audit = true;
    
    /**
     * Whether the system is allowed to write down audit items
     * even if if was not able to collect all necessary data --
     * an URL parameter, targeted on debug purposes only,
     * defaults to <code>false</code>. 
     */
    private boolean allowIncompleteAudit = false;
    
    /**
     * Whether SOAP 1.1 should be used instead of SOAP 1.2
     * for XDS.b transactions.  Default is <code>false</code>.
     * <p>
     * Does not have any meaning for XDS.a transactions,
     * because they use only SOAP 1.1. 
     */
    private boolean soap11 = false;

    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the URI of the endpoint.
     * @param address
     *          the endpoint address from the URI.
     * @param component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    protected DefaultItiEndpoint(String endpointUri, String address, Component component) throws URISyntaxException {
        super(endpointUri, component);
        this.address = address;
        configure();
    }

    private void configure() throws URISyntaxException {
        this.serviceUrl = (secure ? ENDPOINT_PROTOCOL_SECURE : ENDPOINT_PROTOCOL) + address;
        this.serviceAddress = "/" + address;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Returns the URL of the service.
     * <p>
     * The URL is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a producer, this method throws an exception.
     * @return the service URL.
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Returns the address of the service.
     * <p>
     * The address is derived from the endpoint URI defined in the constructor. If the
     * URI does not represent a consumer, this method throws an exception.
     * @return the service address.
     */
    public String getServiceAddress() {
        return serviceAddress;
    }


    /* ----- automatically generated getters and setters ----- */
    
    public boolean isAudit() {
        return audit;
    }
    
    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public void setAllowIncompleteAudit(boolean allowIncompleteAudit) {
        this.allowIncompleteAudit = allowIncompleteAudit;
    }

    public boolean isAllowIncompleteAudit() {
        return allowIncompleteAudit;
    }
    
    public boolean isSoap11() {
        return soap11;
    }

    public void setSoap11(boolean soap11) {
        this.soap11 = soap11;
    }

    /**
     * @return <code>true</code> if https should be used instead of http.
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * @param secure
     *          <code>true</code> if https should be used instead of http.
     */
    public void setSecure(boolean secure) throws URISyntaxException {
        this.secure = secure;
        configure();
    }
}