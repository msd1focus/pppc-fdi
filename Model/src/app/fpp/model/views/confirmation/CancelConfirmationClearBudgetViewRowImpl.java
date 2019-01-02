package app.fpp.model.views.confirmation;

import app.fpp.model.entities.confirmation.ProdBudgetByImpl;
import app.fpp.model.entities.promoproposal.PromoProdukImpl;
import app.fpp.model.entities.promoproposal.ProposalImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Apr 17 16:05:50 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CancelConfirmationClearBudgetViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        ProposalNo {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getProposalNo();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setProposalNo((String)value);
            }
        }
        ,
        ProposalId {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getProposalId();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setProposalId((DBSequence)value);
            }
        }
        ,
        ConfirmNo {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getConfirmNo();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setConfirmNo((String)value);
            }
        }
        ,
        PromoProdukId {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getPromoProdukId();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setPromoProdukId((DBSequence)value);
            }
        }
        ,
        BudgetPostingId {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getBudgetPostingId();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setBudgetPostingId((Number)value);
            }
        }
        ,
        KombinasiBudget {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getKombinasiBudget();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setKombinasiBudget((String)value);
            }
        }
        ,
        BudgetCustId {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getBudgetCustId();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setBudgetCustId((Number)value);
            }
        }
        ,
        Amount {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getAmount();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setAmount((Number)value);
            }
        }
        ,
        BudgetById {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getBudgetById();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setBudgetById((DBSequence)value);
            }
        }
        ,
        Status {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getStatus();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setStatus((String)value);
            }
        }
        ,
        PromoProdukId1 {
            public Object get(CancelConfirmationClearBudgetViewRowImpl obj) {
                return obj.getPromoProdukId1();
            }

            public void put(CancelConfirmationClearBudgetViewRowImpl obj,
                            Object value) {
                obj.setPromoProdukId1((Number)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(CancelConfirmationClearBudgetViewRowImpl object);

        public abstract void put(CancelConfirmationClearBudgetViewRowImpl object,
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


    public static final int PROPOSALNO = AttributesEnum.ProposalNo.index();
    public static final int PROPOSALID = AttributesEnum.ProposalId.index();
    public static final int CONFIRMNO = AttributesEnum.ConfirmNo.index();
    public static final int PROMOPRODUKID = AttributesEnum.PromoProdukId.index();
    public static final int BUDGETPOSTINGID = AttributesEnum.BudgetPostingId.index();
    public static final int KOMBINASIBUDGET = AttributesEnum.KombinasiBudget.index();
    public static final int BUDGETCUSTID = AttributesEnum.BudgetCustId.index();
    public static final int AMOUNT = AttributesEnum.Amount.index();
    public static final int BUDGETBYID = AttributesEnum.BudgetById.index();
    public static final int STATUS = AttributesEnum.Status.index();
    public static final int PROMOPRODUKID1 = AttributesEnum.PromoProdukId1.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CancelConfirmationClearBudgetViewRowImpl() {
    }

    /**
     * Gets Proposal entity object.
     * @return the Proposal
     */
    public ProposalImpl getProposal() {
        return (ProposalImpl)getEntity(0);
    }

    /**
     * Gets PromoProduk entity object.
     * @return the PromoProduk
     */
    public PromoProdukImpl getPromoProduk() {
        return (PromoProdukImpl)getEntity(1);
    }

    /**
     * Gets ProdBudgetBy entity object.
     * @return the ProdBudgetBy
     */
    public ProdBudgetByImpl getProdBudgetBy() {
        return (ProdBudgetByImpl)getEntity(2);
    }

    /**
     * Gets the attribute value for the calculated attribute ProposalNo.
     * @return the ProposalNo
     */
    public String getProposalNo() {
        return (String) getAttributeInternal(PROPOSALNO);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ProposalNo.
     * @param value value to set the  ProposalNo
     */
    public void setProposalNo(String value) {
        setAttributeInternal(PROPOSALNO, value);
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
     * Gets the attribute value for the calculated attribute ConfirmNo.
     * @return the ConfirmNo
     */
    public String getConfirmNo() {
        return (String) getAttributeInternal(CONFIRMNO);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ConfirmNo.
     * @param value value to set the  ConfirmNo
     */
    public void setConfirmNo(String value) {
        setAttributeInternal(CONFIRMNO, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Amount.
     * @return the Amount
     */
    public Number getAmount() {
        return (Number) getAttributeInternal(AMOUNT);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Amount.
     * @param value value to set the  Amount
     */
    public void setAmount(Number value) {
        setAttributeInternal(AMOUNT, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetById.
     * @return the BudgetById
     */
    public DBSequence getBudgetById() {
        return (DBSequence)getAttributeInternal(BUDGETBYID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetById.
     * @param value value to set the  BudgetById
     */
    public void setBudgetById(DBSequence value) {
        setAttributeInternal(BUDGETBYID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetCustId.
     * @return the BudgetCustId
     */
    public Number getBudgetCustId() {
        return (Number) getAttributeInternal(BUDGETCUSTID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetCustId.
     * @param value value to set the  BudgetCustId
     */
    public void setBudgetCustId(Number value) {
        setAttributeInternal(BUDGETCUSTID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetPostingId.
     * @return the BudgetPostingId
     */
    public Number getBudgetPostingId() {
        return (Number) getAttributeInternal(BUDGETPOSTINGID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetPostingId.
     * @param value value to set the  BudgetPostingId
     */
    public void setBudgetPostingId(Number value) {
        setAttributeInternal(BUDGETPOSTINGID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute KombinasiBudget.
     * @return the KombinasiBudget
     */
    public String getKombinasiBudget() {
        return (String) getAttributeInternal(KOMBINASIBUDGET);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute KombinasiBudget.
     * @param value value to set the  KombinasiBudget
     */
    public void setKombinasiBudget(String value) {
        setAttributeInternal(KOMBINASIBUDGET, value);
    }


    /**
     * Gets the attribute value for the calculated attribute PromoProdukId.
     * @return the PromoProdukId
     */
    public DBSequence getPromoProdukId() {
        return (DBSequence)getAttributeInternal(PROMOPRODUKID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute PromoProdukId.
     * @param value value to set the  PromoProdukId
     */
    public void setPromoProdukId(DBSequence value) {
        setAttributeInternal(PROMOPRODUKID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Status.
     * @return the Status
     */
    public String getStatus() {
        return (String) getAttributeInternal(STATUS);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Status.
     * @param value value to set the  Status
     */
    public void setStatus(String value) {
        setAttributeInternal(STATUS, value);
    }

    /**
     * Gets the attribute value for PROMO_PRODUK_ID using the alias name PromoProdukId1.
     * @return the PROMO_PRODUK_ID
     */
    public Number getPromoProdukId1() {
        return (Number) getAttributeInternal(PROMOPRODUKID1);
    }

    /**
     * Sets <code>value</code> as attribute value for PROMO_PRODUK_ID using the alias name PromoProdukId1.
     * @param value value to set the PROMO_PRODUK_ID
     */
    public void setPromoProdukId1(Number value) {
        setAttributeInternal(PROMOPRODUKID1, value);
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
