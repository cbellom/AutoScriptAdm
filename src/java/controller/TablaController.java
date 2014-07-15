package controller;

import entity.Tabla;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import entity.Columna;
import facade.TablaFacade;
import helper.ScriptHelper;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

@Named("tablaController")
@SessionScoped
public class TablaController implements Serializable {

    private Tabla current;
    private DataModel items = null;
    @EJB
    private facade.TablaFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private ScriptHelper scriptHelper = new ScriptHelper();

    public TablaController() {
    }

    public Tabla getSelected() {
        if (current == null) {
            current = new Tabla();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TablaFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Tabla) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Tabla();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("properties/Bundle").getString("TablaCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("properties/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Tabla) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("properties/Bundle").getString("TablaUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("properties/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Tabla) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("properties/Bundle").getString("TablaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("properties/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Tabla getTabla(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Tabla.class)
    public static class TablaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TablaController controller = (TablaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tablaController");
            return controller.getTabla(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Tabla) {
                Tabla o = (Tabla) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tabla.class.getName());
            }
        }
    }

    public void generateCreateFile() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        String fileName = current.getName()+".sql";
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        PrintWriter os = null;
        
        String encabezado = scriptHelper.scriptVersion("Se crea la tabla "+current.getName());
        String query = scriptHelper.scriptCreateTable(current);
        try {
            os = response.getWriter();
            os.println(encabezado + query);
            os.flush();
            os.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void generateTypeFile() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        String fileName = current.getName()+".sql";
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        PrintWriter os = null;
        
        String query = scriptHelper.scriptTypeTable(current);
        try {
            os = response.getWriter();
            os.println(query);
            os.flush();
            os.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void generateCrudFile() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        String fileName = current.getName()+"_type.sql";
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        PrintWriter os = null;        
        String encabezado = scriptHelper.scriptVersion("Se crea la tabla "+current.getName());
        String query = scriptHelper.scriptCreateTable(current);
        try {
            os = response.getWriter();
            os.println(encabezado + query);
            os.flush();
            os.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
