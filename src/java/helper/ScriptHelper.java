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
        query = quitarUltimaComa(query);
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
            query += c.getNombre() + "\t\t" + tabla.getName() + "." + c.getTipo() + "%type,\n";
        }
        if (query.charAt(query.length() - 1) == ',') {
            query = query.substring(0, query.length() - 2);
        }
        return query + ";";
    }

    public String scriptCrudTable(Tabla tabla) {
        String scriptPackage = "prompt\n"
                + "prompt PACKAGE: FA_QMAFA\n"
                + "prompt\n"
                + "\n"
                + "create or replace package XX_QXXX as\n"
                + "  procedure insertar_" + tabla.getName() + "   ( p_ty_" + tabla.getName() + " ty_" + tabla.getName() + ",\n"
                + "                              p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure actualizar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + " ty_" + tabla.getName() + ",\n"
                + "                               p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure eliminar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                             p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             );\n"
                + "  end FA_QMAFA;\n"
                + "  /";
        String scriptBody = "create or replace package body XX_QXXXX as\n"
                + " ----------------------------------------------------------------------------------------------------\n"
                + "  procedure insertar_" + tabla.getName() + "   ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                              p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptInsertTable(tabla)
                + "   p_ty_erro.cod_error := 'OK';\n"
                + "   p_ty_erro.msg_error := 'Inserción exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.insertar_" + tabla.getName() + ":'||sqlerrm, 1, 200);\n"
                + "  end insertar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure actualizar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                               p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptUpdateTable(tabla)
                + "    p_ty_erro.cod_error := 'OK';\n"
                + "    p_ty_erro.msg_error := 'Actualización exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.actualizar_" + tabla.getName() + ":'||sqlerrm, 1, 200);\n"
                + "  end actualizar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "  procedure eliminar_" + tabla.getName() + "  ( p_ty_" + tabla.getName() + "     ty_" + tabla.getName() + ",\n"
                + "                             p_ty_erro      out ge_qtipo.tr_error \n"
                + "                             )is\n"
                + "  begin\n"
                + scriptDeleteTable(tabla)
                + "   p_ty_erro.cod_error := 'OK';\n"
                + "   p_ty_erro.msg_error := 'Eliminación exitosa';\n"
                + "   --\n"
                + "  exception\n"
                + "    when others then\n"
                + "      p_ty_erro.cod_error := 'ORA'||ltrim(to_char(sqlcode, '000000'));\n"
                + "      p_ty_erro.msg_error := substr('Error en XX_QXXXX.eliminar_" + tabla.getName() + ":'||sqlerrm, 1, 200);  \n"
                + "  end eliminar_" + tabla.getName() + ";\n"
                + "  ----------------------------------------------------------------------------------------------------\n"
                + "End XX_QXXXX;\n"
                + "/";

        return scriptPackage + scriptBody;
    }

    public String scriptInsertTable(Tabla tabla) {
        String script = "insert into " + tabla.getName() + "( \n";
        for (Columna c : tabla.getColumnaCollection()) {
            script += c.getNombre() + ",\n";
        }
        script = quitarUltimaComa(script);
        script += ")\n"
                + "values\n"
                + "( ";
        for (Columna c : tabla.getColumnaCollection()) {
            script += "p_ty_" + tabla.getName() + "." + c.getNombre() + ",\n";
        }
        script = quitarUltimaComa(script);
        script = ");";
        return script;
    }

    public String scriptUpdateTable(Tabla tabla) {
        String script = "update " + tabla.getName() + " set\n";
        for (Columna c : tabla.getColumnaCollection()) {
            script += c.getNombre() + " = " + "p_ty_" + tabla.getName() + "." + c.getNombre() + ",\n";
        }
        script = quitarUltimaComa(script);
        script += "where \n"
                + "llave = p_ty_" + tabla.getName() + ".llave;";
        return script;
    }

    public String scriptDeleteTable(Tabla tabla) {
        String script = "delete " + tabla.getName() + " \n"
                + "where  llave  = p_ty_" + tabla.getName() + ".llave;";
        return "";
    }

    public String quitarUltimaComa(String query) {
        if (query.charAt(query.length() - 1) == ',') {
            query = query.substring(0, query.length() - 2);
        }
        return query;
    }
}
