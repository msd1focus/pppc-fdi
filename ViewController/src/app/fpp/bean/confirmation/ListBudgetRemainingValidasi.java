package app.fpp.bean.confirmation;

import java.io.Serializable;

public class ListBudgetRemainingValidasi implements Serializable{
    private static final long serialVersionUID= 1L;
        private String BudgetCustId;
        private String budgetComb;
        private String budgetAsTodateRemaining;

    public void setBudgetCustId(String BudgetCustId) {
        this.BudgetCustId = BudgetCustId;
    }

    public String getBudgetCustId() {
        return BudgetCustId;
    }

    public void setBudgetComb(String budgetComb) {
        this.budgetComb = budgetComb;
    }

    public String getBudgetComb() {
        return budgetComb;
    }

    public void setBudgetAsTodateRemaining(String budgetAsTodateRemaining) {
        this.budgetAsTodateRemaining = budgetAsTodateRemaining;
    }

    public String getBudgetAsTodateRemaining() {
        return budgetAsTodateRemaining;
    }
}
