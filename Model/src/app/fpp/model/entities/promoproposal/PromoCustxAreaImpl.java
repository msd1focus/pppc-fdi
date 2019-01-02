package app.fpp.model.entities.promoproposal;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.SequenceImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Dec 17 01:25:01 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class PromoCustxAreaImpl extends EntityImpl {
    private static EntityDefImpl mDefinitionObject;

    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        PromoCustxAreaId {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getPromoCustxAreaId();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setPromoCustxAreaId((DBSequence)value);
            }
        }
        ,
        PromoProdukId {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getPromoProdukId();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setPromoProdukId((Number)value);
            }
        }
        ,
        RegionCode {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getRegionCode();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setRegionCode((String)value);
            }
        }
        ,
        RegionDesc {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getRegionDesc();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setRegionDesc((String)value);
            }
        }
        ,
        AreaCode {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getAreaCode();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setAreaCode((String)value);
            }
        }
        ,
        AreaDesc {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getAreaDesc();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setAreaDesc((String)value);
            }
        }
        ,
        LocCode {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getLocCode();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setLocCode((String)value);
            }
        }
        ,
        LocDesc {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getLocDesc();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setLocDesc((String)value);
            }
        }
        ,
        CusttypCode {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCusttypCode();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCusttypCode((String)value);
            }
        }
        ,
        CusttypDesc {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCusttypDesc();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCusttypDesc((String)value);
            }
        }
        ,
        CustgrpCode {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustgrpCode();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustgrpCode((String)value);
            }
        }
        ,
        CustgrpDesc {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustgrpDesc();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustgrpDesc((String)value);
            }
        }
        ,
        CustomerId {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustomerId();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustomerId((Number)value);
            }
        }
        ,
        CustomerName {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustomerName();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustomerName((String)value);
            }
        }
        ,
        CustomerNumber {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustomerNumber();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustomerNumber((String)value);
            }
        }
        ,
        CustxRegFlg {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustxRegFlg();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustxRegFlg((String)value);
            }
        }
        ,
        CustxNregFlg {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCustxNregFlg();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setCustxNregFlg((String)value);
            }
        }
        ,
        CreatedBy {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCreatedBy();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        CreatedOn {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getCreatedOn();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        PromoProduk {
            public Object get(PromoCustxAreaImpl obj) {
                return obj.getPromoProduk();
            }

            public void put(PromoCustxAreaImpl obj, Object value) {
                obj.setPromoProduk((PromoProdukImpl)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(PromoCustxAreaImpl object);

        public abstract void put(PromoCustxAreaImpl object, Object value);

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


    public static final int PROMOCUSTXAREAID = AttributesEnum.PromoCustxAreaId.index();
    public static final int PROMOPRODUKID = AttributesEnum.PromoProdukId.index();
    public static final int REGIONCODE = AttributesEnum.RegionCode.index();
    public static final int REGIONDESC = AttributesEnum.RegionDesc.index();
    public static final int AREACODE = AttributesEnum.AreaCode.index();
    public static final int AREADESC = AttributesEnum.AreaDesc.index();
    public static final int LOCCODE = AttributesEnum.LocCode.index();
    public static final int LOCDESC = AttributesEnum.LocDesc.index();
    public static final int CUSTTYPCODE = AttributesEnum.CusttypCode.index();
    public static final int CUSTTYPDESC = AttributesEnum.CusttypDesc.index();
    public static final int CUSTGRPCODE = AttributesEnum.CustgrpCode.index();
    public static final int CUSTGRPDESC = AttributesEnum.CustgrpDesc.index();
    public static final int CUSTOMERID = AttributesEnum.CustomerId.index();
    public static final int CUSTOMERNAME = AttributesEnum.CustomerName.index();
    public static final int CUSTOMERNUMBER = AttributesEnum.CustomerNumber.index();
    public static final int CUSTXREGFLG = AttributesEnum.CustxRegFlg.index();
    public static final int CUSTXNREGFLG = AttributesEnum.CustxNregFlg.index();
    public static final int CREATEDBY = AttributesEnum.CreatedBy.index();
    public static final int CREATEDON = AttributesEnum.CreatedOn.index();
    public static final int PROMOPRODUK = AttributesEnum.PromoProduk.index();

    /**
     * This is the default constructor (do not remove).
     */
    public PromoCustxAreaImpl() {
    }


    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = EntityDefImpl.findDefObject("app.fpp.model.entities.promoproposal.PromoCustxArea");
        }
        return mDefinitionObject;
    }

    /**
     * Gets the attribute value for PromoCustxAreaId, using the alias name PromoCustxAreaId.
     * @return the PromoCustxAreaId
     */
    public DBSequence getPromoCustxAreaId() {
        return (DBSequence)getAttributeInternal(PROMOCUSTXAREAID);
    }

    /**
     * Sets <code>value</code> as the attribute value for PromoCustxAreaId.
     * @param value value to set the PromoCustxAreaId
     */
    public void setPromoCustxAreaId(DBSequence value) {
        setAttributeInternal(PROMOCUSTXAREAID, value);
    }

    /**
     * Gets the attribute value for PromoProdukId, using the alias name PromoProdukId.
     * @return the PromoProdukId
     */
    public Number getPromoProdukId() {
        return (Number)getAttributeInternal(PROMOPRODUKID);
    }

    /**
     * Sets <code>value</code> as the attribute value for PromoProdukId.
     * @param value value to set the PromoProdukId
     */
    public void setPromoProdukId(Number value) {
        setAttributeInternal(PROMOPRODUKID, value);
    }

    /**
     * Gets the attribute value for RegionCode, using the alias name RegionCode.
     * @return the RegionCode
     */
    public String getRegionCode() {
        return (String)getAttributeInternal(REGIONCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for RegionCode.
     * @param value value to set the RegionCode
     */
    public void setRegionCode(String value) {
        setAttributeInternal(REGIONCODE, value);
    }

    /**
     * Gets the attribute value for RegionDesc, using the alias name RegionDesc.
     * @return the RegionDesc
     */
    public String getRegionDesc() {
        return (String)getAttributeInternal(REGIONDESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for RegionDesc.
     * @param value value to set the RegionDesc
     */
    public void setRegionDesc(String value) {
        setAttributeInternal(REGIONDESC, value);
    }

    /**
     * Gets the attribute value for AreaCode, using the alias name AreaCode.
     * @return the AreaCode
     */
    public String getAreaCode() {
        return (String)getAttributeInternal(AREACODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for AreaCode.
     * @param value value to set the AreaCode
     */
    public void setAreaCode(String value) {
        setAttributeInternal(AREACODE, value);
    }

    /**
     * Gets the attribute value for AreaDesc, using the alias name AreaDesc.
     * @return the AreaDesc
     */
    public String getAreaDesc() {
        return (String)getAttributeInternal(AREADESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for AreaDesc.
     * @param value value to set the AreaDesc
     */
    public void setAreaDesc(String value) {
        setAttributeInternal(AREADESC, value);
    }

    /**
     * Gets the attribute value for LocCode, using the alias name LocCode.
     * @return the LocCode
     */
    public String getLocCode() {
        return (String)getAttributeInternal(LOCCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for LocCode.
     * @param value value to set the LocCode
     */
    public void setLocCode(String value) {
        setAttributeInternal(LOCCODE, value);
    }

    /**
     * Gets the attribute value for LocDesc, using the alias name LocDesc.
     * @return the LocDesc
     */
    public String getLocDesc() {
        return (String)getAttributeInternal(LOCDESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for LocDesc.
     * @param value value to set the LocDesc
     */
    public void setLocDesc(String value) {
        setAttributeInternal(LOCDESC, value);
    }

    /**
     * Gets the attribute value for CusttypCode, using the alias name CusttypCode.
     * @return the CusttypCode
     */
    public String getCusttypCode() {
        return (String)getAttributeInternal(CUSTTYPCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for CusttypCode.
     * @param value value to set the CusttypCode
     */
    public void setCusttypCode(String value) {
        setAttributeInternal(CUSTTYPCODE, value);
    }

    /**
     * Gets the attribute value for CusttypDesc, using the alias name CusttypDesc.
     * @return the CusttypDesc
     */
    public String getCusttypDesc() {
        return (String)getAttributeInternal(CUSTTYPDESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for CusttypDesc.
     * @param value value to set the CusttypDesc
     */
    public void setCusttypDesc(String value) {
        setAttributeInternal(CUSTTYPDESC, value);
    }

    /**
     * Gets the attribute value for CustgrpCode, using the alias name CustgrpCode.
     * @return the CustgrpCode
     */
    public String getCustgrpCode() {
        return (String)getAttributeInternal(CUSTGRPCODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustgrpCode.
     * @param value value to set the CustgrpCode
     */
    public void setCustgrpCode(String value) {
        setAttributeInternal(CUSTGRPCODE, value);
    }

    /**
     * Gets the attribute value for CustgrpDesc, using the alias name CustgrpDesc.
     * @return the CustgrpDesc
     */
    public String getCustgrpDesc() {
        return (String)getAttributeInternal(CUSTGRPDESC);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustgrpDesc.
     * @param value value to set the CustgrpDesc
     */
    public void setCustgrpDesc(String value) {
        setAttributeInternal(CUSTGRPDESC, value);
    }

    /**
     * Gets the attribute value for CustomerId, using the alias name CustomerId.
     * @return the CustomerId
     */
    public Number getCustomerId() {
        return (Number)getAttributeInternal(CUSTOMERID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustomerId.
     * @param value value to set the CustomerId
     */
    public void setCustomerId(Number value) {
        setAttributeInternal(CUSTOMERID, value);
    }

    /**
     * Gets the attribute value for CustomerName, using the alias name CustomerName.
     * @return the CustomerName
     */
    public String getCustomerName() {
        return (String)getAttributeInternal(CUSTOMERNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustomerName.
     * @param value value to set the CustomerName
     */
    public void setCustomerName(String value) {
        setAttributeInternal(CUSTOMERNAME, value);
    }

    /**
     * Gets the attribute value for CustomerNumber, using the alias name CustomerNumber.
     * @return the CustomerNumber
     */
    public String getCustomerNumber() {
        return (String)getAttributeInternal(CUSTOMERNUMBER);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustomerNumber.
     * @param value value to set the CustomerNumber
     */
    public void setCustomerNumber(String value) {
        setAttributeInternal(CUSTOMERNUMBER, value);
    }

    /**
     * Gets the attribute value for CustxRegFlg, using the alias name CustxRegFlg.
     * @return the CustxRegFlg
     */
    public String getCustxRegFlg() {
        return (String)getAttributeInternal(CUSTXREGFLG);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustxRegFlg.
     * @param value value to set the CustxRegFlg
     */
    public void setCustxRegFlg(String value) {
        setAttributeInternal(CUSTXREGFLG, value);
    }

    /**
     * Gets the attribute value for CustxNregFlg, using the alias name CustxNregFlg.
     * @return the CustxNregFlg
     */
    public String getCustxNregFlg() {
        return (String)getAttributeInternal(CUSTXNREGFLG);
    }

    /**
     * Sets <code>value</code> as the attribute value for CustxNregFlg.
     * @param value value to set the CustxNregFlg
     */
    public void setCustxNregFlg(String value) {
        setAttributeInternal(CUSTXNREGFLG, value);
    }

    /**
     * Gets the attribute value for CreatedBy, using the alias name CreatedBy.
     * @return the CreatedBy
     */
    public String getCreatedBy() {
        return (String)getAttributeInternal(CREATEDBY);
    }

    /**
     * Gets the attribute value for CreatedOn, using the alias name CreatedOn.
     * @return the CreatedOn
     */
    public Date getCreatedOn() {
        return (Date)getAttributeInternal(CREATEDON);
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

    /**
     * @return the associated entity PromoProdukImpl.
     */
    public PromoProdukImpl getPromoProduk() {
        return (PromoProdukImpl)getAttributeInternal(PROMOPRODUK);
    }

    /**
     * Sets <code>value</code> as the associated entity PromoProdukImpl.
     */
    public void setPromoProduk(PromoProdukImpl value) {
        setAttributeInternal(PROMOPRODUK, value);
    }


    /**
     * @param promoCustxAreaId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(DBSequence promoCustxAreaId) {
        return new Key(new Object[]{promoCustxAreaId});
    }

    /**
     * Add attribute defaulting logic in this method.
     * @param attributeList list of attribute names/values to initialize the row
     */
    protected void create(AttributeList attributeList) {
        super.create(attributeList);
        SequenceImpl seq = new SequenceImpl("PROMO_CUSTX_AREA_SEQ", getDBTransaction());
        this.setPromoCustxAreaId(new DBSequence(seq.getSequenceNumber()));
    }
}
