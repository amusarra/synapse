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

package org.apache.synapse.endpoints.dispatch;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;

import javax.xml.namespace.QName;

/**
 * This dispatcher is implemented to demonstrate a sample client session. It will detect sessions
 * based on the <syn:ClientID xmlns:syn="http://ws.apache.org/namespaces/synapse"> soap header of the
 * request message. Therefore, above header has to be included in the request soap messages by the
 * client who wants to initiate and maintain a session.
 */
public class SimpleClientSessionDispatcher implements Dispatcher {

    private static final Log log = LogFactory.getLog(SimpleClientSessionDispatcher.class);

    public Endpoint getEndpoint(MessageContext synCtx, DispatcherContext dispatcherContext) {

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement sgcIDElm = header.getFirstChildWithName(
                    new QName("http://ws.apache.org/namespaces/synapse", "ClientID", "syn"));

            if (sgcIDElm != null) {
                String sgcID = sgcIDElm.getText();

                if (sgcID != null) {
                    Object o = dispatcherContext.getEndpoint(sgcID);

                    if (o != null && o instanceof Endpoint) {
                        return (Endpoint) o;
                    }
                }
            }
        }

        return null;
    }

    public void updateSession(MessageContext synCtx, DispatcherContext dispatcherContext, Endpoint endpoint) {

        if (endpoint == null || dispatcherContext == null) {
            return;
        }

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement csIDElm = header.getFirstChildWithName(
                    new QName("http://ws.apache.org/namespaces/synapse", "ClientID", "syn"));

            if (csIDElm != null) {
                String csID = csIDElm.getText();

                if (csID != null) {
                    dispatcherContext.setEndpoint(csID, endpoint);
                }
            }
        }
    }


    public void unbind(MessageContext synCtx, DispatcherContext dispatcherContext) {

        if (dispatcherContext == null) {
            return;
        }

        SOAPHeader header = synCtx.getEnvelope().getHeader();

        if (header != null) {
            OMElement csIDElm = header.getFirstChildWithName(
                    new QName("http://ws.apache.org/namespaces/synapse", "ClientID", "syn"));

            if (csIDElm != null) {
                String csID = csIDElm.getText();

                if (csID != null) {
                    dispatcherContext.removeSession(csID);
                }
            }
        }
    }

    public boolean isServerInitiatedSession() {
        return false;
    }
}
