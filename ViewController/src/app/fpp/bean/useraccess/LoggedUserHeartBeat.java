package app.fpp.bean.useraccess;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.ConfirmationAMImpl;

import app.fpp.model.am.UserAccessAMImpl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.myfaces.trinidad.event.PollEvent;

public class LoggedUserHeartBeat {
    public LoggedUserHeartBeat() {
    }

    public void sendBeat(PollEvent pollEvent) {
        UserAccessAMImpl userAccessAM =
            (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest)externalContext.getRequest();
        String sessionId = req.getSession().getId();
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();

        CallableStatement cst = null;
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("BEGIN FCS_LOGIN_BEAT('" + usrName + "', '" + sessionId + "'); END;", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public oracle.jbo.domain.Date getCurrentSysDate() {
        java.sql.Timestamp datetime =
            new java.sql.Timestamp(System.currentTimeMillis());
        oracle.jbo.domain.Date daTime = new oracle.jbo.domain.Date(datetime);
        return daTime;
    }
}
