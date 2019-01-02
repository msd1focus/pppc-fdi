package app.fpp.bean.confirmation;

import java.io.Serializable;

import java.math.BigDecimal;

public class realisasiTempClass implements Serializable{
    private static final long serialVersionUID= 1L;
    private String CONFIRM_NO;
    private String REALISASI_TYPE;
    private BigDecimal PROMO_PRODUK_ID;
    private BigDecimal AMOUNTMF;
    private BigDecimal AMOUNTOT;


    public void setCONFIRM_NO(String CONFIRM_NO) {
        this.CONFIRM_NO = CONFIRM_NO;
    }

    public String getCONFIRM_NO() {
        return CONFIRM_NO;
    }


    public void setPROMO_PRODUK_ID(BigDecimal PROMO_PRODUK_ID) {
        this.PROMO_PRODUK_ID = PROMO_PRODUK_ID;
    }

    public BigDecimal getPROMO_PRODUK_ID() {
        return PROMO_PRODUK_ID;
    }

    public void setAMOUNTMF(BigDecimal AMOUNTMF) {
        this.AMOUNTMF = AMOUNTMF;
    }

    public BigDecimal getAMOUNTMF() {
        return AMOUNTMF;
    }

    public void setAMOUNTOT(BigDecimal AMOUNTOT) {
        this.AMOUNTOT = AMOUNTOT;
    }

    public BigDecimal getAMOUNTOT() {
        return AMOUNTOT;
    }

    public void setREALISASI_TYPE(String REALISASI_TYPE) {
        this.REALISASI_TYPE = REALISASI_TYPE;
    }

    public String getREALISASI_TYPE() {
        return REALISASI_TYPE;
    }
}
