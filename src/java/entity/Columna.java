/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author itc
 */
@Entity
@Table(name = "COLUMNA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Columna.findAll", query = "SELECT c FROM Columna c"),
    @NamedQuery(name = "Columna.findById", query = "SELECT c FROM Columna c WHERE c.id = :id"),
    @NamedQuery(name = "Columna.findByNombre", query = "SELECT c FROM Columna c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Columna.findByTipo", query = "SELECT c FROM Columna c WHERE c.tipo = :tipo")})
public class Columna implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 255)
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "TABLA_ID", referencedColumnName = "ID")
    @ManyToOne
    private Tabla tablaId;

    public Columna() {
    }

    public Columna(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Tabla getTablaId() {
        return tablaId;
    }

    public void setTablaId(Tabla tablaId) {
        this.tablaId = tablaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Columna)) {
            return false;
        }
        Columna other = (Columna) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Columna[ id=" + id + " ]";
    }
    
}
