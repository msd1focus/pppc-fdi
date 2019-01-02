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

public class AddProdukCategoryUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedCategorys;
    List allCategorys;
    Boolean refreshSelectedList = false;
    private final String SELECTED_CATEGORY_ITERATOR =
        "AppUserProdukCategoryView1Iterator";
    private final String ALL_CATEGORY_ITERATOR = "AllUserProdukCategoryShuttleView1Iterator";
    
    public AddProdukCategoryUserBackingBean() {
        super();
    }

    public void setSelectedCategorys(List selectedCategorys) {
        this.selectedCategorys = selectedCategorys;
    }

    public List getSelectedCategorys() {
        if (selectedCategorys == null || refreshSelectedList) {
            selectedCategorys =
                    attributeListForIterator(SELECTED_CATEGORY_ITERATOR, "ProdCategory");            
        }
        return selectedCategorys;
    }

    public void setAllCategorys(List allCategorys) {
        this.allCategorys = allCategorys;
    }

    public List getAllCategorys() {
        if (allCategorys == null) {
            allCategorys = selectItemsForIterator(ALL_CATEGORY_ITERATOR, "SetCategory", "SetCategoryDesc");
        }
        return allCategorys;
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
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterArea =
            (DCIteratorBinding)binding.get(SELECTED_CATEGORY_ITERATOR);
        
        //---f
        boolean isCategoryDeleted = false;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        //check if there's deleted category
        if (getSelectedCategorys().size() < iterArea.getEstimatedRowCount()) {
            isCategoryDeleted = true;
            //get curr selected item in shuttle and put as string
            ArrayList<String> deletedCategoryList = new ArrayList<String>();
            ArrayList<String> deletedClassList = new ArrayList<String>();
            boolean isClassHaveAll = false;
            ArrayList<String> deletedBrandList = new ArrayList<String>();
            boolean isBrandHaveAll = false;
            ArrayList<String> deletedExtList = new ArrayList<String>();
            boolean isExtHaveAll = false;
            ArrayList<String> deletedPackList = new ArrayList<String>();
            boolean isPackHaveAll = false;
            ArrayList<String> deletedVariantList = new ArrayList<String>();
            boolean isVariantHaveAll = false;
            ArrayList<String> deletedItemList = new ArrayList<String>();
            boolean isItemHaveAll = false;
            String currCategorys = "";
            String currusername = getUserName();
            for (int i = 0; i < getSelectedCategorys().size(); i++) {
                currCategorys += "'" + getSelectedCategorys().get(i).toString() + "'";
                if (i < getSelectedCategorys().size() - 1)
                    currCategorys += ", ";
            }
            try {
                //get newly removed category and insert to arraylist
                {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_CATEGORY FROM APP_USER_PRODUK_CATEGORY WHERE USER_NAME = '" + currusername + "' AND PROD_CATEGORY NOT IN (" + currCategorys + ")", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next())
                                deletedCategoryList.add(rs.getString("PROD_CATEGORY"));
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (CLASS)
                for (String deletedCategory : deletedCategoryList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_CATEGORY, SET_CLASS FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_CATEGORY = '" + deletedCategory + "' AND SET_CLASS IN (SELECT PROD_CLASS FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_CLASS");
                                if (strtemp.equals("ALL"))
                                    isClassHaveAll = true;
                                else if (!deletedClassList.contains(strtemp))
                                    deletedClassList.add(strtemp);
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (BRAND)
                for (String deletedClass : deletedClassList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_CLASS, SET_BRAND FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_CLASS = '" + deletedClass + "' AND SET_BRAND IN (SELECT PROD_BRAND FROM APP_USER_PRODUK_BRAND WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_BRAND");
                                if (strtemp.equals("ALL"))
                                    isBrandHaveAll = true;
                                else if (!deletedBrandList.contains(strtemp))
                                    deletedBrandList.add(strtemp);
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //get child of deleted category (EXT)
                for (String deletedBrand : deletedBrandList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT DISTINCT SET_BRAND, SET_EXT FROM APPS.FCS_VIEW_ITEM_MASTER_CATEGORY WHERE SET_BRAND = '" + deletedBrand + "' AND SET_EXT IN (SELECT PROD_EXT FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "')", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next()) {
                                String strtemp = rs.getString("SET_EXT");
                                if (strtemp.equals("ALL"))
                                    isExtHaveAll = true;
                                else if (!deletedExtList.contains(strtemp))
                                    deletedExtList.add(strtemp);
                            }
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
                doCommit();
                //delete (VARIANT)
                for (String deletedVariant : deletedVariantList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "' AND PROD_VARIANT = '" + deletedVariant + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                doCommit();
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
                                doCommit();
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
                doCommit();
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
                                doCommit();
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //delete (EXT)
                for (String deletedExt : deletedExtList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "' AND PROD_EXT = '" + deletedExt + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                doCommit();
                //check if (EXT) remain only 1 and if its 'ALL' or not. if true then delete all remaining (PACK)
                if (isExtHaveAll) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT COUNT(1) FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "'", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_PACK WHERE USER_NAME = '" + currusername + "'", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                                doCommit();
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //delete (BRAND)
                for (String deletedBrand : deletedBrandList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_BRAND WHERE USER_NAME = '" + currusername + "' AND PROD_BRAND = '" + deletedBrand + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                doCommit();
                //check if (BRAND) remain only 1 and if its 'ALL' or not. if true then delete all remaining (EXT)
                if (isBrandHaveAll) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT COUNT(1) FROM APP_USER_PRODUK_BRAND WHERE USER_NAME = '" + currusername + "'", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername, 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                                doCommit();
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
                //delete (CLASS)
                for (String deletedClass : deletedClassList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + currusername + "' AND PROD_CLASS = '" + deletedClass + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
                doCommit();
                //check if (CLASS) remain only 1 and if its 'ALL' or not. if true then delete all remaining (BRAND)
                if (isClassHaveAll) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT COUNT(1) FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + currusername + "'", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            rs.next();
                            if (rs.getInt(1) == 1) {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_BRAND WHERE USER_NAME = '" + currusername + "'", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                                doCommit();
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            }
        }
        //---endf

        //Removing all category rows
        for (Row r : iterArea.getAllRowsInRange()) {
            r.remove();
        }

        if (this.getSelectedCategorys().size() > 0) {
            for (int i = 0; i < getSelectedCategorys().size(); i++) {
                Row row = iterArea.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_CATEGORY_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedCategorys().get(i).equals(r.getAttribute("SetCategory"))) {
                        row.setAttribute("CategoryDesc",
                                         r.getAttribute("SetCategoryDesc"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("ProdCategory", getSelectedCategorys().get(i));

                iterArea.getRowSetIterator().insertRow(row);
                iterArea.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
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
