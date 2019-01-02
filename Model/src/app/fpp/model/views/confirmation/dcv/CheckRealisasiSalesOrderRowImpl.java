package app.fpp.model.views.confirmation.dcv;

import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Feb 19 13:25:33 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CheckRealisasiSalesOrderRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        ConfirmNo {
            public Object get(CheckRealisasiSalesOrderRowImpl obj) {
                return obj.getConfirmNo();
            }

            public void put(CheckRealisasiSalesOrderRowImpl obj,
                            Object value) {
                obj.setConfirmNo((String)value);
            }
        }
        ,
        OrderKet {
            public Object get(CheckRealisasiSalesOrderRowImpl obj) {
                return obj.getOrderKet();
            }

            public void put(CheckRealisasiSalesOrderRowImpl obj,
                            Object value) {
                obj.setOrderKet((String)value);
            }
        }
        ,
        ProposalNo {
            public Object get(CheckRealisasiSalesOrderRowImpl obj) {
                return obj.getProposalNo();
            }

            public void put(CheckRealisasiSalesOrderRowImpl obj,
                            Object value) {
                obj.setProposalNo((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(CheckRealisasiSalesOrderRowImpl object);

        public abstract void put(CheckRealisasiSalesOrderRowImpl object,
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
    public static final int CONFIRMNO = AttributesEnum.ConfirmNo.index();
    public static final int ORDERKET = AttributesEnum.OrderKet.index();
    public static final int PROPOSALNO = AttributesEnum.ProposalNo.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CheckRealisasiSalesOrderRowImpl() {
    }

    /**
     * Gets FcsViewRealisasiSalesorder entity object.
     * @return the FcsViewRealisasiSalesorder
     */
    public EntityImpl getFcsViewRealisasiSalesorder() {
        return (EntityImpl)getEntity(0);
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
     * Gets the attribute value for ORDER_KET using the alias name OrderKet.
     * @return the ORDER_KET
     */
    public String getOrderKet() {
        return (String) getAttributeInternal(ORDERKET);
    }

    /**
     * Sets <code>value</code> as attribute value for ORDER_KET using the alias name OrderKet.
     * @param value value to set the ORDER_KET
     */
    public void setOrderKet(String value) {
        setAttributeInternal(ORDERKET, value);
    }

    /**
     * Gets the attribute value for PROPOSAL_NO using the alias name ProposalNo.
     * @return the PROPOSAL_NO
     */
    public String getProposalNo() {
        return (String) getAttributeInternal(PROPOSALNO);
    }

    /**
     * Sets <code>value</code> as attribute value for PROPOSAL_NO using the alias name ProposalNo.
     * @param value value to set the PROPOSAL_NO
     */
    public void setProposalNo(String value) {
        setAttributeInternal(PROPOSALNO, value);
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
