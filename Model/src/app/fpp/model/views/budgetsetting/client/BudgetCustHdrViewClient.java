package app.fpp.model.views.budgetsetting.client;

import app.fpp.model.views.budgetsetting.common.BudgetCustHdrView;

import oracle.jbo.client.remote.ViewUsageImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Aug 21 11:50:27 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class BudgetCustHdrViewClient extends ViewUsageImpl implements BudgetCustHdrView {
    /**
     * This is the default constructor (do not remove).
     */
    public BudgetCustHdrViewClient() {
    }

    public void searchCustomerHdr(String budgetType, String budgetYear) {
        Object _ret =
            getApplicationModuleProxy().riInvokeExportedMethod(this,"searchCustomerHdr",new String [] {"java.lang.String","java.lang.String"},new Object[] {budgetType, budgetYear});
        return;
    }
}
