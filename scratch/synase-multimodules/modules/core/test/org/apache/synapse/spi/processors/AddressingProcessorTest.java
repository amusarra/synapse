package org.apache.synapse.spi.processors;

import org.apache.synapse.util.Axis2EnvSetup;
import org.apache.synapse.SynapseMessage;
import org.apache.synapse.Constants;
import org.apache.synapse.Processor;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.processors.builtin.axis2.AddressingInProcessor;
import org.apache.synapse.axis2.Axis2SynapseMessage;
import org.apache.synapse.axis2.Axis2SynapseEnvironment;
import junit.framework.TestCase;
/*
* Copyright 2004,2005 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

public class AddressingProcessorTest extends TestCase {

    public void testAddressingProcessor() throws Exception {
        SynapseMessage sm = new Axis2SynapseMessage(
                Axis2EnvSetup.axis2Deployment("target/synapse-repository"));
        Processor addressingProcessor = new AddressingInProcessor();
        SynapseEnvironment env = new Axis2SynapseEnvironment(null,null);
        boolean result = addressingProcessor.process(env,sm);
        assertTrue(((Boolean) sm.getProperty(
                Constants.MEDIATOR_RESPONSE_PROPERTY)).booleanValue());
        assertTrue(result);
    }
}
