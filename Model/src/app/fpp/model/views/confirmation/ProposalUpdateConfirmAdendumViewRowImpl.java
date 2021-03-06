package app.fpp.model.views.confirmation;

import app.fpp.model.entities.promoproposal.ProposalImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri Aug 25 17:37:26 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProposalUpdateConfirmAdendumViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        ProposalId {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getProposalId();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setProposalId((DBSequence)value);
            }
        }
        ,
        ConfirmDate {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getConfirmDate();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setConfirmDate((Date)value);
            }
        }
        ,
        ConfirmNo {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getConfirmNo();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setConfirmNo((String)value);
            }
        }
        ,
        AddendumKe {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getAddendumKe();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setAddendumKe((String)value);
            }
        }
        ,
        NextAddendum {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getNextAddendum();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setNextAddendum((Number)value);
            }
        }
        ,
        CategoryPc {
            public Object get(ProposalUpdateConfirmAdendumViewRowImpl obj) {
                return obj.getCategoryPc();
            }

            public void put(ProposalUpdateConfirmAdendumViewRowImpl obj,
                            Object value) {
                obj.setCategoryPc((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(ProposalUpdateConfirmAdendumViewRowImpl object);

        public abstract void put(ProposalUpdateConfirmAdendumViewRowImpl object,
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
    public static final int CONFIRMDATE = AttributesEnum.ConfirmDate.index();
    public static final int CONFIRMNO = AttributesEnum.ConfirmNo.index();
    public static final int ADDENDUMKE = AttributesEnum.AddendumKe.index();
    public static final int NEXTADDENDUM = AttributesEnum.NextAddendum.index();
    public static final int CATEGORYPC = AttributesEnum.CategoryPc.index();

    /**
     * This is the default constructor (do not remove).
     */
    public ProposalUpdateConfirmAdendumViewRowImpl() {
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
     * Gets the attribute value for CONFIRM_DATE using the alias name ConfirmDate.
     * @return the CONFIRM_DATE
     */
    public Date getConfirmDate() {
        return (Date) getAttributeInternal(CONFIRMDATE);
    }

    /**
     * Sets <code>value</code> as attribute value for CONFIRM_DATE using the alias name ConfirmDate.
     * @param value value to set the CONFIRM_DATE
     */
    public void setConfirmDate(Date value) {
        setAttributeInternal(CONFIRMDATE, value);
    }

    /**
     * Gets the attribute value for CONFIRM_NO using the alias name ConfirmNo.
     * @return the CONFIRM_NO
     */
    public String getConfirmNo() {
        return (String) getAttributeInternal(CONFIRMNO);
    }

    /**
     * Sets <code>value</code> as attribute value for CONFIRM_NO using the alias name ConfirmNo.
     * @param value value to set the CONFIRM_NO
     */
    public void setConfirmNo(String value) {
        setAttributeInternal(CONFIRMNO, value);
    }

    /**
     * Gets the attribute value for ADDENDUM_KE using the alias name AddendumKe.
     * @return the ADDENDUM_KE
     */
    public String getAddendumKe() {
        return (String) getAttributeInternal(ADDENDUMKE);
    }

    /**
     * Sets <code>value</code> as attribute value for ADDENDUM_KE using the alias name AddendumKe.
     * @param value value to set the ADDENDUM_KE
     */
    public void setAddendumKe(String value) {
        setAttributeInternal(ADDENDUMKE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute NextAddendum.
     * @return the NextAddendum
     */
    public Number getNextAddendum() {
        return (Number) getAttributeInternal(NEXTADDENDUM);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute NextAddendum.
     * @param value value to set the  NextAddendum
     */
    public void setNextAddendum(Number value) {
        setAttributeInternal(NEXTADDENDUM, value);
    }

    /**
     * Gets the attribute value for CATEGORY_PC using the alias name CategoryPc.
     * @return the CATEGORY_PC
     */
    public String getCategoryPc() {
        return (String) getAttributeInternal(CATEGORYPC);
    }

    /**
     * Sets <code>value</code> as attribute value for CATEGORY_PC using the alias name CategoryPc.
     * @param value value to set the CATEGORY_PC
     */
    public void setCategoryPc(String value) {
        setAttributeInternal(CATEGORYPC, value);
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
