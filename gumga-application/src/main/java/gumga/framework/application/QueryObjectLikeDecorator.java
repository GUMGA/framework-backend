/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import gumga.framework.core.QueryObject;

/**
 *
 * @author munif
 */
public class QueryObjectLikeDecorator {

    private QueryObject query;

    public QueryObjectLikeDecorator(QueryObject _query) {
        query = _query;
    }

    public QueryObject build() {
        StringBuilder where = new StringBuilder();
        if (query.getSearchFields() != null) {
            for (String param : query.getSearchFields()) {
                where.append("obj." + param + " like '%" + query.getQ() + "%'");
            }
            query.setAq(where.toString());
        }
        return query;
    }
}
