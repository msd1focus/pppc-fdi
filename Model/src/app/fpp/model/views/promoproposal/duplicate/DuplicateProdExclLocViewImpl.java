package app.fpp.model.views.promoproposal.duplicate;

import oracle.jbo.server.ViewObjectImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed Nov 15 11:50:08 ICT 2017
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class DuplicateProdExclLocViewImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public DuplicateProdExclLocViewImpl() {
    }

    /**
     * Returns the bind variable value for promoProdukId.
     * @return bind variable value for promoProdukId
     */
    public String getpromoProdukId() {
        return (String)getNamedWhereClauseParam("promoProdukId");
    }

    /**
     * Sets <code>value</code> for bind variable promoProdukId.
     * @param value value to bind as promoProdukId
     */
    public void setpromoProdukId(String value) {
        setNamedWhereClauseParam("promoProdukId", value);
    }
}
