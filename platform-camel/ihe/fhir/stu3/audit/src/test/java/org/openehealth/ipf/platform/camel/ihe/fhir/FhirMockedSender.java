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
package org.openehealth.ipf.platform.camel.ihe.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.AuditEvent;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.core.atna.AbstractMockedAuditSender;
import org.openehealth.ipf.commons.ihe.fhir.translation.AuditRecordTranslator;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;

/**
 * @author Dmytro Rud
 */
@Slf4j
public class FhirMockedSender extends AbstractMockedAuditSender<AuditEvent> {

    private final FhirContext fhirContext;
    private final AuditRecordTranslator translator = new AuditRecordTranslator();

    public FhirMockedSender(FhirContext fhirContext, boolean needValidation) {
        super(needValidation);
        this.fhirContext = fhirContext;
    }

    @Override
    public void sendAuditEvent(AuditEventMessage[] msg) throws Exception {
        for (AuditEventMessage atnaMessage : msg) {
            log.debug(atnaMessage.toString());
            AuditEvent auditEventResource = translator.translate(atnaMessage);
            log.debug(fhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(auditEventResource));
            if (needValidation) {
                FhirValidator validator = fhirContext.newValidator();
                FhirInstanceValidator instanceValidator = new FhirInstanceValidator();
                validator.registerValidatorModule(instanceValidator);
                ValidationResult result = validator.validateWithResult(auditEventResource);
                if (!result.isSuccessful()) {
                    StringBuilder sb = new StringBuilder("Validation of FHIR AuditEvent failed:");
                    for (SingleValidationMessage error : result.getMessages()) {
                        sb.append('\n').append(error.toString());
                    }
                    throw new ValidationException(sb.toString());
                }
            }
            messages.add(auditEventResource);
        }
    }

}
