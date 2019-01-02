package app.fpp.bean.shiro;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.useraccessmenu.UserData;

import app.fpp.model.am.SecurityAMImpl;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import sun.misc.BASE64Encoder;
import javax.servlet.http.HttpServletResponse;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.binding.OperationBinding;

public class SecurityHandlerBean {
    private String userName;
    private String password;
    private String encpassword;
    private RichInputText inputLogin, inputPassword;
    private boolean remember;
    public static final String HOME_URL = "/faces/dashboard.jspx";
    public static final String LOGIN_URL = "slogin.jspx";
    private static String userAccessDisabled = "D";
    private static final String ipPrefixLocal = "192"; 
    public static final String PASS_THRU_USER_ADMIN = "admin";
    
    public SecurityHandlerBean() {
    }
    
    public String login() {
        try {
            //encpassword=getKeyDigestString(password,null);
            encpassword=password;
            SecurityUtils.getSubject().login(new UsernamePasswordToken(userName, encpassword, remember));
            
            // PPPC Application Data Session
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
            String reportDomain = (String)m.get("ReportDomain");
            String reportDomainLoc = (String)m.get("ReportDomainLoc");
            String copyPpArea = (String)m.get("CopyPpArea");
            String userLogged = (String)m.get("UserLogged");
            String sessionTime = (String)m.get("SessionTime");
            String printCount = (String)m.get("PrintCount");
            String unApprove = (String)m.get("UnApprove");
            String budgetStatus = (String)m.get("BudgetStatus");
            String approvalBudget = (String)m.get("ApprovalBudget");
            String printMode = (String)m.get("PrintMode");
            String uploadUser = (String)m.get("UploadUser");
            String userCustPriv = (String)m.get("UserCustPriv");
            // User Customer Privileges Break
            String[] custPriv = userCustPriv.split(";");
            String userCustRegion = custPriv[0].trim();
            String userCustArea = custPriv[1].trim();
            String userCustLocation = custPriv[2].trim();
            String userCustType = custPriv[3].trim();
            String userCustGroup = custPriv[4].trim();
            String userCustName = custPriv[5].trim();
            
            HttpServletRequest request =
            (HttpServletRequest)(FacesContext.getCurrentInstance().getExternalContext().getRequest());
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

            String reportDomainUrl = null;
                
            String ipAddress = request.getHeader("X-FORWARDED-FOR");  
            if (ipAddress == null) {  
                ipAddress = request.getRemoteAddr(); 
            }
            
            String ipPrefix = ipAddress.substring(0, 3);
            if (ipPrefixLocal.equalsIgnoreCase(ipPrefix)) {
                reportDomainUrl = reportDomainLoc; //Local IP
            } else {
                reportDomainUrl = reportDomain; //Public IP
            }
            
            /*
            if (savedRequest != null) {
                System.out.println("***SavedRequest URL:" + savedRequest.getRequestUrl());
            }
            */
            //externalContext.redirect(savedRequest != null ? savedRequest.getRequestUrl() : HOME_URL);
            
            if (accessStatus.equalsIgnoreCase(userAccessDisabled)) {
                StringBuilder message = new StringBuilder("<html><body>");
                message.append("<p>Username yang anda masukan telah expired.</p>");
                message.append("</body></html>");
                FacesMessage msg = new FacesMessage(message.toString());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                boolean sessionValid = true;
                StringBuilder messageSes = new StringBuilder("<html><body>");
                if (userLogged != null && userLogged.equalsIgnoreCase(userNameLogin) && !userLogged.equalsIgnoreCase(PASS_THRU_USER_ADMIN)) {
                    messageSes.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;Username <b>" + userNameLogin + "</b> sedang dalam sesi aktif / digunakan oleh user.&nbsp;&nbsp;&nbsp;&nbsp;</p>");
                    messageSes.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;Pastikan username tidak sedang digunakan oleh orang lain,</br>");
                    messageSes.append("&nbsp;&nbsp;&nbsp;&nbsp;atau apabila username tidak dipergunakan oleh orang lain,</br>");
                    messageSes.append("&nbsp;&nbsp;&nbsp;&nbsp;tunggu <b>" + sessionTime + " menit</b> supaya username login dapat dipergunakan kembali.&nbsp;&nbsp;&nbsp;&nbsp;</p>");
                    sessionValid = false;
                } else {
                    sessionValid = true;
                }
                messageSes.append("</body></html>");
                
                if (sessionValid) {
                    
                    UserData userData =
                      (UserData)JSFUtils.resolveExpression("#{UserData}");
                    userData.setLoggedIn(Boolean.TRUE);
                    userData.setFullName(fullName);
                    String fullNameSub = null;
                    if (fullName.length() > 16) {
                        List<String> nameList = new ArrayList<String>(Arrays.asList(fullName.split(" ")));
                        int nameSize = nameList.size();
                        if (nameSize > 1) {
                            fullNameSub = nameList.get(0) + " " + nameList.get(1);
                        } else {
                            fullNameSub = nameList.get(0);
                        }
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
                    userData.setReportDomain(reportDomainUrl);
                    userData.setCopyPpArea(copyPpArea);
                    userData.setUserLogged(userLogged);
                    userData.setPrintCount(printCount);
                    userData.setUnApprove(unApprove);
                    userData.setBudgetStatus(budgetStatus);
                    userData.setApprovalBudget(approvalBudget);
                    userData.setPrintMode(printMode);
                    userData.setUploadUser(uploadUser);
                    userData.setUserCustRegion(userCustRegion);
                    userData.setUserCustArea(userCustArea);
                    userData.setUserCustLocation(userCustLocation);
                    userData.setUserCustType(userCustType);
                    userData.setUserCustGroup(userCustGroup);
                    userData.setUserCustName(userCustName);
                    
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
                    
                    OperationBinding _historyApprovalAMSession =
                      ADFUtils.findOperation("setLoginToSession_HistoryApprovalAM");
                    _historyApprovalAMSession.execute();

                    externalContext.redirect( request.getContextPath()+HOME_URL);

                    SecurityAMImpl securityAM =
                        (SecurityAMImpl)ADFUtils.getApplicationModuleForDataControl("SecurityAMDataControl");
                    CallableStatement cst = null;
                    try {
                        cst =
                    securityAM.getDBTransaction().createCallableStatement("BEGIN FCS_LOGIN_BEAT('" + userNameLogin + "', 'INITIAL LOGIN " + userNameLogin + "'); END;", 0);
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
                } else {
                    FacesMessage msg = new FacesMessage(messageSes.toString());
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        } catch (Exception e) {        
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Isian salah pada \"Username\" atau \"Password\".</p>");
            message.append("</body></html>");
            FacesMessage msg = new FacesMessage(message.toString());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return "";
    }

    String getAbsoluteApplicationUrl() throws Exception {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)externalContext.getRequest();
        URL url = new URL(request.getRequestURL().toString());
        URL newUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), request.getContextPath());
        return newUrl.toString();
    }
    
    public String logout_action()throws IOException {
        SecurityUtils.getSubject().logout();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse)externalContext.getResponse();
        HttpServletRequest req = (HttpServletRequest)externalContext.getRequest();
        //externalContext.invalidateSession();
        externalContext.redirect(LOGIN_URL);
        try {
            response.sendRedirect((new StringBuilder()).append(req.getContextPath()).append("/faces/dashboard.jspx").toString());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(e.getLocalizedMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }       
        return null;               
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setRemember(boolean remember) {
        this.remember = remember;
    }
    public boolean isRemember() {
        return remember;
    }
    public String getKeyDigestString(String message, String key) throws NoSuchProviderException {
        try {
            String pwCompareStr = "";
            byte[] messageByte = message.getBytes();
            // if no key is provided, the message string gets encrypted with itself
            byte[] keyByte = (key != null && key.length() > 0) ? key.getBytes() : message.getBytes();
            // get SHA1 instance      
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1", "SUN");
            sha1.update(messageByte);
           
            //byte[] digestByte = sha1.digest(keyByte);
            byte[] digestByte = sha1.digest();

            // base 64 encoding
            BASE64Encoder b64Encoder = new BASE64Encoder();
            pwCompareStr = (b64Encoder.encode(digestByte));
            pwCompareStr = new StringBuilder("{SHA-1}").append(pwCompareStr).toString();
            return pwCompareStr;
        } catch (NoSuchAlgorithmException e) {
            FacesMessage msg = new FacesMessage(e.getLocalizedMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
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
}
