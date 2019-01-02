package app.fpp.bean;

import app.fpp.adfextensions.ADFUtils;

import app.fpp.adfextensions.JSFUtils;

import app.fpp.bean.useraccessmenu.UserData;

import app.fpp.model.am.ApprovalAMImpl;
import app.fpp.model.am.ConfirmationAMImpl;
import app.fpp.model.am.PromoProposalAMImpl;
import app.fpp.model.am.UserAccessAMImpl;

import java.io.IOException;

import java.sql.CallableStatement;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.binding.OperationBinding;

import weblogic.security.SimpleCallbackHandler;

import weblogic.servlet.security.ServletAuthentication;

public class LoginBean {
    private RichInputText inputLogin, inputPassword;
    private static String userAccessDisabled = "D";

    public LoginBean() {
        super();
    }

    public void setInputLogin(RichInputText inputLogin) {
        this.inputLogin = inputLogin;
    }

    public RichInputText getInputLogin() {
        return inputLogin;
    }

    public void setInputPassword(RichInputText inputPassword) {
        this.inputPassword = inputPassword;
    }

    public RichInputText getInputPassword() {
        return inputPassword;
    }

    public String loginAction() throws LoginException {
        String action = "";
        String un = "";
        byte[] pw = ("").getBytes();
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)ctx.getExternalContext().getRequest();
        try {
            OperationBinding login =
                ADFUtils.findOperation("authenticateUser");
            Map m = (Map)login.execute();

            String fullName = (String)m.get("FullName");
            HashMap userAccess = (HashMap)m.get("UserAccess");
            String userPassword = (String)m.get("Password");
            String userNameLogin = (String)m.get("UserName");
            String title = (String)m.get("Title");
            String userRole = (String)m.get("UserRole");
            String contactNo = (String)m.get("ContactNo");
            String companyId = (String)m.get("CompanyId");
            String accessStatus = (String)m.get("AccessStatus");
            String userType = (String)m.get("UserType");
            String userInitial = (String)m.get("UserInitial");
            String userDivision = (String)m.get("UserDivision");
            String userCustomer = (String)m.get("UserCustomer");
            String copyPpArea = (String)m.get("CopyPpArea");
                
            un = userNameLogin;
            pw = (userPassword).getBytes();
            
            //CallbackHandler handler = new URLCallbackHandler(un, pw);
            CallbackHandler handler = new SimpleCallbackHandler(un, pw);
            Subject mySubject =
                weblogic.security.services.Authentication.login(handler);
            weblogic.servlet.security.ServletAuthentication.runAs(mySubject,
                                                                  request);
            
            ServletAuthentication.generateNewSessionID(request);
            if (accessStatus.equalsIgnoreCase(userAccessDisabled)) {
                StringBuilder message = new StringBuilder("<html><body>");
                message.append("<p>Username yang anda masukan telah expired.</p>");
                message.append("</body></html>");
                FacesMessage msg = new FacesMessage(message.toString());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                ctx.addMessage(null, msg);
            } else {
                UserData userData =
                    (UserData)JSFUtils.resolveExpression("#{UserData}");
                userData.setLoggedIn(Boolean.TRUE);
                userData.setFullName(fullName);
                String fullNameSub = null;
                if (fullName.length() > 16) {
                    fullNameSub = (fullName.substring(0, 12)).concat("...");
                } else {
                    fullNameSub = fullName;
                }
                userData.setFullNameSubstr(fullNameSub);
                userData.setUserAccess(userAccess);
                userData.setUserNameLogin(userNameLogin);
                userData.setUserPassword(userPassword);
                userData.setTitle(title);
                userData.setUserRole(userRole);
                userData.setContactNo(contactNo);
                userData.setCompanyId(companyId);
                userData.setUserType(userType);
                userData.setUserInitial(userInitial);
                userData.setUserDivision(userDivision);
                userData.setUserCustomer(userCustomer);
                userData.setCopyPpArea(copyPpArea);

                OperationBinding _dashboardAMSession =
                    ADFUtils.findOperation("setLoginToSession_DashboardAM");
                _dashboardAMSession.execute();
                
                OperationBinding _promoProposalAMSession =
                    ADFUtils.findOperation("setLoginToSession_PromoProposalAM");
                _promoProposalAMSession.execute();

                OperationBinding _confirmationAMSession =
                    ADFUtils.findOperation("setLoginToSession_ConfirmationAM");
                _confirmationAMSession.execute();

                OperationBinding _approvalAMSession =
                    ADFUtils.findOperation("setLoginToSession_ApprovalAM");
                _approvalAMSession.execute();

                OperationBinding _approvalSettingAMSession =
                    ADFUtils.findOperation("setLoginToSession_ApprovalSettingAM");
                _approvalSettingAMSession.execute();

                OperationBinding _budgetSettingAMSession =
                    ADFUtils.findOperation("setLoginToSession_BudgetSettingAM");
                _budgetSettingAMSession.execute();

                String loginUrl =
                    "/adfAuthentication?success_url=/faces/dashboard.jspx";
                HttpServletResponse response =
                    (HttpServletResponse)ctx.getExternalContext().getResponse();
                sendForward(request, response, loginUrl);
                action = "success";
            }
        } catch (FailedLoginException fle) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Isian salah pada \"Username\" atau \"Password\".</p>");
            message.append("</body></html>");
            FacesMessage msg = new FacesMessage(message.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            ctx.addMessage(null, msg);
        } catch (LoginException le) {
            FacesMessage msg = new FacesMessage(le.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            ctx.addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getLocalizedMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            ctx.addMessage(null, msg);
        }
        return action;
    }

    private void sendForward(HttpServletRequest request,
                             HttpServletResponse response, String forwardUrl) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        RequestDispatcher dispatcher =
            request.getRequestDispatcher(forwardUrl);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            reportUnexpectedLoginError("ServletException", se);
        } catch (IOException ie) {
            reportUnexpectedLoginError("IOException", ie);
        }
        ctx.responseComplete();
    }

    private void reportUnexpectedLoginError(String errType, Exception e) {
        FacesMessage msg =
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected error during login",
                             "Unexpected error during login (" + errType +
                             "), please consult logs for detail");
        FacesContext.getCurrentInstance().addMessage("d2:it35", msg);
        e.printStackTrace();
    }

    public String doLogout() {        
        UserAccessAMImpl userAccessAM =
            (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        
        // Commit all dirty transactions 
        ApprovalAMImpl approvalAM =
            (ApprovalAMImpl)ADFUtils.getApplicationModuleForDataControl("ApprovalAMDataControl");
        if (approvalAM.getTransaction().isDirty()) {
            approvalAM.getDBTransaction().commit();
        }
        
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        if (confirmationAM.getTransaction().isDirty()) {
            confirmationAM.getDBTransaction().commit();
        }
        
        PromoProposalAMImpl promoProposalAM =
            (PromoProposalAMImpl)ADFUtils.getApplicationModuleForDataControl("PromoProposalAMDataControl");
        if (promoProposalAM.getTransaction().isDirty()) {
            promoProposalAM.getDBTransaction().commit();
        }
                
        FacesContext fctx = FacesContext.getCurrentInstance();
        ExternalContext ectx = fctx.getExternalContext();
        HttpServletRequest req = (HttpServletRequest)ectx.getRequest();
        String sessionId = req.getSession().getId();
        
        String url =
            ectx.getRequestContextPath() + "/adfAuthentication?logout=true&end_url=/faces/dashboard.jspx";
        try {
            ectx.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fctx.responseComplete();
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();

        CallableStatement cst = null;
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("BEGIN FCS_LOGIN_OUT('" + usrName + "', '" + sessionId + "'); END;", 0);
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
        return null;
    }
}
