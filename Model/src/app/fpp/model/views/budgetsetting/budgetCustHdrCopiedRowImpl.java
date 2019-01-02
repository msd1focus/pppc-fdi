package app.fpp.model.views.budgetsetting;

import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed Aug 08 13:27:16 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class budgetCustHdrCopiedRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        BudgetCustHdrId {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getBudgetCustHdrId();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setBudgetCustHdrId((Number)value);
            }
        }
        ,
        CustomerId {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getCustomerId();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setCustomerId((String)value);
            }
        }
        ,
        KodePosting {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getKodePosting();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setKodePosting((String)value);
            }
        }
        ,
        BudgetType {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getBudgetType();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setBudgetType((String)value);
            }
        }
        ,
        BudgetYear {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getBudgetYear();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setBudgetYear((String)value);
            }
        }
        ,
        CreatedBy {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getCreatedBy();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setCreatedBy((String)value);
            }
        }
        ,
        CreationDate {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getCreationDate();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setCreationDate((Date)value);
            }
        }
        ,
        ModifiedBy {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getModifiedBy();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setModifiedBy((String)value);
            }
        }
        ,
        ModifiedOn {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getModifiedOn();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setModifiedOn((Date)value);
            }
        }
        ,
        CustHdrIdref {
            public Object get(budgetCustHdrCopiedRowImpl obj) {
                return obj.getCustHdrIdref();
            }

            public void put(budgetCustHdrCopiedRowImpl obj, Object value) {
                obj.setCustHdrIdref((Number)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(budgetCustHdrCopiedRowImpl object);

        public abstract void put(budgetCustHdrCopiedRowImpl object,
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
    public static final int BUDGETCUSTHDRID = AttributesEnum.BudgetCustHdrId.index();
    public static final int CUSTOMERID = AttributesEnum.CustomerId.index();
    public static final int KODEPOSTING = AttributesEnum.KodePosting.index();
    public static final int BUDGETTYPE = AttributesEnum.BudgetType.index();
    public static final int BUDGETYEAR = AttributesEnum.BudgetYear.index();
    public static final int CREATEDBY = AttributesEnum.CreatedBy.index();
    public static final int CREATIONDATE = AttributesEnum.CreationDate.index();
    public static final int MODIFIEDBY = AttributesEnum.ModifiedBy.index();
    public static final int MODIFIEDON = AttributesEnum.ModifiedOn.index();
    public static final int CUSTHDRIDREF = AttributesEnum.CustHdrIdref.index();

    /**
     * This is the default constructor (do not remove).
     */
    public budgetCustHdrCopiedRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetCustHdrId.
     * @return the BudgetCustHdrId
     */
    public Number getBudgetCustHdrId() {
        return (Number) getAttributeInternal(BUDGETCUSTHDRID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetCustHdrId.
     * @param value value to set the  BudgetCustHdrId
     */
    public void setBudgetCustHdrId(Number value) {
        setAttributeInternal(BUDGETCUSTHDRID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CustomerId.
     * @return the CustomerId
     */
    public String getCustomerId() {
        return (String) getAttributeInternal(CUSTOMERID);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CustomerId.
     * @param value value to set the  CustomerId
     */
    public void setCustomerId(String value) {
        setAttributeInternal(CUSTOMERID, value);
    }

    /**
     * Gets the attribute value for the calculated attribute KodePosting.
     * @return the KodePosting
     */
    public String getKodePosting() {
        return (String) getAttributeInternal(KODEPOSTING);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute KodePosting.
     * @param value value to set the  KodePosting
     */
    public void setKodePosting(String value) {
        setAttributeInternal(KODEPOSTING, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetType.
     * @return the BudgetType
     */
    public String getBudgetType() {
        return (String) getAttributeInternal(BUDGETTYPE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetType.
     * @param value value to set the  BudgetType
     */
    public void setBudgetType(String value) {
        setAttributeInternal(BUDGETTYPE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute BudgetYear.
     * @return the BudgetYear
     */
    public String getBudgetYear() {
        return (String) getAttributeInternal(BUDGETYEAR);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute BudgetYear.
     * @param value value to set the  BudgetYear
     */
    public void setBudgetYear(String value) {
        setAttributeInternal(BUDGETYEAR, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CreatedBy.
     * @return the CreatedBy
     */
    public String getCreatedBy() {
        return (String) getAttributeInternal(CREATEDBY);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CreatedBy.
     * @param value value to set the  CreatedBy
     */
    public void setCreatedBy(String value) {
        setAttributeInternal(CREATEDBY, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CreationDate.
     * @return the CreationDate
     */
    public Date getCreationDate() {
        return (Date) getAttributeInternal(CREATIONDATE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CreationDate.
     * @param value value to set the  CreationDate
     */
    public void setCreationDate(Date value) {
        setAttributeInternal(CREATIONDATE, value);
    }

    /**
     * Gets the attribute value for the calculated attribute ModifiedBy.
     * @return the ModifiedBy
     */
    public String getModifiedBy() {
        return (String) getAttributeInternal(MODIFIEDBY);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ModifiedBy.
     * @param value value to set the  ModifiedBy
     */
    public void setModifiedBy(String value) {
        setAttributeInternal(MODIFIEDBY, value);
    }

    /**
     * Gets the attribute value for the calculated attribute ModifiedOn.
     * @return the ModifiedOn
     */
    public Date getModifiedOn() {
        return (Date) getAttributeInternal(MODIFIEDON);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute ModifiedOn.
     * @param value value to set the  ModifiedOn
     */
    public void setModifiedOn(Date value) {
        setAttributeInternal(MODIFIEDON, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CustHdrIdref.
     * @return the CustHdrIdref
     */
    public Number getCustHdrIdref() {
        return (Number) getAttributeInternal(CUSTHDRIDREF);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CustHdrIdref.
     * @param value value to set the  CustHdrIdref
     */
    public void setCustHdrIdref(Number value) {
        setAttributeInternal(CUSTHDRIDREF, value);
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
