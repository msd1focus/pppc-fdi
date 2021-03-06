package app.fpp.model.views.promoproposal.validation;

import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Jan 22 21:42:00 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProdRegionCustTypeValidationRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        PromoProdukId {
            public Object get(ProdRegionCustTypeValidationRowImpl obj) {
                return obj.getPromoProdukId();
            }

            public void put(ProdRegionCustTypeValidationRowImpl obj,
                            Object value) {
                obj.setPromoProdukId((Number)value);
            }
        }
        ,
        CustType {
            public Object get(ProdRegionCustTypeValidationRowImpl obj) {
                return obj.getCustType();
            }

            public void put(ProdRegionCustTypeValidationRowImpl obj,
                            Object value) {
                obj.setCustType((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(ProdRegionCustTypeValidationRowImpl object);

        public abstract void put(ProdRegionCustTypeValidationRowImpl object,
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
    public static final int PROMOPRODUKID = AttributesEnum.PromoProdukId.index();
    public static final int CUSTTYPE = AttributesEnum.CustType.index();

    /**
     * This is the default constructor (do not remove).
     */
    public ProdRegionCustTypeValidationRowImpl() {
    }

    /**
     * Gets ProdRegionCustType entity object.
     * @return the ProdRegionCustType
     */
    public EntityImpl getProdRegionCustType() {
        return (EntityImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for PROMO_PRODUK_ID using the alias name PromoProdukId.
     * @return the PROMO_PRODUK_ID
     */
    public Number getPromoProdukId() {
        return (Number) getAttributeInternal(PROMOPRODUKID);
    }

    /**
     * Sets <code>value</code> as attribute value for PROMO_PRODUK_ID using the alias name PromoProdukId.
     * @param value value to set the PROMO_PRODUK_ID
     */
    public void setPromoProdukId(Number value) {
        setAttributeInternal(PROMOPRODUKID, value);
    }

    /**
     * Gets the attribute value for CUST_TYPE using the alias name CustType.
     * @return the CUST_TYPE
     */
    public String getCustType() {
        return (String) getAttributeInternal(CUSTTYPE);
    }

    /**
     * Sets <code>value</code> as attribute value for CUST_TYPE using the alias name CustType.
     * @param value value to set the CUST_TYPE
     */
    public void setCustType(String value) {
        setAttributeInternal(CUSTTYPE, value);
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
