/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core.service.template;

import gumga.framework.core.exception.TemplateEngineException;

/**
 * Interface describing a template engine service. It contains the basic methods
 * to merge a template with the given data.
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
public interface GumgaTemplateEngineService<INPUT, OUTPUT_WRITER, OUTPUT> {

    /**
     * Init method that must be called by default after instantiating the
     * concrete implementations. Useful to place the template engine
     * configuration.
     *
     * @throws TemplateEngineException
     */
    public void init() throws TemplateEngineException;

    /**
     * Sets the folder where the templates are stored
     *
     * @param folder
     */
    public void setTemplateFolder(String folder);

    /**
     * Sets the parser engine's default encoding
     *
     * @param encoding
     */
    public void setDefaultEncoding(String encoding);

    /**
     * Merges the given values to the given template and save the result in the
     * specified writer.
     *
     * @param values The values to be merged with the template
     * @param template The template to be parsed
     * @param writer The object responsible to save the output file. It may be
     * an OutputStream for example.
     */
    public void parse(INPUT values, String template, OUTPUT_WRITER out) throws TemplateEngineException;

    /**
     * Merges the given values to the given template and returns the result of
     * the type specified by the OUTPUT.
     *
     * @param values The values to be merged with the template
     * @param template The template to be parsed
     * @return The result specified by the OUTPUT generic type
     */
    public OUTPUT parse(INPUT values, String template) throws TemplateEngineException;
}
