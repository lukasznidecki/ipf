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

package org.openehealth.ipf.platform.camel.ihe.fhir.iti66;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.atna.MockedSender;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomIHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.dicom.DICOMEventIdCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ActiveParticipantCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.*;

import javax.servlet.ServletException;

import static org.junit.Assert.*;

/**
 *
 */
public class TestIti66Success extends AbstractTestIti66 {

    private static final String CONTEXT_DESCRIPTOR = "iti-66.xml";

    @BeforeClass
    public static void setUpClass() throws ServletException {
        startServer(CONTEXT_DESCRIPTOR);
    }

    @Test
    public void testGetConformance() {
        assertConformance("DocumentManifest");
    }

    @Test
    public void testSendManualIti66() {
        Bundle result = sendManually(manifestPatientIdentifierParameter());

        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        DocumentManifest p = (DocumentManifest) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());

        // Check ATNA Audit

        MockedSender sender = getAuditSender();
        assertEquals(1, sender.getMessages().size());
        AuditMessage event = sender.getMessages().get(0).getAuditMessage();

        // Event
        assertEquals(
                RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS.getCode().intValue(),
                event.getEventIdentification().getEventOutcomeIndicator());
        assertEquals(
                RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE.getCode(),
                event.getEventIdentification().getEventActionCode());
        CodedValueType eventId = event.getEventIdentification().getEventID();
        CodedValueType expectedEventId = new DICOMEventIdCodes.Query();
        assertEquals(expectedEventId.getCode(), eventId.getCode());
        assertEquals(expectedEventId.getCodeSystemName(), eventId.getCodeSystemName());
        assertEquals(expectedEventId.getOriginalText(), eventId.getOriginalText());
        CodedValueType eventTypeCode = event.getEventIdentification().getEventTypeCode().get(0);
        CodedValueType expectedEventTypeCode = new CustomIHETransactionEventTypeCodes.DocumentManifestQuery();
        assertEquals(expectedEventTypeCode.getCode(), eventTypeCode.getCode());
        assertEquals(expectedEventTypeCode.getCodeSystemName(), eventTypeCode.getCodeSystemName());
        assertEquals(expectedEventTypeCode.getOriginalText(), eventTypeCode.getOriginalText());

        // ActiveParticipant Source
        ActiveParticipantType source = event.getActiveParticipant().get(0);
        assertTrue(source.isUserIsRequestor());
        assertEquals("127.0.0.1", source.getNetworkAccessPointID());
        assertEquals(RFC3881ActiveParticipantCodes.RFC3881NetworkAccessPointTypeCodes.IP_ADDRESS.getCode(), source.getNetworkAccessPointTypeCode());

        // ActiveParticipant Destination
        ActiveParticipantType destination = event.getActiveParticipant().get(1);
        assertFalse(destination.isUserIsRequestor());
        assertEquals("http://localhost:" + DEMO_APP_PORT + "/DocumentManifest", destination.getUserID());
        assertEquals("localhost", destination.getNetworkAccessPointID());

        // Audit Source
        AuditSourceIdentificationType sourceIdentificationType = event.getAuditSourceIdentification().get(0);
        assertEquals("IPF", sourceIdentificationType.getAuditSourceID());
        assertEquals("IPF", sourceIdentificationType.getAuditEnterpriseSiteID());

        // Patient
        ParticipantObjectIdentificationType patient = event.getParticipantObjectIdentification().get(0);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.PERSON.getCode(), patient.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.PATIENT.getCode(), patient.getParticipantObjectTypeCodeRole());
        assertEquals("urn:oid:2.16.840.1.113883.3.37.4.1.1.2.1.1|1", new String(patient.getParticipantObjectID()));

        // Query Parameters
        ParticipantObjectIdentificationType query = event.getParticipantObjectIdentification().get(1);
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes.SYSTEM.getCode(), query.getParticipantObjectTypeCode());
        assertEquals(RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeRoleCodes.QUERY.getCode(), query.getParticipantObjectTypeCodeRole());
        assertEquals("http://localhost:8999/DocumentManifest?patient.identifier=urn%3Aoid%3A2.16.840.1.113883.3.37.4.1.1.2.1.1%7C1", new String(query.getParticipantObjectQuery()));

        CodedValueType poitTypeCode = query.getParticipantObjectIDTypeCode();
        assertEquals("ITI-66", poitTypeCode.getCode());
        assertEquals("IHE Transactions", poitTypeCode.getCodeSystemName());
        assertEquals("Mobile Document Manifest Query", poitTypeCode.getOriginalText());
        assertEquals("MobileDocumentManifestQuery", query.getParticipantObjectID());
    }

    @Test
    public void testSendIti66WithPatientReference() {
        Bundle result = sendManually(manifestPatientReferenceParameter());
        assertEquals(Bundle.BundleType.SEARCHSET, result.getType());
        assertEquals(ResourceType.Bundle, result.getResourceType());
        assertEquals(1, result.getTotal());

        DocumentManifest p = (DocumentManifest) result.getEntry().get(0).getResource();
        assertEquals("9bc72458-49b0-11e6-8a1c-3c1620524153", p.getIdElement().getIdPart());
    }

    @Test
    public void testGetResource() {
        DocumentManifest p = client.read()
                .resource(DocumentManifest.class)
                .withId("9bc72458-49b0-11e6-8a1c-3c1620524153")
                .execute();
        assertEquals(String.format("http://localhost:%d/DocumentManifest/9bc72458-49b0-11e6-8a1c-3c1620524153", DEMO_APP_PORT), p.getId());
    }


}
