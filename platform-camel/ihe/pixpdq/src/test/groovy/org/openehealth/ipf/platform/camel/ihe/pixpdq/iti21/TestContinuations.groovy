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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.iti21

import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpTestContainer;
import static org.junit.Assert.*

/**
 * Tests for HL7 continuations, see � 2.10.2 of the HL7 v.2.5 specification.
 * @author Dmytro Rud
 */
class TestContinuations extends MllpTestContainer {
   
    final String REQUEST_MESSAGE =
        'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|PIM|' + 
            '20081031112704||QBP^Q22|324406609|P|2.5|||ER\r' +
        'SFT|XON|ST|ST|ST|TX|TS\r' + 
        'QPD|IHE PDQ Query|1402274727|@PID.3.1^12345678~@PID.3.2.1^BLABLA~' + 
            '@PID.3.4.2^1.2.3.4~@PID.3.4.3^KRYSO|||||\r' +
        'RCP|I|1^RD|||||\n'
     
    @BeforeClass
    static void setUpClass() {
        init('iti21/iti-21-continuations.xml')
    }
    
    @Test
    void testHappyCaseAndAudit1() {
        def msg = send(
            'pdq-iti21://localhost:18219?timeout=30000000000' + 
            '&supportInteractiveContinuation=true' + 
            '&supportUnsolicitedFragmentation=true&unsolicitedFragmentationThreshold=3' + 
            '&supportSegmentFragmentation=true',
            REQUEST_MESSAGE)
        assert 4 == msg.QUERY_RESPONSE().size()
        assert 2 == auditSender.messages.size
        assert '4' == msg.QAK[4].value
        assert '4' == msg.QAK[5].value
        assert '0' == msg.QAK[6].value
    }

}
