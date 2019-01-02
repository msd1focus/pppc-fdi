package app.fpp.bean.batchadmin;


import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;

import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.MasterDataAMImpl;
import app.fpp.model.am.UserAccessAMImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.http.HttpServletRequest;

import javax.sql.DataSource;

import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.jbo.JboException;

public class batchModifierBean {
    private RichPanelFormLayout frmStatus;

    public batchModifierBean() {
    }

    public void modifierBatch(ActionEvent actionEvent) {        
        Connection conn = null;
        String getModifierSql = "{CALL FCS_BATCH_REALISASI_MODIFIER()}";
        updateBatchLog("BRMOD", "INPROCESS");        
        AdfFacesContext.getCurrentInstance().addPartialTarget(frmStatus);
        try {
            Context ctxUrl = new InitialContext();
            DataSource ds = (DataSource)ctxUrl.lookup("jdbc/fcsBatchDS");
            conn = ds.getConnection();
            PreparedStatement statement =
                conn.prepareCall(getModifierSql);
            statement.executeUpdate();
            statement.close();
            conn.close();
            updateBatchLog("BRMOD", "FINISHED");
            AdfFacesContext.getCurrentInstance().addPartialTarget(frmStatus);
        } catch (Exception exc) {
            updateBatchLog("BRMOD", "ERROR");
            AdfFacesContext.getCurrentInstance().addPartialTarget(frmStatus);
            JSFUtils.addFacesErrorMessage(exc.getMessage());
        }
    }
    
    public void modifierReset(ActionEvent actionEvent) {        
        updateBatchLog("BRMOD", "-");
    }
    
    public void updateBatchLog(String batchStatus, String batchName) {
        MasterDataAMImpl masterDataAM =
            (MasterDataAMImpl)ADFUtils.getApplicationModuleForDataControl("MasterDataAMDataControl");

        CallableStatement cst = null;
        try {
            cst =
        masterDataAM.getDBTransaction().createCallableStatement("BEGIN FCS_BATCH_LOG('" + batchName + "', '" + batchStatus + "'); END;", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }
            }
        }        
        masterDataAM.getBatchModifierLogView1().executeQuery();
    }

    public void setFrmStatus(RichPanelFormLayout frmStatus) {
        this.frmStatus = frmStatus;
    }

    public RichPanelFormLayout getFrmStatus() {
        return frmStatus;
    }
}
