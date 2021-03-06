package app.fpp.model.views.confirmation;

import app.fpp.model.entities.confirmation.CategoryPcLogImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Jan 11 08:11:32 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CategoryPcLogViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        CategoryPcLogId {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getCategoryPcLogId();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setCategoryPcLogId((DBSequence)value);
            }
        }
        ,
        NoPc {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getNoPc();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setNoPc((String)value);
            }
        }
        ,
        NoPp {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getNoPp();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setNoPp((String)value);
            }
        }
        ,
        CreationDate {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getCreationDate();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setCreationDate((Date)value);
            }
        }
        ,
        CreatedBy {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getCreatedBy();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setCreatedBy((String)value);
            }
        }
        ,
        FullName {
            public Object get(CategoryPcLogViewRowImpl obj) {
                return obj.getFullName();
            }

            public void put(CategoryPcLogViewRowImpl obj, Object value) {
                obj.setFullName((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(CategoryPcLogViewRowImpl object);

        public abstract void put(CategoryPcLogViewRowImpl object,
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


    public static final int CATEGORYPCLOGID = AttributesEnum.CategoryPcLogId.index();
    public static final int NOPC = AttributesEnum.NoPc.index();
    public static final int NOPP = AttributesEnum.NoPp.index();
    public static final int CREATIONDATE = AttributesEnum.CreationDate.index();
    public static final int CREATEDBY = AttributesEnum.CreatedBy.index();
    public static final int FULLNAME = AttributesEnum.FullName.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CategoryPcLogViewRowImpl() {
    }

    /**
     * Gets CategoryPcLog entity object.
     * @return the CategoryPcLog
     */
    public CategoryPcLogImpl getCategoryPcLog() {
        return (CategoryPcLogImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for CATEGORY_PC_LOG_ID using the alias name CategoryPcLogId.
     * @return the CATEGORY_PC_LOG_ID
     */
    public DBSequence getCategoryPcLogId() {
        return (DBSequence)getAttributeInternal(CATEGORYPCLOGID);
    }

    /**
     * Sets <code>value</code> as attribute value for CATEGORY_PC_LOG_ID using the alias name CategoryPcLogId.
     * @param value value to set the CATEGORY_PC_LOG_ID
     */
    public void setCategoryPcLogId(DBSequence value) {
        setAttributeInternal(CATEGORYPCLOGID, value);
    }

    /**
     * Gets the attribute value for NO_PC using the alias name NoPc.
     * @return the NO_PC
     */
    public String getNoPc() {
        return (String) getAttributeInternal(NOPC);
    }

    /**
     * Sets <code>value</code> as attribute value for NO_PC using the alias name NoPc.
     * @param value value to set the NO_PC
     */
    public void setNoPc(String value) {
        setAttributeInternal(NOPC, value);
    }

    /**
     * Gets the attribute value for NO_PP using the alias name NoPp.
     * @return the NO_PP
     */
    public String getNoPp() {
        return (String) getAttributeInternal(NOPP);
    }

    /**
     * Sets <code>value</code> as attribute value for NO_PP using the alias name NoPp.
     * @param value value to set the NO_PP
     */
    public void setNoPp(String value) {
        setAttributeInternal(NOPP, value);
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
     * Gets the attribute value for the calculated attribute FullName.
     * @return the FullName
     */
    public String getFullName() {
        return (String) getAttributeInternal(FULLNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute FullName.
     * @param value value to set the  FullName
     */
    public void setFullName(String value) {
        setAttributeInternal(FULLNAME, value);
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
