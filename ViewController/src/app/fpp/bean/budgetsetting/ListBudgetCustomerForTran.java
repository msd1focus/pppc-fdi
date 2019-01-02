package app.fpp.bean.budgetsetting;

import java.io.Serializable;

public class ListBudgetCustomerForTran implements Serializable{
    private static final long serialVersionUID= 1L;
    private String BudgetCustomerId;
    private String Percentag;
    private String Amount;
    private String YearlyBudgetAmount;


    public void setBudgetCustomerId(String BudgetCustomerId) {
        this.BudgetCustomerId = BudgetCustomerId;
    }

    public String getBudgetCustomerId() {
        return BudgetCustomerId;
    }

    public void setPercentag(String Percentag) {
        this.Percentag = Percentag;
    }

    public String getPercentag() {
        return Percentag;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getAmount() {
        return Amount;
    }

    public void setYearlyBudgetAmount(String YearlyBudgetAmount) {
        this.YearlyBudgetAmount = YearlyBudgetAmount;
    }

    public String getYearlyBudgetAmount() {
        return YearlyBudgetAmount;
    }
}
