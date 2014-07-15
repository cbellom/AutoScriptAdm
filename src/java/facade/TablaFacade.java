/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Tabla;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author itc
 */
@Stateless
public class TablaFacade extends AbstractFacade<Tabla> {
    @PersistenceContext(unitName = "AutoScriptPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TablaFacade() {
        super(Tabla.class);
    }
    
}
