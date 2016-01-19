package gumga.framework.domain;

import gumga.framework.core.QueryObject;
import gumga.framework.core.utils.ReflectionUtils;

import java.text.Normalizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.activation.UnsupportedDataTypeException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
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
            } catch (ParseException ex) {
                throw new RuntimeException("Problem creating creterion.Cannot parse field "+field);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Problem creating creterion.Number format problem in field  "+field);
            } catch (UnsupportedDataTypeException ex) {
                throw new RuntimeException("Problem creating creterion.Unsupported data type in field "+field);
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
