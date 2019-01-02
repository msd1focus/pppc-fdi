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

public class AddProdukVariantUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedVariants;
    List allVariants;
    Boolean refreshSelectedList = false;
    private final String SELECTED_VARIANT_ITERATOR =
        "AppUserProdukVariantView1Iterator";
    private final String ALL_VARIANT_ITERATOR = "AllUserProdukVariantShuttleView1Iterator";
    
    public AddProdukVariantUserBackingBean() {
        super();
    }

    public void setSelectedVariants(List selectedVariants) {
        this.selectedVariants = selectedVariants;
    }

    public List getSelectedVariants() {
        if (selectedVariants == null || refreshSelectedList) {
            selectedVariants =
                    attributeListForIterator(SELECTED_VARIANT_ITERATOR, "ProdVariant");            
        }
        return selectedVariants;
    }

    public void setAllVariants(List allVariants) {
        this.allVariants = allVariants;
    }

    public List getAllVariants() {
        if (allVariants == null) {
            allVariants = selectItemsForIterator(ALL_VARIANT_ITERATOR, "SetVariant", "SetVariantDesc");
        }
        return allVariants;
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
        int actualSelectedVariantSize = selectedVariants == null ? 0 : selectedVariants.size();
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterArea =
            (DCIteratorBinding)binding.get(SELECTED_VARIANT_ITERATOR);
        
        //---f
        boolean isVariantDeleted = false;
        UserAccessAMImpl userAccessAM = (UserAccessAMImpl)ADFUtils.getApplicationModuleForDataControl("UserAccessAMDataControl");
        //check if there's deleted variant
        if (actualSelectedVariantSize < iterArea.getEstimatedRowCount()) {
            isVariantDeleted = true;
            ArrayList<String> deletedVariantList = new ArrayList<String>();
            ArrayList<String> deletedItemList = new ArrayList<String>();
            boolean isItemHaveAll = false;
            String currVariants = "";
            String currusername = getUserName();
            for (int i = 0; i < getSelectedVariants().size(); i++) {
                currVariants += "'" + getSelectedVariants().get(i).toString() + "'";
                if (i < getSelectedVariants().size() - 1)
                    currVariants += ", ";
            }
            try {
                //get newly removed variant and insert to arraylist
                {
                    PreparedStatement st = null;
                    if (actualSelectedVariantSize == 0)
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_VARIANT FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "'", 1);
                    else
                        st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT PROD_VARIANT FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + currusername + "' AND PROD_VARIANT NOT IN (" + currVariants + ")", 1);
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            while (rs.next())
                                deletedVariantList.add(rs.getString("PROD_VARIANT"));
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
                userAccessAM.getDBTransaction().commit();
                doCommit();
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            }
        }
        //---endf

        //Removing all variant rows
        for (Row r : iterArea.getAllRowsInRange()) {
            r.remove();
        }

        if (actualSelectedVariantSize > 0) {
            for (int i = 0; i < getSelectedVariants().size(); i++) {
                Row row = iterArea.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_VARIANT_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedVariants().get(i).equals(r.getAttribute("SetVariant"))) {
                        row.setAttribute("VariantDesc",
                                         r.getAttribute("SetVariantDesc"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("ProdVariant", getSelectedVariants().get(i));

                iterArea.getRowSetIterator().insertRow(row);
                iterArea.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
        //---f
        //check if (VARIANT) remain only 1 and if its 'ALL'/'BLANK' or not. if true then delete all child data then insert 'ALL'/'BLANK' to all child
        try {
            PreparedStatement st = userAccessAM.getDBTransaction().createPreparedStatement("SELECT (SELECT COUNT(1) FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + getUserName() + "'), PROD_VARIANT FROM APP_USER_PRODUK_VARIANT WHERE USER_NAME = '" + getUserName() + "'", 1);
            try {
                ResultSet rs = st.executeQuery();
                try {
                    rs.next();
                        if ((rs.getInt(1) == 1) && ((rs.getString("PROD_VARIANT").equals("ALL")) || (rs.getString("PROD_VARIANT").equals("PV0000")))) {
                            //delete all child data
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
