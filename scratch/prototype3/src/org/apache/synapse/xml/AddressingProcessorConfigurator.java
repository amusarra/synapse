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

import javax.xml.namespace.QName;


import org.apache.axis2.om.OMElement;
import org.apache.synapse.xml.Constants;
import org.apache.synapse.Processor;
import org.apache.synapse.SynapseEnvironment;
import org.apache.synapse.processors.builtin.axis2.AddressingProcessor;

/**
 *
 * 
 *  <xmp><synapse:addressing/></xmp>
 *         <p>
 *         This tag turns on the addressing module 
 * 
 */
public class AddressingProcessorConfigurator extends AbstractProcessorConfigurator {
	private static final QName ADD_Q = new QName(Constants.SYNAPSE_NAMESPACE,
			"addressing");


	public QName getTagQName() {

		return ADD_Q;
	}


	public Processor createProcessor(SynapseEnvironment se, OMElement el) {
		AddressingProcessor ap = new AddressingProcessor();
		super.setNameOnProcessor(se,el,ap);
		return ap;
	}

}
