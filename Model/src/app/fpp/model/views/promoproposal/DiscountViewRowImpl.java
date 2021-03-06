package app.fpp.model.views.promoproposal;

import app.fpp.model.entities.promoproposal.DiscountImpl;

import oracle.jbo.Row;
import oracle.jbo.RowSet;
import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Oct 12 14:26:10 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class DiscountViewRowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. DO NOT MODIFY.
     */
    public enum AttributesEnum {
        DiscountId {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getDiscountId();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setDiscountId((DBSequence)value);
            }
        }
        ,
        PromoProdukId {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getPromoProdukId();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setPromoProdukId((Number)value);
            }
        }
        ,
        TipePerhitungan {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getTipePerhitungan();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setTipePerhitungan((String)value);
            }
        }
        ,
        Uom {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getUom();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setUom((String)value);
            }
        }
        ,
        QtyFrom {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getQtyFrom();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setQtyFrom((Number)value);
            }
        }
        ,
        QtyTo {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getQtyTo();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setQtyTo((Number)value);
            }
        }
        ,
        TipePotongan {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getTipePotongan();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setTipePotongan((String)value);
            }
        }
        ,
        DiscNonYearly {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getDiscNonYearly();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setDiscNonYearly((Number)value);
            }
        }
        ,
        DiscYearly {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getDiscYearly();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setDiscYearly((Number)value);
            }
        }
        ,
        TipePerhitunganDesc {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getTipePerhitunganDesc();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setTipePerhitunganDesc((String)value);
            }
        }
        ,
        SatuanPotonganDesc {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getSatuanPotonganDesc();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setSatuanPotonganDesc((String)value);
            }
        }
        ,
        TipePotonganDesc {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getTipePotonganDesc();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setTipePotonganDesc((String)value);
            }
        }
        ,
        Kelipatan {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getKelipatan();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setKelipatan((Number)value);
            }
        }
        ,
        CheckRowStatus {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getCheckRowStatus();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setCheckRowStatus((Integer)value);
            }
        }
        ,
        LovTipePerhitungan {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getLovTipePerhitungan();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        LovTipePotongan {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getLovTipePotongan();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        ProductUomLov {
            public Object get(DiscountViewRowImpl obj) {
                return obj.getProductUomLov();
            }

            public void put(DiscountViewRowImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static final int firstIndex = 0;

        public abstract Object get(DiscountViewRowImpl object);

        public abstract void put(DiscountViewRowImpl object, Object value);

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

    public static final int DISCOUNTID = AttributesEnum.DiscountId.index();
    public static final int PROMOPRODUKID = AttributesEnum.PromoProdukId.index();
    public static final int TIPEPERHITUNGAN = AttributesEnum.TipePerhitungan.index();
    public static final int UOM = AttributesEnum.Uom.index();
    public static final int QTYFROM = AttributesEnum.QtyFrom.index();
    public static final int QTYTO = AttributesEnum.QtyTo.index();
    public static final int TIPEPOTONGAN = AttributesEnum.TipePotongan.index();
    public static final int DISCNONYEARLY = AttributesEnum.DiscNonYearly.index();
    public static final int DISCYEARLY = AttributesEnum.DiscYearly.index();
    public static final int TIPEPERHITUNGANDESC = AttributesEnum.TipePerhitunganDesc.index();
    public static final int SATUANPOTONGANDESC = AttributesEnum.SatuanPotonganDesc.index();
    public static final int TIPEPOTONGANDESC = AttributesEnum.TipePotonganDesc.index();
    public static final int KELIPATAN = AttributesEnum.Kelipatan.index();
    public static final int CHECKROWSTATUS = AttributesEnum.CheckRowStatus.index();
    public static final int LOVTIPEPERHITUNGAN = AttributesEnum.LovTipePerhitungan.index();
    public static final int LOVTIPEPOTONGAN = AttributesEnum.LovTipePotongan.index();
    public static final int PRODUCTUOMLOV = AttributesEnum.ProductUomLov.index();

    /**
     * This is the default constructor (do not remove).
     */
    public DiscountViewRowImpl() {
    }

    /**
     * Gets Discount entity object.
     * @return the Discount
     */
    public DiscountImpl getDiscount() {
        return (DiscountImpl)getEntity(0);
    }

    /**
     * Gets the attribute value for DISCOUNT_ID using the alias name DiscountId.
     * @return the DISCOUNT_ID
     */
    public DBSequence getDiscountId() {
        return (DBSequence)getAttributeInternal(DISCOUNTID);
    }

    /**
     * Sets <code>value</code> as attribute value for DISCOUNT_ID using the alias name DiscountId.
     * @param value value to set the DISCOUNT_ID
     */
    public void setDiscountId(DBSequence value) {
        setAttributeInternal(DISCOUNTID, value);
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
     * Gets the attribute value for TIPE_PERHITUNGAN using the alias name TipePerhitungan.
     * @return the TIPE_PERHITUNGAN
     */
    public String getTipePerhitungan() {
        return (String) getAttributeInternal(TIPEPERHITUNGAN);
    }

    /**
     * Sets <code>value</code> as attribute value for TIPE_PERHITUNGAN using the alias name TipePerhitungan.
     * @param value value to set the TIPE_PERHITUNGAN
     */
    public void setTipePerhitungan(String value) {
        setAttributeInternal(TIPEPERHITUNGAN, value);
    }

    /**
     * Gets the attribute value for UOM using the alias name Uom.
     * @return the UOM
     */
    public String getUom() {
        return (String) getAttributeInternal(UOM);
    }

    /**
     * Sets <code>value</code> as attribute value for UOM using the alias name Uom.
     * @param value value to set the UOM
     */
    public void setUom(String value) {
        setAttributeInternal(UOM, value);
    }

    /**
     * Gets the attribute value for QTY_FROM using the alias name QtyFrom.
     * @return the QTY_FROM
     */
    public Number getQtyFrom() {
        return (Number) getAttributeInternal(QTYFROM);
    }

    /**
     * Sets <code>value</code> as attribute value for QTY_FROM using the alias name QtyFrom.
     * @param value value to set the QTY_FROM
     */
    public void setQtyFrom(Number value) {
        setAttributeInternal(QTYFROM, value);
    }

    /**
     * Gets the attribute value for QTY_TO using the alias name QtyTo.
     * @return the QTY_TO
     */
    public Number getQtyTo() {
        return (Number) getAttributeInternal(QTYTO);
    }

    /**
     * Sets <code>value</code> as attribute value for QTY_TO using the alias name QtyTo.
     * @param value value to set the QTY_TO
     */
    public void setQtyTo(Number value) {
        setAttributeInternal(QTYTO, value);
    }

    /**
     * Gets the attribute value for TIPE_POTONGAN using the alias name TipePotongan.
     * @return the TIPE_POTONGAN
     */
    public String getTipePotongan() {
        return (String) getAttributeInternal(TIPEPOTONGAN);
    }

    /**
     * Sets <code>value</code> as attribute value for TIPE_POTONGAN using the alias name TipePotongan.
     * @param value value to set the TIPE_POTONGAN
     */
    public void setTipePotongan(String value) {
        setAttributeInternal(TIPEPOTONGAN, value);
    }

    /**
     * Gets the attribute value for DISC_NON_YEARLY using the alias name DiscNonYearly.
     * @return the DISC_NON_YEARLY
     */
    public Number getDiscNonYearly() {
        return (Number) getAttributeInternal(DISCNONYEARLY);
    }

    /**
     * Sets <code>value</code> as attribute value for DISC_NON_YEARLY using the alias name DiscNonYearly.
     * @param value value to set the DISC_NON_YEARLY
     */
    public void setDiscNonYearly(Number value) {
        setAttributeInternal(DISCNONYEARLY, value);
    }

    /**
     * Gets the attribute value for DISC_YEARLY using the alias name DiscYearly.
     * @return the DISC_YEARLY
     */
    public Number getDiscYearly() {
        return (Number) getAttributeInternal(DISCYEARLY);
    }

    /**
     * Sets <code>value</code> as attribute value for DISC_YEARLY using the alias name DiscYearly.
     * @param value value to set the DISC_YEARLY
     */
    public void setDiscYearly(Number value) {
        setAttributeInternal(DISCYEARLY, value);
    }

    /**
     * Gets the attribute value for the calculated attribute TipePerhitunganDesc.
     * @return the TipePerhitunganDesc
     */
    public String getTipePerhitunganDesc() {
        String tipePerhitungan = null;
        String tipePerhitunganDesc = null;
        if (getTipePerhitungan() != null) {
            tipePerhitungan = getTipePerhitungan();
            Row[] tipePerhitunganRows;
            tipePerhitunganRows = this.getLovTipePerhitungan().getFilteredRows("Value", tipePerhitungan);
            if (tipePerhitunganRows.length > 0) {
                tipePerhitunganDesc = (tipePerhitunganRows[0].getAttribute("Descr").toString());
            }
            return tipePerhitunganDesc;
        } else {
            return (String) getAttributeInternal(TIPEPERHITUNGANDESC);
        }
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute TipePerhitunganDesc.
     * @param value value to set the  TipePerhitunganDesc
     */
    public void setTipePerhitunganDesc(String value) {
        setAttributeInternal(TIPEPERHITUNGANDESC, value);
    }

    /**
     * Gets the attribute value for the calculated attribute SatuanPotonganDesc.
     * @return the SatuanPotonganDesc
     */
    public String getSatuanPotonganDesc() {
        String satuanPotongan = null;
        String satuanPotonganDesc = null;
        if (getUom() != null) {
            satuanPotongan = getUom();
            Row[] satuanPotonganRows;
            satuanPotonganRows = this.getProductUomLov().getFilteredRows("UomCode", satuanPotongan);
            if (satuanPotonganRows.length > 0) {
                satuanPotonganDesc = (satuanPotonganRows[0].getAttribute("UnitOfMeasure").toString());
            }
            return satuanPotonganDesc;
        } else {
            return (String) getAttributeInternal(SATUANPOTONGANDESC);
        }
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute SatuanPotonganDesc.
     * @param value value to set the  SatuanPotonganDesc
     */
    public void setSatuanPotonganDesc(String value) {
        setAttributeInternal(SATUANPOTONGANDESC, value);
    }

    /**
     * Gets the attribute value for the calculated attribute TipePotonganDesc.
     * @return the TipePotonganDesc
     */
    public String getTipePotonganDesc() {
        String tipePotongan = null;
        String tipePotonganDesc = null;
        if (getTipePotongan() != null) {
            tipePotongan = getTipePotongan();
            Row[] tipePotonganRows;
            tipePotonganRows = this.getLovTipePotongan().getFilteredRows("Value", tipePotongan);
            if (tipePotonganRows.length > 0) {
                tipePotonganDesc = (tipePotonganRows[0].getAttribute("Descr").toString());
            }
            return tipePotonganDesc;
        } else {
            return (String) getAttributeInternal(TIPEPOTONGANDESC);
        }
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute TipePotonganDesc.
     * @param value value to set the  TipePotonganDesc
     */
    public void setTipePotonganDesc(String value) {
        setAttributeInternal(TIPEPOTONGANDESC, value);
    }

    /**
     * Gets the attribute value for KELIPATAN using the alias name Kelipatan.
     * @return the KELIPATAN
     */
    public Number getKelipatan() {
        return (Number) getAttributeInternal(KELIPATAN);
    }

    /**
     * Sets <code>value</code> as attribute value for KELIPATAN using the alias name Kelipatan.
     * @param value value to set the KELIPATAN
     */
    public void setKelipatan(Number value) {
        setAttributeInternal(KELIPATAN, value);
    }

    /**
     * Gets the attribute value for the calculated attribute CheckRowStatus.
     * @return the CheckRowStatus
     */
     public Integer getCheckRowStatus() {
         byte entityState = this.getEntity(0).getEntityState();
         return new Integer(entityState);

         // return (Integer) getAttributeInternal(CHECKROWSTATUS);
     }
    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute CheckRowStatus.
     * @param value value to set the  CheckRowStatus
     */
    public void setCheckRowStatus(Integer value) {
        setAttributeInternal(CHECKROWSTATUS, value);
    }

    /**
     * Gets the view accessor <code>RowSet</code> LovTipePerhitungan.
     */
    public RowSet getLovTipePerhitungan() {
        return (RowSet)getAttributeInternal(LOVTIPEPERHITUNGAN);
    }

    /**
     * Gets the view accessor <code>RowSet</code> LovTipePotongan.
     */
    public RowSet getLovTipePotongan() {
        return (RowSet)getAttributeInternal(LOVTIPEPOTONGAN);
    }

    /**
     * Gets the view accessor <code>RowSet</code> ProductUomLov.
     */
    public RowSet getProductUomLov() {
        return (RowSet)getAttributeInternal(PRODUCTUOMLOV);
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
