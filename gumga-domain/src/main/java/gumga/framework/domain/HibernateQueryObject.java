package gumga.framework.domain;

import gumga.framework.core.GumgaValues;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.hibernate.criterion.Restrictions.eq;
import gumga.framework.core.QueryObject;
import gumga.framework.core.utils.ReflectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.UnsupportedDataTypeException;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateQueryObject {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected final QueryObject queryObject;

    protected final Map<Class<?>, CriterionParser> parsers;


    public HibernateQueryObject(QueryObject queryObject) {
       
        this.queryObject = queryObject;

        this.parsers = GumgaQueryParserProvider.defaultMap;
        
        if (null==GumgaQueryParserProvider.defaultMap){
            throw new RuntimeException("GumgaQueryParserProvider.defaultMap must be set in Application configuration");
        }
        
        this.queryObject.setQ(Normalizer.normalize(queryObject.getQ(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase());
    }

    public Criterion[] getCriterions(Class<?> clazz) {
        if (!queryObject.isValid()) {
            return new Criterion[0];
        }

        List<Criterion> criterions = new ArrayList<>();

        for (String field : queryObject.getSearchFields()) {
            try {
                criterions.add(createCriterion(field, queryObject.getQ(), clazz));
            } catch (Exception ex) {
                logger.debug(ex.getMessage());
            }
        }

        if (criterions.isEmpty()) {
            forceNoResults(criterions);
        }

        return criterions.toArray(new Criterion[criterions.size()]);
    }

    private Criterion createCriterion(String field, String value, Class<?> clazz) throws ParseException, NumberFormatException, UnsupportedDataTypeException {
        String[] chain = field.split("\\.");
        Class<?> type = null;

        if (chain.length > 1) {
            Class<?> superType = ReflectionUtils.findField(clazz, chain[0]).getType();
            type = ReflectionUtils.findField(superType, chain[1]).getType();
        } else {
            type = ReflectionUtils.findField(clazz, field).getType();
        }

        CriterionParser parser = parsers.get(type);

        if (parser == null) {
            throw new UnsupportedDataTypeException(type.getName());
        }

        return parser.parse(field, value);
    }

    protected void forceNoResults(List<Criterion> criterions) {
        criterions.add(Restrictions.sqlRestriction("(1=0)"));
    }

}
