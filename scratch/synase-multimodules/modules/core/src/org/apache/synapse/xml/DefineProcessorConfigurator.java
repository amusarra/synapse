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
 */
package org.apache.synapse.xml;

import org.apache.synapse.Processor;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.processors.DefineProcessor;
import org.apache.axis2.om.OMElement;

import javax.xml.namespace.QName;


public class DefineProcessorConfigurator extends AbstractListProcessorConfigurator{
    private static final QName DEFINE_Q = new QName(Constants.SYNAPSE_NAMESPACE,
                "define");


    public Processor createProcessor(SynapseEnvironment se, OMElement el) {
        DefineProcessor defineProcessor = new DefineProcessor();
        super.addChildrenAndSetName(se,el,defineProcessor);
        return defineProcessor;
    }

    public QName getTagQName() {
        return DEFINE_Q;
    }
}
