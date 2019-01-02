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
import oracle.jbo.RowSetIterator;

public class AddProdukClassUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedClasses;
    List allClasses;
    Boolean refreshSelectedList = false;
    private final String SELECTED_CLASS_ITERATOR =
        "AppUserProdukClassView1Iterator";
    private final String ALL_CLASS_ITERATOR = "AllUserProdukClassShuttleView1Iterator";
    
    public AddProdukClassUserBackingBean() {
        super();
    }

    public void setSelectedClasses(List selectedClasses) {
        this.selectedClasses = selectedClasses;
    }

    public List getSelectedClasses() {
        if (selectedClasses == null) {
            selectedClasses =
                    attributeListForIterator(SELECTED_CLASS_ITERATOR, "ProdClass");            
        }
        return selectedClasses;
    }

    public void setAllClasses(List allClasses) {
        this.allClasses = allClasses;
    }

    public List getAllClasses() {
        if (allClasses == null) {
            allClasses = selectItemsForIterator(ALL_CLASS_ITERATOR, "SetClass", "SetClassDesc");
        }
        return allClasses;
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
        int actualSelectedClassSize = selectedClasses == null ? 0 : selectedClasses.size();
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterArea =
            (DCIteratorBinding)binding.get(SELECTED_CLASS_ITERATOR);
        
        //---f
        boolean isClassDeleted = false;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        //check if there's deleted category
        if (actualSelectedClassSize < iterArea.getEstimatedRowCount()) {
            isClassDeleted = true;
            ArrayList<String> deletedClassList = new ArrayList<String>();
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
            String currClasses = "";
            String currusername = getUserName();
            for (int i = 0; i < actualSelectedClassSize; i++) {
                currClasses += "'" + getSelectedClasses().get(i).toString() + "'";
                if (i < getSelectedClasses().size() - 1)
                    currClasses += ", ";
            }
            try {
                //get newly removed class and insert to arraylist
                {
                    PreparedStatement st = null;
                    if (actualSelectedClassSize == 0)
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_CLASS FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + currusername + "'", 1);
                    else
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_CLASS FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + currusername + "' AND PROD_CLASS NOT IN (" + currClasses + ")", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next())
                                deletedClassList.add(rs.getString("PROD_CLASS"));
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
                //delete (EXT)
                for (String deletedExt : deletedExtList) {
                    PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_EXT WHERE USER_NAME = '" + currusername + "' AND PROD_EXT = '" + deletedExt + "'", 1);
                    try {
                        st.execute();
                    } finally {
                        st.close();
                    }
                }
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
                            }
                        } finally {
                            rs.close();
                        }
                    } finally {
                        st.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            userAccessAM.getDBTransaction().commit();
            doCommit();
        }
        //---endf

        //Removing all class rows;
        for (Row r : iterArea.getAllRowsInRange()) {
            r.remove();
        }

        if (actualSelectedClassSize > 0) {
            for (int i = 0; i < getSelectedClasses().size(); i++) {
                Row row = iterArea.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_CLASS_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedClasses().get(i).equals(r.getAttribute("SetClass"))) {
                        row.setAttribute("ClassDesc",
                                         r.getAttribute("SetClassDesc"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("ProdClass", getSelectedClasses().get(i));

                iterArea.getRowSetIterator().insertRow(row);
                iterArea.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
        //---f
        //check if (CLASS) remain only 1 and if its 'ALL'/'BLANK' or not. if true then delete all child data then insert 'ALL'/'BLANK' to all child
        try {
            PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT (SELECT COUNT(1) FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + getUserName() + "'), PROD_CLASS FROM APP_USER_PRODUK_CLASS WHERE USER_NAME = '" + getUserName() + "'", 1);
            try {
                ResultSet rs = st.executeQuery();
                try {
                    rs.next();
                    if ((rs.getInt(1) == 1) && ((rs.getString("PROD_CLASS").equals("ALL")) || (rs.getString("PROD_CLASS").equals("PC000")))) {
                        //delete all child data
                        {
                            PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_BRAND WHERE USER_NAME  = '" + getUserName() + "'", 1);
                            try {
                                st1.execute();
                            } finally {
                                st1.close();
                            }
                        }
                        {
                            PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("DELETE FROM APP_USER_PRODUK_EXT WHERE USER_NAME  = '" + getUserName() + "'", 1);
                            try {
                                st1.execute();
                            } finally {
                                st1.close();
                            }
                        }
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
                        if (rs.getString("PROD_CLASS").equals("ALL")) {
                            //insert 'ALL' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_BRAND (USER_NAME, PROD_BRAND, BRAND_DESC) VALUES ('" + getUserName() + "', 'ALL', 'ALL')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_EXT (USER_NAME, PROD_EXT, EXT_DESC) VALUES ('" + getUserName() + "', 'ALL', 'ALL')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
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
                        else if (rs.getString("PROD_CLASS").equals("PC000")) {
                            //insert 'BLANK' to all child except item
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_BRAND (USER_NAME, PROD_BRAND, BRAND_DESC) VALUES ('" + getUserName() + "', 'PC000', 'BLANK')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
                            {
                                PreparedStatement st1 = userAccessAM.getDBTransaction().createPreparedStatement("INSERT INTO APP_USER_PRODUK_EXT (USER_NAME, PROD_EXT, EXT_DESC) VALUES ('" + getUserName() + "', 'PE00000', 'BLANK')", 1);
                                try {
                                    st1.execute();
                                } finally {
                                    st1.close();
                                }
                            }
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
            //JSFUtils.addFacesErrorMessage(e.getMessage());
            e.printStackTrace();
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
