/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

import org.hibernate.criterion.Criterion;

/**
 *
 * @author munif
 */
public interface CriterionParser {
    Criterion parse(String field, String value);
}
