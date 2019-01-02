package app.fpp.bean.confirmation;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.ConfirmationAMImpl;

import app.fpp.model.views.confirmation.getPPorPCViewImpl;

import com.sun.mail.smtp.SMTPTransport;

import java.math.BigDecimal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.event.ActionEvent;

import javax.faces.model.SelectItem;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import javax.naming.InitialContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.adf.view.rich.model.AutoSuggestUIHints;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.DBSequence;
import oracle.jbo.server.ViewObjectImpl;


public class PcEmailNotifReceiverBean {
    private RichInputText txt_newEmail;

    public PcEmailNotifReceiverBean() {
    }

    public void btn_addEmailToList(ActionEvent actionEvent) {
        try {
            String Email =txt_newEmail.getValue() == null ?"":txt_newEmail.getValue().toString();
            if (!Email.equalsIgnoreCase("")) {
                String newmail = txt_newEmail.getValue().toString();
                if (newmail.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                    ConfirmationAMImpl confAM = (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
                    ADFContext adfCtx = ADFContext.getCurrent();
                    Map pageflowscope = adfCtx.getPageFlowScope();
                    DBSequence propIdDbSeq = (DBSequence)pageflowscope.get("propId");
                    String seqnum = propIdDbSeq.getSequenceNumber().toString();
                    PreparedStatement stq = confAM.getDBTransaction().createPreparedStatement("SELECT EMAIL_ADDRESS FROM EMAIL_NOTIF_RECEIVER WHERE PROPOSAL_ID = " + seqnum + " AND EMAIL_ADDRESS = '" + newmail + "'", 1);
                    boolean isEmailExists = false;
                    try {
                        ResultSet rsq = stq.executeQuery();
                        try {
                            if (rsq.next())
                                isEmailExists = true;
                        } finally {
                            rsq.close();
                        }
                    } finally {
                        stq.close();
                    }
                    if (!isEmailExists) {
                        ViewObjectImpl emailNotifReceiverVO = confAM.getEmailNotifReceiverView1();
                        emailNotifReceiverVO.executeQuery();
                       
                        Row emailNotifReceiverRow = emailNotifReceiverVO.createRow();
                        emailNotifReceiverRow.setAttribute("ProposalId", seqnum);
                        emailNotifReceiverRow.setAttribute("EmailAddress", newmail);
                        emailNotifReceiverRow.setAttribute("SendDate",new oracle.jbo.domain.Date(oracle.jbo.domain.Date.getCurrentDate()));
                        
                        emailNotifReceiverVO.insertRow(emailNotifReceiverRow);
                        ((BindingContext.getCurrent().getCurrentBindingsEntry()).getOperationBinding("Commit")).execute();
                        //confAM.getDBTransaction().commit();
                    }
                    else {
                        JSFUtils.addFacesErrorMessage("Alamat email yang akan ditambahkan sudah ada di dalam list.");
                    }
                    txt_newEmail.setValue("");
                    AdfFacesContext.getCurrentInstance().addPartialTarget(getTxt_newEmail());
                }
            }else{
                JSFUtils.addFacesErrorMessage("Alamat email yang akan ditambahkan masih kosong.");
            }
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
    }
    
    public static String getDisplayedDateFormat(String dateFormat)
      {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
      }
    public void btn_deleteRow(ActionEvent actionEvent) {
        try {
            ((BindingContext.getCurrent().getCurrentBindingsEntry()).getOperationBinding("Delete")).execute();
            ((BindingContext.getCurrent().getCurrentBindingsEntry()).getOperationBinding("Commit")).execute();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
    }

    public void setTxt_newEmail(RichInputText txt_newEmail) {
        this.txt_newEmail = txt_newEmail;
    }

    public RichInputText getTxt_newEmail() {
        return txt_newEmail;
    }
    
    private List<SelectItem> getList(String x) {
        List<SelectItem> list = new ArrayList<SelectItem>();
        try {
            ConfirmationAMImpl confAM = (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageflowscope = adfCtx.getPageFlowScope();
            DBSequence propIdDbSeq = (DBSequence)pageflowscope.get("propId");
            String seqnum = propIdDbSeq.getSequenceNumber().toString();
           /*  PreparedStatement stx = confAM.getDBTransaction().createPreparedStatement("SELECT COUNTROW,EMAIL_ADDRESS FROM " +
                "(SELECT DISTINCT COUNT(1) AS COUNTROW, EMAIL_ADDRESS FROM EMAIL_NOTIF_RECEIVER " +
                "WHERE EMAIL_ADDRESS NOT IN (SELECT EMAIL_ADDRESS FROM EMAIL_NOTIF_RECEIVER WHERE PROPOSAL_ID = " + seqnum + ") " +
                "AND EMAIL_ADDRESS LIKE '%" + x + "%' GROUP BY EMAIL_ADDRESS ORDER BY COUNTROW DESC, EMAIL_ADDRESS ASC) " +
                "WHERE ROWNUM <= 5", 1); */
             PreparedStatement stx = confAM.getDBTransaction().createPreparedStatement("select aa.COUNTROW,trim(aa.EMAIL_ADDRESS)as EMAIL_ADDRESS from(select COUNTROW,EMAIL_ADDRESS from(" + 
             "  SELECT DISTINCT COUNT (1) AS COUNTROW, EMAIL_ADDRESS" + 
             "            FROM EMAIL_NOTIF_RECEIVER" + 
             "           WHERE EMAIL_ADDRESS NOT IN (SELECT EMAIL_ADDRESS\n" + 
             "                                         FROM EMAIL_NOTIF_RECEIVER\n" + 
             "                                        WHERE PROPOSAL_ID =" + seqnum + ")" + 
             "                         GROUP BY EMAIL_ADDRESS" + 
             "        ORDER BY COUNTROW DESC, EMAIL_ADDRESS ASC)" + 
             "       union " + 
             "       SELECT DISTINCT COUNT (1) AS COUNTROW,descr as EMAIL_ADDRESS from app_user_access" + 
             "       group by descr)aa" + 
             "       where aa.EMAIL_ADDRESS LIKE '%" + x + "%' and aa.EMAIL_ADDRESS is not null and aa.EMAIL_ADDRESS !='-'" +
             "       and ROWNUM <= 5", 1);
            try {
                ResultSet rsx = stx.executeQuery();
                try {
                    while (rsx.next())
                        list.add(new SelectItem(rsx.getObject("EMAIL_ADDRESS"), rsx.getString("EMAIL_ADDRESS").trim()));
                } finally {
                    rsx.close();
                }
            } finally {
                stx.close();
            }
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        return list;
    }

    public List getEmailSmartList() {
        return getList(null);
    }

    public List getEmailSuggested(String x) {
        return getList(x);
    }

    public void sendEmailNotifBean(ActionEvent actionEvent) {
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        DCIteratorBinding dciterEmailNotifInsertView = ADFUtils.findIterator("EmailNotifReceiverView1Iterator"); 
        dciterEmailNotifInsertView.executeQuery();
        String emailUserList = null;        
        String mailSubject = "";
        String mailRecipient = "";
        String names=null;
        String nameRequester="";
        String Titlereq="";
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String Domain =
            userData.getReportDomain()== null ? "" : userData.getReportDomain();
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageflowscope = adfCtx.getPageFlowScope();
        DBSequence propIdDbSeq = (DBSequence)pageflowscope.get("propId");
        String propId = propIdDbSeq.getSequenceNumber().toString();
         getPPorPCViewImpl  getpppc=(getPPorPCViewImpl)confirmationAM.getgetPPorPCView1();
         getpppc.setNamedWhereClauseParam("pid", propId);
         getpppc.executeQuery();
        ViewObject find = getpppc.getViewObject();
        String pp="";
        String pc="";
        String ppdate="";
        String pcdate="";
//         Row er=find.getCurrentRow();
        while(find.hasNext()){
            Row er=find.next();
          pp =er.getAttribute("ProposalNo")==null?"":er.getAttribute("ProposalNo").toString();
          pc =er.getAttribute("ConfirmNo")==null?"":er.getAttribute("ConfirmNo").toString();
          ppdate=er.getAttribute("ProposalDate")==null?"":er.getAttribute("ProposalDate").toString();
          pcdate=er.getAttribute("ConfirmDate")==null?"":er.getAttribute("ConfirmDate").toString();
        }
//        System.out.println(ppdate+" "+pcdate+" "+pp+" "+pp);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date date = null;
        java.util.Date date1 = null;
        try {
            date = format1.parse(ppdate);
            date1 = format1.parse(pcdate);
        } catch (ParseException e) {
            JSFUtils.addFacesErrorMessage("Error",
                                          e.getLocalizedMessage());
        }
        String ppdatelast=formatter.format(date);
        String pcdatelast=formatter.format(date1);
        StringBuilder listName = new StringBuilder();
        if(dciterEmailNotifInsertView.getEstimatedRowCount()>0){
            for (Row r:dciterEmailNotifInsertView.getAllRowsInRange()){
                String  emailuser=r.getAttribute("EmailAddress").toString();
                String str=emailuser;
              if (emailUserList == null && str != null) {
                  emailUserList = str;
              } else {
                  if (str != null && !emailUserList.contains(str)) {
                      emailUserList = emailUserList + "," + str;
                  }
              }
                String action=r.getAttribute("Action")==null ? "":r.getAttribute("Action").toString();
                if(action.equalsIgnoreCase("SUBMIT")){
                    nameRequester=r.getAttribute("FullName").toString();
                    Titlereq=r.getAttribute("Title")==null?"":r.getAttribute("Title").toString();
                }else if(action.equalsIgnoreCase("APPROVED")){
                    String Title=r.getAttribute("Title")==null?"":r.getAttribute("Title").toString();
                    String nameApproval=r.getAttribute("FullName").toString();
                    listName.append(Title.replace("-", "")+" "+nameApproval+",");
                }
          }
            
            String namesOri = listName.toString();
            if(namesOri.length() > 0){
            names=namesOri.substring(0, namesOri.length()-1);
            }
        }
        mailRecipient = emailUserList;
        /* System.out.println("mailRecipient "+mailRecipient);
        System.out.println("names "+names);
        System.out.println("nameRequester  "+nameRequester); */
        mailSubject ="No.PP "+pp+" Anda telah sukses diproses ke PC dengan No.PC "+pc+"";
        ArrayList<String> recipient =
            new ArrayList<String>(Arrays.asList(mailRecipient.split(",")));
        //Add body content + docAction +  "+ fullName +"
        String bodyContent = "<div>Dear "+Titlereq.replace("-", "")+""+" "+""+nameRequester+","+"</br></br></div>\n" +
        "<div>Pesan ini disampaikan sebagai notifikasi bahwa PP Anda telah berhasil di proses menjadi PC.</br></br></div>\n"+
        "<div>No.PP: "+pp+"/"+ppdatelast+"</br></br></div>\n" +
        "<div>No.PC: "+pc+"/"+pcdatelast+"</br></br></div>\n" +
        "<div>Untuk melihat informasi PC secara detail silahkan buka link di bawah ini: </br></br></div>\n" +
        "<div><a href="+Domain+"/birt/output?__report=pcpp-prop.rptdesign&PROP_ID="+propId+"&&dpi=96&format=pdf&pageoverflow=0&_overwrite=false&&__format=pdf&__pageoverflow=2&__overwrite=false>"+"Link Dokumen PC"+"</a> </br></br></div>\n"+
        "<div></br></br></div>\n" +
        "<div>cc: "+names+"</br></br></div>\n" +                  
        "<div>Terima kasih.</br></br></div>\n" +
        "<div></br></br></div>\n" +
        "<div>NB: Agar tidak me-reply email ini karena email ini digenerate otomatis dari system.</br></br></div>\n" +
        "<div>Jika ada pertanyaan silahkan menghubungi tim promo atau email ke admpromo@focusjkt.com / hp: 08111931299. </br></br></div>\n";
        sendMail(mailSubject, recipient, bodyContent);
        String statusEmail=sendMail(mailSubject, recipient, bodyContent)==null ?"":sendMail(mailSubject, recipient, bodyContent);
                if(statusEmail.equalsIgnoreCase("successfully")){
                    for (Row r:dciterEmailNotifInsertView.getAllRowsInRange()){
                          String pId= r.getAttribute("ProposalId").toString();
                        if(pId.equalsIgnoreCase(propId)){
                            BigDecimal count=new BigDecimal(r.getAttribute("MailCount")== null ? "0":r.getAttribute("MailCount").toString());
        //                    if(count.compareTo(BigDecimal.ZERO)>0){
                            BigDecimal  total=count.add(BigDecimal.ONE);
                            r.setAttribute("MailCount", total);
        //                    }
                        }
                    }
                    dciterEmailNotifInsertView.getDataControl().commitTransaction();
                }
    }
    
    public String sendMail(String subject, ArrayList<String> recipient, String bodyContent){
        try{
                /**
                 * Initialize mail session
                 * Note: "focus.mailsession" is the JNDI name in mail session defined in Weblogic server
                 */
                InitialContext ic = new InitialContext();
                Session session1 = (Session) ic.lookup("focus.mailsession");
                Properties props1 = session1.getProperties();
                //props.list(System.out);
                //fetch properties set in Mail-session in Weblogic server
                String mailhost = props1.getProperty("mail.smtp.host");
                String protocol = props1.getProperty("mail.transport.protocol");
                String port = props1.getProperty("mail.smtp.port");
                final String password1 = props1.getProperty("mail.smpt.password");  
                final String user = props1.getProperty("mail.smtp.user"); 
                String mailDisabled1 = props1.getProperty("mail.disable");
                  String mailDisabled = mailDisabled1;
                final String username = user;
                final String password = password1;
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", mailhost);
                props.put("mail.smtp.port",port);
                Session session = Session.getInstance(props,
                  new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                        }
                  });
             
                //get message body text
                String bodyText = null;
                bodyText =  "Email ini dikirim oleh sistem notifikasi PPPC. Tidak usah dibalas. \n \n ";
            
                if(bodyContent != null)
                    bodyText = bodyText+bodyContent;
                bodyText =  bodyText+"\n \n Akhir email notifikasi \n";
                
                //get message subject
                if(subject==null || subject.equals(""))
                    subject = "Email Sistem Notifikasi PPPC";
                /**
                 * use sendDisabled flag to control mail enable or disable option from weblogic server. 
                 * Whenever you want to disable mail sending, you can set this "sentDisable" = true
                 * 
                 */
                 
                mailDisabled = mailDisabled == null ? "false" : mailDisabled;
                boolean sentDisabled = false;
                if(mailDisabled.equals("true"))
                    sentDisabled = true;
        
                //check if mail send option is disabled in Weblogic server.
                if(!sentDisabled){
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setSubject(subject, "UTF-8");
                    message.setSentDate(new Date());      
                    
                    //Add all recipients
                    InternetAddress[] addressTo = new InternetAddress[recipient.size()];
                    for (int i = 0; i < recipient.size(); i++)
                    {
                        addressTo[i] = new InternetAddress(recipient.get(i));
                    }
                    
                    //if recipient list is null then send email to default DL(distribution List)
                    if(recipient.size() <1) 
                        addressTo[0] = new InternetAddress("oraclefocus@focusjkt.com");
                    
                    message.setRecipients(Message.RecipientType.TO, addressTo); 
                    
                    //Add body part of message here, Alternative TEXT/HTML content    
                    MimeBodyPart wrap = new MimeBodyPart();
                    
                    MimeMultipart cover = new MimeMultipart("alternative");
                    MimeBodyPart html = new MimeBodyPart();
                    MimeBodyPart text = new MimeBodyPart();
                    cover.addBodyPart(html);
                    
                    wrap.setContent(cover);
                    
                    MimeMultipart content = new MimeMultipart("related");
                    message.setContent(content);
                    content.addBodyPart(wrap);
                    
                    html.setContent("<html><head><style type=\"text/css\"> " +
                            ".rsTbl { " + 
                            "        border:thin; " + 
                            "        border-color:#666666; " + 
                            "        border-style:solid; " + 
                            "        font-family:Arial, Helvetica, sans-serif; " + 
                            "        font-size:14px; " + 
                            "} " + 
                            ".rsColTblHdr { " + 
                            "        border-bottom:thin; " + 
                            "        border-color:#666666; " + 
                            "        background-color:#B0F169; " + 
                            "        font-family:Arial, Helvetica, sans-serif; " + 
                            "        font-size:14px; " + 
                            "} " + 
                            ".rsColTblContent { " + 
                            "        border:thin; " + 
                            "        padding:5px; " + 
                            "        border-right:thin; " + 
                            "        border-color:#666666; " + 
                            "        font-family:Arial, Helvetica, sans-serif; " + 
                            "        font-size:14px; " + 
                            "} " + 
                            " " + 
                            ".rowOdd {        " + 
                            "        background-color:#FFFFFF; " + 
                            "} " + 
                            ".rowEven { " + 
                            "        background-color:#DDFCBA; " + 
                            "} " + 
                            " " +  
                            "</style></head><body>" + bodyContent + "</body></html>", "text/html");
                    // Send the message                    
                    SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
                    try {
                        //t.connect(mailhost, port, null);
                        t.send(message);
                        JSFUtils.addFacesInformationMessage("Message dikirim");
                    } finally {
                        t.close();
                    }
                
                }else{
                    System.out.println("Mail Sending is disabled.");
                }
            } catch(Exception e){
                e.printStackTrace();
                JSFUtils.addFacesErrorMessage(e.getMessage());
            }        
        return "successfully";
    }
}
