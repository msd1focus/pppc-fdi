package app.fpp.model.views.useraccess.uploadfile.validate;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Fri May 18 11:09:44 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class UtRolesValidationImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public UtRolesValidationImpl() {
    }


    /**
     * Returns the bind variable value for role.
     * @return bind variable value for role
     */
    public String getrole() {
        return (String)getNamedWhereClauseParam("role");
    }

    /**
     * Sets <code>value</code> for bind variable role.
     * @param value value to bind as role
     */
    public void setrole(String value) {
        setNamedWhereClauseParam("role", value);
    }
}