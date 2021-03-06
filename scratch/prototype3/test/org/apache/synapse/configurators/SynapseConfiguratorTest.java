package org.apache.synapse.configurators;

import junit.framework.TestCase;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.SynapseMessage;
import org.apache.synapse.Constants;
import org.apache.synapse.util.Axis2EvnSetup;
import org.apache.synapse.axis2.Axis2SynapseEnvironmentFinder;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.description.Parameter;
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

public class SynapseConfiguratorTest extends TestCase {
    public void testSynapseEnvironmentFinder() throws Exception {
        MessageContext mc = Axis2EvnSetup.axis2Deployment("repo");
        SynapseEnvironment env = Axis2SynapseEnvironmentFinder
				.getSynapseEnvironment(mc);
        assertNotNull(env);

        AxisConfiguration ac = mc.getSystemContext().getAxisConfiguration();

        Parameter parm =  ac.getParameter(Constants.SYNAPSE_ENVIRONMENT);
        assertNotNull(parm);

    }

}
