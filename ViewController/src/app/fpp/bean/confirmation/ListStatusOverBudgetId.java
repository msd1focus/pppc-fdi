package app.fpp.bean.confirmation;

import java.io.Serializable;

public class ListStatusOverBudgetId implements Serializable{
    private static final long serialVersionUID= 1L;
    private String BudgetId;
    private String StatusOver;


    public void setBudgetId(String BudgetId) {
        this.BudgetId = BudgetId;
    }

    public String getBudgetId() {
        return BudgetId;
    }

    public void setStatusOver(String StatusOver) {
        this.StatusOver = StatusOver;
    }

    public String getStatusOver() {
        return StatusOver;
    }
}
