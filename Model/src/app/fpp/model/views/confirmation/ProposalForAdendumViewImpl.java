package app.fpp.model.views.confirmation;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed May 02 18:06:46 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProposalForAdendumViewImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public ProposalForAdendumViewImpl() {
    }

    /**
     * Returns the bind variable value for copySrc.
     * @return bind variable value for copySrc
     */
    public String getcopySrc() {
        return (String)getNamedWhereClauseParam("copySrc");
    }

    /**
     * Sets <code>value</code> for bind variable copySrc.
     * @param value value to bind as copySrc
     */
    public void setcopySrc(String value) {
        setNamedWhereClauseParam("copySrc", value);
    }

    /**
     * Returns the variable value for propNo.
     * @return variable value for propNo
     */
    public String getpropNo() {
        return (String)ensureVariableManager().getVariableValue("propNo");
    }

    /**
     * Sets <code>value</code> for variable propNo.
     * @param value value to bind as propNo
     */
    public void setpropNo(String value) {
        ensureVariableManager().setVariableValue("propNo", value);
    }
}
