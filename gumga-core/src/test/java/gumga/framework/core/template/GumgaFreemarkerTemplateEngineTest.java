/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core.template;

import gumga.framework.core.service.template.GumgaFreemarkerTemplateEngineService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author gyowannyqueiroz
 */
public class GumgaFreemarkerTemplateEngineTest {

    private GumgaFreemarkerTemplateEngineService templateEngine;

    private static final String OUTPUT_FOLDER = System.getProperty("user.dir") + "/templateEngineTestFolder";

    @Before
    public void startup() throws Exception {
        URL resourceUrl = getClass().getResource("/templates");
        Path resourcePath = Paths.get(resourceUrl.toURI());
        templateEngine = new GumgaFreemarkerTemplateEngineService(resourcePath.toString(), "UTF-8");
        templateEngine.init();
        createOutputFolder(OUTPUT_FOLDER);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testParse() throws Exception {
        Map<String, Object> values = new HashMap<>();
        String name = "Lara Croft";
        values.put("name", name);
        values.put("age", new Integer(35));
        String result = templateEngine.parse(values, "testTemplate.ftl");
        assertFalse(result == null || result.isEmpty());
        assertTrue(result.indexOf(name) > -1);
    }

    @Test
    public void testParseToFile() throws Exception {
        Map<String, Object> values = new HashMap<>();
        String name = "Lara Croft";
        values.put("name", name);
        values.put("age", new Integer(35));
        File file = new File(OUTPUT_FOLDER + "/testResult.txt");
        FileOutputStream fos = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(fos);
        try {
            templateEngine.parse(values, "testTemplate.ftl", writer);
        } finally {
            fos.close();
            writer.flush();
            writer.close();
        }
        assertTrue(file.exists());
    }

    private void createOutputFolder(String folder) {
        File file = new File(folder);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }
}
