package app.fpp.model.views.budgetsetting;

import app.fpp.model.entities.budgetsetting.BudgetCustTranImpl;
import app.fpp.model.entities.budgetsetting.BudgetCustomerImpl;

import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Dec 13 11:08:46 GMT+07:00 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class BudgetCustTranApprovalViewRowImpl extends ViewRowImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public BudgetCustTranApprovalViewRowImpl() {
    }

    /**
     * Gets BudgetCustomer entity object.
     * @return the BudgetCustomer
     */
    public BudgetCustomerImpl getBudgetCustomer() {
        return (BudgetCustomerImpl)getEntity(0);
    }

    /**
     * Gets BudgetCustTran entity object.
     * @return the BudgetCustTran
     */
    public BudgetCustTranImpl getBudgetCustTran() {
        return (BudgetCustTranImpl)getEntity(1);
    }

    /**
     * Gets FcsViewCategCombDesc entity object.
     * @return the FcsViewCategCombDesc
     */
    public EntityImpl getFcsViewCategCombDesc() {
        return (EntityImpl)getEntity(2);
    }
}
