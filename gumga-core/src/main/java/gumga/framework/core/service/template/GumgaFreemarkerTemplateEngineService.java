/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core.service.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import gumga.framework.core.exception.TemplateEngineException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * Template engine service implementation using the Freemarker engine.
 * http://freemarker.org
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
public class GumgaFreemarkerTemplateEngineService implements
        GumgaTemplateEngineService<Map<String, Object>, Writer, String> {

    private static Configuration cfg;

    private String templateFolder;
    private String defaultEncoding;

    public GumgaFreemarkerTemplateEngineService(String templateFolder,
            String defaultEncoding) {
        this.templateFolder = templateFolder;
        this.defaultEncoding = defaultEncoding;
    }

    @Override
    public void setTemplateFolder(String folder) {
        templateFolder = folder;
    }

    @Override
    public void setDefaultEncoding(String encoding) {
        defaultEncoding = encoding;
    }

    @Override
    public void parse(Map<String, Object> values, String template, Writer out) throws TemplateEngineException {
        try {
            Template t = cfg.getTemplate(template);
            t.process(values, out);
        } catch (IOException | TemplateException ex) {
            throw new TemplateEngineException(String.format("An error occurred while parsing the template - %s", template), ex);
        }
    }

    @Override
    public String parse(Map<String, Object> values, String template) throws TemplateEngineException {
        StringWriter out = new StringWriter();
        parse(values, template, out);
        return out.toString();
    }

    @Override
    public void init() throws TemplateEngineException {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setDirectoryForTemplateLoading(new File(this.templateFolder));
            cfg.setDefaultEncoding(this.defaultEncoding);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException ex) {
            throw new TemplateEngineException("An error occurred while initializating the template engine", ex);
        }
    }
}
