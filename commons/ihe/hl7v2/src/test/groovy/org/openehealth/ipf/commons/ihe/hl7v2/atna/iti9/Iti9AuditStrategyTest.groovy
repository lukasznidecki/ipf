/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.Parser
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

/**
 * Test for #144
 */
class Iti9AuditStrategyTest {

    private static final Parser PARSER = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pix", "2.5"),
            PixPdqTransactions.ITI9).pipeParser
    private String msg;

    @Before
    public void setup() {
        msg = getClass().getResourceAsStream("/rsp.hl7").text
    }

    @Test
    public void testEnrichResponse() {
        Iti9ClientAuditStrategy strategy = new Iti9ClientAuditStrategy()
        QueryAuditDataset dataset = new QueryAuditDataset(false)
        Message message = PARSER.parse(msg)
        strategy.enrichAuditDatasetFromResponse(dataset, message)
        println dataset
    }
}
