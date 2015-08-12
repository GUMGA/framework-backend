/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import java.text.Normalizer;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gyowanny
 */
public abstract class AbstractStringCriterionParser implements CriterionParser{
    private static final Logger logger = LoggerFactory.getLogger(AbstractStringCriterionParser.class);
    
    public static final String SOURCE_CHARS = "âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç";
    public static final String TARGET_CHARS = "AAAAAAAAEEEEIIOOOOOOUUUUCC";
    
    @Override
    public Criterion parse(String field, String value) {

        value = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        String[] chain = field.split("\\.");

        if (chain.length > 1) {
            return Restrictions.like(field, value, MatchMode.ANYWHERE).ignoreCase();
        }
        //String ignoraAcentos = "upper({alias}." + field + ") like (?)";

        //ignoraAcentos = "upper(translate({alias}." + field + ",'"+SOURCE_CHARS+"','"+TARGET_CHARS+"')) like (?)"; //NAO FUNCIONA NO MYSQL
        String soundex = createSoundexExpression(field, value);
        
        if (logger.isDebugEnabled()){
            logger.debug("Soundex search: "+soundex);
        }
        
        return Restrictions.sqlRestriction(soundex, "%" + value + "%", StandardBasicTypes.STRING);
    }
    
    public abstract String createSoundexExpression(String field, String value);

    
    /* DATABASE SPECIFIC IMPLEMENTATIONS */
    
    /**
     * Oracle String Criterion Parser
     */
    public static final AbstractStringCriterionParser ORACLE_STRING_CRITERION_PARSER = new AbstractStringCriterionParser() {

        @Override
        public String createSoundexExpression(String field, String value) {
            return "upper(translate({alias}." + field + ",'"+SOURCE_CHARS+"','"+TARGET_CHARS+"')) like (?)";
        }
    };
    
    /**
     * MYSQL String Criterion Parser
     */
    public static final AbstractStringCriterionParser MYSQL_STRING_CRITERION_PARSER = new AbstractStringCriterionParser() {
        
        @Override
        public String createSoundexExpression(String field, String value) {
            return "SOUNDEX("+field+") LIKE SOUNDEX( (?) )";
        }
    };
    
    public static final AbstractStringCriterionParser POSTGRESQL_STRING_CRITERION_PARSER = new AbstractStringCriterionParser() {

        @Override
        public String createSoundexExpression(String field, String value) {
            return "dmetaphone("+field+") = dmetaphone( (?) )";
        }
    };
}
