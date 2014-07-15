/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author itc
 */
@Entity
@Table(name = "TABLA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tabla.findAll", query = "SELECT t FROM Tabla t"),
    @NamedQuery(name = "Tabla.findById", query = "SELECT t FROM Tabla t WHERE t.id = :id"),
    @NamedQuery(name = "Tabla.findByName", query = "SELECT t FROM Tabla t WHERE t.name = :name")})
public class Tabla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "tablaId")
    private Collection<Columna> columnaCollection;

    public Tabla() {
    }

    public Tabla(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Columna> getColumnaCollection() {
        return columnaCollection;
    }

    public void setColumnaCollection(Collection<Columna> columnaCollection) {
        this.columnaCollection = columnaCollection;
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
        if (!(object instanceof Tabla)) {
            return false;
        }
        Tabla other = (Tabla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Tabla[ id=" + id + " ]";
    }
    
}
