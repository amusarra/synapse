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

package org.apache.synapse.mediators.builtin;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.synapse.SynapseMessage;
import org.apache.synapse.SynapseContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.MediatorProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


/**
 * Logs the specified message into the configured logger. The log levels specify
 * which attributes would be logged, and is configurable. Additionally custom
 * properties may be defined to the logger, where literal values or expressions
 * could be specified for logging. The custom properties are printed into the log
 * using the defined seperator (\n, "," etc)
 */
public class LogMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(LogMediator.class);

    public static final int CUSTOM = 0;
    public static final int SIMPLE = 1;
    public static final int HEADERS = 2;
    public static final int FULL = 3;

    private int logLevel = SIMPLE;
    private String SEP = ", ";
    private List properties = new ArrayList();

    /**
     * Logs the current message according to the supplied semantics
     * @param synCtx (current) message to be logged
     * @return true always
     */
    public boolean mediate(SynapseContext synCtx) {
        log.debug(getType() + " mediate()");
        log.info(getLogMessage(synCtx));
        return true;
    }

    private String getLogMessage(SynapseContext synCtx) {
        switch (logLevel) {
            case CUSTOM:
                return getCustomLogMessage(synCtx);
            case SIMPLE:
                return getSimpleLogMessage(synCtx);
            case HEADERS:
                return getHeadersLogMessage(synCtx);
            case FULL:
                return getFullLogMessage(synCtx);
            default:
                return "Invalid log level specified";
        }
    }

    private String getCustomLogMessage(SynapseContext synCtx) {
        StringBuffer sb = new StringBuffer();
        setCustomProperties(sb, synCtx);
        return sb.toString();
    }

    private String getSimpleLogMessage(SynapseContext synCtx) {
        SynapseMessage synMsg = synCtx.getSynapseMessage();
        StringBuffer sb = new StringBuffer();
        if (synMsg.getTo() != null)
            sb.append("To: " + synMsg.getTo().getAddress());
        if (synMsg.getFrom() != null)
            sb.append(SEP + "From: " + synMsg.getFrom().getAddress());
        if (synMsg.getWSAAction() != null)
            sb.append(SEP + "WSAction: " + synMsg.getWSAAction());
        if (synMsg.getSoapAction() != null)
            sb.append(SEP + "SOAPAction: " + synMsg.getSoapAction());
        if (synMsg.getReplyTo() != null)
            sb.append(SEP + "ReplyTo: " + synMsg.getReplyTo().getAddress());
        if (synMsg.getMessageID() != null)
            sb.append(SEP + "MessageID: " + synMsg.getMessageID());
        setCustomProperties(sb, synCtx);
        return sb.toString();
    }

    private String getHeadersLogMessage(SynapseContext synCtx) {
        SynapseMessage synMsg = synCtx.getSynapseMessage();
        StringBuffer sb = new StringBuffer();
        Iterator iter = synMsg.getEnvelope().getHeader().examineAllHeaderBlocks();
        while (iter.hasNext()) {
            SOAPHeader header = (SOAPHeader) iter.next();
            sb.append(SEP + header.getLocalName() + " : " + header.getText());
        }
        setCustomProperties(sb, synCtx);
        return sb.toString();
    }

    private String getFullLogMessage(SynapseContext synCtx) {
        SynapseMessage synMsg = synCtx.getSynapseMessage();
        StringBuffer sb = new StringBuffer();
        sb.append(getSimpleLogMessage(synCtx));
        if (synMsg.getEnvelope() != null)
            sb.append(SEP + "Envelope: " + synMsg.getEnvelope());
        setCustomProperties(sb, synCtx);
        return sb.toString();
    }

    private void setCustomProperties(StringBuffer sb, SynapseContext synCtx) {
        if (properties != null && !properties.isEmpty()) {
            Iterator iter = properties.iterator();
            while (iter.hasNext()) {
                MediatorProperty prop = (MediatorProperty) iter.next();
                sb.append(SEP + prop.getName() + " = " +
                    (prop.getValue() != null ? prop.getValue() : prop.getEvaluatedExpression(synCtx)));
            }
        }
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getSeperator() {
        return SEP;
    }

    public void setSeperator(String SEP) {
        this.SEP = SEP;
    }

    public void addProperty(MediatorProperty p) {
        properties.add(p);
    }

    public void addAllProperties(List list) {
        properties.addAll(list);
    }

}
