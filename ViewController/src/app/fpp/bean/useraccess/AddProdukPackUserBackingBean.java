package app.fpp.bean.useraccess;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.model.am.UserAccessAMImpl;

import java.io.Serializable;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;

public class AddProdukPackUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedPacks;
    List allPacks;
    Boolean refreshSelectedList = false;
    private final String SELECTED_PACK_ITERATOR =
        "AppUserProdukPackView1Iterator";
    private final String ALL_PACK_ITERATOR = "AllUserProdukPackShuttleView1Iterator";
    
    public AddProdukPackUserBackingBean() {
        super();
    }

    public void setSelectedPacks(List selectedPacks) {
        this.selectedPacks = selectedPacks;
    }

    public List getSelectedPacks() {
        if (selectedPacks == null || refreshSelectedList) {
            selectedPacks =
                    attributeListForIterator(SELECTED_PACK_ITERATOR, "ProdPack");            
        }
        return selectedPacks;
    }

    public void setAllPacks(List allPacks) {
        this.allPacks = allPacks;
    }

    public List getAllPacks() {
        if (allPacks == null) {
            allPacks = selectItemsForIterator(ALL_PACK_ITERATOR, "SetPackaging", "SetPackagingDesc");
        }
        return allPacks;
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    
    // Shuttle operations
    public static List attributeListForIterator(String iteratorName,
                                                String valueAttrName) {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding iter = binding.findIteratorBinding(iteratorName);
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getAttribute(valueAttrName));
        }
        return attributeList;
    }

    public static List<SelectItem> selectItemsForIterator(String iteratorName,
                                                          String valueAttrName,
                                                          String displayAttrName) {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding iter = binding.findIteratorBinding(iteratorName);
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName),
                                           (String)r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    public String processShuttle() {
        int actualSelectedPackSize = selectedPacks == null ? 0 : selectedPacks.size();
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterArea =
            (DCIteratorBinding)binding.get(SELECTED_PACK_ITERATOR);
        
        //---f
        boolean isPackDeleted = false;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        //check if there's deleted pack
        if (actualSelectedPackSize < iterArea.getEstimatedRowCount()) {
            isPackDeleted = true;
            ArrayList<String> deletedPackList = new ArrayList<String>();
            ArrayList<String> deletedVariantList = new ArrayList<String>();
            boolean isVariantHaveAll = false;
            ArrayList<String> deletedItemList = new ArrayList<String>();
            boolean isItemHaveAll = false;
            String currPacks = "";
            String currusername = getUserName();
            for (int i = 0; i < actualSelectedPackSize; i++) {
                currPacks += "'" + getSelectedPacks().get(i).toString() + "'";
                if (i < getSelectedPacks().size() - 1)
                    currPacks += ", ";
            }
            try {
                //get newly removed pack and insert to arraylist
                {
                    PreparedStatement st = null;
                    if (actualSelectedPackSize == 0)
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_PACK FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "'", 1);
                    else
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_PACK FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "' AND PROD_PACK NOT IN (" + currPacks + ")", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next())
                                deletedPackList.add(rs.getString("PROD_PACK"));
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (VARIANT)
                for (String deletedPack : deletedPackList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_PACKAGING, SET_VARIANT FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_PACKAGING = '" + deletedPack + "' AND SET_VARIANT IN (SELECT PROD_VARIANT FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_VARIANT");
                                if (strtemp.equals("ALL"))
                                    isVariantHaveAll = true;
                                else if (!deletedVariantList.contains(strtemp))
                                    deletedVariantList.add(strtemp);
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (ITEM)
                for (String deletedVariant : deletedVariantList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_VARIANT, ITEM FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_VARIANT = '" + deletedVariant + "' AND ITEM IN (SELECT PROD_ITEM FROM APP_USER_PRODUK_ITEM WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_ITEM");
                                if (strtemp.equals("ALL"))
                                    isItemHaveAll = true;
                                else if (!deletedItemList.contains(strtemp))
                                    deletedItemList.add(strtemp);
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //--------
                //delete data from the list
                //delete (ITEM)
                for (String deletedItem : deletedItemList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_ITEM WHERE USER_NAME = '" + currusername + "' AND PROD_ITEM = '" + deletedItem + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                //delete (VARIANT)
                for (String deletedVariant : deletedVariantList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "' AND PROD_VARIANT = '" + deletedVariant + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                //check if (VARIANT) remain only 1 and if its 'ALL' or not. if true then delete all remaining (ITEM)
                if (isVariantHaveAll) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT COUNT(1) FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "'", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_ITEM WHERE USER_NAME = '" + currusername + "'", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                userAccessAM.getDBTransaction().commit();
                doCommit();
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            }
        }
        //---endf

        //Removing all packaging rows
        for (Row r : iterArea.getAllRowsInRange()) {
            r.remove();
        }

        if (actualSelectedPackSize > 0) {
            for (int i = 0; i < getSelectedPacks().size(); i++) {
                Row row = iterArea.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_PACK_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedPacks().get(i).equals(r.getAttribute("SetPackaging"))) {
                        row.setAttribute("PackDesc",
                                         r.getAttribute("SetPackagingDesc"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("ProdPack", getSelectedPacks().get(i));

                iterArea.getRowSetIterator().insertRow(row);
                iterArea.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
        //---f
        //check if (PACK) remain only 1 and if its 'ALL'/'BLANK' or not. if true then delete all child data then insert 'ALL'/'BLANK' to all child
        try {
            PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT (SELECT COUNT(1) FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + getUserName() + "'), PROD_PACK FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + getUserName() + "'", 1);
            try {
                ResultSet rs = st.executeQuery();
                try {
                    rs.next();
                    if ((rs.getInt(1) == 1) && ((rs.getString("PROD_PACK").equals("ALL")) || (rs.getString("PROD_PACK").equals("PKT-00000")))) {
                        //delete all child data
                        {
                            PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME  = '" + getUserName() + "'", 1);
                            try {
                                st1.execute();
                            } finally {
                                st1.close();
                            }
                        }
                        {
                            PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_ITEM WHERE USER_NAME  = '" + getUserName() + "'", 1);
                            try {
                                st1.execute();
                            } finally {
                                st1.close();
                            }
                        }
                        userAccessAM.getDBTransaction().commit();
                        doCommit();
                        if (rs.getString("PROD_PACK").equals("ALL")) {
                            //insert 'ALL' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_VARIANT (USER_NAME, PROD_VARIANT, VARIANT_DESC) VALUES ('" + getUserName() + "', 'ALL', 'ALL')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                        }
                        else if (rs.getString("PROD_PACK").equals("PKT-00000")) {
                            //insert 'BLANK' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_VARIANT (USER_NAME, PROD_VARIANT, VARIANT_DESC) VALUES ('" + getUserName() + "', 'PV0000', 'BLANK')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                        }
                        userAccessAM.getDBTransaction().commit();
                        doCommit();
                    }
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }
        //---endf
        
        return closeAfter;
    }

    public String processCancel() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        //String cancel = doRollback();
        return closeAfter;
    }

    public String doCommit() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String doRollback() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String getUserName() {
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        String userName = (String)pageFlowScope.get("userName");
        return userName;
    }
}
