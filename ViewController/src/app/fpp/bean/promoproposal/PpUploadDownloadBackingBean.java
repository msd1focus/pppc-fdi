package app.fpp.bean.promoproposal;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.model.am.PromoProposalAMImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.util.ResetUtils;

import oracle.binding.BindingContainer;

import oracle.jbo.JboException;
import oracle.jbo.ViewObject;

import org.apache.myfaces.trinidad.model.UploadedFile;

public class PpUploadDownloadBackingBean {

    private RichInputFile itUpload;
    private RichOutputText pathBind;

    public PpUploadDownloadBackingBean() {
    }
    
    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    
    private String uploadFile(UploadedFile file) {
        UploadedFile myfile = file;
        String path = null;
        if (myfile == null) {
        } else {
            // All uploaded files will be stored in below path
            String PathName=null;
            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindings.findIteratorBinding("LookupCodeCRUDView1Iterator");
            ViewObject vo = dcItteratorBindings.getViewObject();
            vo.setWhereClause("LookupCode.TITLE = 'APP_UPLOAD_FOL_PATH'");
            vo.executeQuery();
                PathName= vo.first().getAttribute("Value").toString();
            path = PathName.trim() + myfile.getFilename();
            InputStream inputStream = null;
            try {
                FileOutputStream out = new FileOutputStream(path);
                inputStream = myfile.getInputStream();
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
                out.close();
            } catch (Exception ex) {
                // handle exception
                ex.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }
        return path;
    }


    public void uploadDocEvent(ActionEvent actionEvent) {
        PromoProposalAMImpl promoProposalAM =
            (PromoProposalAMImpl)ADFUtils.getApplicationModuleForDataControl("PromoProposalAMDataControl");
        if ( itUpload.getValue() != null) {
        UploadedFile fileVal = (UploadedFile)itUpload.getValue();

        String path = uploadFile(fileVal);
        AdfFacesContext context = AdfFacesContext.getCurrentInstance();
        Map pfScope = context.getPageFlowScope();
        DCIteratorBinding iter = ADFUtils.findIterator("UploadDownloadView1Iterator");
        PreparedStatement addProdBonusItemStmt = null;
        try {
            if(iter.getEstimatedRowCount() < 6){
            String SQL =
                "INSERT INTO UPLOAD_DOWNLOAD (PROPOSAL_ID, FILE_NAME, PATH, IDFILE ) " +
                "VALUES (" + pfScope.get("propId") + ", '" + fileVal.getFilename() + "','" + path + "', UPLOAD_DOWNLOAD_SEQ.NEXTVAL)";
                addProdBonusItemStmt =
                        promoProposalAM.getDBTransaction().createPreparedStatement(SQL,1);
                addProdBonusItemStmt.executeUpdate();
            }
            /*else{
                String SQL =
                    "UPDATE UPLOAD_DOWNLOAD SET FILE_NAME=?, PATH=? WHERE PROPOSAL_ID=?";
                    addProdBonusItemStmt =
                            promoProposalAM.getDBTransaction().createPreparedStatement(SQL,1);
                addProdBonusItemStmt.setString(1,fileVal.getFilename());
                addProdBonusItemStmt.setString(2, path);
                addProdBonusItemStmt.setString(3, pfScope.get("propId").toString());
                addProdBonusItemStmt.executeUpdate();
            }*/
        } catch (SQLException e) {
            throw new JboException(e.getMessage());
        } finally {
            promoProposalAM.getDBTransaction().commit();
            if (addProdBonusItemStmt != null) {
                try {
                    addProdBonusItemStmt.close();
                } catch (Exception e) {
                    throw new JboException(e.getMessage());
                }
            }
        }
        iter.executeQuery();
        ResetUtils.reset(actionEvent.getComponent());
    }
    }

    public void downloadAction(FacesContext facesContext,
                               OutputStream outputStream) throws Exception {
            
        File filed = new File(pathBind.getValue().toString());
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);

            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.flush();
    }

    public void setItUpload(RichInputFile itUpload) {
        this.itUpload = itUpload;
    }

    public RichInputFile getItUpload() {
        return itUpload;
    }
    public void setPathBind(RichOutputText pathBind) {
        this.pathBind = pathBind;
    }

    public RichOutputText getPathBind() {
        return pathBind;
    }
}
