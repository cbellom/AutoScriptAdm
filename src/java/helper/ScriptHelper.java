/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import entity.Columna;
import entity.Tabla;

/**
 *
 * @author Camilo
 */
public class ScriptHelper {

    public ScriptHelper() {
    }

    public String scriptCreateTable(Tabla tabla) {
        String query = "Prompt\n"
                + "Prompt Creando tabla " + tabla.getName() + "\n"
                + "Promp\n"
                + "--";
        query = "--\n"
                + "create table " + tabla.getName() + "(\n";
        for (Columna c : tabla.getColumnaCollection()) {
            if (c.isRequerido()) {
                query += c.getNombre() + "\t\t" + c.getTipo() + "\t\t nn_" + tabla.getName() + "_" + c.getNombre().substring(c.getNombre().indexOf("_") + 1) + " not null,\n";
            } else {
                query += c.getNombre() + "\t\t " + c.getTipo() + ",\n";
            }
        }
        if (query.charAt(query.length() - 1) == ',') {
            query = query.substring(0, query.length() - 2);
        }
        query += ")\n"
                + "tablespace ts_dsfi\n"
                + "logging\n"
                + "nocompress\n"
                + "nocache\n"
                + "noparallel\n"
                + "monitoring;";
        return query;
    }

    public String scriptVersion(String descripcion) {
        String version = "--\n"
                + "-- #VERSION:0000001000\n"
                + "--\n"
                + "-- HISTORIAL DE CAMBIOS\n"
                + "--\n"
                + "-- Versión     GAP           Solicitud        Fecha        Realizó        Descripción\n"
                + "-- =========== ============= ================ ============ ============== ==============================================================================\n"
                + "-- 1000        XXXXX         XXXXXXXXX        XX/XX/XXXX                  . " + descripcion + "\n"
                + "-- =========== ============= ================ ============ ============== ==============================================================================\n"
                + "--\n";
        return version;
    }

    public String scriptTypeTable(Tabla tabla) {
        String query = "Prompt\n"
                + "Prompt Creando type para la tabla " + tabla.getName() + "\n"
                + "Promp\n"
                + "--";
        query = "--\n"
                + "type ty_" + tabla.getName() + " is record (\n";
        for (Columna c : tabla.getColumnaCollection()) {
            query += c.getNombre() + "\t\t" + tabla.getName()+"."+c.getTipo() + "%type,\n";
        }
        if (query.charAt(query.length() - 1) == ',') {
            query = query.substring(0, query.length() - 2);
        }
        return query+";";
    }
}
