package gumga.framework.domain;

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
		this.parsers = new HashMap<>();	
		
		parsers.put(String.class, STRING_CRITERION_PARSER);
		parsers.put(Character.class, CHARACTER_CRITERION_PARSER);
		parsers.put(char.class, CHARACTER_CRITERION_PARSER);
		parsers.put(Boolean.class, BOOLEAN_CRITERION_PARSER);
		parsers.put(boolean.class, BOOLEAN_CRITERION_PARSER);
		parsers.put(Short.class, SHORT_CRITERION_PARSER);
		parsers.put(short.class, SHORT_CRITERION_PARSER);
		parsers.put(Integer.class, INTEGER_CRITERION_PARSER);
		parsers.put(int.class, INTEGER_CRITERION_PARSER);
		parsers.put(Long.class, LONG_CRITERION_PARSER);
		parsers.put(long.class, LONG_CRITERION_PARSER);
		parsers.put(Float.class, FLOAT_CRITERION_PARSER);
		parsers.put(float.class, FLOAT_CRITERION_PARSER);
		parsers.put(Double.class, DOUBLE_CRITERION_PARSER);
		parsers.put(double.class, DOUBLE_CRITERION_PARSER);
		parsers.put(BigInteger.class, BIGINTEGER_CRITERION_PARSER);
		parsers.put(BigDecimal.class, BIGDECIMAL_CRITERION_PARSER);
		parsers.put(Date.class, DATE_CRITERION_PARSER);
		
		this.queryObject.setQ(Normalizer.normalize(queryObject.getQ(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase());
	}
	
	public Criterion[] getCriterions(Class<?> clazz) {
		if (queryObject.getSearchFields() == null)
			return new Criterion[0];
		
		List<Criterion> criterions = new ArrayList<>();
		
		for (String field : queryObject.getSearchFields()) {
			try {
				criterions.add(createCriterion(field, queryObject.getQ(), clazz));
			} catch(Exception ex) {
				logger.debug(ex.getMessage());
			}
		}
		
		if (criterions.isEmpty())
			forceNoResults(criterions);
		
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
		
		if (parser == null)
			throw new UnsupportedDataTypeException(type.getName());
		
		return parser.parse(field, value);
	}
	
	protected void forceNoResults(List<Criterion> criterions) {
		criterions.add(Restrictions.sqlRestriction("(1=0)"));
	}
	
	protected interface CriterionParser {
		Criterion parse(String field, String value);
	}
	
	protected static final CriterionParser STRING_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			String[] chain = field.split("\\.");
			
			if (chain.length > 1) 
				return Restrictions.like(field, value, MatchMode.START).ignoreCase();
			
			String ignoraAcentos = "upper(translate({alias}."+field+",'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç','AAAAAAAAEEEEIIOOOOOOUUUUCC')) like (?)";
			return Restrictions.sqlRestriction(ignoraAcentos, value+"%", StandardBasicTypes.STRING);
		}
	};
	
	protected static final CriterionParser CHARACTER_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			if (value.length() == 1)
				return eq(field, new Character(value.charAt(0)));
			
			throw new IllegalArgumentException(value);
		}
	};
	
	protected static final CriterionParser BOOLEAN_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			value = value.toLowerCase();
			
			if (value.equals("sim")) value = "true";
			else if (value.equals("não") || value.equals("nao")) value = "false"; 
			
			if (value.equals("true") || value.equals("false"))
				return eq(field, new Boolean(value));
			
			throw new IllegalArgumentException(value);
		}
	};
	
	protected static final CriterionParser SHORT_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new Short(value));
		}
	};
	
	protected static final CriterionParser INTEGER_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new Integer(value));
		}
	};
	
	protected static final CriterionParser LONG_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new Long(value));
		}
	};
	
	protected static final CriterionParser FLOAT_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new Float(value));
		}
	};
	
	protected static final CriterionParser DOUBLE_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new Double(value));
		}
	};
	
	protected static final CriterionParser BIGINTEGER_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new BigInteger(value));
		}
	};
	
	protected static final CriterionParser BIGDECIMAL_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			return eq(field, new BigDecimal(value));
		}
	};
	
	protected static final CriterionParser DATE_CRITERION_PARSER = new CriterionParser() {
		@Override public Criterion parse(String field, String value) {
			try {
				 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			     
				 Date minDate = formatter.parse(value);
				 Date maxDate = new Date(minDate.getTime() + DAYS.toMillis(1));
				
				 Conjunction and = Restrictions.conjunction();
				 and.add( Restrictions.ge(field, minDate) );
			     and.add( Restrictions.lt(field, maxDate) ); 
				
				return and;
			} catch (ParseException e) {
				throw new IllegalArgumentException(value);
			}
		}
	};
	
}
