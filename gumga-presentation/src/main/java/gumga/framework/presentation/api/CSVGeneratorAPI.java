/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.application.GumgaService;
import gumga.framework.application.GumgaTempFileService;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author munif
 */
public interface CSVGeneratorAPI {

    static final String CSV_SEPARATOR = ";";
    static final String CSV_LINE_DELIMITER = "\r\n";
    static final Logger log=LoggerFactory.getLogger(CSVGeneratorAPI.class);

    GumgaService getGumgaService();

    @org.springframework.transaction.annotation.Transactional
    @ApiOperation(value = "csv", notes = "Gera resultado da pesquisa em um arquivo CSV.")
    @RequestMapping(value = "/csv", method = RequestMethod.GET) 
    @ResponseBody
    default void geraCSV(HttpServletResponse response) throws IOException {
        StringBuilder sb = new StringBuilder();
        QueryObject qo = new QueryObject();
        SearchResult pesquisa = getGumgaService().pesquisa(qo);
        if (pesquisa.getValues().isEmpty()) {
            response.getWriter().write("NO DATA");
            return;
        }
        sb.append(classToCsvTitle(pesquisa.getValues().get(0).getClass()));
        for (Object obj : pesquisa.getValues()) {
            sb.append(objectToCsvLine(obj));
        }
        response.getWriter().write(sb.toString());
    }

    public static String classToCsvTitle(Class clazz) {
        StringBuilder sb = new StringBuilder();
        for (Field f : getAllAtributes(clazz)) {
            sb.append(f.getName());
            sb.append(CSV_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(CSV_LINE_DELIMITER);
        return sb.toString();
    }

    public static StringBuilder objectToCsvLine(Object gm) {
        StringBuilder sb = new StringBuilder();
        for (Field f : getAllAtributes(gm.getClass())) {
            try {
                f.setAccessible(true);
                if (f.get(gm) != null) {
                    Field idField = getIdField(f.getType());

                    if (idField != null) {
                        idField.setAccessible(true);
                        Object idValue = idField.get(f.get(gm));
                        sb.append(idValue.toString());
                    } else {
                        sb.append(f.get(gm).toString());
                    }
                }
            } catch (Exception ex) {
                log.error("erro ao criar linha csv", ex);
            }
            sb.append(CSV_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(CSV_LINE_DELIMITER);
        return sb;
    }

    public static Field getIdField(Class clazz) {
        for (Field f : getAllAtributes(clazz)) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        return null;
    }

    public static List<Field> getAllAtributes(Class clazz) {
        List<Field> fields = new ArrayList<>();
        Class superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            fields.addAll(getAllAtributes(clazz.getSuperclass()));
        }
        for (Field f : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                fields.add(f);
            }
        }
        return fields;
    }

}
