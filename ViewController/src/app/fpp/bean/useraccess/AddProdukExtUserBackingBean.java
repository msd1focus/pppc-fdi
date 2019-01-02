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

public class AddProdukExtUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedExts;
    List allExts;
    Boolean refreshSelectedList = false;
    private final String SELECTED_EXT_ITERATOR =
        "AppUserProdukExtView1Iterator";
    private final String ALL_EXT_ITERATOR = "AllUserProdukExtShuttleView1Iterator";
    
    public AddProdukExtUserBackingBean() {
        super();
    }

    public void setSelectedExts(List selectedExts) {
        this.selectedExts = selectedExts;
    }

    public List getSelectedExts() {
        if (selectedExts == null || refreshSelectedList) {
            selectedExts =
                    attributeListForIterator(SELECTED_EXT_ITERATOR, "ProdExt");            
        }
        return selectedExts;
    }

    public void setAllExts(List allExts) {
        this.allExts = allExts;
    }

    public List getAllExts() {
        if (allExts == null) {
            allExts = selectItemsForIterator(ALL_EXT_ITERATOR, "SetExt", "SetExtDesc");
        }
        return allExts;
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
        int actualSelectedExtSize = selectedExts == null ? 0 : selectedExts.size();
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterArea =
            (DCIteratorBinding)binding.get(SELECTED_EXT_ITERATOR);
        
        //---f
        boolean isExtDeleted = false;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        //check if there's deleted ext
        if (actualSelectedExtSize < iterArea.getEstimatedRowCount()) {
            isExtDeleted = true;
            ArrayList<String> deletedExtList = new ArrayList<String>();
            ArrayList<String> deletedPackList = new ArrayList<String>();
            boolean isPackHaveAll = false;
            ArrayList<String> deletedVariantList = new ArrayList<String>();
            boolean isVariantHaveAll = false;
            ArrayList<String> deletedItemList = new ArrayList<String>();
            boolean isItemHaveAll = false;
            String currExts = "";
            String currusername = getUserName();
            for (int i = 0; i < actualSelectedExtSize; i++) {
                currExts += "'" + getSelectedExts().get(i).toString() + "'";
                if (i < getSelectedExts().size() - 1)
                    currExts += ", ";
            }
            try {
                //get newly removed ext and insert to arraylist
                {
                    PreparedStatement st = null;
                    if (actualSelectedExtSize == 0)
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_EXT FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "'", 1);
                    else
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_EXT FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "' AND PROD_EXT NOT IN (" + currExts + ")", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next())
                                deletedExtList.add(rs.getString("PROD_EXT"));
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (PACK)
                for (String deletedExt : deletedExtList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_EXT, SET_PACKAGING FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_EXT = '" + deletedExt + "' AND SET_PACKAGING IN (SELECT PROD_PACK FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_PACKAGING");
                                if (strtemp.equals("ALL"))
                                    isPackHaveAll = true;
                                else if (!deletedPackList.contains(strtemp))
                                    deletedPackList.add(strtemp);
                            }
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
                //delete (PACK)
                for (String deletedPack : deletedPackList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "' AND PROD_PACK = '" + deletedPack + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                //check if (PACK) remain only 1 and if its 'ALL' or not. if true then delete all remaining (VARIANT)
                if (isPackHaveAll) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT COUNT(1) FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "'", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "'", 1);
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

        //Removing all ext rows
        for (Row r : iterArea.getAllRowsInRange()) {
            r.remove();
        }

        if (actualSelectedExtSize > 0) {
            for (int i = 0; i < getSelectedExts().size(); i++) {
                Row row = iterArea.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_EXT_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedExts().get(i).equals(r.getAttribute("SetExt"))) {
                        row.setAttribute("ExtDesc",
                                         r.getAttribute("SetExtDesc"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("ProdExt", getSelectedExts().get(i));

                iterArea.getRowSetIterator().insertRow(row);
                iterArea.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
        //---f
        //check if (EXT) remain only 1 and if its 'ALL'/'BLANK' or not. if true then delete all child data then insert 'ALL'/'BLANK' to all child
        try {
            PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT (SELECT COUNT(1) FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + getUserName() + "'), PROD_EXT FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + getUserName() + "'", 1);
            try {
                ResultSet rs = st.executeQuery();
                try {
                    rs.next();
                    if ((rs.getInt(1) == 1) && ((rs.getString("PROD_EXT").equals("ALL")) || (rs.getString("PROD_EXT").equals("PE00000")))) {
                        //delete all child data
                        {
                            PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_PACK WHERE USER_NAME  = '" + getUserName() + "'", 1);
                            try {
                                st1.execute();
                            } finally {
                                st1.close();
                            }
                        }
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
                        if (rs.getString("PROD_EXT").equals("ALL")) {
                            //insert 'ALL' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_PACK (USER_NAME, PROD_PACK, PACK_DESC) VALUES ('" + getUserName() + "', 'ALL', 'ALL')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_VARIANT (USER_NAME, PROD_VARIANT, VARIANT_DESC) VALUES ('" + getUserName() + "', 'ALL', 'ALL')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                        }
                        else if (rs.getString("PROD_EXT").equals("PE00000")) {
                            //insert 'BLANK' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_PACK (USER_NAME, PROD_PACK, PACK_DESC) VALUES ('" + getUserName() + "', 'PKT-00000', 'PKT')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
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
