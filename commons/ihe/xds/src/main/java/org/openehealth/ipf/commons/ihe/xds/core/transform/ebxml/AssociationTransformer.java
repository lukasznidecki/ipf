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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.*;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AssociationLabelFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AvailabilityStatusFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AvailabilityStatusForQueryFactory;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Transforms an {@link Association} to its ebXML representation.
 * @author Jens Riemschneider
 */
public class AssociationTransformer {
    private final EbXMLFactory factory;
    private final CodeTransformer codeTransformer;
    private final StringToBoolTransformer stringToBoolTransformer;
    
    /**
     * Constructs the transformer
     * @param factory
     *          factory for version independent ebXML objects. 
     */
    public AssociationTransformer(EbXMLFactory factory) {
        notNull(factory, "factory cannot be null");
        this.factory = factory;
        codeTransformer = new CodeTransformer(factory);
        stringToBoolTransformer = new StringToBoolTransformer();
    }
    
    /**
     * Transforms the given association to its ebXML representation.
     * @param association
     *          the association to transform. Can be <code>null</code>.
     * @param objectLibrary 
     *          the object library.
     * @return the ebXML representation. <code>null</code> if the input was <code>null</code>.
     */
    public EbXMLAssociation toEbXML(Association association, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        if (association == null) {
            return null;
        }
        
        EbXMLAssociation result = factory.createAssociation(association.getEntryUuid(), objectLibrary);
        result.setAssociationType(association.getAssociationType());
        result.setSource(association.getSourceUuid());
        result.setTarget(association.getTargetUuid());

        String label = new AssociationLabelFactory().toEbXML(association.getLabel());
        result.addSlot(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS, label);

        String previousVersion = association.getPreviousVersion();
        result.addSlot(Vocabulary.SLOT_NAME_PREVIOUS_VERSION, previousVersion);

        AvailabilityStatus originalStatus = association.getOriginalStatus();
        result.addSlot(Vocabulary.SLOT_NAME_ORIGINAL_STATUS, new AvailabilityStatusFactory30().toEbXML(originalStatus));

        AvailabilityStatus newStatus = association.getNewStatus();
        result.addSlot(Vocabulary.SLOT_NAME_NEW_STATUS, new AvailabilityStatusForQueryFactory().toEbXML(newStatus));

        result.addSlot(Vocabulary.SLOT_NAME_ASSOCIATION_PROPAGATION,
                                    stringToBoolTransformer.toEbXML(association.getAssociationPropagation()));

        EbXMLClassification contentType = codeTransformer.toEbXML(association.getDocCode(), objectLibrary);
        result.addClassification(contentType, Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME);

        result.setExtraMetadata(association.getExtraMetadata());
        result.setStatus(association.getAvailabilityStatus());

        return result;
    }
    
    /**
     * Transforms the given ebXML representation into an {@link Association}. 
     * @param association
     *          the ebXML association. Can be <code>null</code>.
     * @return the created {@link Association} instance. <code>null</code> if the input 
     *          was <code>null</code>.
     */
    public Association fromEbXML(EbXMLAssociation association) {
        if (association == null) {
            return null;
        }
        Association result = new Association();
        result.setAssociationType(association.getAssociationType());
        result.setTargetUuid(association.getTarget());
        result.setSourceUuid(association.getSource());
        result.setEntryUuid(association.getId());

        String label = association.getSingleSlotValue(Vocabulary.SLOT_NAME_SUBMISSION_SET_STATUS);
        result.setLabel(new AssociationLabelFactory().fromEbXML(label));

        String previousVersion = association.getSingleSlotValue(Vocabulary.SLOT_NAME_PREVIOUS_VERSION);
        result.setPreviousVersion(previousVersion);

        String originalStatus = association.getSingleSlotValue(Vocabulary.SLOT_NAME_ORIGINAL_STATUS);
        result.setOriginalStatus(new AvailabilityStatusFactory30().fromEbXML(originalStatus));

        String newStatus = association.getSingleSlotValue(Vocabulary.SLOT_NAME_NEW_STATUS);
        result.setNewStatus(new AvailabilityStatusFactory30().fromEbXML(newStatus));

        String associationPropagation = association.getSingleSlotValue(Vocabulary.SLOT_NAME_ASSOCIATION_PROPAGATION);
        result.setAssociationPropagation(stringToBoolTransformer.fromEbXML(associationPropagation));

        EbXMLClassification docCode = association.getSingleClassification(Vocabulary.ASSOCIATION_DOC_CODE_CLASS_SCHEME);
        result.setDocCode(codeTransformer.fromEbXML(docCode));

        result.setExtraMetadata(association.getExtraMetadata());
        result.setAvailabilityStatus(association.getStatus());

        return result;
    }
}
