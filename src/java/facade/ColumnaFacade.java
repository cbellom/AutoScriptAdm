/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Columna;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author itc
 */
@Stateless
public class ColumnaFacade extends AbstractFacade<Columna> {
    @PersistenceContext(unitName = "AutoScriptPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ColumnaFacade() {
        super(Columna.class);
    }
    
    public Collection<Columna> buscarTodosPorTabla (java.lang.Integer id){
        Collection<Columna> columnas =findAll();
        for(Columna c:columnas){
            if(c.getTablaId().getId()!=id){
                columnas.remove(c);
            }
        }
        return columnas;
        
    }    
}
