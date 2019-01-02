package app.fpp.bean.budgetsetting;

import java.io.Serializable;

public class ListBudgetCustomerHeader implements Serializable{
    private static final long serialVersionUID= 1L;
    
    private String CustomerId;
    private String KodePosting;
    private String BudgetType;
    private String BudgetYear;
    private String CreatedBy;
    private String CreationDate;
    private String ModifiedBy;
    private String ModifiedOn;
    private String BudgetCustHdrIdref;
    private String BudgetCustHdrId;

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setKodePosting(String KodePosting) {
        this.KodePosting = KodePosting;
    }

    public String getKodePosting() {
        return KodePosting;
    }

    public void setBudgetType(String BudgetType) {
        this.BudgetType = BudgetType;
    }

    public String getBudgetType() {
        return BudgetType;
    }

    public void setBudgetYear(String BudgetYear) {
        this.BudgetYear = BudgetYear;
    }

    public String getBudgetYear() {
        return BudgetYear;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreationDate(String CreationDate) {
        this.CreationDate = CreationDate;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setModifiedBy(String ModifiedBy) {
        this.ModifiedBy = ModifiedBy;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedOn(String ModifiedOn) {
        this.ModifiedOn = ModifiedOn;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setBudgetCustHdrIdref(String BudgetCustHdrIdref) {
        this.BudgetCustHdrIdref = BudgetCustHdrIdref;
    }

    public String getBudgetCustHdrIdref() {
        return BudgetCustHdrIdref;
    }

    public void setBudgetCustHdrId(String BudgetCustHdrId) {
        this.BudgetCustHdrId = BudgetCustHdrId;
    }

    public String getBudgetCustHdrId() {
        return BudgetCustHdrId;
    }
}
