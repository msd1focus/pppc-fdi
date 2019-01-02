package app.fpp.model.views.useraccess.uploadfile.validate;

import java.sql.ResultSet;

import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.server.ViewRowImpl;
import oracle.jbo.server.ViewRowSetImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Aug 07 18:20:36 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class UtUserRoleValidationFoodAreaImpl extends ViewObjectImpl {
    /**
     * This is the default constructor (do not remove).
     */
    public UtUserRoleValidationFoodAreaImpl() {
    }

    /**
     * Returns the bind variable value for compId.
     * @return bind variable value for compId
     */
    public String getcompId() {
        return (String)getNamedWhereClauseParam("compId");
    }

    /**
     * Sets <code>value</code> for bind variable compId.
     * @param value value to bind as compId
     */
    public void setcompId(String value) {
        setNamedWhereClauseParam("compId", value);
    }

    /**
     * Returns the bind variable value for spvUsrNm.
     * @return bind variable value for spvUsrNm
     */
    public String getspvUsrNm() {
        return (String)getNamedWhereClauseParam("spvUsrNm");
    }

    /**
     * Sets <code>value</code> for bind variable spvUsrNm.
     * @param value value to bind as spvUsrNm
     */
    public void setspvUsrNm(String value) {
        setNamedWhereClauseParam("spvUsrNm", value);
    }

    /**
     * Returns the bind variable value for usrName.
     * @return bind variable value for usrName
     */
    public String getusrName() {
        return (String)getNamedWhereClauseParam("usrName");
    }

    /**
     * Sets <code>value</code> for bind variable usrName.
     * @param value value to bind as usrName
     */
    public void setusrName(String value) {
        setNamedWhereClauseParam("usrName", value);
    }

    /**
     * Returns the bind variable value for usrRole.
     * @return bind variable value for usrRole
     */
    public String getusrRole() {
        return (String)getNamedWhereClauseParam("usrRole");
    }

    /**
     * Sets <code>value</code> for bind variable usrRole.
     * @param value value to bind as usrRole
     */
    public void setusrRole(String value) {
        setNamedWhereClauseParam("usrRole", value);
    }

    /**
     * executeQueryForCollection - overridden for custom java data source support.
     */
    protected void executeQueryForCollection(Object qc, Object[] params,
                                             int noUserParams) {
        super.executeQueryForCollection(qc, params, noUserParams);
    }

    /**
     * hasNextForCollection - overridden for custom java data source support.
     */
    protected boolean hasNextForCollection(Object qc) {
        boolean bRet = super.hasNextForCollection(qc);
        return bRet;
    }

    /**
     * createRowFromResultSet - overridden for custom java data source support.
     */
    protected ViewRowImpl createRowFromResultSet(Object qc,
                                                 ResultSet resultSet) {
        ViewRowImpl value = super.createRowFromResultSet(qc, resultSet);
        return value;
    }

    /**
     * getQueryHitCount - overridden for custom java data source support.
     */
    public long getQueryHitCount(ViewRowSetImpl viewRowSet) {
        long value = super.getQueryHitCount(viewRowSet);
        return value;
    }
}
