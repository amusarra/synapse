package org.apache.synapse.rules;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.soap.SOAPEnvelope;
import org.apache.axis2.om.xpath.AXIOMXPath;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.synapse.SynapseConstants;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.SimpleNamespaceContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: saminda
 * Date: Oct 18, 2005
 * Time: 10:12:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class SynapseRuleConfiguration {
    /**
     * this is the reference parameter to the MessageReceiver that
     * should be used
     */
    private String operationName = "receiver";

    private SynapseRuleReader ruleReader;

    private ArrayList mediatorList;

    private ArrayList generalRuleList;
    private ArrayList xpathRuleList;

    private ArrayList cumulativeRuleList; // all rules are cumulated so this is extensible

    public SynapseRuleConfiguration() {
        ruleReader = new SynapseRuleReader();
        mediatorList = new ArrayList();
        generalRuleList = new ArrayList();
        xpathRuleList = new ArrayList();
        cumulativeRuleList = new ArrayList();
    }

    public void ruleConfiguration(MessageContext msgCtx) throws AxisFault {
        AxisConfiguration registry = msgCtx.getSystemContext()
                .getAxisConfiguration();

        HashMap serviceMap = registry.getServices();

        ruleReader.populateRules(msgCtx);

        Iterator ite = serviceMap.entrySet().iterator();
        Map.Entry entry = null;
        String key = null;
        while (ite.hasNext()) {
            entry = (Map.Entry) ite.next();
            key = (String) entry.getKey();

            Iterator iterator = ruleReader.getRulesIterator();

            while (iterator.hasNext()) {
                SynapaseRuleBean bean = (SynapaseRuleBean) iterator.next();

                if (bean.getMediator().equalsIgnoreCase(key)) {
                    this.mediatorList.add(key);
                }
            }

        }
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public ArrayList getArrayofMediatorList() {
        return mediatorList;
    }

    public ArrayList getArrayofGeneralRuleList() {
        return generalRuleList;
    }

    public ArrayList getArrayofXpathRuleList() {
        return xpathRuleList;
    }

    public void validateXpath(MessageContext messageContext)
            throws JaxenException {
        Iterator ruleIte = ruleReader.getRulesIterator();

        /**
         * Need registering qulified namesapaces to Jaxen to work on
         * xpath.
         */
        HashMap namespacesMap = ruleReader.getNamespaceMap();


        SOAPEnvelope envelope = messageContext.getEnvelope();
        envelope.build();

        while (ruleIte.hasNext()) {
            SynapaseRuleBean bean = (SynapaseRuleBean) ruleIte.next();
            /**
             * general rule handling
             */
            if (bean.getCondition().equals("*")) {
                // this could be more than this.
                this.generalRuleList.add(bean);
            } else {

                Iterator nsIte = namespacesMap.entrySet().iterator();
                Map.Entry entry = null;
                String prifix = null;
                String uri = null;

                //settingup the namespace which needed to be dealt with care
                SimpleNamespaceContext nsCtx = new SimpleNamespaceContext();

                while (nsIte.hasNext()) {
                    entry = (Map.Entry) nsIte.next();
                    prifix = (String) entry.getKey();
                    uri = (String) entry.getValue();
                    nsCtx.addNamespace(prifix, uri);
                }

                // which deal with the xpath of the message
                String xpathExpression = bean.getCondition();
                XPath xpath = new AXIOMXPath(xpathExpression);

                xpath.setNamespaceContext(nsCtx);
                /**
                 * Xpath validation for the incomming message.
                 */
                boolean xpathBool = xpath
                        .booleanValueOf(envelope);

                if (xpathBool) {
                    this.xpathRuleList.add(bean);
                }
            }
        }

        /**
         * Cumulation of all Rule to single collection so the looping of Rule Engine
         * Become more simple
         */
        cumulativeRuleList.addAll(generalRuleList);
        cumulativeRuleList.addAll(xpathRuleList);

        messageContext.setProperty(
                SynapseConstants.SynapseRuleEngine.CUMULATIVE_RUEL_ARRAY_LIST,
                cumulativeRuleList);
    }

}
