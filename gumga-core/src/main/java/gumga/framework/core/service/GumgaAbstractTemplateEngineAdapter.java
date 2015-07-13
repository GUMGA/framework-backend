/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core.service;

import java.io.Writer;
import java.util.Map;

/**
 * This is the basic generic inferring for the template engine types. If some of
 * the types differ from the ones specified here it's recommended to implement
 * the GumgaTemplateEngineService interface directly.
 *
 * @author gyowannyqueiroz
 * @since jul/2015
 */
public abstract class GumgaAbstractTemplateEngineAdapter implements
        GumgaTemplateEngineService<Map<String, Object>, Writer, String> {

}
