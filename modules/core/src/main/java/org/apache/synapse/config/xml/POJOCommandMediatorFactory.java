/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.ext.POJOCommandMediator;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * Creates an instance of a Class mediator using XML configuration specified
 * <p/>
 * <pre>
 * &lt;pojoCommand name=&quot;class-name&quot;&gt;
 *   &lt;property name=&quot;string&quot; value=&quot;literal&quot;
 *                action=(&quot;get&quot; | &quot;set&quot;)&gt;
 *      either literal or XML child
 *   &lt;/property&gt;
 *   &lt;property name=&quot;string&quot; expression=&quot;XPATH expression&quot;
 *                action=(&quot;get&quot; | &quot;set&quot;)/&gt;
 * &lt;/pojoCommand&gt;
 * </pre>
 */
public class POJOCommandMediatorFactory extends AbstractMediatorFactory {

    private static final QName POJO_COMMAND_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "pojoCommand");
    protected static final QName ATT_ACTION   = new QName("action");    

    public Mediator createMediator(OMElement elem) {

        POJOCommandMediator pojoMediator = new POJOCommandMediator();

        // Class name of the Command object should be present
        OMAttribute name = elem.getAttribute(ATT_NAME);
        if (name == null) {
            String msg = "The name of the actual POJO command implementation class" +
                    " is a required attribute";
            log.error(msg);
            throw new SynapseException(msg);
        }

        // load the class for the command object
        try {
            pojoMediator.setCommand(
                    getClass().getClassLoader().loadClass(name.getAttributeValue()));
        } catch (ClassNotFoundException e) {
            handleException("Unable to load the class specified as the command "
                    + name.getAttributeValue(), e);
        }

        // setting the properties to the command. these properties will be instantiated
        // at the mediation time
        for (Iterator it = elem.getChildElements(); it.hasNext();) {
            OMElement child = (OMElement) it.next();
            if("property".equals(child.getLocalName())) {

                String propName = child.getAttribute(ATT_NAME).getAttributeValue();
                if (propName == null) {
                    handleException(
                        "A POJO command mediator property must specify the name attribute");
                } else {
                    if (child.getAttribute(ATT_EXPRN) != null) {
                        AXIOMXPath xpath = null;

                        try {
                            xpath = new AXIOMXPath(child.getAttribute(ATT_EXPRN).getAttributeValue());
                            OMElementUtils.addNameSpaces(xpath, child, log);

                            if (child.getAttribute(ATT_ACTION) != null) {
                                
                                String action = child.getAttribute(ATT_ACTION).getAttributeValue();
                                if ("get".equals(action)) {
                                    pojoMediator.addMessageGetterProperty(propName, xpath);
                                } else if ("set".equals(action)) {
                                    pojoMediator.addDynamicSetterProperty(propName, xpath);
                                } else {
                                    // if there is an action attribute and the value is not neigther get nor set
                                    handleException("Property action for the " +
                                            "POJOCommand mediator should be eigther 'get' or 'set'");
                                }
                            } else {
                                // if no action is provided take it as a setter propperty
                                pojoMediator.addDynamicSetterProperty(propName, xpath);
                            }
                            
                        } catch (JaxenException e) {
                            handleException("Error instantiating XPath expression : " +
                                child.getAttribute(ATT_EXPRN), e);
                        }
                    } else {
                        if (child.getAttribute(ATT_VALUE) != null) {

                            String value = child.getAttribute(ATT_VALUE).getAttributeValue();
                            if (child.getAttribute(ATT_ACTION) != null) {

                                String action = child.getAttribute(ATT_ACTION).getAttributeValue();
                                if ("get".equals(action)) {
                                    pojoMediator.addContextGetterProperty(propName, value);
                                } else if ("set".equals(action)) {
                                    pojoMediator.addStaticSetterProperty(propName, value);
                                } else {
                                    // if there is an action attribute and the value is not neigther get nor set
                                    handleException("Property action for the " +
                                            "POJOCommand mediator should be eigther 'get' or 'set'");
                                }
                            } else {
                                // if no action is provided take it as a setter propperty
                                pojoMediator.addStaticSetterProperty(propName, value);
                            }
                        } else {
                            handleException("A POJO mediator property must specify either " +
                                    "name and expression attributes, or name and value attributes");
                        }
                    }
                }
            }
        }

        return pojoMediator;
    }

    public QName getTagQName() {
        return POJO_COMMAND_Q;
    }

}

