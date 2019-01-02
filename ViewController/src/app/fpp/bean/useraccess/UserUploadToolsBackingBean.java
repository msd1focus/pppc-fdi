package app.fpp.bean.useraccess;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.model.am.UserAccessAMImpl;
import app.fpp.model.views.useraccess.AppUserAccessViewImpl;
import app.fpp.model.views.useraccess.uploadfile.sync.UpdateAppUserAccessImpl;
import app.fpp.model.views.useraccess.uploadfile.sync.UpdateAppUserAccessRowImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtRolesValidationImpl;
import app.fpp.model.views.useraccess.uploadfile.upload.UtUserAccessViewImpl;
import app.fpp.model.views.useraccess.uploadfile.upload.UtUserAccessViewRowImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.CheckRoleExistInApprovalFlowImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.CustHierarchyLookupImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.CustHierarchyLookupRowImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtCodeValidateImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtUserInitialValidationImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtUserRoleValidationFoodAreaImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtUserRoleValidationFoodHoImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtUserRoleValidationNonFoodAreaImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.UtUserRoleValidationNonFoodHoImpl;
import app.fpp.model.views.useraccess.uploadfile.validate.ValidateLookupCodeImpl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Scanner;
import javax.faces.event.ActionEvent;

import oracle.adf.model.BindingContext;

import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;
import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.layout.RichPanelFormLayout;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.jbo.Row;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.apache.commons.csv.*;

public class UserUploadToolsBackingBean {
    private UploadedFile file;
    private RichPanelFormLayout frmStatus;
    private RichInputFile inputFileBinding;
    private RichPanelFormLayout frmStatusSync;
    private String aprvlNmFoodHo = "PROPOSAL FOOD HO";
    private String aprvlNmNonFoodHo = "PROPOSAL NON FOOD HO";
    private String aprvlNmFoodArea = "PROPOSAL FOOD";
    private String aprvlNmNonFoodArea = "PROPOSAL NON FOOD";
    private String skipCompany = "FDN";
    private UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
    private ArrayList<UserAccess> listValidateUtParent = new ArrayList<UserAccess>();
    private RichShowDetailItem tabProcessLog;
    private RichShowDetailItem tabUpload;

    public UserUploadToolsBackingBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFrmStatus(RichPanelFormLayout frmStatus) {
        this.frmStatus = frmStatus;
    }

    public RichPanelFormLayout getFrmStatus() {
        return frmStatus;
    }

    public void setInputFileBinding(RichInputFile inputFileBinding) {
        this.inputFileBinding = inputFileBinding;
    }

    public RichInputFile getInputFileBinding() {
        return inputFileBinding;
    }

    public void btnUpload_Click() {
        BindingContainer bindings = getBindings();
        getInputFileBinding().setImmediate(true);
        if (file == null)
            JSFUtils.addFacesErrorMessage("Pilih file yang akan diupload terlebih dahulu.");
        else if (!file.getFilename().endsWith(".csv"))
            JSFUtils.addFacesErrorMessage("File yang akan diupload harus dalam format file .csv");
        else {
            try {
                Scanner fileScanner = new Scanner(file.getInputStream());
                String fileString = fileScanner.nextLine() + "\n";
                char csvDelimiter = getDelimiter(fileString);
                while (fileScanner.hasNext())
                    fileString += fileScanner.nextLine() + "\n";
                CSVParser parsedFile = CSVParser.parse(fileString, CSVFormat.DEFAULT.withDelimiter(csvDelimiter).withFirstRecordAsHeader());
                if (parsedFile.getRecords().get(0).size() != 44)
                    JSFUtils.addFacesErrorMessage("File yang akan diupload tidak sesuai dengan template (44 kolom). ");
                else {
                    parsedFile = CSVParser.parse(fileString, CSVFormat.DEFAULT.withDelimiter(csvDelimiter).withFirstRecordAsHeader());
                    clearDataBeforeUpload();
                    for (CSVRecord csvLine : parsedFile) {
                        boolean isLineHaveCustData = false;
                        boolean isLineHaveProdData = false;
                        if (csvLine.get(5).equals(skipCompany))
                            continue;
                        UtUserAccessViewImpl utUserAccessView = userAccessAM.getUtUserAccessView1();
                        //check if user access already exists, if not, insert new user access
                        utUserAccessView.setWhereClause("UtUserAccess.USER_NAME = '" + csvLine.get(1) + "'");
                        utUserAccessView.executeQuery();
                        if (utUserAccessView.getEstimatedRowCount() == 0) {
                            listValidateUtParent.add(new UserAccess(csvLine.get(1).toUpperCase()));
                            UtUserAccessViewRowImpl newUserAccess = (UtUserAccessViewRowImpl)utUserAccessView.createRow();
                            newUserAccess.setUserName(csvLine.get(1).toUpperCase());
                            newUserAccess.setPassword(csvLine.get(1).toUpperCase());
                            newUserAccess.setFullName(csvLine.get(2));
                            newUserAccess.setDescr(!csvLine.get(41).isEmpty() ? csvLine.get(41) : "-");
                            newUserAccess.setTitle("-");
                            newUserAccess.setContactNo(!csvLine.get(43).isEmpty() ? csvLine.get(43) : "-");
                            newUserAccess.setCompanyId(skipCompany == "FDN" ? "I" : "N");
                            newUserAccess.setActivePeriodFrom((new Date(csvLine.get(0).substring(0, 4) + "-" + csvLine.get(0).substring(4, 6) + "-" + "01")));
                            newUserAccess.setActivePeriodTo(new Date((Integer.parseInt(csvLine.get(0).substring(0, 4)) + 1) + "-" + "12" + "-" + "31"));
                            newUserAccess.setUserInitial(csvLine.get(40).length() == 3 ? csvLine.get(40).toUpperCase() : randomUserInitial(csvLine.get(2)));
                            newUserAccess.setUserType(csvLine.get(42));
                            newUserAccess.setUserDivision(csvLine.get(26).equalsIgnoreCase("CT001") ? "FOOD" : (csvLine.get(26).equalsIgnoreCase("CT002") ? "NONFOOD" : "ALL"));
                            newUserAccess.setDirectSpv(csvLine.get(6).toUpperCase());
                            newUserAccess.setCustAuthFlag("-");
                            utUserAccessView.insertRow(newUserAccess);
                            bindings.getOperationBinding("Commit").execute();
                            utUserAccessView.executeQuery();
                            UtUserAccessViewRowImpl currRowUserAccess = (UtUserAccessViewRowImpl)utUserAccessView.first();
                            RowIterator utUserAccessRolesRI = currRowUserAccess.getUtUserAccessRolesView();
                            Row newUserAccessRoles = utUserAccessRolesRI.createRow();
                            newUserAccessRoles.setAttribute("UserName", csvLine.get(1).toUpperCase());
                            newUserAccessRoles.setAttribute("Role", csvLine.get(3));
                            utUserAccessRolesRI.insertRow(newUserAccessRoles);
                            RowIterator utUserProdukCategoryRI = currRowUserAccess.getUtUserProdukCategoryView();
                            if (currRowUserAccess.getUserDivision().equals("ALL")) {
                                ViewObjectImpl utAllProdukCategoryVO = userAccessAM.getUtAllProdukCategory1();
                                utAllProdukCategoryVO.executeQuery();
                                while (utAllProdukCategoryVO.hasNext()) {
                                    Row utAllProdukCategoryRow = utAllProdukCategoryVO.next();
                                    Row newUserProdukCategory = utUserProdukCategoryRI.createRow();
                                    newUserProdukCategory.setAttribute("UserName", csvLine.get(1).toUpperCase());
                                    newUserProdukCategory.setAttribute("ProdCategory", utAllProdukCategoryRow.getAttribute("SetCategory"));
                                    newUserProdukCategory.setAttribute("CategoryDesc", "-");
                                    utUserProdukCategoryRI.insertRow(newUserProdukCategory);
                                }
                                utAllProdukCategoryVO.closeRowSet();
                            }
                            else {
                                Row newUserProdukCategory = utUserProdukCategoryRI.createRow();
                                newUserProdukCategory.setAttribute("UserName", csvLine.get(1).toUpperCase());
                                newUserProdukCategory.setAttribute("ProdCategory", csvLine.get(26).toUpperCase());
                                newUserProdukCategory.setAttribute("CategoryDesc", "-");
                                utUserProdukCategoryRI.insertRow(newUserProdukCategory);
                            }
                            bindings.getOperationBinding("Commit").execute();
                        }
                        utUserAccessView.executeQuery();
                        UtUserAccessViewRowImpl currRowUserAccess = (UtUserAccessViewRowImpl)utUserAccessView.first();
                        RowIterator utUserRegionRI = currRowUserAccess.getUtUserRegionView();
                        RowIterator utUserAreaRI = currRowUserAccess.getUtUserAreaView();
                        RowIterator utUserLocRI = currRowUserAccess.getUtUserLocView();
                        RowIterator utUserCustTypeRI = currRowUserAccess.getUtUserCustTypeView();
                        RowIterator utUserCustGroupRI = currRowUserAccess.getUtUserCustGroupView();
                        RowIterator utUserCustRI = currRowUserAccess.getUtUserCustView();
                        RowIterator utUserProdClassRI = currRowUserAccess.getUtUserProdukClassView();
                        RowIterator utUserProdBrandRI = currRowUserAccess.getUtUserProdukBrandView();
                        RowIterator utUserProdExtRI = currRowUserAccess.getUtUserProdukExtView();
                        RowIterator utUserProdPackRI = currRowUserAccess.getUtUserProdukPackView();
                        RowIterator utUserProdVariantRI = currRowUserAccess.getUtUserProdukVariantView();
                        RowIterator utUserProdItemRI = currRowUserAccess.getUtUserProdukItemView();
                        String username = csvLine.get(1).toUpperCase();
                        UserAccess currListValidateUtParent = listValidateUtParent.get(getIndexListValdateUtParent(username));
                        //insert to user_region table if data specified in .csv
                        if (!csvLine.get(9).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newregion = csvLine.get(9).toUpperCase();
                            //check if the region code already exists
                            boolean isUserRegionExists = false;
                            while (utUserRegionRI.hasNext()) {
                                Row utUserRegionRow = utUserRegionRI.next();
                                if (utUserRegionRow.getAttribute("RegionCode").equals(newregion))
                                    isUserRegionExists = true;
                            }
                            if (!isUserRegionExists) {
                                Row newUserRegion = utUserRegionRI.createRow();
                                newUserRegion.setAttribute("UserName", username);
                                newUserRegion.setAttribute("RegionCode", newregion);
                                utUserRegionRI.insertRow(newUserRegion);
                                currListValidateUtParent.userCode.add(newregion);
                                if (currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("-"))
                                    currRowUserAccess.setCustAuthFlag("REGION");
                            }
                        }
                        //insert to user_area table if data specified in .csv
                        if (!csvLine.get(11).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newarea = csvLine.get(11).toUpperCase();
                            //check if the area code already exists
                            boolean isUserAreaExists = false;
                            while (utUserAreaRI.hasNext()) {
                                Row utUserAreaRow = utUserAreaRI.next();
                                if (utUserAreaRow.getAttribute("AreaCode").equals(newarea))
                                    isUserAreaExists = true;
                            }
                            if (!isUserAreaExists) {
                                Row newUserArea = utUserAreaRI.createRow();
                                newUserArea.setAttribute("UserName", username);
                                newUserArea.setAttribute("AreaCode", newarea);
                                utUserAreaRI.insertRow(newUserArea);
                                currListValidateUtParent.userCode.add(newarea);
                                    if ((currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("-")) || (currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("REGION")))
                                    currRowUserAccess.setCustAuthFlag("AREA");
                                //add parent data (region_code)
                                //disabled: already done by trigger
                                /*CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                                boolean isDataNull = true;
                                while (isDataNull) {
                                    hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custArea", newarea);
                                    hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custType", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custId", "");
                                    hierarchyLookup.executeQuery();
                                    if (hierarchyLookup.getEstimatedRowCount() == 0)
                                        break;
                                    else {
                                        CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.first();
                                        String resCustRegion = hierarchyLookupRow.getCustRegion();
                                        String resCustArea = hierarchyLookupRow.getCustArea();
                                        //System.out.println(username + ":" + newarea + "(" + hierarchyLookup.getEstimatedRowCount() + ")" + " => " + resCustRegion + " => " + resCustArea);
                                        if ((resCustRegion != null) && (resCustArea != null))
                                            isDataNull = false;
                                        if (!isDataNull) {
                                            if (!currListValidateUtParent.userCode.contains(resCustRegion)) {
                                                Row newUserRegion = utUserRegionRI.createRow();
                                                newUserRegion.setAttribute("UserName", username);
                                                newUserRegion.setAttribute("RegionCode", resCustRegion);
                                                utUserRegionRI.insertRow(newUserRegion);
                                                currListValidateUtParent.userCode.add(resCustRegion);
                                            }
                                        }
                                    }
                                }
                                hierarchyLookup.closeRowSet();*/
                            }
                        }
                        //insert to user_loc table if data specified in .csv
                        if (!csvLine.get(10).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newloc = csvLine.get(10).toUpperCase();
                            //check if the loc code already exists
                            boolean isUserLocExists = false;
                            while (utUserLocRI.hasNext()) {
                                Row utUserLocRow = utUserLocRI.next();
                                if (utUserLocRow.getAttribute("LocationCode").equals(newloc))
                                    isUserLocExists = true;
                            }
                            if (!isUserLocExists) {
                                Row newUserLoc = utUserLocRI.createRow();
                                newUserLoc.setAttribute("UserName", username);
                                newUserLoc.setAttribute("LocationCode", newloc);
                                utUserLocRI.insertRow(newUserLoc);
                                currListValidateUtParent.userCode.add(newloc);
                                if ((currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("-")) || (currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("REGION")) || (currRowUserAccess.getCustAuthFlag().equalsIgnoreCase("AREA")))
                                    currRowUserAccess.setCustAuthFlag("LOCATION");
                                //add parent data (region_code, area_code)
                                //disabled: already done by trigger
                                /*CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                                boolean isDataNull = true;
                                while (isDataNull) {
                                    hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custLoc", newloc);
                                    hierarchyLookup.setNamedWhereClauseParam("custType", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custId", "");
                                    hierarchyLookup.executeQuery();
                                    if (hierarchyLookup.getEstimatedRowCount() == 0)
                                        break;
                                    else {
                                        CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.first();
                                        String resCustRegion = hierarchyLookupRow.getCustRegion();
                                        String resCustArea = hierarchyLookupRow.getCustArea();
                                        String resCustLoc = hierarchyLookupRow.getCustLoc();
                                        //System.out.println(username + ":" + newloc + "(" + hierarchyLookup.getEstimatedRowCount() + ")" + " => " + resCustRegion + " => " + resCustArea + " => S" + resCustLoc);
                                        if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null))
                                            isDataNull = false;
                                        if (!isDataNull) {
                                            if (!currListValidateUtParent.userCode.contains(resCustRegion)) {
                                                Row newUserRegion = utUserRegionRI.createRow();
                                                newUserRegion.setAttribute("UserName", username);
                                                newUserRegion.setAttribute("RegionCode", resCustRegion);
                                                utUserRegionRI.insertRow(newUserRegion);
                                                currListValidateUtParent.userCode.add(resCustRegion);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustArea)) {
                                                Row newUserArea = utUserAreaRI.createRow();
                                                newUserArea.setAttribute("UserName", username);
                                                newUserArea.setAttribute("AreaCode", resCustArea);
                                                utUserAreaRI.insertRow(newUserArea);
                                                currListValidateUtParent.userCode.add(resCustArea);
                                            }
                                        }
                                    }
                                }
                                hierarchyLookup.closeRowSet();*/
                            }
                        }
                        //insert to user_cust_type table if data specified in .csv
                        if (!csvLine.get(13).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newcusttype = csvLine.get(13).toUpperCase();
                            //check if the cust type code already exists
                            boolean isUserCustTypeExists = false;
                            while (utUserCustTypeRI.hasNext()) {
                                Row utUserCustTypeRow = utUserCustTypeRI.next();
                                if (utUserCustTypeRow.getAttribute("CustType").equals(newcusttype))
                                    isUserCustTypeExists = true;
                            }
                            if (!isUserCustTypeExists) {
                                Row newUserCustType = utUserCustTypeRI.createRow();
                                newUserCustType.setAttribute("UserName", username);
                                newUserCustType.setAttribute("CustType", newcusttype);
                                utUserCustTypeRI.insertRow(newUserCustType);
                                currListValidateUtParent.userCode.add(newcusttype);
                                //add parent data (region_code, area_code, location_code)
                                //disabled: already done by trigger
                                /*CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                                boolean isDataNull = true;
                                while (isDataNull) {
                                    hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custType", newcusttype);
                                    hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custId", "");
                                    hierarchyLookup.executeQuery();
                                    if (hierarchyLookup.getEstimatedRowCount() == 0)
                                        break;
                                    else {
                                        CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.first();
                                        String resCustRegion = hierarchyLookupRow.getCustRegion();
                                        String resCustArea = hierarchyLookupRow.getCustArea();
                                        String resCustLoc = hierarchyLookupRow.getCustLoc();
                                        String resCustType = hierarchyLookupRow.getCustType();
                                        //System.out.println(username + ":" + newcusttype + "(" + hierarchyLookup.getEstimatedRowCount() + ")" + " => " + resCustRegion + " => " + resCustArea + " => S" + resCustLoc + " => " + resCustType);
                                        if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null))
                                            isDataNull = false;
                                        if (!isDataNull) {
                                            if (!currListValidateUtParent.userCode.contains(resCustRegion)) {
                                                Row newUserRegion = utUserRegionRI.createRow();
                                                newUserRegion.setAttribute("UserName", username);
                                                newUserRegion.setAttribute("RegionCode", resCustRegion);
                                                utUserRegionRI.insertRow(newUserRegion);
                                                currListValidateUtParent.userCode.add(resCustRegion);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustArea)) {
                                                Row newUserArea = utUserAreaRI.createRow();
                                                newUserArea.setAttribute("UserName", username);
                                                newUserArea.setAttribute("AreaCode", resCustArea);
                                                utUserAreaRI.insertRow(newUserArea);
                                                currListValidateUtParent.userCode.add(resCustArea);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustLoc)) {
                                                Row newUserLoc = utUserLocRI.createRow();
                                                newUserLoc.setAttribute("UserName", username);
                                                newUserLoc.setAttribute("LocationCode", resCustLoc);
                                                utUserLocRI.insertRow(newUserLoc);
                                                currListValidateUtParent.userCode.add(resCustLoc);
                                            }
                                        }
                                    }
                                }
                                hierarchyLookup.closeRowSet();*/
                            }
                        }
                        //insert to user_cust_group table if data specified in .csv
                        if (!csvLine.get(12).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newcustgroup = csvLine.get(12).toUpperCase();
                            //check if the cust group code already exists
                            boolean isUserCustGroupExists = false;
                            while (utUserCustGroupRI.hasNext()) {
                                Row utUserCustGroupRow = utUserCustGroupRI.next();
                                if (utUserCustGroupRow.getAttribute("CustGroup").equals(newcustgroup))
                                    isUserCustGroupExists = true;
                            }
                            if (!isUserCustGroupExists) {
                                Row newUserCustGroup = utUserCustGroupRI.createRow();
                                newUserCustGroup.setAttribute("UserName", username);
                                newUserCustGroup.setAttribute("CustGroup", newcustgroup);
                                utUserCustGroupRI.insertRow(newUserCustGroup);
                                currListValidateUtParent.userCode.add(newcustgroup);
                                //add parent data (region_code, area_code, location_code, cust_type)
                                //disabled: already done by trigger
                                /*CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                                boolean isDataNull = true;
                                while (isDataNull) {
                                    hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custType", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custGroup", newcustgroup);
                                    hierarchyLookup.setNamedWhereClauseParam("custId", "");
                                    hierarchyLookup.executeQuery();
                                    if (hierarchyLookup.getEstimatedRowCount() == 0)
                                        break;
                                    else {
                                        CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.first();
                                        String resCustRegion = hierarchyLookupRow.getCustRegion();
                                        String resCustArea = hierarchyLookupRow.getCustArea();
                                        String resCustLoc = hierarchyLookupRow.getCustLoc();
                                        String resCustType = hierarchyLookupRow.getCustType();
                                        String resCustGroup = hierarchyLookupRow.getCustGroup();
                                        //System.out.println(username + ":" + newcustgroup + "(" + hierarchyLookup.getEstimatedRowCount() + ")" + " => " + resCustRegion + " => " + resCustArea + " => S" + resCustLoc + " => " + resCustType + " => " + resCustGroup);
                                        if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null) && (resCustGroup != null))
                                            isDataNull = false;
                                        if (!isDataNull) {
                                            if (!currListValidateUtParent.userCode.contains(resCustRegion)) {
                                                Row newUserRegion = utUserRegionRI.createRow();
                                                newUserRegion.setAttribute("UserName", username);
                                                newUserRegion.setAttribute("RegionCode", resCustRegion);
                                                utUserRegionRI.insertRow(newUserRegion);
                                                currListValidateUtParent.userCode.add(resCustRegion);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustArea)) {
                                                Row newUserArea = utUserAreaRI.createRow();
                                                newUserArea.setAttribute("UserName", username);
                                                newUserArea.setAttribute("AreaCode", resCustArea);
                                                utUserAreaRI.insertRow(newUserArea);
                                                currListValidateUtParent.userCode.add(resCustArea);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustLoc)) {
                                                Row newUserLoc = utUserLocRI.createRow();
                                                newUserLoc.setAttribute("UserName", username);
                                                newUserLoc.setAttribute("LocationCode", resCustLoc);
                                                utUserLocRI.insertRow(newUserLoc);
                                                currListValidateUtParent.userCode.add(resCustLoc);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustType)) {
                                                Row newUserCustType = utUserCustTypeRI.createRow();
                                                newUserCustType.setAttribute("UserName", username);
                                                newUserCustType.setAttribute("CustType", resCustType);
                                                utUserCustTypeRI.insertRow(newUserCustType);
                                                currListValidateUtParent.userCode.add(resCustType);
                                            }
                                        }
                                    }
                                }
                                hierarchyLookup.closeRowSet();*/
                            }
                        }
                        //insert to user_cust table if data specified in .csv
                        if (!csvLine.get(14).equals("")) {
                            if (!isLineHaveCustData) isLineHaveCustData = true;
                            String newusercust = csvLine.get(14);
                            //check if the cust id code already exists
                            boolean isUserCustExists = false;
                            
                            while (utUserCustRI.hasNext()) {
                                Row utUserCustRow = utUserCustRI.next();
                                if (utUserCustRow.getAttribute("CustomerId").equals(newusercust))
                                    isUserCustExists = true;
                            }
                            if (!isUserCustExists) {
                                Row newUserCust = utUserCustRI.createRow();
                                newUserCust.setAttribute("UserName", username);
                                newUserCust.setAttribute("CustomerId", newusercust);
                                utUserCustRI.insertRow(newUserCust);
                                currListValidateUtParent.userCode.add(newusercust);
                                //add parent data (region_code, area_code, location_code, cust_type, cust_group)
                                //disabled: already done by trigger
                                /*CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                                boolean isDataNull = true;
                                while (isDataNull) {
                                    hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custType", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                                    hierarchyLookup.setNamedWhereClauseParam("custId", newusercust);
                                    hierarchyLookup.executeQuery();
                                    if (hierarchyLookup.getEstimatedRowCount() == 0)
                                        break;
                                    else {
                                        CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.first();
                                        String resCustRegion = hierarchyLookupRow.getCustRegion();
                                        String resCustArea = hierarchyLookupRow.getCustArea();
                                        String resCustLoc = hierarchyLookupRow.getCustLoc();
                                        String resCustType = hierarchyLookupRow.getCustType();
                                        String resCustGroup = hierarchyLookupRow.getCustGroup();
                                        String resCustId = hierarchyLookupRow.getCustId().toString();
                                        //System.out.println(username + ":" + newusercust + "(" + hierarchyLookup.getEstimatedRowCount() + ")" + " => " + resCustRegion + " => " + resCustArea + " => S" + resCustLoc + " => " + resCustType + " => " + resCustGroup + " => " + resCustId);
                                        if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null) && (resCustGroup != null) && (resCustId != null))
                                            isDataNull = false;
                                        if (!isDataNull) {
                                            if (!currListValidateUtParent.userCode.contains(resCustRegion)) {
                                                Row newUserRegion = utUserRegionRI.createRow();
                                                newUserRegion.setAttribute("UserName", username);
                                                newUserRegion.setAttribute("RegionCode", resCustRegion);
                                                utUserRegionRI.insertRow(newUserRegion);
                                                currListValidateUtParent.userCode.add(resCustRegion);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustArea)) {
                                                Row newUserArea = utUserAreaRI.createRow();
                                                newUserArea.setAttribute("UserName", username);
                                                newUserArea.setAttribute("AreaCode", resCustArea);
                                                utUserAreaRI.insertRow(newUserArea);
                                                currListValidateUtParent.userCode.add(resCustArea);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustLoc)) {
                                                Row newUserLoc = utUserLocRI.createRow();
                                                newUserLoc.setAttribute("UserName", username);
                                                newUserLoc.setAttribute("LocationCode", resCustLoc);
                                                utUserLocRI.insertRow(newUserLoc);
                                                currListValidateUtParent.userCode.add(resCustLoc);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustType)) {
                                                Row newUserCustType = utUserCustTypeRI.createRow();
                                                newUserCustType.setAttribute("UserName", username);
                                                newUserCustType.setAttribute("CustType", resCustType);
                                                utUserCustTypeRI.insertRow(newUserCustType);
                                                currListValidateUtParent.userCode.add(resCustType);
                                            }
                                            if (!currListValidateUtParent.userCode.contains(resCustGroup)) {
                                                Row newUserCustGroup = utUserCustGroupRI.createRow();
                                                newUserCustGroup.setAttribute("UserName", username);
                                                newUserCustGroup.setAttribute("CustGroup", resCustGroup);
                                                utUserCustGroupRI.insertRow(newUserCustGroup);
                                                currListValidateUtParent.userCode.add(resCustGroup);
                                            }
                                        }
                                    }
                                }
                                hierarchyLookup.closeRowSet();*/
                            }
                        }
                        //insert to prod_class if data specified in .csv
                        if (!csvLine.get(27).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newprodclass = csvLine.get(27);
                            //check if prod_class already exists
                            if (!currListValidateUtParent.classList.contains(newprodclass)) {
                                Row newrow = utUserProdClassRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdClass", newprodclass);
                                newrow.setAttribute("ClassDesc", "-");
                                utUserProdClassRI.insertRow(newrow);
                                currListValidateUtParent.classList.add(newprodclass);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", newprodclass);
//                                prodLookup.setNamedWhereClauseParam("pBrand", "");
//                                prodLookup.setNamedWhereClauseParam("pExt", "");
//                                prodLookup.setNamedWhereClauseParam("pPack", "");
//                                prodLookup.setNamedWhereClauseParam("pVariant", "");
//                                prodLookup.setNamedWhereClauseParam("pItem", "");
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        //insert to prod_brand if data specified in .csv
                        if (!csvLine.get(28).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newprodbrand = csvLine.get(28);
                            //check if prod_brand already exists
                            if (!currListValidateUtParent.brandList.contains(newprodbrand)) {
                                Row newrow = utUserProdBrandRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdBrand", newprodbrand);
                                newrow.setAttribute("BrandDesc", "-");
                                utUserProdBrandRI.insertRow(newrow);
                                currListValidateUtParent.brandList.add(newprodbrand);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", "");
//                                prodLookup.setNamedWhereClauseParam("pBrand", newprodbrand);
//                                prodLookup.setNamedWhereClauseParam("pExt", "");
//                                prodLookup.setNamedWhereClauseParam("pPack", "");
//                                prodLookup.setNamedWhereClauseParam("pVariant", "");
//                                prodLookup.setNamedWhereClauseParam("pItem", "");
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    String resBrand = resProdLookup.getProdBrand() == null ? "" : resProdLookup.getProdBrand().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                    if ((!currListValidateUtParent.brandList.contains(resBrand)) && (!resBrand.equals(""))) {
//                                        Row newUserProdBrand = utUserProdBrandRI.createRow();
//                                        newUserProdBrand.setAttribute("UserName", username);
//                                        newUserProdBrand.setAttribute("ProdBrand", resBrand);
//                                        newUserProdBrand.setAttribute("BrandDesc", "-");
//                                        utUserProdBrandRI.insertRow(newUserProdBrand);
//                                        currListValidateUtParent.brandList.add(resBrand);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        //insert to prod_ext if data specified in .csv
                        if (!csvLine.get(29).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newprodext = csvLine.get(29);
                            //check if prod_ext already exists
                            if (!currListValidateUtParent.extList.contains(newprodext)) {
                                Row newrow = utUserProdExtRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdExt", newprodext);
                                newrow.setAttribute("ExtDesc", "-");
                                utUserProdExtRI.insertRow(newrow);
                                currListValidateUtParent.extList.add(newprodext);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", "");
//                                prodLookup.setNamedWhereClauseParam("pBrand", "");
//                                prodLookup.setNamedWhereClauseParam("pExt", newprodext);
//                                prodLookup.setNamedWhereClauseParam("pPack", "");
//                                prodLookup.setNamedWhereClauseParam("pVariant", "");
//                                prodLookup.setNamedWhereClauseParam("pItem", "");
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    String resBrand = resProdLookup.getProdBrand() == null ? "" : resProdLookup.getProdBrand().trim();
//                                    String resExt = resProdLookup.getProdExt() == null ? "" : resProdLookup.getProdExt().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                    if ((!currListValidateUtParent.brandList.contains(resBrand)) && (!resBrand.equals(""))) {
//                                        Row newUserProdBrand = utUserProdBrandRI.createRow();
//                                        newUserProdBrand.setAttribute("UserName", username);
//                                        newUserProdBrand.setAttribute("ProdBrand", resBrand);
//                                        newUserProdBrand.setAttribute("BrandDesc", "-");
//                                        utUserProdBrandRI.insertRow(newUserProdBrand);
//                                        currListValidateUtParent.brandList.add(resBrand);
//                                    }
//                                    if ((!currListValidateUtParent.extList.contains(resExt)) && (!resExt.equals(""))) {
//                                        Row newUserProdExt = utUserProdExtRI.createRow();
//                                        newUserProdExt.setAttribute("UserName", username);
//                                        newUserProdExt.setAttribute("ProdExt", resExt);
//                                        newUserProdExt.setAttribute("ExtDesc", "-");
//                                        utUserProdExtRI.insertRow(newUserProdExt);
//                                        currListValidateUtParent.extList.add(resExt);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        //insert to prod_pack if data specified in .csv
                        if (!csvLine.get(30).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newprodpack = csvLine.get(30);
                            //check if prod_pack already exists
                            if (!currListValidateUtParent.packList.contains(newprodpack)) {
                                Row newrow = utUserProdPackRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdPack", newprodpack);
                                newrow.setAttribute("PackDesc", "-");
                                utUserProdPackRI.insertRow(newrow);
                                currListValidateUtParent.packList.add(newprodpack);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", "");
//                                prodLookup.setNamedWhereClauseParam("pBrand", "");
//                                prodLookup.setNamedWhereClauseParam("pExt", "");
//                                prodLookup.setNamedWhereClauseParam("pPack", newprodpack);
//                                prodLookup.setNamedWhereClauseParam("pVariant", "");
//                                prodLookup.setNamedWhereClauseParam("pItem", "");
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    String resBrand = resProdLookup.getProdBrand() == null ? "" : resProdLookup.getProdBrand().trim();
//                                    String resExt = resProdLookup.getProdExt() == null ? "" : resProdLookup.getProdExt().trim();
//                                    String resPack = resProdLookup.getProdPack() == null ? "" : resProdLookup.getProdPack().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                    if ((!currListValidateUtParent.brandList.contains(resBrand)) && (!resBrand.equals(""))) {
//                                        Row newUserProdBrand = utUserProdBrandRI.createRow();
//                                        newUserProdBrand.setAttribute("UserName", username);
//                                        newUserProdBrand.setAttribute("ProdBrand", resBrand);
//                                        newUserProdBrand.setAttribute("BrandDesc", "-");
//                                        utUserProdBrandRI.insertRow(newUserProdBrand);
//                                        currListValidateUtParent.brandList.add(resBrand);
//                                    }
//                                    if ((!currListValidateUtParent.extList.contains(resExt)) && (!resExt.equals(""))) {
//                                        Row newUserProdExt = utUserProdExtRI.createRow();
//                                        newUserProdExt.setAttribute("UserName", username);
//                                        newUserProdExt.setAttribute("ProdExt", resExt);
//                                        newUserProdExt.setAttribute("ExtDesc", "-");
//                                        utUserProdExtRI.insertRow(newUserProdExt);
//                                        currListValidateUtParent.extList.add(resExt);
//                                    }
//                                    if ((!currListValidateUtParent.packList.contains(resPack)) && (!resPack.equals(""))) {
//                                        Row newUserProdPack = utUserProdPackRI.createRow();
//                                        newUserProdPack.setAttribute("UserName", username);
//                                        newUserProdPack.setAttribute("ProdPack", resPack);
//                                        newUserProdPack.setAttribute("PackDesc", "-");
//                                        utUserProdPackRI.insertRow(newUserProdPack);
//                                        currListValidateUtParent.packList.add(resPack);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        //insert to prod_variant if data specified in .csv
                        if (!csvLine.get(31).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newprodvariant = csvLine.get(31);
                            //check if prod_variant already exists
                            if (!currListValidateUtParent.variantList.contains(newprodvariant)) {
                                Row newrow = utUserProdVariantRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdVariant", newprodvariant);
                                newrow.setAttribute("VariantDesc", "-");
                                utUserProdVariantRI.insertRow(newrow);
                                currListValidateUtParent.variantList.add(newprodvariant);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", "");
//                                prodLookup.setNamedWhereClauseParam("pBrand", "");
//                                prodLookup.setNamedWhereClauseParam("pExt", "");
//                                prodLookup.setNamedWhereClauseParam("pPack", "");
//                                prodLookup.setNamedWhereClauseParam("pVariant", newprodvariant);
//                                prodLookup.setNamedWhereClauseParam("pItem", "");
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    String resBrand = resProdLookup.getProdBrand() == null ? "" : resProdLookup.getProdBrand().trim();
//                                    String resExt = resProdLookup.getProdExt() == null ? "" : resProdLookup.getProdExt().trim();
//                                    String resPack = resProdLookup.getProdPack() == null ? "" : resProdLookup.getProdPack().trim();
//                                    String resVariant = resProdLookup.getProdVariant() == null ? "" : resProdLookup.getProdVariant().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                    if ((!currListValidateUtParent.brandList.contains(resBrand)) && (!resBrand.equals(""))) {
//                                        Row newUserProdBrand = utUserProdBrandRI.createRow();
//                                        newUserProdBrand.setAttribute("UserName", username);
//                                        newUserProdBrand.setAttribute("ProdBrand", resBrand);
//                                        newUserProdBrand.setAttribute("BrandDesc", "-");
//                                        utUserProdBrandRI.insertRow(newUserProdBrand);
//                                        currListValidateUtParent.brandList.add(resBrand);
//                                    }
//                                    if ((!currListValidateUtParent.extList.contains(resExt)) && (!resExt.equals(""))) {
//                                        Row newUserProdExt = utUserProdExtRI.createRow();
//                                        newUserProdExt.setAttribute("UserName", username);
//                                        newUserProdExt.setAttribute("ProdExt", resExt);
//                                        newUserProdExt.setAttribute("ExtDesc", "-");
//                                        utUserProdExtRI.insertRow(newUserProdExt);
//                                        currListValidateUtParent.extList.add(resExt);
//                                    }
//                                    if ((!currListValidateUtParent.packList.contains(resPack)) && (!resPack.equals(""))) {
//                                        Row newUserProdPack = utUserProdPackRI.createRow();
//                                        newUserProdPack.setAttribute("UserName", username);
//                                        newUserProdPack.setAttribute("ProdPack", resPack);
//                                        newUserProdPack.setAttribute("PackDesc", "-");
//                                        utUserProdPackRI.insertRow(newUserProdPack);
//                                        currListValidateUtParent.packList.add(resPack);
//                                    }
//                                    if ((!currListValidateUtParent.variantList.contains(resVariant)) && (!resVariant.equals(""))) {
//                                        Row newUserProdVariant = utUserProdVariantRI.createRow();
//                                        newUserProdVariant.setAttribute("UserName", username);
//                                        newUserProdVariant.setAttribute("ProdVariant", resVariant);
//                                        newUserProdVariant.setAttribute("VariantDesc", "-");
//                                        utUserProdVariantRI.insertRow(newUserProdVariant);
//                                        currListValidateUtParent.variantList.add(resVariant);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        //insert to prod_item if data specified in .csv
                        if (!csvLine.get(24).equals("")) {
                            if (!isLineHaveProdData) isLineHaveProdData = true;
                            String newproditem = csvLine.get(24);
                            //check if prod_item already exists
                            if (!currListValidateUtParent.itemList.contains(newproditem)) {
                                Row newrow = utUserProdItemRI.createRow();
                                newrow.setAttribute("UserName", username);
                                newrow.setAttribute("ProdItem", newproditem);
                                newrow.setAttribute("ItemDesc", "-");
                                utUserProdItemRI.insertRow(newrow);
                                currListValidateUtParent.itemList.add(newproditem);
//                                CustProdLookupImpl prodLookup = userAccessAM.getCustProdLookup1();
//                                prodLookup.setNamedWhereClauseParam("pCategory", "");
//                                prodLookup.setNamedWhereClauseParam("pClass", "");
//                                prodLookup.setNamedWhereClauseParam("pBrand", "");
//                                prodLookup.setNamedWhereClauseParam("pExt", "");
//                                prodLookup.setNamedWhereClauseParam("pPack", "");
//                                prodLookup.setNamedWhereClauseParam("pVariant", "");
//                                prodLookup.setNamedWhereClauseParam("pItem", newproditem);
//                                prodLookup.executeQuery();
//                                while (prodLookup.hasNext()) {
//                                    CustProdLookupRowImpl resProdLookup = (CustProdLookupRowImpl)prodLookup.next();
//                                    String resClass = resProdLookup.getProdClass() == null ? "" : resProdLookup.getProdClass().trim();
//                                    String resBrand = resProdLookup.getProdBrand() == null ? "" : resProdLookup.getProdBrand().trim();
//                                    String resExt = resProdLookup.getProdExt() == null ? "" : resProdLookup.getProdExt().trim();
//                                    String resPack = resProdLookup.getProdPack() == null ? "" : resProdLookup.getProdPack().trim();
//                                    String resVariant = resProdLookup.getProdVariant() == null ? "" : resProdLookup.getProdVariant().trim();
//                                    String resItem = resProdLookup.getProdItem() == null ? "" : resProdLookup.getProdItem().trim();
//                                    if ((!currListValidateUtParent.classList.contains(resClass)) && (!resClass.equals(""))) {
//                                        Row newUserProdClass = utUserProdClassRI.createRow();
//                                        newUserProdClass.setAttribute("UserName", username);
//                                        newUserProdClass.setAttribute("ProdClass", resClass);
//                                        newUserProdClass.setAttribute("ClassDesc", "-");
//                                        utUserProdClassRI.insertRow(newUserProdClass);
//                                        currListValidateUtParent.classList.add(resClass);
//                                    }
//                                    if ((!currListValidateUtParent.brandList.contains(resBrand)) && (!resBrand.equals(""))) {
//                                        Row newUserProdBrand = utUserProdBrandRI.createRow();
//                                        newUserProdBrand.setAttribute("UserName", username);
//                                        newUserProdBrand.setAttribute("ProdBrand", resBrand);
//                                        newUserProdBrand.setAttribute("BrandDesc", "-");
//                                        utUserProdBrandRI.insertRow(newUserProdBrand);
//                                        currListValidateUtParent.brandList.add(resBrand);
//                                    }
//                                    if ((!currListValidateUtParent.extList.contains(resExt)) && (!resExt.equals(""))) {
//                                        Row newUserProdExt = utUserProdExtRI.createRow();
//                                        newUserProdExt.setAttribute("UserName", username);
//                                        newUserProdExt.setAttribute("ProdExt", resExt);
//                                        newUserProdExt.setAttribute("ExtDesc", "-");
//                                        utUserProdExtRI.insertRow(newUserProdExt);
//                                        currListValidateUtParent.extList.add(resExt);
//                                    }
//                                    if ((!currListValidateUtParent.packList.contains(resPack)) && (!resPack.equals(""))) {
//                                        Row newUserProdPack = utUserProdPackRI.createRow();
//                                        newUserProdPack.setAttribute("UserName", username);
//                                        newUserProdPack.setAttribute("ProdPack", resPack);
//                                        newUserProdPack.setAttribute("PackDesc", "-");
//                                        utUserProdPackRI.insertRow(newUserProdPack);
//                                        currListValidateUtParent.packList.add(resPack);
//                                    }
//                                    if ((!currListValidateUtParent.variantList.contains(resVariant)) && (!resVariant.equals(""))) {
//                                        Row newUserProdVariant = utUserProdVariantRI.createRow();
//                                        newUserProdVariant.setAttribute("UserName", username);
//                                        newUserProdVariant.setAttribute("ProdVariant", resVariant);
//                                        newUserProdVariant.setAttribute("VariantDesc", "-");
//                                        utUserProdVariantRI.insertRow(newUserProdVariant);
//                                        currListValidateUtParent.variantList.add(resVariant);
//                                    }
//                                    if ((!currListValidateUtParent.itemList.contains(resItem)) && (!resItem.equals(""))) {
//                                        Row newUserProdItem = utUserProdItemRI.createRow();
//                                        newUserProdItem.setAttribute("UserName", username);
//                                        newUserProdItem.setAttribute("ProdItem", resItem);
//                                        newUserProdItem.setAttribute("ItemDesc", "-");
//                                        utUserProdItemRI.insertRow(newUserProdItem);
//                                        currListValidateUtParent.itemList.add(resItem);
//                                    }
//                                }
//                                prodLookup.closeRowSet();
                            }
                        }
                        if (!isLineHaveCustData) {
                            ViewObjectImpl allRegionCodeVO = userAccessAM.getUtAllRegionCode1();
                            allRegionCodeVO.setWhereClause("");
                            allRegionCodeVO.executeQuery();
                            while (allRegionCodeVO.hasNext()) {
                                Row currAllRegionCodeRow = allRegionCodeVO.next();
                                String regioncode = currAllRegionCodeRow.getAttribute("RegionCode").toString();
                                if (!currListValidateUtParent.userCode.contains(regioncode)) {
                                    Row newUtUserRegionRow = utUserRegionRI.createRow();
                                    newUtUserRegionRow.setAttribute("UserName", username);
                                    newUtUserRegionRow.setAttribute("RegionCode", regioncode);
                                    utUserRegionRI.insertRow(newUtUserRegionRow);
                                    currListValidateUtParent.userCode.add(regioncode);
                                }
                            }
                            allRegionCodeVO.closeRowSet();
                            currRowUserAccess.setCustAuthFlag("REGION");
                        }
                        if (!isLineHaveProdData) {
                            // Untuk validasi
                        }
                        OperationBinding operationBindingCommit =                 
                            bindings.getOperationBinding("Commit");             
                        operationBindingCommit.execute();
                        utUserAccessView.closeRowSet();
                    }
                    updateUploadLog("UTUPLOAD", "FINISHED");
                }
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
                //e.printStackTrace();
            }
        }
        setFile(null);
        AdfFacesContext.getCurrentInstance().addPartialTarget(getInputFileBinding());
    }
    
    private void clearDataBeforeUpload() {
        BindingContainer bindings = getBindings();
        listValidateUtParent.clear();
        CallableStatement cst = null;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        
        //delete ut_user_region
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_REGION", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_loc
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_LOC", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_area
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_AREA", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_cust_type
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_CUST_TYPE", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_cust_group
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_CUST_GROUP", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_cust
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_CUST", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_category
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_CATEGORY", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_class
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_CLASS", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_brand
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_BRAND", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_ext
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_EXT", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_pack
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_PACK", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_variant
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_VARIANT", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_produk_item
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_PRODUK_ITEM", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //delete ut_user_access_roles
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_ACCESS_ROLES", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //delete ut_user_access
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE UT_USER_ACCESS", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        //Clear all logs
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("TRUNCATE TABLE USER_UPLOAD_LOG", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        

        OperationBinding oprBindRefreshSyncLog =                 
            bindings.getOperationBinding("ExecuteSyncLog");             
        oprBindRefreshSyncLog.execute();
        
        OperationBinding oprBindRefreshValidateLog =                 
            bindings.getOperationBinding("ExecuteValidateLog");             
        oprBindRefreshValidateLog.execute();
        
        updateUploadLog("UTUPLOAD", "-");
        utValidateReset(null);
    }
    
    private char getDelimiter(String txt) {
        txt = txt.replaceAll("[A-Za-z0-9] ", "");
        int[] charcounter = new int[2];
        for (int i = 0; i < txt.length(); i++) {
            switch (txt.charAt(i)) {
            case ',': charcounter[0]++; break;
            case ';': charcounter[1]++; break;
            }
        }
        return charcounter[0] > charcounter[1] ? ',' : ';';
    }
    
    private String randomUserInitial(String txt) {
        String userInitial;
        boolean isExist = true;
        txt = txt.replaceAll("[^A-Za-z] ", "");
        do {
            userInitial = "";
            userInitial += txt.charAt((int)(Math.random() * txt.length()));
            userInitial += txt.charAt((int)(Math.random() * txt.length()));
            userInitial += txt.charAt((int)(Math.random() * txt.length()));
            userInitial = userInitial.toUpperCase();
            //check if user initial already exists
            UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView1();
            utUserAccessVO.setWhereClause("UtUserAccess.USER_INITIAL = '" + userInitial + "'");
            utUserAccessVO.executeQuery();
            AppUserAccessViewImpl appUserAccessVO = (AppUserAccessViewImpl)userAccessAM.getAppUserAccessView1();
            appUserAccessVO.setWhereClause("AppUserAccess.USER_INITIAL = '" + userInitial + "'");
            appUserAccessVO.executeQuery();
            if ((utUserAccessVO.getEstimatedRowCount() == 0) && (appUserAccessVO.getEstimatedRowCount() == 0))
                isExist = false;
            utUserAccessVO.closeRowSet();
            appUserAccessVO.closeRowSet();
        } while (isExist);
        return userInitial;
    }
    
    private int getIndexListValdateUtParent(String username) {
        int index = -1;
        for (int i = 0; i < listValidateUtParent.size(); i++) {
            UserAccess row = listValidateUtParent.get(i);
            if (username.equals(row.userName))
                return index = i;
        }
        return index;
    }
    
    public void utUploadReset(ActionEvent actionEvent) {        
        //updateUploadLog("UTUPLOAD", "-");
        clearDataBeforeUpload();
        setFile(null);
        getInputFileBinding().resetValue();
        AdfFacesContext.getCurrentInstance().addPartialTarget(getInputFileBinding());
        //ADFUtils.refreshCurrentPage();
    }
    
    public void updateUploadLog(String batchStatus, String batchName) {
        UserAccessAMImpl userAccessAM =
            (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");

        CallableStatement cst = null;
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("BEGIN FCS_USER_UPLOAD_LOG('" + batchName + "', '" + batchStatus + "'); END;", 0);
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
        userAccessAM.getUserUploadLog1().executeQuery();
    }
    
    public void updateValidateLog(String batchStatus, String batchName) {
        UserAccessAMImpl userAccessAM =
            (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");

        CallableStatement cst = null;
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("BEGIN FCS_USER_UPLOAD_VAL_LOG('" + batchName + "', '" + batchStatus + "'); END;", 0);
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
        userAccessAM.getUserUploadValidateLog1().executeQuery();
    }
    
    public void updateSyncLog(String batchStatus, String batchName) {
        UserAccessAMImpl userAccessAM =
            (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");

        CallableStatement cst = null;
        try {
            cst =
        userAccessAM.getDBTransaction().createCallableStatement("BEGIN FCS_USER_UPLOAD_SYNC_LOG('" + batchName + "', '" + batchStatus + "'); END;", 0);
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
        userAccessAM.getUserUploadSyncLog1().executeQuery();
    }

    public void btnValidate_Click() {
        utValidateReset(null);
        updateValidateLog("VALSTART", "Mulai validasi...");
        
        // Validate process param #1: check if user role exists in app_roles
        try {
            ViewObjectImpl utUserAccessRolesVO = userAccessAM.getUtUserAccessRolesView2();
            utUserAccessRolesVO.setWhereClause(null);
            utUserAccessRolesVO.executeQuery();
            ArrayList<String> roleValidateList = new ArrayList<String>();
            while (utUserAccessRolesVO.hasNext()) {
                Row utUserAccessRolesRow = utUserAccessRolesVO.next();
                String role = (String)utUserAccessRolesRow.getAttribute("Role");
                UtRolesValidationImpl appRolesVO = userAccessAM.getUtRolesValidation1();
                appRolesVO.setNamedWhereClauseParam("role", role);
                appRolesVO.executeQuery();
                if ((appRolesVO.getEstimatedRowCount() == 0) && (!roleValidateList.contains(role))) {
                    updateValidateLog("VALERROR", "Role milik user " + utUserAccessRolesRow.getAttribute("UserName") + " dengan nama " + role + " tidak ditemukan.");
                    roleValidateList.add(role);
                }
                appRolesVO.closeRowSet();
            }
            utUserAccessRolesVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        
        // Validate process param #2: check if region_code/area_code/location_code/cust_type/cust_group/cust_id exists on apps.FCS_VIEW_MASTER_FLEX_CUSTOMER
        try {
            UtCodeValidateImpl utCodeValidateVO = userAccessAM.getUtCodeValidate1();
            {
                ViewObjectImpl utUserRegionVO = userAccessAM.getUtUserRegionView2();
                utUserRegionVO.setWhereClause("");
                utUserRegionVO.executeQuery();
                while (utUserRegionVO.hasNext()) {
                    Row rowUtUserRegion = utUserRegionVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserRegion.getAttribute("RegionCode") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserRegion.getAttribute("RegionCode") + " yang dialokasikan pada user " + rowUtUserRegion.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserRegionVO.closeRowSet();
            }
            {
                ViewObjectImpl utUserAreaVO = userAccessAM.getUtUserAreaView2();
                utUserAreaVO.setWhereClause("");
                utUserAreaVO.executeQuery();
                while (utUserAreaVO.hasNext()) {
                    Row rowUtUserArea = utUserAreaVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserArea.getAttribute("AreaCode") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserArea.getAttribute("AreaCode") + " yang dialokasikan pada user " + rowUtUserArea.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserAreaVO.closeRowSet();
            }
            {
                ViewObjectImpl utUserLocVO = userAccessAM.getUtUserLocView2();
                utUserLocVO.setWhereClause("");
                utUserLocVO.executeQuery();
                while (utUserLocVO.hasNext()) {
                    Row rowUtUserLoc = utUserLocVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserLoc.getAttribute("LocationCode") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserLoc.getAttribute("LocationCode") + " yang dialokasikan pada user " + rowUtUserLoc.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserLocVO.closeRowSet();
            }
            {
                ViewObjectImpl utUserCustTypeVO = userAccessAM.getUtUserCustTypeView2();
                utUserCustTypeVO.setWhereClause("");
                utUserCustTypeVO.executeQuery();
                while (utUserCustTypeVO.hasNext()) {
                    Row rowUtUserCustType = utUserCustTypeVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserCustType.getAttribute("CustType") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserCustType.getAttribute("CustType") + " yang dialokasikan pada user " + rowUtUserCustType.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserCustTypeVO.closeRowSet();
            }
            {
                ViewObjectImpl utUserCustGroupVO = userAccessAM.getUtUserCustGroupView2();
                utUserCustGroupVO.setWhereClause("");
                utUserCustGroupVO.executeQuery();
                while (utUserCustGroupVO.hasNext()) {
                    Row rowUtUserCustGroup = utUserCustGroupVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserCustGroup.getAttribute("CustGroup") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserCustGroup.getAttribute("CustGroup") + " yang dialokasikan pada user " + rowUtUserCustGroup.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserCustGroupVO.closeRowSet();
            }
            {
                ViewObjectImpl utUserCustVO = userAccessAM.getUtUserCustView2();
                utUserCustVO.setWhereClause("");
                utUserCustVO.executeQuery();
                while (utUserCustVO.hasNext()) {
                    Row rowUtUserCust = utUserCustVO.next();
                    utCodeValidateVO.setWhereClause("FLEX_VALUE = '" + rowUtUserCust.getAttribute("CustomerId") + "'");
                    utCodeValidateVO.executeQuery();
                    if (!utCodeValidateVO.hasNext())
                        updateValidateLog("VALERROR", "Kode " + rowUtUserCust.getAttribute("CustomerId") + " yang dialokasikan pada user " + rowUtUserCust.getAttribute("UserName") + " tidak ditemukan.");
                }
                utUserCustVO.closeRowSet();
            }
            utCodeValidateVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        
        // Validate process param #3: spv validation
        try {
            UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView2();
            utUserAccessVO.executeQuery();

            while (utUserAccessVO.hasNext()) {
                Row utUserAccessRow = utUserAccessVO.next();
                String username = (String)utUserAccessRow.getAttribute("UserName");
                String directspv = (String)utUserAccessRow.getAttribute("DirectSpv");
                String companyid = (String)utUserAccessRow.getAttribute("CompanyId");
                String role = "";
                String usertype = (String)utUserAccessRow.getAttribute("UserType");
                String userdivision = (String)utUserAccessRow.getAttribute("UserDivision");
                ViewObjectImpl utUserAccessRolesVO = userAccessAM.getUtUserAccessRolesView2();
                utUserAccessRolesVO.setWhereClause("UtUserAccessRoles.USER_NAME = '" + username + "'");
                utUserAccessRolesVO.executeQuery();
                if (utUserAccessRolesVO.hasNext())
                    role = (String)utUserAccessRolesVO.next().getAttribute("Role");
                utUserAccessRolesVO.closeRowSet();
                //check if role included in skipped role list
                ValidateLookupCodeImpl validateLookupCodeVO = userAccessAM.getValidateLookupCode1();
                boolean skippedRole = false;
                validateLookupCodeVO.setWhereClause("LookupCode.TITLE = 'APP_SKIPPED_ROLE' AND LookupCode.VALUE = '" + role + "'");
                validateLookupCodeVO.executeQuery();
                if (validateLookupCodeVO.hasNext()) {
                    //System.out.println(role + " skipped!");
                    skippedRole = true;
                }
                validateLookupCodeVO.closeRowSet();
                if (skippedRole)
                    continue;
                
                //food area
                if ((usertype.equals("AREA")) && (!userdivision.equals("NONFOOD"))) {
                    UtUserRoleValidationFoodAreaImpl utUserRoleValidationFoodAreaVO = userAccessAM.getUtUserRoleValidationFoodArea1();
                    utUserRoleValidationFoodAreaVO.setNamedWhereClauseParam("usrName", username);
                    utUserRoleValidationFoodAreaVO.setNamedWhereClauseParam("usrRole", role);
                    utUserRoleValidationFoodAreaVO.setNamedWhereClauseParam("spvUsrNm", directspv);
                    utUserRoleValidationFoodAreaVO.setNamedWhereClauseParam("compId", companyid);
                    utUserRoleValidationFoodAreaVO.executeQuery();
                    if (utUserRoleValidationFoodAreaVO.hasNext()) {
                        Row utUserRoleValidationFoodAreaRow = utUserRoleValidationFoodAreaVO.next();
                        int curseq = ((Number)utUserRoleValidationFoodAreaRow.getAttribute("CurSeq")).intValue();
                        int spvseq = ((Number)utUserRoleValidationFoodAreaRow.getAttribute("SpvUsrSeq")).intValue();
                        int lastseq = ((Number)utUserRoleValidationFoodAreaRow.getAttribute("LastSeq")).intValue();
                        if ((directspv.equals("")) || (directspv.equals(null))) {
                            if (curseq != lastseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                        else {
                            if (curseq >= spvseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                    }
                    else {
                        CheckRoleExistInApprovalFlowImpl checkRoleExistsInApprovalFlowVO = userAccessAM.getCheckRoleExistInApprovalFlow1();
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("aprvlNm", aprvlNmFoodArea);
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("roleNm", role);
                        checkRoleExistsInApprovalFlowVO.executeQuery();
                        if (checkRoleExistsInApprovalFlowVO.getEstimatedRowCount() == 0) 
                            updateValidateLog("VALERROR", "Role " + role + " pada username " + username + " tidak ada dalam flow approval FOOD AREA");
                    }
                    utUserRoleValidationFoodAreaVO.closeRowSet();
                }
                
                //food ho
                if ((usertype.equals("HO")) && (!userdivision.equals("NONFOOD"))) {
                    UtUserRoleValidationFoodHoImpl utUserRoleValidationFoodHoVO = userAccessAM.getUtUserRoleValidationFoodHo1();
                    utUserRoleValidationFoodHoVO.setNamedWhereClauseParam("usrName", username);
                    utUserRoleValidationFoodHoVO.setNamedWhereClauseParam("usrRole", role);
                    utUserRoleValidationFoodHoVO.setNamedWhereClauseParam("spvUsrNm", directspv);
                    utUserRoleValidationFoodHoVO.setNamedWhereClauseParam("compId", companyid);
                    utUserRoleValidationFoodHoVO.executeQuery();
                    if (utUserRoleValidationFoodHoVO.hasNext()) {
                        Row utUserRoleValidationFoodHoRow = utUserRoleValidationFoodHoVO.next();
                        int curseq = ((Number)utUserRoleValidationFoodHoRow.getAttribute("CurSeq")).intValue();
                        int spvseq = ((Number)utUserRoleValidationFoodHoRow.getAttribute("SpvUsrSeq")).intValue();
                        int lastseq = ((Number)utUserRoleValidationFoodHoRow.getAttribute("LastSeq")).intValue();
                        if ((directspv.equals("")) || (directspv.equals(null))) {
                            if (curseq != lastseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                        else {
                            if (curseq >= spvseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                    }
                    else {
                        CheckRoleExistInApprovalFlowImpl checkRoleExistsInApprovalFlowVO = userAccessAM.getCheckRoleExistInApprovalFlow1();
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("aprvlNm", aprvlNmFoodHo);
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("roleNm", role);
                        checkRoleExistsInApprovalFlowVO.executeQuery();
                        if (checkRoleExistsInApprovalFlowVO.getEstimatedRowCount() == 0)
                            updateValidateLog("VALERROR", "Role " + role + " pada username " + username + " tidak ada dalam flow approval FOOD HO");
                    }
                    utUserRoleValidationFoodHoVO.closeRowSet();
                }
                
                //non food area
                if ((usertype.equals("AREA")) && (!userdivision.equals("FOOD"))) {
                    UtUserRoleValidationNonFoodAreaImpl utUserRoleValidationNonFoodAreaVO = userAccessAM.getUtUserRoleValidationNonFoodArea1();
                    utUserRoleValidationNonFoodAreaVO.setNamedWhereClauseParam("usrName", username);
                    utUserRoleValidationNonFoodAreaVO.setNamedWhereClauseParam("usrRole", role);
                    utUserRoleValidationNonFoodAreaVO.setNamedWhereClauseParam("spvUsrNm", directspv);
                    utUserRoleValidationNonFoodAreaVO.setNamedWhereClauseParam("compId", companyid);
                    utUserRoleValidationNonFoodAreaVO.executeQuery();
                    if (utUserRoleValidationNonFoodAreaVO.hasNext()) {
                        Row utUserRoleValidationNonFoodAreaRow = utUserRoleValidationNonFoodAreaVO.next();
                        int curseq = ((Number)utUserRoleValidationNonFoodAreaRow.getAttribute("CurSeq")).intValue();
                        int spvseq = ((Number)utUserRoleValidationNonFoodAreaRow.getAttribute("SpvUsrSeq")).intValue();
                        int lastseq = ((Number)utUserRoleValidationNonFoodAreaRow.getAttribute("LastSeq")).intValue();
                        if ((directspv.equals("")) || (directspv.equals(null))) {
                            if (curseq != lastseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                        else {
                            if (curseq >= spvseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                    }
                    else {
                        CheckRoleExistInApprovalFlowImpl checkRoleExistsInApprovalFlowVO = userAccessAM.getCheckRoleExistInApprovalFlow1();
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("aprvlNm", aprvlNmNonFoodArea);
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("roleNm", role);
                        checkRoleExistsInApprovalFlowVO.executeQuery();
                        if (checkRoleExistsInApprovalFlowVO.getEstimatedRowCount() == 0)
                            updateValidateLog("VALERROR", "Role " + role + " pada username " + username + " tidak ada dalam flow approval NONFOOD AREA");
                    }
                    utUserRoleValidationNonFoodAreaVO.closeRowSet();
                }
                
                //non food ho
                if ((usertype.equals("HO")) && (!userdivision.equals("FOOD"))) {
                    UtUserRoleValidationNonFoodHoImpl utUserRoleValidationNonFoodHoVO = userAccessAM.getUtUserRoleValidationNonFoodHo1();
                    utUserRoleValidationNonFoodHoVO.setNamedWhereClauseParam("usrName", username);
                    utUserRoleValidationNonFoodHoVO.setNamedWhereClauseParam("usrRole", role);
                    utUserRoleValidationNonFoodHoVO.setNamedWhereClauseParam("spvUsrNm", directspv);
                    utUserRoleValidationNonFoodHoVO.setNamedWhereClauseParam("compId", companyid);
                    utUserRoleValidationNonFoodHoVO.executeQuery();
                    if (utUserRoleValidationNonFoodHoVO.hasNext()) {
                        Row utUserRoleValidationNonFoodHoRow = utUserRoleValidationNonFoodHoVO.next();
                        int curseq = ((Number)utUserRoleValidationNonFoodHoRow.getAttribute("CurSeq")).intValue();
                        int spvseq = ((Number)utUserRoleValidationNonFoodHoRow.getAttribute("SpvUsrSeq")).intValue();
                        int lastseq = ((Number)utUserRoleValidationNonFoodHoRow.getAttribute("LastSeq")).intValue();
                        if ((directspv.equals("")) || (directspv.equals(null))) {
                            if (curseq != lastseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                        else {
                            if (curseq >= spvseq)
                                updateValidateLog("VALERROR", "Username " + username + " tidak memiliki atasan yang valid.");
                        }
                    }
                    else {
                        CheckRoleExistInApprovalFlowImpl checkRoleExistsInApprovalFlowVO = userAccessAM.getCheckRoleExistInApprovalFlow1();
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("aprvlNm", aprvlNmNonFoodHo);
                        checkRoleExistsInApprovalFlowVO.setNamedWhereClauseParam("roleNm", role);
                        checkRoleExistsInApprovalFlowVO.executeQuery();
                        if (checkRoleExistsInApprovalFlowVO.getEstimatedRowCount() == 0)
                            updateValidateLog("VALERROR", "Role " + role + " pada username " + username + " tidak ada dalam flow approval NONFOOD HO");
                    }
                    utUserRoleValidationNonFoodHoVO.closeRowSet();
                }
            }
            utUserAccessVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        
        // Validate process param #4: user initial validation
        try {
            UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView2();
            utUserAccessVO.setWhereClause("");
            utUserAccessVO.executeQuery();
            while (utUserAccessVO.hasNext()) {
                Row utUserAccessRow = utUserAccessVO.next();
                String username = (String)utUserAccessRow.getAttribute("UserName");
                String initial = (String)utUserAccessRow.getAttribute("UserInitial");
                UtUserInitialValidationImpl utUserInitialValidationVO = userAccessAM.getUtUserInitialValidation1();
                utUserInitialValidationVO.setNamedWhereClauseParam("usrInitial", initial);
                utUserInitialValidationVO.executeQuery();
                if (utUserInitialValidationVO.hasNext()) {
                    Row utUserInitialValidationRow = utUserInitialValidationVO.next();
                    if (!((String)utUserInitialValidationRow.getAttribute("UserName")).equals(username))
                        updateValidateLog("VALERROR", "Inisial user berikut ini: " + initial + ", sudah digunakan oleh user yang lain.");
                }
                utUserInitialValidationVO.closeRowSet();
            }
            utUserAccessVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        
        // Validate process param #5: check if user have food/nonfood product but in different division and fill the description
        try {
            UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView1();
            utUserAccessVO.setWhereClause("");
            utUserAccessVO.executeQuery();
            while (utUserAccessVO.hasNext()) {
                UtUserAccessViewRowImpl utUserAccessRow = (UtUserAccessViewRowImpl)utUserAccessVO.next();
                String username = utUserAccessRow.getUserName();
                //get current username category
                ArrayList<String> currUsernameCategoryList = new ArrayList<String>();
                {
                    RowIterator utProdCategoryRI = utUserAccessRow.getUtUserProdukCategoryView();
                    while (utProdCategoryRI.hasNext()) {
                        Row utProdCategoryRow = utProdCategoryRI.next();
                        currUsernameCategoryList.add(utProdCategoryRow.getAttribute("ProdCategory").toString());
                    }
                }
                //check item code
                {
                    RowIterator utProdItemRI = utUserAccessRow.getUtUserProdukItemView();
                    while (utProdItemRI.hasNext()) {
                        Row utProdItemRow = utProdItemRI.next();
                        String code = utProdItemRow.getAttribute("ProdItem").toString();
                        ViewObjectImpl utGetProdukItemDescVO = userAccessAM.getUtGetProdukItemDesc1();
                        utGetProdukItemDescVO.setWhereClause("ITEM = '" + code + "'");
                        utGetProdukItemDescVO.executeQuery();
                        if (!utGetProdukItemDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Item " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukItemDescVO.first();
                            String desc = rowdata.getAttribute("ItemDescription") == null ? "" : rowdata.getAttribute("ItemDescription").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Item " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdItemRow.setAttribute("UserName", username);
                                utProdItemRow.setAttribute("ProdItem", code);
                                utProdItemRow.setAttribute("ItemDesc", desc);
                                utProdItemRI.setCurrentRow(utProdItemRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukItemDesc1();
                                getCategoryVO.setWhereClause("ITEM = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Item " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukItemDescVO.closeRowSet();
                    }
                }
                //check variant code
                {
                    RowIterator utProdVariantRI = utUserAccessRow.getUtUserProdukVariantView();
                    while (utProdVariantRI.hasNext()) {
                        Row utProdVariantRow = utProdVariantRI.next();
                        String code = utProdVariantRow.getAttribute("ProdVariant").toString();
                        ViewObjectImpl utGetProdukVariantDescVO = userAccessAM.getUtGetProdukVariantDesc1();
                        utGetProdukVariantDescVO.setWhereClause("SET_VARIANT = '" + code + "'");
                        utGetProdukVariantDescVO.executeQuery();
                        if (!utGetProdukVariantDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Variant " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukVariantDescVO.first();
                            String desc = rowdata.getAttribute("SetVariantDesc") == null ? "" : rowdata.getAttribute("SetVariantDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Variant " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdVariantRow.setAttribute("UserName", username);
                                utProdVariantRow.setAttribute("ProdVariant", code);
                                utProdVariantRow.setAttribute("VariantDesc", desc);
                                utProdVariantRI.setCurrentRow(utProdVariantRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukVariantDesc1();
                                getCategoryVO.setWhereClause("SET_VARIANT = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Variant " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukVariantDescVO.closeRowSet();
                    }
                }
                //check pack code
                {
                    RowIterator utProdPackRI = utUserAccessRow.getUtUserProdukPackView();
                    while (utProdPackRI.hasNext()) {
                        Row utProdPackRow = utProdPackRI.next();
                        String code = utProdPackRow.getAttribute("ProdPack").toString();
                        ViewObjectImpl utGetProdukPackDescVO = userAccessAM.getUtGetProdukPackDesc1();
                        utGetProdukPackDescVO.setWhereClause("SET_PACKAGING = '" + code + "'");
                        utGetProdukPackDescVO.executeQuery();
                        if (!utGetProdukPackDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Pack " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukPackDescVO.first();
                            String desc = rowdata.getAttribute("SetPackagingDesc") == null ? "" : rowdata.getAttribute("SetPackagingDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Pack " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdPackRow.setAttribute("UserName", username);
                                utProdPackRow.setAttribute("ProdPack", code);
                                utProdPackRow.setAttribute("PackDesc", desc);
                                utProdPackRI.setCurrentRow(utProdPackRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukPackDesc1();
                                getCategoryVO.setWhereClause("SET_PACKAGING = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Pack " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukPackDescVO.closeRowSet();
                    }
                }
                //check ext code
                {
                    RowIterator utProdExtRI = utUserAccessRow.getUtUserProdukExtView();
                    while (utProdExtRI.hasNext()) {
                        Row utProdExtRow = utProdExtRI.next();
                        String code = utProdExtRow.getAttribute("ProdExt").toString();
                        ViewObjectImpl utGetProdukExtDescVO = userAccessAM.getUtGetProdukExtDesc1();
                        utGetProdukExtDescVO.setWhereClause("SET_EXT = '" + code + "'");
                        utGetProdukExtDescVO.executeQuery();
                        if (!utGetProdukExtDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Ext " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukExtDescVO.first();
                            String desc = rowdata.getAttribute("SetExtDesc") == null ? "" : rowdata.getAttribute("SetExtDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Ext " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdExtRow.setAttribute("UserName", username);
                                utProdExtRow.setAttribute("ProdExt", code);
                                utProdExtRow.setAttribute("ExtDesc", desc);
                                utProdExtRI.setCurrentRow(utProdExtRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukExtDesc1();
                                getCategoryVO.setWhereClause("SET_EXT = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Ext " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukExtDescVO.closeRowSet();
                    }
                }
                //check brand code
                {
                    RowIterator utProdBrandRI = utUserAccessRow.getUtUserProdukBrandView();
                    while (utProdBrandRI.hasNext()) {
                        Row utProdBrandRow = utProdBrandRI.next();
                        String code = utProdBrandRow.getAttribute("ProdBrand").toString();
                        ViewObjectImpl utGetProdukBrandDescVO = userAccessAM.getUtGetProdukBrandDesc1();
                        utGetProdukBrandDescVO.setWhereClause("SET_BRAND = '" + code + "'");
                        utGetProdukBrandDescVO.executeQuery();
                        if (!utGetProdukBrandDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Brand " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukBrandDescVO.first();
                            String desc = rowdata.getAttribute("SetBrandDesc") == null ? "" : rowdata.getAttribute("SetBrandDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Brand " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdBrandRow.setAttribute("UserName", username);
                                utProdBrandRow.setAttribute("ProdBrand", code);
                                utProdBrandRow.setAttribute("BrandDesc", desc);
                                utProdBrandRI.setCurrentRow(utProdBrandRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukBrandDesc1();
                                getCategoryVO.setWhereClause("SET_BRAND = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Brand " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukBrandDescVO.closeRowSet();
                    }
                }
                //check class code
                {
                    RowIterator utProdClassRI = utUserAccessRow.getUtUserProdukClassView();
                    while (utProdClassRI.hasNext()) {
                        Row utProdClassRow = utProdClassRI.next();
                        String code = utProdClassRow.getAttribute("ProdClass").toString();
                        ViewObjectImpl utGetProdukClassDescVO = userAccessAM.getUtGetProdukClassDesc1();
                        utGetProdukClassDescVO.setWhereClause("SET_CLASS = '" + code + "'");
                        utGetProdukClassDescVO.executeQuery();
                        if (!utGetProdukClassDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Class " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukClassDescVO.first();
                            String desc = rowdata.getAttribute("SetClassDesc") == null ? "" : rowdata.getAttribute("SetClassDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Class " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdClassRow.setAttribute("UserName", username);
                                utProdClassRow.setAttribute("ProdClass", code);
                                utProdClassRow.setAttribute("ClassDesc", desc);
                                utProdClassRI.setCurrentRow(utProdClassRow);
                            }
                            boolean isCorrectDivision = false;
                            for (String currUserCategory : currUsernameCategoryList) {
                                ViewObjectImpl getCategoryVO = userAccessAM.getUtGetProdukClassDesc1();
                                getCategoryVO.setWhereClause("SET_CLASS = '" + code + "'" + " AND SET_CATEGORY = '" + currUserCategory + "'");
                                getCategoryVO.executeQuery();
                                if (getCategoryVO.hasNext())
                                    isCorrectDivision = true;
                                getCategoryVO.closeRowSet();
                            }
                            if (!isCorrectDivision)
                                updateValidateLog("VALERROR", "Kategori dari kode produk Class " + code + " tidak sesuai dengan divisi user " + username + ".");
                        }
                        utGetProdukClassDescVO.closeRowSet();
                    }
                }
                //insert category description
                {
                    RowIterator utProdCategoryRI = utUserAccessRow.getUtUserProdukCategoryView();
                    while (utProdCategoryRI.hasNext()) {
                        Row utProdCategoryRow = utProdCategoryRI.next();
                        String code = utProdCategoryRow.getAttribute("ProdCategory").toString();
                        ViewObjectImpl utGetProdukCategoryDescVO = userAccessAM.getUtGetProdukCategoryDesc1();
                        utGetProdukCategoryDescVO.setWhereClause("SET_CATEGORY = '" + code + "'");
                        utGetProdukCategoryDescVO.executeQuery();
                        if (!utGetProdukCategoryDescVO.hasNext())
                            updateValidateLog("VALERROR", "Kode produk Category " + code + " yang dialokasikan pada user " + username + " tidak ditemukan.");
                        else {
                            Row rowdata = utGetProdukCategoryDescVO.first();
                            String desc = rowdata.getAttribute("SetCategoryDesc") == null ? "" : rowdata.getAttribute("SetCategoryDesc").toString().trim();
                            if (desc.equals(""))
                                updateValidateLog("VALERROR", "Kode produk Category " + code + " yang dialokasikan pada user " + username + " tidak memiliki deskripsi.");
                            else {
                                utProdCategoryRow.setAttribute("UserName", username);
                                utProdCategoryRow.setAttribute("ProdCategory", code);
                                utProdCategoryRow.setAttribute("CategoryDesc", desc);
                                utProdCategoryRI.setCurrentRow(utProdCategoryRow);
                            }
                        }
                        utGetProdukCategoryDescVO.closeRowSet();
                    }
                }
                getBindings().getOperationBinding("Commit").execute();
            }
            utUserAccessVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        // Validate process param #6: check if user have cust_type/cust_group/cust_id but didn't have group/area/loc
        try {
            UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView1();
            utUserAccessVO.setWhereClause("");
            utUserAccessVO.executeQuery();
            while (utUserAccessVO.hasNext()) {
                UtUserAccessViewRowImpl currUtUserAccessRow = (UtUserAccessViewRowImpl)utUserAccessVO.next();
                //check cust_type
                RowIterator currUserCustTypeRI = currUtUserAccessRow.getUtUserCustTypeView();
                while (currUserCustTypeRI.hasNext()) {
                    Row currUserCustTypeRow = currUserCustTypeRI.next();
                    CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                    boolean hasregcode = false;
                    boolean isDataNull = true;
                    while (isDataNull) {
                        hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                        hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                        hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                        hierarchyLookup.setNamedWhereClauseParam("custType", currUserCustTypeRow.getAttribute("CustType").toString());
                        hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                        hierarchyLookup.setNamedWhereClauseParam("custId", "");
                        hierarchyLookup.executeQuery();
                        if (hierarchyLookup.getEstimatedRowCount() == 0)
                            break;
                        else {
                            while ((hierarchyLookup.hasNext()) && (!hasregcode)) {
                                CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.next();
                                String resCustRegion = hierarchyLookupRow.getCustRegion();
                                String resCustArea = hierarchyLookupRow.getCustArea();
                                String resCustLoc = hierarchyLookupRow.getCustLoc();
                                String resCustType = hierarchyLookupRow.getCustType();
                                if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null))
                                    isDataNull = false;
                                if (isDataNull)
                                    break;
                                //check region
                                RowIterator currUserRegionRI = currUtUserAccessRow.getUtUserRegionView();
                                while ((currUserRegionRI.hasNext()) && (!hasregcode)) {
                                    Row currUserRegionRow = currUserRegionRI.next();
                                    if (currUserRegionRow.getAttribute("RegionCode").equals(resCustRegion))
                                        hasregcode = true;
                                }
                                //check area
                                RowIterator currUserAreaRI = currUtUserAccessRow.getUtUserAreaView();
                                while ((currUserAreaRI.hasNext()) && (!hasregcode)) {
                                    Row currUserAreaRow = currUserAreaRI.next();
                                    if (currUserAreaRow.getAttribute("AreaCode").equals(resCustArea))
                                        hasregcode = true;
                                }
                                //check loc
                                RowIterator currUserLocRI = currUtUserAccessRow.getUtUserLocView();
                                while ((currUserLocRI.hasNext()) && (!hasregcode)) {
                                    Row currUserLocRow = currUserLocRI.next();
                                    if (currUserLocRow.getAttribute("LocationCode").equals(resCustLoc))
                                        hasregcode = true;
                                }
                            }
                        }
                    }
                    if (!hasregcode)
                        updateValidateLog("VALERROR", "Kode Customer Type " + currUserCustTypeRow.getAttribute("CustType").toString() + " yang dialokasikan pada user " + currUtUserAccessRow.getUserName() + " tidak memiliki kode region yang sesuai.");
                    hierarchyLookup.closeRowSet();
                }
                //check cust_group
                RowIterator currUserCustGroupRI = currUtUserAccessRow.getUtUserCustGroupView();
                while (currUserCustGroupRI.hasNext()) {
                    Row currUserCustGroupRow = currUserCustGroupRI.next();
                    CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                    boolean hasregcode = false;
                    boolean isDataNull = true;
                    while (isDataNull) {
                        hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                        hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                        hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                        hierarchyLookup.setNamedWhereClauseParam("custType", "");
                        hierarchyLookup.setNamedWhereClauseParam("custGroup", currUserCustGroupRow.getAttribute("CustGroup").toString());
                        hierarchyLookup.setNamedWhereClauseParam("custId", "");
                        hierarchyLookup.executeQuery();
                        if (hierarchyLookup.getEstimatedRowCount() == 0)
                            break;
                        else {
                            while ((hierarchyLookup.hasNext()) && (!hasregcode)) {
                                CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.next();
                                String resCustRegion = hierarchyLookupRow.getCustRegion();
                                String resCustArea = hierarchyLookupRow.getCustArea();
                                String resCustLoc = hierarchyLookupRow.getCustLoc();
                                String resCustType = hierarchyLookupRow.getCustType();
                                String resCustGroup = hierarchyLookupRow.getCustGroup();
                                if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null) && (resCustGroup != null))
                                    isDataNull = false;
                                if (isDataNull)
                                    break;
                                //check region
                                RowIterator currUserRegionRI = currUtUserAccessRow.getUtUserRegionView();
                                while ((currUserRegionRI.hasNext()) && (!hasregcode)) {
                                    Row currUserRegionRow = currUserRegionRI.next();
                                    if (currUserRegionRow.getAttribute("RegionCode").equals(resCustRegion))
                                        hasregcode = true;
                                }
                                //check area
                                RowIterator currUserAreaRI = currUtUserAccessRow.getUtUserAreaView();
                                while ((currUserAreaRI.hasNext()) && (!hasregcode)) {
                                    Row currUserAreaRow = currUserAreaRI.next();
                                    if (currUserAreaRow.getAttribute("AreaCode").equals(resCustArea))
                                        hasregcode = true;
                                }
                                //check loc
                                RowIterator currUserLocRI = currUtUserAccessRow.getUtUserLocView();
                                while ((currUserLocRI.hasNext()) && (!hasregcode)) {
                                    Row currUserLocRow = currUserLocRI.next();
                                    if (currUserLocRow.getAttribute("LocationCode").equals(resCustLoc))
                                        hasregcode = true;
                                }
                            }
                        }
                    }
                    if (!hasregcode)
                        updateValidateLog("VALERROR", "Kode Customer Group " + currUserCustGroupRow.getAttribute("CustGroup").toString() + " yang dialokasikan pada user " + currUtUserAccessRow.getUserName() + " tidak memiliki kode region yang sesuai.");
                    hierarchyLookup.closeRowSet();
                }
                //check cust_id
                RowIterator currUserCustRI = currUtUserAccessRow.getUtUserCustView();
                while (currUserCustRI.hasNext()) {
                    Row currUserCustRow = currUserCustRI.next();
                    CustHierarchyLookupImpl hierarchyLookup = userAccessAM.getCustHierarchyLookup1();
                    boolean hasregcode = false;
                    boolean isDataNull = true;
                    while (isDataNull) {
                        hierarchyLookup.setNamedWhereClauseParam("custReg", "");
                        hierarchyLookup.setNamedWhereClauseParam("custArea", "");
                        hierarchyLookup.setNamedWhereClauseParam("custLoc", "");
                        hierarchyLookup.setNamedWhereClauseParam("custType", "");
                        hierarchyLookup.setNamedWhereClauseParam("custGroup", "");
                        hierarchyLookup.setNamedWhereClauseParam("custId", currUserCustRow.getAttribute("CustomerId").toString());
                        hierarchyLookup.executeQuery();
                        if (hierarchyLookup.getEstimatedRowCount() == 0)
                            break;
                        else {
                            while ((hierarchyLookup.hasNext()) && (!hasregcode)) {
                                CustHierarchyLookupRowImpl hierarchyLookupRow = (CustHierarchyLookupRowImpl)hierarchyLookup.next();
                                String resCustRegion = hierarchyLookupRow.getCustRegion();
                                String resCustArea = hierarchyLookupRow.getCustArea();
                                String resCustLoc = hierarchyLookupRow.getCustLoc();
                                String resCustType = hierarchyLookupRow.getCustType();
                                String resCustGroup = hierarchyLookupRow.getCustGroup();
                                String resCustId = hierarchyLookupRow.getCustId().toString();
                                if ((resCustRegion != null) && (resCustArea != null) && (resCustLoc != null) && (resCustType != null) && (resCustGroup != null) && (resCustId != null))
                                    isDataNull = false;
                                if (isDataNull)
                                    break;
                                //check region
                                RowIterator currUserRegionRI = currUtUserAccessRow.getUtUserRegionView();
                                while ((currUserRegionRI.hasNext()) && (!hasregcode)) {
                                    Row currUserRegionRow = currUserRegionRI.next();
                                    if (currUserRegionRow.getAttribute("RegionCode").equals(resCustRegion))
                                        hasregcode = true;
                                }
                                //check area
                                RowIterator currUserAreaRI = currUtUserAccessRow.getUtUserAreaView();
                                while ((currUserAreaRI.hasNext()) && (!hasregcode)) {
                                    Row currUserAreaRow = currUserAreaRI.next();
                                    if (currUserAreaRow.getAttribute("AreaCode").equals(resCustArea))
                                        hasregcode = true;
                                }
                                //check loc
                                RowIterator currUserLocRI = currUtUserAccessRow.getUtUserLocView();
                                while ((currUserLocRI.hasNext()) && (!hasregcode)) {
                                    Row currUserLocRow = currUserLocRI.next();
                                    if (currUserLocRow.getAttribute("LocationCode").equals(resCustLoc))
                                        hasregcode = true;
                                }
                            }
                        }
                    }
                    if (!hasregcode)
                        updateValidateLog("VALERROR", "Kode Customer ID " + currUserCustRow.getAttribute("CustomerId").toString() + " yang dialokasikan pada user " + currUtUserAccessRow.getUserName() + " tidak memiliki kode region yang sesuai.");
                    hierarchyLookup.closeRowSet();
                }
            }
            utUserAccessVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        updateValidateLog("VALEND", "Finished.");
    }

    public void btnSync_Click() {
        BindingContainer bindings = getBindings();
        try {
            //insert ut_user_access data not in app_user_access to arraylist
            ArrayList<String> li_username = new ArrayList<String>();
            ArrayList<String> li_password = new ArrayList<String>();
            ArrayList<Date> li_periodfrom = new ArrayList<Date>();
            ArrayList<String> li_initial = new ArrayList<String>();
            UtUserAccessViewImpl newUtUserAccessVO = userAccessAM.getUtUserAccessView2();
            newUtUserAccessVO.setWhereClause("UtUserAccess.USER_NAME NOT IN (SELECT AppUserAccess.USER_NAME FROM APP_USER_ACCESS AppUserAccess)");
            newUtUserAccessVO.executeQuery();
            while (newUtUserAccessVO.hasNext()) {
                UtUserAccessViewRowImpl newUtUserAccessRow = (UtUserAccessViewRowImpl)newUtUserAccessVO.next();
                li_username.add(newUtUserAccessRow.getUserName());
                li_password.add(newUtUserAccessRow.getPassword());
                li_periodfrom.add(newUtUserAccessRow.getActivePeriodFrom());
                li_initial.add(newUtUserAccessRow.getUserInitial());
            }
            newUtUserAccessVO.closeRowSet();
            //expired user access
            PreparedStatement stExpiredUser = userAccessAM.getDBTransaction().createPreparedStatement("SELECT USER_NAME FROM APP_USER_ACCESS WHERE APP_USER_ACCESS.USER_NAME NOT IN (SELECT UT_USER_ACCESS.USER_NAME FROM UT_USER_ACCESS) AND PASSWORD <> 'UnUsedResign'", 0);
            try {
                ResultSet rsExpiredUser = stExpiredUser.executeQuery();
                try {
                    while (rsExpiredUser.next()) {
                        PreparedStatement stisadminapp = userAccessAM.getDBTransaction().createPreparedStatement("SELECT ROLE FROM APP_USER_ACCESS_ROLES WHERE USER_NAME = '" + rsExpiredUser.getString("USER_NAME") + "'", 0);
                        try {
                            ResultSet rsisadminapp = stisadminapp.executeQuery();
                            try {
                                if (rsisadminapp.next()) {
                                    if (!rsisadminapp.getString("ROLE").equals("adminapp")) {
                                        PreparedStatement stUpdateExpiredUser = userAccessAM.getDBTransaction().createPreparedStatement("UPDATE APP_USER_ACCESS SET PASSWORD = 'UnUsedResign', ACTIVE_PERIOD_TO = TRUNC(SYSDATE) WHERE USER_NAME = '" + rsExpiredUser.getString("USER_NAME") + "'", 0);
                                        try {
                                            stUpdateExpiredUser.executeUpdate();
                                        } finally {
                                            stUpdateExpiredUser.close();
                                        }
                                    }
                                }
                                else {
                                    PreparedStatement stUpdateExpiredUser = userAccessAM.getDBTransaction().createPreparedStatement("UPDATE APP_USER_ACCESS SET PASSWORD = 'UnUsedResign', ACTIVE_PERIOD_TO = TRUNC(SYSDATE) WHERE USER_NAME = '" + rsExpiredUser.getString("USER_NAME") + "'", 0);
                                    try {
                                        stUpdateExpiredUser.executeUpdate();
                                    } finally {
                                        stUpdateExpiredUser.close();
                                    }
                                }
                            } finally {
                                rsisadminapp.close();
                            }
                        } finally {
                            stisadminapp.close();
                        }
                    }
                    userAccessAM.getDBTransaction().commit();
                } finally {
                    rsExpiredUser.close();
                }
            } finally {
                stExpiredUser.close();
            }
            //updated user access
            UpdateAppUserAccessImpl oldAppUserAccessVO = userAccessAM.getUpdateAppUserAccess1();
            oldAppUserAccessVO.setWhereClause("AppUserAccess.USER_NAME IN (SELECT UtUserAccess.USER_NAME FROM UT_USER_ACCESS UtUserAccess)");
            oldAppUserAccessVO.executeQuery();
            while (oldAppUserAccessVO.hasNext()) {
                UpdateAppUserAccessRowImpl oldAppUserAccessRow = (UpdateAppUserAccessRowImpl)oldAppUserAccessVO.next();
                String username = oldAppUserAccessRow.getUserName();
                li_username.add(username);
                String password = oldAppUserAccessRow.getPassword().equals("UnUsedResign") ? oldAppUserAccessRow.getUserName() : oldAppUserAccessRow.getPassword();
                li_password.add(password);
                Date periodfrom = oldAppUserAccessRow.getActivePeriodFrom();
                li_periodfrom.add(periodfrom);
                String initial = oldAppUserAccessRow.getUserInitial();
                li_initial.add(initial);
                //delete all old data
                ViewObjectImpl oldAppUserAccessRolesVO = userAccessAM.getUpdateAppUserAccessRoles2();
                oldAppUserAccessRolesVO.setWhereClause("AppUserAccessRoles.USER_NAME = '" + username + "'");
                oldAppUserAccessRolesVO.executeQuery();
                while (oldAppUserAccessRolesVO.hasNext())
                    oldAppUserAccessRolesVO.next().remove();
                OperationBinding operBindCommitOldAppUsrRole =                 
                     bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrRole.execute();
                oldAppUserAccessRolesVO.closeRowSet();
                ViewObjectImpl oldAppUserRegionVO = userAccessAM.getUpdateAppUserRegion2();
                oldAppUserRegionVO.setWhereClause("AppUserRegion.USER_NAME = '" + username + "'");
                oldAppUserRegionVO.executeQuery();
                while (oldAppUserRegionVO.hasNext())
                    oldAppUserRegionVO.next().remove();
                OperationBinding operBindCommitOldAppUsrRegion =                 
                    bindings.getOperationBinding("Commit");            
                operBindCommitOldAppUsrRegion.execute();
                oldAppUserRegionVO.closeRowSet();
                ViewObjectImpl oldAppUserLocVO = userAccessAM.getUpdateAppUserLoc2();
                oldAppUserLocVO.setWhereClause("AppUserLoc.USER_NAME = '" + username + "'");
                oldAppUserLocVO.executeQuery();
                while (oldAppUserLocVO.hasNext())
                    oldAppUserLocVO.next().remove();
                OperationBinding operBindCommitOldAppUsrLoc =                 
                    bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrLoc.execute();
                oldAppUserLocVO.closeRowSet();
                ViewObjectImpl oldAppUserAreaVO = userAccessAM.getUpdateAppUserArea2();
                oldAppUserAreaVO.setWhereClause("AppUserArea.USER_NAME = '" + username + "'");
                oldAppUserAreaVO.executeQuery();
                while (oldAppUserAreaVO.hasNext())
                    oldAppUserAreaVO.next().remove();
                OperationBinding operBindCommitOldAppUsrArea =                 
                    bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrArea.execute();
                oldAppUserAreaVO.closeRowSet();
                ViewObjectImpl oldAppUserCustTypeVO = userAccessAM.getUpdateAppUserCustType2();
                oldAppUserCustTypeVO.setWhereClause("AppUserCustType.USER_NAME = '" + username + "'");
                oldAppUserCustTypeVO.executeQuery();
                while (oldAppUserCustTypeVO.hasNext())
                    oldAppUserCustTypeVO.next().remove();
                OperationBinding operBindCommitOldAppUsrCustType =                 
                    bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrCustType.execute();
                oldAppUserCustTypeVO.closeRowSet();
                ViewObjectImpl oldAppUserCustGroupVO = userAccessAM.getUpdateAppUserCustGroup2();
                oldAppUserCustGroupVO.setWhereClause("AppUserCustGroup.USER_NAME = '" + username + "'");
                oldAppUserCustGroupVO.executeQuery();
                while (oldAppUserCustGroupVO.hasNext())
                    oldAppUserCustGroupVO.next().remove();
                OperationBinding operBindCommitOldAppUsrCustGroup =                 
                    bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrCustGroup.execute();
                oldAppUserCustGroupVO.closeRowSet();
                ViewObjectImpl oldAppUserCustVO = userAccessAM.getUpdateAppUserCust2();
                oldAppUserCustVO.setWhereClause("AppUserCust.USER_NAME = '" + username + "'");
                oldAppUserCustVO.executeQuery();
                while (oldAppUserCustVO.hasNext())
                    oldAppUserCustVO.next().remove();
                OperationBinding operBindCommitOldAppUsrCust =                 
                     bindings.getOperationBinding("Commit");             
                operBindCommitOldAppUsrCust.execute();
                oldAppUserCustVO.closeRowSet();
                //
                ViewObjectImpl oldAppUserProdCategoryVO = userAccessAM.getUpdateAppUserProdukCategory2();
                oldAppUserProdCategoryVO.setWhereClause("AppUserProdukCategory.USER_NAME = '" + username + "'");
                oldAppUserProdCategoryVO.executeQuery();
                while (oldAppUserProdCategoryVO.hasNext())
                    oldAppUserProdCategoryVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdCategoryVO.closeRowSet();
                ViewObjectImpl oldAppUserProdClassVO = userAccessAM.getUpdateAppUserProdukClass2();
                oldAppUserProdClassVO.setWhereClause("AppUserProdukClass.USER_NAME = '" + username + "'");
                oldAppUserProdClassVO.executeQuery();
                while (oldAppUserProdClassVO.hasNext())
                    oldAppUserProdClassVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdClassVO.closeRowSet();
                ViewObjectImpl oldAppUserProdBrandVO = userAccessAM.getUpdateAppUserProdukBrand2();
                oldAppUserProdBrandVO.setWhereClause("AppUserProdukBrand.USER_NAME = '" + username + "'");
                oldAppUserProdBrandVO.executeQuery();
                while (oldAppUserProdBrandVO.hasNext())
                    oldAppUserProdBrandVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdBrandVO.closeRowSet();
                ViewObjectImpl oldAppUserProdExtVO = userAccessAM.getUpdateAppUserProdukExt2();
                oldAppUserProdExtVO.setWhereClause("AppUserProdukExt.USER_NAME = '" + username + "'");
                oldAppUserProdExtVO.executeQuery();
                while (oldAppUserProdExtVO.hasNext())
                    oldAppUserProdExtVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdExtVO.closeRowSet();
                ViewObjectImpl oldAppUserProdPackVO = userAccessAM. getUpdateAppUserProdukPack2();
                oldAppUserProdPackVO.setWhereClause("AppUserProdukPack.USER_NAME = '" + username + "'");
                oldAppUserProdPackVO.executeQuery();
                while (oldAppUserProdPackVO.hasNext())
                    oldAppUserProdPackVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdPackVO.closeRowSet();
                ViewObjectImpl oldAppUserProdVariantVO = userAccessAM.getUpdateAppUserProdukVariant2();
                oldAppUserProdVariantVO.setWhereClause("AppUserProdukVariant.USER_NAME = '" + username + "'");
                oldAppUserProdVariantVO.executeQuery();
                while (oldAppUserProdVariantVO.hasNext())
                    oldAppUserProdVariantVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdVariantVO.closeRowSet();
                ViewObjectImpl oldAppUserProdItemVO = userAccessAM.getUpdateAppUserProdukItem2();
                oldAppUserProdItemVO.setWhereClause("AppUserProdukItem.USER_NAME = '" + username + "'");
                oldAppUserProdItemVO.executeQuery();
                while (oldAppUserProdItemVO.hasNext())
                    oldAppUserProdItemVO.next().remove();
                bindings.getOperationBinding("Commit").execute();
                oldAppUserProdItemVO.closeRowSet();
                
                oldAppUserAccessRow.remove();
                                                 
                OperationBinding operationBindingCommit =                 
                     bindings.getOperationBinding("Commit");             
                operationBindingCommit.execute();
            }
            OperationBinding operationBindingCommit =                 
                bindings.getOperationBinding("Commit");             
            operationBindingCommit.execute();
            oldAppUserAccessVO.closeRowSet();
            try {
                for (int i = 0; i < li_username.size(); i++) {
                    UpdateAppUserAccessImpl newAppUserAccessVO = userAccessAM.getUpdateAppUserAccess1();
                    newAppUserAccessVO.executeQuery();
                    UpdateAppUserAccessRowImpl newAppUserAccessRow = (UpdateAppUserAccessRowImpl)newAppUserAccessVO.createRow();
                    //insert to app_user_access
                    UtUserAccessViewImpl utUserAccessVO = userAccessAM.getUtUserAccessView2();
                    utUserAccessVO.setWhereClause("UtUserAccess.USER_NAME = '" + li_username.get(i) + "'");
                    utUserAccessVO.executeQuery();
                    UtUserAccessViewRowImpl utUserAccessRow = (UtUserAccessViewRowImpl)utUserAccessVO.first();
                    //System.out.println(li_username.get(i) + " | " + li_password.get(i) + " | " + utUserAccessRow.getFullName() + " | " + utUserAccessRow.getDescr() + " | " + utUserAccessRow.getTitle() + " | " + utUserAccessRow.getContactNo() + " | " + utUserAccessRow.getCompanyId() + " | " + li_periodfrom.get(i) + " | " + li_periodto.get(i) + " | " + li_initial.get(i) + " | " + utUserAccessRow.getUserType() + " | " + utUserAccessRow.getUserDivision() + " | " + utUserAccessRow.getDirectSpv());
                    newAppUserAccessRow.setUserName(li_username.get(i));
                    newAppUserAccessRow.setPassword(li_password.get(i));
                    newAppUserAccessRow.setFullName(utUserAccessRow.getFullName());
                    newAppUserAccessRow.setDescr(utUserAccessRow.getDescr());
                    //newAppUserAccessRow.setDescr(!utUserAccessRow.getDescr().isEmpty() ? utUserAccessRow.getDescr() : "-");
                    newAppUserAccessRow.setTitle(utUserAccessRow.getTitle());
                    newAppUserAccessRow.setContactNo(utUserAccessRow.getContactNo());
                    //newAppUserAccessRow.setContactNo(!utUserAccessRow.getContactNo().isEmpty() ? utUserAccessRow.getContactNo() : "-");
                    newAppUserAccessRow.setCompanyId(utUserAccessRow.getCompanyId());
                    newAppUserAccessRow.setActivePeriodFrom(li_periodfrom.get(i));
                    newAppUserAccessRow.setActivePeriodTo(utUserAccessRow.getActivePeriodTo());
                    newAppUserAccessRow.setUserInitial(li_initial.get(i));
                    newAppUserAccessRow.setUserType(utUserAccessRow.getUserType());
                    newAppUserAccessRow.setUserDivision(utUserAccessRow.getUserDivision());
                    newAppUserAccessRow.setDirectSpv(utUserAccessRow.getDirectSpv());
                    newAppUserAccessRow.setCustAuthFlag(utUserAccessRow.getCustAuthFlag());
                    newAppUserAccessVO.insertRow(newAppUserAccessRow);
                    OperationBinding operBindCommitInsNewUsrAcc =                 
                        bindings.getOperationBinding("Commit");             
                    operBindCommitInsNewUsrAcc.execute();
                    utUserAccessVO.closeRowSet();
                    newAppUserAccessVO.setWhereClause("AppUserAccess.USER_NAME = '" + li_username.get(i) + "'");
                    newAppUserAccessVO.executeQuery();
                    UpdateAppUserAccessRowImpl currAppUserAccessRow = (UpdateAppUserAccessRowImpl)newAppUserAccessVO.first();
                    //insert to app_user_access_roles
                    ViewObjectImpl utUserAccessRolesVO = userAccessAM.getUtUserAccessRolesView2();
                    utUserAccessRolesVO.setWhereClause("UtUserAccessRoles.USER_NAME = '" + li_username.get(i) + "'");
                    utUserAccessRolesVO.executeQuery();
                    Row utUserAccessRolesRow = utUserAccessRolesVO.first();
                    //System.out.println(li_username.get(i) + " " + utUserAccessRolesRow.getAttribute("Role"));
                    RowIterator newAppUserAccessRolesRI = currAppUserAccessRow.getUpdateAppUserAccessRoles();
                    Row newAppUserAccessRolesRow = newAppUserAccessRolesRI.createRow();
                    newAppUserAccessRolesRow.setAttribute("UserName", li_username.get(i));
                    newAppUserAccessRolesRow.setAttribute("Role", utUserAccessRolesRow.getAttribute("Role"));
                    newAppUserAccessRolesRI.insertRow(newAppUserAccessRolesRow);
                    utUserAccessRolesVO.closeRowSet();
                    //insert to app_user_region
                    ViewObjectImpl utUserRegionVO = userAccessAM.getUtUserRegionView2();
                    utUserRegionVO.setWhereClause("UtUserRegion.USER_NAME = '" + li_username.get(i) + "'");
                    utUserRegionVO.executeQuery();
                    RowIterator newAppUserRegionRI = currAppUserAccessRow.getUpdateAppUserRegion();
                    while(utUserRegionVO.hasNext()) {
                        Row utUserRegionRow = utUserRegionVO.next();
                        //System.out.println("(" + utUserRegionVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserRegionRow.getAttribute("RegionCode"));
                        Row newAppUserRegionRow = newAppUserRegionRI.createRow();
                        newAppUserRegionRow.setAttribute("UserName", li_username.get(i));
                        newAppUserRegionRow.setAttribute("RegionCode", utUserRegionRow.getAttribute("RegionCode"));
                        newAppUserRegionRI.insertRow(newAppUserRegionRow);
                    }
                    utUserRegionVO.closeRowSet();
                    //insert to app_user_loc
                    ViewObjectImpl utUserLocVO = userAccessAM.getUtUserLocView2();
                    utUserLocVO.setWhereClause("UtUserLoc.USER_NAME = '" + li_username.get(i) + "'");
                    utUserLocVO.executeQuery();
                    RowIterator newAppUserLocRI = currAppUserAccessRow.getUpdateAppUserLoc();
                    while (utUserLocVO.hasNext()) {
                        Row utUserLocRow = utUserLocVO.next();
                        //System.out.println("(" + utUserLocVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserLocRow.getAttribute("LocationCode"));
                        Row newAppUserLocRow = newAppUserLocRI.createRow();
                        newAppUserLocRow.setAttribute("UserName", li_username.get(i));
                        newAppUserLocRow.setAttribute("LocationCode", utUserLocRow.getAttribute("LocationCode"));
                        newAppUserLocRI.insertRow(newAppUserLocRow);
                    }
                    utUserLocVO.closeRowSet();
                    //insert to app_user_area
                    ViewObjectImpl utUserAreaVO = userAccessAM.getUtUserAreaView2();
                    utUserAreaVO.setWhereClause("UtUserArea.USER_NAME = '" + li_username.get(i) + "'");
                    utUserAreaVO.executeQuery();
                    RowIterator newAppUserAreaRI = currAppUserAccessRow.getUpdateAppUserArea();
                    while (utUserAreaVO.hasNext()) {
                        Row utUserAreaRow = utUserAreaVO.next();
                        //System.out.println("(" + utUserAreaVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserAreaRow.getAttribute("AreaCode"));
                        Row newAppUserAreaRow = newAppUserAreaRI.createRow();
                        newAppUserAreaRow.setAttribute("UserName", li_username.get(i));
                        newAppUserAreaRow.setAttribute("AreaCode", utUserAreaRow.getAttribute("AreaCode"));
                        newAppUserAreaRI.insertRow(newAppUserAreaRow);
                    }
                    utUserAreaVO.closeRowSet();
                    //insert to app_user_cust_type
                    ViewObjectImpl utUserCustTypeVO = userAccessAM.getUtUserCustTypeView2();
                    utUserCustTypeVO.setWhereClause("UtUserCustType.USER_NAME = '" + li_username.get(i) + "'");
                    utUserCustTypeVO.executeQuery();
                    RowIterator newAppUserCustTypeRI = currAppUserAccessRow.getUpdateAppUserCustType();
                    while (utUserCustTypeVO.hasNext()) {
                        Row utUserCustTypeRow = utUserCustTypeVO.next();
                        //System.out.println("(" + utUserCustTypeVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserCustTypeRow.getAttribute("CustType"));
                        Row newAppUserCustTypeRow = newAppUserCustTypeRI.createRow();
                        newAppUserCustTypeRow.setAttribute("UserName", li_username.get(i));
                        newAppUserCustTypeRow.setAttribute("CustType", utUserCustTypeRow.getAttribute("CustType"));
                        newAppUserCustTypeRI.insertRow(newAppUserCustTypeRow);
                    }
                    utUserCustTypeVO.closeRowSet();
                    //insert to app_user_cust_group
                    ViewObjectImpl utUserCustGroupVO = userAccessAM.getUtUserCustGroupView2();
                    utUserCustGroupVO.setWhereClause("UtUserCustGroup.USER_NAME = '" + li_username.get(i) + "'");
                    utUserCustGroupVO.executeQuery();
                    RowIterator newAppUserCustGroupRI = currAppUserAccessRow.getUpdateAppUserCustGroup();
                    while (utUserCustGroupVO.hasNext()) {
                        Row utUserCustGroupRow = utUserCustGroupVO.next();
                        //System.out.println("(" + utUserCustGroupVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserCustGroupRow.getAttribute("CustGroup"));
                        Row newAppUserCustGroupRow = newAppUserCustGroupRI.createRow();
                        newAppUserCustGroupRow.setAttribute("UserName", li_username.get(i));
                        newAppUserCustGroupRow.setAttribute("CustGroup", utUserCustGroupRow.getAttribute("CustGroup"));
                        newAppUserCustGroupRI.insertRow(newAppUserCustGroupRow);
                    }
                    utUserCustGroupVO.closeRowSet();
                    //insert to app_user_cust
                    ViewObjectImpl utUserCustVO = userAccessAM.getUtUserCustView2();
                    utUserCustVO.setWhereClause("UtUserCust.USER_NAME = '" + li_username.get(i) + "'");
                    utUserCustVO.executeQuery();
                    RowIterator newAppUserCustRI = currAppUserAccessRow.getUpdateAppUserCust();
                    while (utUserCustVO.hasNext()) {
                        Row utUserCustRow = utUserCustVO.next();
                        //System.out.println("(" + utUserCustVO.getEstimatedRowCount() + ")" + li_username.get(i) + " " + utUserCustRow.getAttribute("CustomerId"));
                        Row newAppUserCustRow = newAppUserCustRI.createRow();
                        newAppUserCustRow.setAttribute("UserName", li_username.get(i));
                        newAppUserCustRow.setAttribute("CustomerId", utUserCustRow.getAttribute("CustomerId"));
                        newAppUserCustRI.insertRow(newAppUserCustRow);
                    }
                    utUserCustVO.closeRowSet();
                    //insert to app_user_produk_category
                    {
                        ViewObjectImpl utProdCategoryVO = userAccessAM.getUtUserProdukCategoryView2();
                        utProdCategoryVO.setWhereClause("UtUserProdukCategory.USER_NAME = '" + li_username.get(i) + "'");
                        utProdCategoryVO.executeQuery();
                        RowIterator newAppProdCategoryRI = currAppUserAccessRow.getUpdateAppUserProdukCategory();
                        while (utProdCategoryVO.hasNext()) {
                            Row utProdCategoryRow = utProdCategoryVO.next();
                            Row newAppProdCategoryRow = newAppProdCategoryRI.createRow();
                            newAppProdCategoryRow.setAttribute("UserName", li_username.get(i));
                            newAppProdCategoryRow.setAttribute("ProdCategory", utProdCategoryRow.getAttribute("ProdCategory"));
                            newAppProdCategoryRow.setAttribute("CategoryDesc", utProdCategoryRow.getAttribute("CategoryDesc"));
                            newAppProdCategoryRI.insertRow(newAppProdCategoryRow);
                        }
                        utProdCategoryVO.closeRowSet();
                    }
                    //insert to app_user_produk_class
                    {
                        ViewObjectImpl utProdClassVO = userAccessAM.getUtUserProdukClassView2();
                        utProdClassVO.setWhereClause("UtUserProdukClass.USER_NAME = '" + li_username.get(i) + "'");
                        utProdClassVO.executeQuery();
                        RowIterator newAppProdClassRI = currAppUserAccessRow.getUpdateAppUserProdukClass();
                        while (utProdClassVO.hasNext()) {
                            Row utProdClassRow = utProdClassVO.next();
                            Row newAppProdClassRow = newAppProdClassRI.createRow();
                            newAppProdClassRow.setAttribute("UserName", li_username.get(i));
                            newAppProdClassRow.setAttribute("ProdClass", utProdClassRow.getAttribute("ProdClass"));
                            newAppProdClassRow.setAttribute("ClassDesc", utProdClassRow.getAttribute("ClassDesc"));
                            newAppProdClassRI.insertRow(newAppProdClassRow);
                        }
                        utProdClassVO.closeRowSet();
                    }
                    //insert to app_user_produk_brand
                    {
                        ViewObjectImpl utProdBrandVO = userAccessAM.getUtUserProdukBrandView2();
                        utProdBrandVO.setWhereClause("UtUserProdukBrand.USER_NAME = '" + li_username.get(i) + "'");
                        utProdBrandVO.executeQuery();
                        RowIterator newAppProdBrandRI = currAppUserAccessRow.getUpdateAppUserProdukBrand();
                        while (utProdBrandVO.hasNext()) {
                            Row utProdBrandRow = utProdBrandVO.next();
                            Row newAppProdBrandRow = newAppProdBrandRI.createRow();
                            newAppProdBrandRow.setAttribute("UserName", li_username.get(i));
                            newAppProdBrandRow.setAttribute("ProdBrand", utProdBrandRow.getAttribute("ProdBrand"));
                            newAppProdBrandRow.setAttribute("BrandDesc", utProdBrandRow.getAttribute("BrandDesc"));
                            newAppProdBrandRI.insertRow(newAppProdBrandRow);
                        }
                        utProdBrandVO.closeRowSet();
                    }
                    //insert to app_user_produk_ext
                    {
                        ViewObjectImpl utProdExtVO = userAccessAM.getUtUserProdukExtView2();
                        utProdExtVO.setWhereClause("UtUserProdukExt.USER_NAME = '" + li_username.get(i) + "'");
                        utProdExtVO.executeQuery();
                        RowIterator newAppProdExtRI = currAppUserAccessRow.getUpdateAppUserProdukExt();
                        while (utProdExtVO.hasNext()) {
                            Row utProdExtRow = utProdExtVO.next();
                            Row newAppProdExtRow = newAppProdExtRI.createRow();
                            newAppProdExtRow.setAttribute("UserName", li_username.get(i));
                            newAppProdExtRow.setAttribute("ProdExt", utProdExtRow.getAttribute("ProdExt"));
                            newAppProdExtRow.setAttribute("ExtDesc", utProdExtRow.getAttribute("ExtDesc"));
                            newAppProdExtRI.insertRow(newAppProdExtRow);
                        }
                        utProdExtVO.closeRowSet();
                    }
                    //insert to app_user_produk_pack
                    {
                        ViewObjectImpl utProdPackVO = userAccessAM.getUtUserProdukPackView2();
                        utProdPackVO.setWhereClause("UtUserProdukPack.USER_NAME = '" + li_username.get(i) + "'");
                        utProdPackVO.executeQuery();
                        RowIterator newAppProdPackRI = currAppUserAccessRow.getUpdateAppUserProdukPack();
                        while (utProdPackVO.hasNext()) {
                            Row utProdPackRow = utProdPackVO.next();
                            Row newAppProdPackRow = newAppProdPackRI.createRow();
                            newAppProdPackRow.setAttribute("UserName", li_username.get(i));
                            newAppProdPackRow.setAttribute("ProdPack", utProdPackRow.getAttribute("ProdPack"));
                            newAppProdPackRow.setAttribute("PackDesc", utProdPackRow.getAttribute("PackDesc"));
                            newAppProdPackRI.insertRow(newAppProdPackRow);
                        }
                        utProdPackVO.closeRowSet();
                    }
                    //insert to app_user_produk_variant
                    {
                        ViewObjectImpl utProdVariantVO = userAccessAM.getUtUserProdukVariantView2();
                        utProdVariantVO.setWhereClause("UtUserProdukVariant.USER_NAME = '" + li_username.get(i) + "'");
                        utProdVariantVO.executeQuery();
                        RowIterator newAppProdVariantRI = currAppUserAccessRow.getUpdateAppUserProdukVariant();
                        while (utProdVariantVO.hasNext()) {
                            Row utProdVariantRow = utProdVariantVO.next();
                            Row newAppProdVariantRow = newAppProdVariantRI.createRow();
                            newAppProdVariantRow.setAttribute("UserName", li_username.get(i));
                            newAppProdVariantRow.setAttribute("ProdVariant", utProdVariantRow.getAttribute("ProdVariant"));
                            newAppProdVariantRow.setAttribute("VariantDesc", utProdVariantRow.getAttribute("VariantDesc"));
                            newAppProdVariantRI.insertRow(newAppProdVariantRow);
                        }
                        utProdVariantVO.closeRowSet();
                    }
                    //insert to app_user_produk_item
                    {
                        ViewObjectImpl utProdItemVO = userAccessAM.getUtUserProdukItemView2();
                        utProdItemVO.setWhereClause("UtUserProdukItem.USER_NAME = '" + li_username.get(i) + "'");
                        utProdItemVO.executeQuery();
                        RowIterator newAppProdItemRI = currAppUserAccessRow.getUpdateAppUserProdukItem();
                        while (utProdItemVO.hasNext()) {
                            Row utProdItemRow = utProdItemVO.next();
                            Row newAppProdItemRow = newAppProdItemRI.createRow();
                            newAppProdItemRow.setAttribute("UserName", li_username.get(i));
                            newAppProdItemRow.setAttribute("ProdItem", utProdItemRow.getAttribute("ProdItem"));
                            newAppProdItemRow.setAttribute("ItemDesc", utProdItemRow.getAttribute("ItemDesc"));
                            newAppProdItemRI.insertRow(newAppProdItemRow);
                        }
                        utProdItemVO.closeRowSet();
                    }
                    newAppUserAccessVO.closeRowSet();
                    OperationBinding operBindCommitInsNewAppUsrAcc =                 
                        bindings.getOperationBinding("Commit");             
                    operBindCommitInsNewAppUsrAcc.execute();
                }
                //commit
                OperationBinding operBindCommitAll =                 
                    bindings.getOperationBinding("Commit");             
                operBindCommitAll.execute();
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            }
        updateSyncLog("UTSYNC", "FINISHED");
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
    }

    public void setFrmStatusSync(RichPanelFormLayout frmStatusSync) {
        this.frmStatusSync = frmStatusSync;
    }

    public RichPanelFormLayout getFrmStatusSync() {
        return frmStatusSync;
    }

    public void utSyncReset(ActionEvent actionEvent) {
        updateSyncLog("UTSYNC", "-");
    }

    public void utValidateReset(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        try {
            UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
            ViewObjectImpl utUserUploadLogVO = userAccessAM.getUtUserUploadLog1();
            utUserUploadLogVO.setWhereClause("UserUploadLog.UT_CODE = 'UTVALIDATE'");
            utUserUploadLogVO.executeQuery();
            while (utUserUploadLogVO.hasNext()) {
                utUserUploadLogVO.next().remove();
            }
            OperationBinding operationBindingCommit =                 
                bindings.getOperationBinding("Commit");             
            operationBindingCommit.execute();
            utUserUploadLogVO.closeRowSet();
            userAccessAM.getUserUploadValidateLog1().executeQuery();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
    }

    public String cbUploadSync_click() {
        btnUpload_Click();
        try {
            ViewObjectImpl utUserUploadLogVO = userAccessAM.getUtUserUploadLog1();
            utUserUploadLogVO.setWhereClause("UserUploadLog.UT_CODE = 'UTUPLOAD' AND UserUploadLog.UT_STATUS = 'FINISHED'");
            utUserUploadLogVO.executeQuery();
            if (utUserUploadLogVO.hasNext()) {
                btnValidate_Click();
                utUserUploadLogVO.setWhereClause("UserUploadLog.UT_CODE = 'UTVALIDATE' AND UserUploadLog.VAL_CODE = 'VALERROR'");
                utUserUploadLogVO.executeQuery();
                if (utUserUploadLogVO.hasNext()) {
                    updateUploadLog("UTUPLOAD", "VALIDATION ERROR");
                    getTabUpload().setDisclosed(false);
                    getTabProcessLog().setDisclosed(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(getTabUpload());
                    AdfFacesContext.getCurrentInstance().addPartialTarget(getTabProcessLog());
                }
                else {
                    btnSync_Click();
                    utUserUploadLogVO.setWhereClause("UserUploadLog.UT_CODE = 'UTSYNC' AND UserUploadLog.SYNC_STATUS = 'FINISHED'");
                    utUserUploadLogVO.executeQuery();
                    if (utUserUploadLogVO.hasNext())
                        updateUploadLog("UTUPLOAD", "UPLOAD AND SYNC FINISHED");
                    else
                    updateUploadLog("UTUPLOAD", "SYNC ERROR");
                }
            }
            else {
                updateUploadLog("UTUPLOAD", "UPLOAD ERROR");
            }
            utUserUploadLogVO.closeRowSet();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void setTabProcessLog(RichShowDetailItem tabProcessLog) {
        this.tabProcessLog = tabProcessLog;
    }

    public RichShowDetailItem getTabProcessLog() {
        return tabProcessLog;
    }

    public void setTabUpload(RichShowDetailItem tabUpload) {
        this.tabUpload = tabUpload;
    }

    public RichShowDetailItem getTabUpload() {
        return tabUpload;
    }

    private class UserAccess {
        String userName;
        ArrayList<String> userCode;
        ArrayList<String> categoryList;
        ArrayList<String> classList;
        ArrayList<String> brandList;
        ArrayList<String> extList;
        ArrayList<String> packList;
        ArrayList<String> variantList;
        ArrayList<String> itemList;
        
        UserAccess(String userName) {
            this.userName = userName;
            this.userCode = new ArrayList<String>();
            this.categoryList = new ArrayList<String>();
            this.classList = new ArrayList<String>();
            this.brandList = new ArrayList<String>();
            this.extList = new ArrayList<String>();
            this.packList = new ArrayList<String>();
            this.variantList = new ArrayList<String>();
            this.itemList = new ArrayList<String>();
        }
    }
}
