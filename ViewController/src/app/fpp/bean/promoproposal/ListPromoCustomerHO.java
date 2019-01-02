package app.fpp.bean.promoproposal;

import java.io.Serializable;

import java.util.Date;

public class ListPromoCustomerHO implements Serializable {
    private static final long serialVersionUID= 1L;
    private String PromoCustomerId;
    private String PromoProdukId;
    private String RegionCode;
    private String RegionDesc;
    private String AreaCode;
    private String AreaDesc;
    private String LocCode;
    private String LocDesc;
    private String CusttypCode;
    private String CusttypDesc;
    private String CustgrpCode;
    private String CustgrpDesc;
    private String CustomerId;
    private String CustomerName;
    private String CustomerNumber;
    private String CustRegFlg;
    private String CustNregFlg;
    private String CreatedBy;
    private Date CreatedOn;
    private String UserName;

    public void setPromoCustomerId(String PromoCustomerId) {
        this.PromoCustomerId = PromoCustomerId;
    }

    public String getPromoCustomerId() {
        return PromoCustomerId;
    }

    public void setPromoProdukId(String PromoProdukId) {
        this.PromoProdukId = PromoProdukId;
    }

    public String getPromoProdukId() {
        return PromoProdukId;
    }

    public void setRegionCode(String RegionCode) {
        this.RegionCode = RegionCode;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionDesc(String RegionDesc) {
        this.RegionDesc = RegionDesc;
    }

    public String getRegionDesc() {
        return RegionDesc;
    }

    public void setAreaCode(String AreaCode) {
        this.AreaCode = AreaCode;
    }

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaDesc(String AreaDesc) {
        this.AreaDesc = AreaDesc;
    }

    public String getAreaDesc() {
        return AreaDesc;
    }

    public void setLocCode(String LocCode) {
        this.LocCode = LocCode;
    }

    public String getLocCode() {
        return LocCode;
    }

    public void setLocDesc(String LocDesc) {
        this.LocDesc = LocDesc;
    }

    public String getLocDesc() {
        return LocDesc;
    }

    public void setCusttypCode(String CusttypCode) {
        this.CusttypCode = CusttypCode;
    }

    public String getCusttypCode() {
        return CusttypCode;
    }

    public void setCusttypDesc(String CusttypDesc) {
        this.CusttypDesc = CusttypDesc;
    }

    public String getCusttypDesc() {
        return CusttypDesc;
    }

    public void setCustgrpCode(String CustgrpCode) {
        this.CustgrpCode = CustgrpCode;
    }

    public String getCustgrpCode() {
        return CustgrpCode;
    }

    public void setCustgrpDesc(String CustgrpDesc) {
        this.CustgrpDesc = CustgrpDesc;
    }

    public String getCustgrpDesc() {
        return CustgrpDesc;
    }

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerNumber(String CustomerNumber) {
        this.CustomerNumber = CustomerNumber;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustRegFlg(String CustRegFlg) {
        this.CustRegFlg = CustRegFlg;
    }

    public String getCustRegFlg() {
        return CustRegFlg;
    }

    public void setCustNregFlg(String CustNregFlg) {
        this.CustNregFlg = CustNregFlg;
    }

    public String getCustNregFlg() {
        return CustNregFlg;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedOn(Date CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserName() {
        return UserName;
    }
}
