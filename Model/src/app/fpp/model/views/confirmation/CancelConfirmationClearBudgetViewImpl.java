package app.fpp.model.views.confirmation;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Apr 17 16:05:50 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CancelConfirmationClearBudgetViewImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public CancelConfirmationClearBudgetViewImpl() {
    }


    /**
     * Returns the bind variable value for propNo.
     * @return bind variable value for propNo
     */
    public String getpropNo() {
        return (String)getNamedWhereClauseParam("propNo");
    }

    /**
     * Sets <code>value</code> for bind variable propNo.
     * @param value value to bind as propNo
     */
    public void setpropNo(String value) {
        setNamedWhereClauseParam("propNo", value);
    }
}
