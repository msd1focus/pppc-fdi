package app.fpp.model.views.confirmation.modifier;

import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Jan 08 14:26:59 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class FcsViewNoModifierViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        NoConfirm {
            public Object get(FcsViewNoModifierViewRowImpl obj) {
                return obj.getNoConfirm();
            }

            public void put(FcsViewNoModifierViewRowImpl obj, Object value) {
                obj.setNoConfirm((String)value);
            }
        }
        ,
        Description {
            public Object get(FcsViewNoModifierViewRowImpl obj) {
                return obj.getDescription();
            }

            public void put(FcsViewNoModifierViewRowImpl obj, Object value) {
                obj.setDescription((String)value);
            }
        }
        ,
        OrigNoConfirm {
            public Object get(FcsViewNoModifierViewRowImpl obj) {
                return obj.getOrigNoConfirm();
            }

            public void put(FcsViewNoModifierViewRowImpl obj, Object value) {
                obj.setOrigNoConfirm((String)value);
            }
        }
        ,
        Active {
            public Object get(FcsViewNoModifierViewRowImpl obj) {
                return obj.getActive();
            }

            public void put(FcsViewNoModifierViewRowImpl obj, Object value) {
                obj.setActive((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(FcsViewNoModifierViewRowImpl object);

        public abstract void put(FcsViewNoModifierViewRowImpl object,
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
    public static final int NOCONFIRM = AttributesEnum.NoConfirm.index();
    public static final int DESCRIPTION = AttributesEnum.Description.index();
    public static final int ORIGNOCONFIRM = AttributesEnum.OrigNoConfirm.index();
    public static final int ACTIVE = AttributesEnum.Active.index();

    /**
     * This is the default constructor (do not remove).
     */
    public FcsViewNoModifierViewRowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute NoConfirm.
     * @return the NoConfirm
     */
    public String getNoConfirm() {
        return (String) getAttributeInternal(NOCONFIRM);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute NoConfirm.
     * @param value value to set the  NoConfirm
     */
    public void setNoConfirm(String value) {
        setAttributeInternal(NOCONFIRM, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Description.
     * @return the Description
     */
    public String getDescription() {
        return (String) getAttributeInternal(DESCRIPTION);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Description.
     * @param value value to set the  Description
     */
    public void setDescription(String value) {
        setAttributeInternal(DESCRIPTION, value);
    }

    /**
     * Gets the attribute value for the calculated attribute OrigNoConfirm.
     * @return the OrigNoConfirm
     */
    public String getOrigNoConfirm() {
        return (String) getAttributeInternal(ORIGNOCONFIRM);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute OrigNoConfirm.
     * @param value value to set the  OrigNoConfirm
     */
    public void setOrigNoConfirm(String value) {
        setAttributeInternal(ORIGNOCONFIRM, value);
    }

    /**
     * Gets the attribute value for the calculated attribute Active.
     * @return the Active
     */
    public String getActive() {
        return (String) getAttributeInternal(ACTIVE);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute Active.
     * @param value value to set the  Active
     */
    public void setActive(String value) {
        setAttributeInternal(ACTIVE, value);
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