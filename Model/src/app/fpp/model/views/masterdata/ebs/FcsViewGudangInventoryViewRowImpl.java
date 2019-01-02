package app.fpp.model.views.masterdata.ebs;

import oracle.jbo.domain.Number;
import oracle.jbo.domain.RowID;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Oct 09 09:09:29 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class FcsViewGudangInventoryViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        OrgName {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getOrgName();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setOrgName((String)value);
            }
        }
        ,
        ShortCode {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getShortCode();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setShortCode((String)value);
            }
        }
        ,
        OrgId {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getOrgId();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setOrgId((Number)value);
            }
        }
        ,
        SetOfBooksId {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getSetOfBooksId();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setSetOfBooksId((String)value);
            }
        }
        ,
        BussinessGroupId {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getBussinessGroupId();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setBussinessGroupId((Number)value);
            }
        }
        ,
        InventoryOrganizationName {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getInventoryOrganizationName();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setInventoryOrganizationName((String)value);
            }
        }
        ,
        OrganizationCode {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getOrganizationCode();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setOrganizationCode((String)value);
            }
        }
        ,
        OrganizationId {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getOrganizationId();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setOrganizationId((Number)value);
            }
        }
        ,
        ChartOfAccountId {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getChartOfAccountId();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setChartOfAccountId((Number)value);
            }
        }
        ,
        SecondaryInventoryName {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getSecondaryInventoryName();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setSecondaryInventoryName((String)value);
            }
        }
        ,
        Description {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getDescription();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setDescription((String)value);
            }
        }
        ,
        FlagBjp {
            public Object get(FcsViewGudangInventoryViewRowImpl obj) {
                return obj.getFlagBjp();
            }

            public void put(FcsViewGudangInventoryViewRowImpl obj,
                            Object value) {
                obj.setFlagBjp((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(FcsViewGudangInventoryViewRowImpl object);

        public abstract void put(FcsViewGudangInventoryViewRowImpl object,
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

    public static final int ORGNAME = AttributesEnum.OrgName.index();
    public static final int SHORTCODE = AttributesEnum.ShortCode.index();
    public static final int ORGID = AttributesEnum.OrgId.index();
    public static final int SETOFBOOKSID = AttributesEnum.SetOfBooksId.index();
    public static final int BUSSINESSGROUPID = AttributesEnum.BussinessGroupId.index();
    public static final int INVENTORYORGANIZATIONNAME = AttributesEnum.InventoryOrganizationName.index();
    public static final int ORGANIZATIONCODE = AttributesEnum.OrganizationCode.index();
    public static final int ORGANIZATIONID = AttributesEnum.OrganizationId.index();
    public static final int CHARTOFACCOUNTID = AttributesEnum.ChartOfAccountId.index();
    public static final int SECONDARYINVENTORYNAME = AttributesEnum.SecondaryInventoryName.index();
    public static final int DESCRIPTION = AttributesEnum.Description.index();
    public static final int FLAGBJP = AttributesEnum.FlagBjp.index();

    /**
     * This is the default constructor (do not remove).
     */
    public FcsViewGudangInventoryViewRowImpl() {
    }

    /**
     * Gets FcsViewGudangInventory entity object.
     * @return the FcsViewGudangInventory
     */
    public EntityImpl getFcsViewGudangInventory() {
        return (EntityImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for ORG_NAME using the alias name OrgName.
     * @return the ORG_NAME
     */
    public String getOrgName() {
        return (String) getAttributeInternal(ORGNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for ORG_NAME using the alias name OrgName.
     * @param value value to set the ORG_NAME
     */
    public void setOrgName(String value) {
        setAttributeInternal(ORGNAME, value);
    }

    /**
     * Gets the attribute value for SHORT_CODE using the alias name ShortCode.
     * @return the SHORT_CODE
     */
    public String getShortCode() {
        return (String) getAttributeInternal(SHORTCODE);
    }

    /**
     * Sets <code>value</code> as attribute value for SHORT_CODE using the alias name ShortCode.
     * @param value value to set the SHORT_CODE
     */
    public void setShortCode(String value) {
        setAttributeInternal(SHORTCODE, value);
    }

    /**
     * Gets the attribute value for ORG_ID using the alias name OrgId.
     * @return the ORG_ID
     */
    public Number getOrgId() {
        return (Number) getAttributeInternal(ORGID);
    }

    /**
     * Sets <code>value</code> as attribute value for ORG_ID using the alias name OrgId.
     * @param value value to set the ORG_ID
     */
    public void setOrgId(Number value) {
        setAttributeInternal(ORGID, value);
    }

    /**
     * Gets the attribute value for SET_OF_BOOKS_ID using the alias name SetOfBooksId.
     * @return the SET_OF_BOOKS_ID
     */
    public String getSetOfBooksId() {
        return (String) getAttributeInternal(SETOFBOOKSID);
    }

    /**
     * Sets <code>value</code> as attribute value for SET_OF_BOOKS_ID using the alias name SetOfBooksId.
     * @param value value to set the SET_OF_BOOKS_ID
     */
    public void setSetOfBooksId(String value) {
        setAttributeInternal(SETOFBOOKSID, value);
    }

    /**
     * Gets the attribute value for BUSSINESS_GROUP_ID using the alias name BussinessGroupId.
     * @return the BUSSINESS_GROUP_ID
     */
    public Number getBussinessGroupId() {
        return (Number) getAttributeInternal(BUSSINESSGROUPID);
    }

    /**
     * Sets <code>value</code> as attribute value for BUSSINESS_GROUP_ID using the alias name BussinessGroupId.
     * @param value value to set the BUSSINESS_GROUP_ID
     */
    public void setBussinessGroupId(Number value) {
        setAttributeInternal(BUSSINESSGROUPID, value);
    }

    /**
     * Gets the attribute value for INVENTORY_ORGANIZATION_NAME using the alias name InventoryOrganizationName.
     * @return the INVENTORY_ORGANIZATION_NAME
     */
    public String getInventoryOrganizationName() {
        return (String) getAttributeInternal(INVENTORYORGANIZATIONNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for INVENTORY_ORGANIZATION_NAME using the alias name InventoryOrganizationName.
     * @param value value to set the INVENTORY_ORGANIZATION_NAME
     */
    public void setInventoryOrganizationName(String value) {
        setAttributeInternal(INVENTORYORGANIZATIONNAME, value);
    }

    /**
     * Gets the attribute value for ORGANIZATION_CODE using the alias name OrganizationCode.
     * @return the ORGANIZATION_CODE
     */
    public String getOrganizationCode() {
        return (String) getAttributeInternal(ORGANIZATIONCODE);
    }

    /**
     * Sets <code>value</code> as attribute value for ORGANIZATION_CODE using the alias name OrganizationCode.
     * @param value value to set the ORGANIZATION_CODE
     */
    public void setOrganizationCode(String value) {
        setAttributeInternal(ORGANIZATIONCODE, value);
    }

    /**
     * Gets the attribute value for ORGANIZATION_ID using the alias name OrganizationId.
     * @return the ORGANIZATION_ID
     */
    public Number getOrganizationId() {
        return (Number) getAttributeInternal(ORGANIZATIONID);
    }

    /**
     * Sets <code>value</code> as attribute value for ORGANIZATION_ID using the alias name OrganizationId.
     * @param value value to set the ORGANIZATION_ID
     */
    public void setOrganizationId(Number value) {
        setAttributeInternal(ORGANIZATIONID, value);
    }

    /**
     * Gets the attribute value for CHART_OF_ACCOUNT_ID using the alias name ChartOfAccountId.
     * @return the CHART_OF_ACCOUNT_ID
     */
    public Number getChartOfAccountId() {
        return (Number) getAttributeInternal(CHARTOFACCOUNTID);
    }

    /**
     * Sets <code>value</code> as attribute value for CHART_OF_ACCOUNT_ID using the alias name ChartOfAccountId.
     * @param value value to set the CHART_OF_ACCOUNT_ID
     */
    public void setChartOfAccountId(Number value) {
        setAttributeInternal(CHARTOFACCOUNTID, value);
    }

    /**
     * Gets the attribute value for SECONDARY_INVENTORY_NAME using the alias name SecondaryInventoryName.
     * @return the SECONDARY_INVENTORY_NAME
     */
    public String getSecondaryInventoryName() {
        return (String) getAttributeInternal(SECONDARYINVENTORYNAME);
    }

    /**
     * Sets <code>value</code> as attribute value for SECONDARY_INVENTORY_NAME using the alias name SecondaryInventoryName.
     * @param value value to set the SECONDARY_INVENTORY_NAME
     */
    public void setSecondaryInventoryName(String value) {
        setAttributeInternal(SECONDARYINVENTORYNAME, value);
    }

    /**
     * Gets the attribute value for DESCRIPTION using the alias name Description.
     * @return the DESCRIPTION
     */
    public String getDescription() {
        return (String) getAttributeInternal(DESCRIPTION);
    }

    /**
     * Sets <code>value</code> as attribute value for DESCRIPTION using the alias name Description.
     * @param value value to set the DESCRIPTION
     */
    public void setDescription(String value) {
        setAttributeInternal(DESCRIPTION, value);
    }

    /**
     * Gets the attribute value for FLAG_BJP using the alias name FlagBjp.
     * @return the FLAG_BJP
     */
    public String getFlagBjp() {
        return (String) getAttributeInternal(FLAGBJP);
    }

    /**
     * Sets <code>value</code> as attribute value for FLAG_BJP using the alias name FlagBjp.
     * @param value value to set the FLAG_BJP
     */
    public void setFlagBjp(String value) {
        setAttributeInternal(FLAGBJP, value);
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