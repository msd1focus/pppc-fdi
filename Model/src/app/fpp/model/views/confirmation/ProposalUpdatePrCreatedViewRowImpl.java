package app.fpp.model.views.confirmation;

import app.fpp.model.entities.promoproposal.ProposalImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Sep 04 14:12:14 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProposalUpdatePrCreatedViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        ProposalId {
            public Object get(ProposalUpdatePrCreatedViewRowImpl obj) {
                return obj.getProposalId();
            }

            public void put(ProposalUpdatePrCreatedViewRowImpl obj,
                            Object value) {
                obj.setProposalId((DBSequence)value);
            }
        }
        ,
        PrCreated {
            public Object get(ProposalUpdatePrCreatedViewRowImpl obj) {
                return obj.getPrCreated();
            }

            public void put(ProposalUpdatePrCreatedViewRowImpl obj,
                            Object value) {
                obj.setPrCreated((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(ProposalUpdatePrCreatedViewRowImpl object);

        public abstract void put(ProposalUpdatePrCreatedViewRowImpl object,
                                 Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static final int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static final AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }
    public static final int PROPOSALID = AttributesEnum.ProposalId.index();
    public static final int PRCREATED = AttributesEnum.PrCreated.index();

    /**
     * This is the default constructor (do not remove).
     */
    public ProposalUpdatePrCreatedViewRowImpl() {
    }

    /**
     * Gets Proposal entity object.
     * @return the Proposal
     */
    public ProposalImpl getProposal() {
        return (ProposalImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for PROPOSAL_ID using the alias name ProposalId.
     * @return the PROPOSAL_ID
     */
    public DBSequence getProposalId() {
        return (DBSequence)getAttributeInternal(PROPOSALID);
    }

    /**
     * Sets <code>value</code> as attribute value for PROPOSAL_ID using the alias name ProposalId.
     * @param value value to set the PROPOSAL_ID
     */
    public void setProposalId(DBSequence value) {
        setAttributeInternal(PROPOSALID, value);
    }

    /**
     * Gets the attribute value for PR_CREATED using the alias name PrCreated.
     * @return the PR_CREATED
     */
    public String getPrCreated() {
        return (String) getAttributeInternal(PRCREATED);
    }

    /**
     * Sets <code>value</code> as attribute value for PR_CREATED using the alias name PrCreated.
     * @param value value to set the PR_CREATED
     */
    public void setPrCreated(String value) {
        setAttributeInternal(PRCREATED, value);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }
}