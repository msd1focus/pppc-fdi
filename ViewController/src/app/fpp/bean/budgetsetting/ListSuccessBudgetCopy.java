package app.fpp.bean.budgetsetting;

import java.io.Serializable;

public class ListSuccessBudgetCopy implements Serializable{
    private static final long serialVersionUID= 1L;
    private String CustPostIdGagal;
    private String BudgetTypeGagal;
    private String CustPostIdSdhada;
    private String BudgetTypeSdhada;

  

  
    public void setCustPostIdGagal(String CustPostIdGagal) {
        this.CustPostIdGagal = CustPostIdGagal;
    }

    public String getCustPostIdGagal() {
        return CustPostIdGagal;
    }

    public void setCustPostIdSdhada(String CustPostIdSdhada) {
        this.CustPostIdSdhada = CustPostIdSdhada;
    }

    public String getCustPostIdSdhada() {
        return CustPostIdSdhada;
    }

    public void setBudgetTypeGagal(String BudgetTypeGagal) {
        this.BudgetTypeGagal = BudgetTypeGagal;
    }

    public String getBudgetTypeGagal() {
        return BudgetTypeGagal;
    }

    public void setBudgetTypeSdhada(String BudgetTypeSdhada) {
        this.BudgetTypeSdhada = BudgetTypeSdhada;
    }

    public String getBudgetTypeSdhada() {
        return BudgetTypeSdhada;
    }
}
