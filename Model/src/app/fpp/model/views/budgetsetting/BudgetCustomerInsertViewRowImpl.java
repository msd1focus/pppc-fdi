package app.fpp.model.views.budgetsetting;

import app.fpp.model.entities.budgetsetting.BudgetCustomerImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Jul 12 16:47:18 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class BudgetCustomerInsertViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        BudgetAsToDate {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetAsToDate();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetAsToDate((Number)value);
            }
        }
        ,
        BudgetAsToDateUsed {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetAsToDateUsed();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetAsToDateUsed((Number)value);
            }
        }
        ,
        BudgetBrand {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetBrand();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetBrand((String)value);
            }
        }
        ,
        BudgetCategory {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetCategory();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetCategory((String)value);
            }
        }
        ,
        BudgetClass {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetClass();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetClass((String)value);
            }
        }
        ,
        BudgetCustHdrId {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetCustHdrId();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetCustHdrId((Number)value);
            }
        }
        ,
        BudgetCustomerId {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetCustomerId();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetCustomerId((DBSequence)value);
            }
        }
        ,
        BudgetCustomerIdref {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetCustomerIdref();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetCustomerIdref((Number)value);
            }
        }
        ,
        BudgetExtention {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetExtention();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetExtention((String)value);
            }
        }
        ,
        BudgetPackaging {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetPackaging();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetPackaging((String)value);
            }
        }
        ,
        BudgetVariant {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getBudgetVariant();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setBudgetVariant((String)value);
            }
        }
        ,
        CreatedBy {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getCreatedBy();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setCreatedBy((String)value);
            }
        }
        ,
        CreationDate {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getCreationDate();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setCreationDate((Date)value);
            }
        }
        ,
        ModifiedBy {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getModifiedBy();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setModifiedBy((String)value);
            }
        }
        ,
        ModifiedOn {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getModifiedOn();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setModifiedOn((Date)value);
            }
        }
        ,
        OverBudget {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getOverBudget();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setOverBudget((Number)value);
            }
        }
        ,
        Status {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getStatus();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setStatus((String)value);
            }
        }
        ,
        YearlyBudgetAmount {
            public Object get(BudgetCustomerInsertViewRowImpl obj) {
                return obj.getYearlyBudgetAmount();
            }

            public void put(BudgetCustomerInsertViewRowImpl obj,
                            Object value) {
                obj.setYearlyBudgetAmount((Number)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(BudgetCustomerInsertViewRowImpl object);

        public abstract void put(BudgetCustomerInsertViewRowImpl object,
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


    public static final int BUDGETASTODATE = AttributesEnum.BudgetAsToDate.index();
    public static final int BUDGETASTODATEUSED = AttributesEnum.BudgetAsToDateUsed.index();
    public static final int BUDGETBRAND = AttributesEnum.BudgetBrand.index();
    public static final int BUDGETCATEGORY = AttributesEnum.BudgetCategory.index();
    public static final int BUDGETCLASS = AttributesEnum.BudgetClass.index();
    public static final int BUDGETCUSTHDRID = AttributesEnum.BudgetCustHdrId.index();
    public static final int BUDGETCUSTOMERID = AttributesEnum.BudgetCustomerId.index();
    public static final int BUDGETCUSTOMERIDREF = AttributesEnum.BudgetCustomerIdref.index();
    public static final int BUDGETEXTENTION = AttributesEnum.BudgetExtention.index();
    public static final int BUDGETPACKAGING = AttributesEnum.BudgetPackaging.index();
    public static final int BUDGETVARIANT = AttributesEnum.BudgetVariant.index();
    public static final int CREATEDBY = AttributesEnum.CreatedBy.index();
    public static final int CREATIONDATE = AttributesEnum.CreationDate.index();
    public static final int MODIFIEDBY = AttributesEnum.ModifiedBy.index();
    public static final int MODIFIEDON = AttributesEnum.ModifiedOn.index();
    public static final int OVERBUDGET = AttributesEnum.OverBudget.index();
    public static final int STATUS = AttributesEnum.Status.index();
    public static final int YEARLYBUDGETAMOUNT = AttributesEnum.YearlyBudgetAmount.index();

    /**
     * This is the default constructor (do not remove).
     */
    public BudgetCustomerInsertViewRowImpl() {
    }

    /**
     * Gets BudgetCustomer entity object.
     * @return the BudgetCustomer
     */
    public BudgetCustomerImpl getBudgetCustomer() {
        return (BudgetCustomerImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for BUDGET_AS_TO_DATE using the alias name BudgetAsToDate.
     * @return the BUDGET_AS_TO_DATE
     */
    public Number getBudgetAsToDate() {
        return (Number) getAttributeInternal(BUDGETASTODATE);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_AS_TO_DATE using the alias name BudgetAsToDate.
     * @param value value to set the BUDGET_AS_TO_DATE
     */
    public void setBudgetAsToDate(Number value) {
        setAttributeInternal(BUDGETASTODATE, value);
    }

    /**
     * Gets the attribute value for BUDGET_AS_TO_DATE_USED using the alias name BudgetAsToDateUsed.
     * @return the BUDGET_AS_TO_DATE_USED
     */
    public Number getBudgetAsToDateUsed() {
        return (Number) getAttributeInternal(BUDGETASTODATEUSED);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_AS_TO_DATE_USED using the alias name BudgetAsToDateUsed.
     * @param value value to set the BUDGET_AS_TO_DATE_USED
     */
    public void setBudgetAsToDateUsed(Number value) {
        setAttributeInternal(BUDGETASTODATEUSED, value);
    }

    /**
     * Gets the attribute value for BUDGET_BRAND using the alias name BudgetBrand.
     * @return the BUDGET_BRAND
     */
    public String getBudgetBrand() {
        return (String) getAttributeInternal(BUDGETBRAND);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_BRAND using the alias name BudgetBrand.
     * @param value value to set the BUDGET_BRAND
     */
    public void setBudgetBrand(String value) {
        setAttributeInternal(BUDGETBRAND, value);
    }

    /**
     * Gets the attribute value for BUDGET_CATEGORY using the alias name BudgetCategory.
     * @return the BUDGET_CATEGORY
     */
    public String getBudgetCategory() {
        return (String) getAttributeInternal(BUDGETCATEGORY);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_CATEGORY using the alias name BudgetCategory.
     * @param value value to set the BUDGET_CATEGORY
     */
    public void setBudgetCategory(String value) {
        setAttributeInternal(BUDGETCATEGORY, value);
    }

    /**
     * Gets the attribute value for BUDGET_CLASS using the alias name BudgetClass.
     * @return the BUDGET_CLASS
     */
    public String getBudgetClass() {
        return (String) getAttributeInternal(BUDGETCLASS);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_CLASS using the alias name BudgetClass.
     * @param value value to set the BUDGET_CLASS
     */
    public void setBudgetClass(String value) {
        setAttributeInternal(BUDGETCLASS, value);
    }

    /**
     * Gets the attribute value for BUDGET_CUST_HDR_ID using the alias name BudgetCustHdrId.
     * @return the BUDGET_CUST_HDR_ID
     */
    public Number getBudgetCustHdrId() {
        return (Number) getAttributeInternal(BUDGETCUSTHDRID);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_CUST_HDR_ID using the alias name BudgetCustHdrId.
     * @param value value to set the BUDGET_CUST_HDR_ID
     */
    public void setBudgetCustHdrId(Number value) {
        setAttributeInternal(BUDGETCUSTHDRID, value);
    }

    /**
     * Gets the attribute value for BUDGET_CUSTOMER_ID using the alias name BudgetCustomerId.
     * @return the BUDGET_CUSTOMER_ID
     */
    public DBSequence getBudgetCustomerId() {
        return (DBSequence)getAttributeInternal(BUDGETCUSTOMERID);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_CUSTOMER_ID using the alias name BudgetCustomerId.
     * @param value value to set the BUDGET_CUSTOMER_ID
     */
    public void setBudgetCustomerId(DBSequence value) {
        setAttributeInternal(BUDGETCUSTOMERID, value);
    }

    /**
     * Gets the attribute value for BUDGET_CUSTOMER_IDREF using the alias name BudgetCustomerIdref.
     * @return the BUDGET_CUSTOMER_IDREF
     */
    public Number getBudgetCustomerIdref() {
        return (Number) getAttributeInternal(BUDGETCUSTOMERIDREF);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_CUSTOMER_IDREF using the alias name BudgetCustomerIdref.
     * @param value value to set the BUDGET_CUSTOMER_IDREF
     */
    public void setBudgetCustomerIdref(Number value) {
        setAttributeInternal(BUDGETCUSTOMERIDREF, value);
    }

    /**
     * Gets the attribute value for BUDGET_EXTENTION using the alias name BudgetExtention.
     * @return the BUDGET_EXTENTION
     */
    public String getBudgetExtention() {
        return (String) getAttributeInternal(BUDGETEXTENTION);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_EXTENTION using the alias name BudgetExtention.
     * @param value value to set the BUDGET_EXTENTION
     */
    public void setBudgetExtention(String value) {
        setAttributeInternal(BUDGETEXTENTION, value);
    }

    /**
     * Gets the attribute value for BUDGET_PACKAGING using the alias name BudgetPackaging.
     * @return the BUDGET_PACKAGING
     */
    public String getBudgetPackaging() {
        return (String) getAttributeInternal(BUDGETPACKAGING);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_PACKAGING using the alias name BudgetPackaging.
     * @param value value to set the BUDGET_PACKAGING
     */
    public void setBudgetPackaging(String value) {
        setAttributeInternal(BUDGETPACKAGING, value);
    }

    /**
     * Gets the attribute value for BUDGET_VARIANT using the alias name BudgetVariant.
     * @return the BUDGET_VARIANT
     */
    public String getBudgetVariant() {
        return (String) getAttributeInternal(BUDGETVARIANT);
    }

    /**
     * Sets <code>value</code> as attribute value for BUDGET_VARIANT using the alias name BudgetVariant.
     * @param value value to set the BUDGET_VARIANT
     */
    public void setBudgetVariant(String value) {
        setAttributeInternal(BUDGETVARIANT, value);
    }

    /**
     * Gets the attribute value for CREATED_BY using the alias name CreatedBy.
     * @return the CREATED_BY
     */
    public String getCreatedBy() {
        return (String) getAttributeInternal(CREATEDBY);
    }

    /**
     * Sets <code>value</code> as attribute value for CREATED_BY using the alias name CreatedBy.
     * @param value value to set the CREATED_BY
     */
    public void setCreatedBy(String value) {
        setAttributeInternal(CREATEDBY, value);
    }

    /**
     * Gets the attribute value for CREATION_DATE using the alias name CreationDate.
     * @return the CREATION_DATE
     */
    public Date getCreationDate() {
        return (Date) getAttributeInternal(CREATIONDATE);
    }

    /**
     * Sets <code>value</code> as attribute value for CREATION_DATE using the alias name CreationDate.
     * @param value value to set the CREATION_DATE
     */
    public void setCreationDate(Date value) {
        setAttributeInternal(CREATIONDATE, value);
    }

    /**
     * Gets the attribute value for MODIFIED_BY using the alias name ModifiedBy.
     * @return the MODIFIED_BY
     */
    public String getModifiedBy() {
        return (String) getAttributeInternal(MODIFIEDBY);
    }

    /**
     * Sets <code>value</code> as attribute value for MODIFIED_BY using the alias name ModifiedBy.
     * @param value value to set the MODIFIED_BY
     */
    public void setModifiedBy(String value) {
        setAttributeInternal(MODIFIEDBY, value);
    }

    /**
     * Gets the attribute value for MODIFIED_ON using the alias name ModifiedOn.
     * @return the MODIFIED_ON
     */
    public Date getModifiedOn() {
        return (Date) getAttributeInternal(MODIFIEDON);
    }

    /**
     * Sets <code>value</code> as attribute value for MODIFIED_ON using the alias name ModifiedOn.
     * @param value value to set the MODIFIED_ON
     */
    public void setModifiedOn(Date value) {
        setAttributeInternal(MODIFIEDON, value);
    }

    /**
     * Gets the attribute value for OVER_BUDGET using the alias name OverBudget.
     * @return the OVER_BUDGET
     */
    public Number getOverBudget() {
        return (Number) getAttributeInternal(OVERBUDGET);
    }

    /**
     * Sets <code>value</code> as attribute value for OVER_BUDGET using the alias name OverBudget.
     * @param value value to set the OVER_BUDGET
     */
    public void setOverBudget(Number value) {
        setAttributeInternal(OVERBUDGET, value);
    }

    /**
     * Gets the attribute value for STATUS using the alias name Status.
     * @return the STATUS
     */
    public String getStatus() {
        return (String) getAttributeInternal(STATUS);
    }

    /**
     * Sets <code>value</code> as attribute value for STATUS using the alias name Status.
     * @param value value to set the STATUS
     */
    public void setStatus(String value) {
        setAttributeInternal(STATUS, value);
    }

    /**
     * Gets the attribute value for YEARLY_BUDGET_AMOUNT using the alias name YearlyBudgetAmount.
     * @return the YEARLY_BUDGET_AMOUNT
     */
    public Number getYearlyBudgetAmount() {
        return (Number) getAttributeInternal(YEARLYBUDGETAMOUNT);
    }

    /**
     * Sets <code>value</code> as attribute value for YEARLY_BUDGET_AMOUNT using the alias name YearlyBudgetAmount.
     * @param value value to set the YEARLY_BUDGET_AMOUNT
     */
    public void setYearlyBudgetAmount(Number value) {
        setAttributeInternal(YEARLYBUDGETAMOUNT, value);
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
