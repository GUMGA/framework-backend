package gumga.framework.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gumga.framework.core.QueryObjectElement;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.AbstractStringCriterionParser;
import gumga.framework.domain.GumgaQueryParserProvider;
import gumga.framework.domain.domains.GumgaMoney;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

public class GumgaGenericRepositoryHelper {

    private static Map<GumgaHqlEntry, GumgaHqlElement> hqlConverter;

    private GumgaGenericRepositoryHelper() {
    }

    public static Map<GumgaHqlEntry, GumgaHqlElement> getHqlConverter() {
        if (hqlConverter == null) {
            hqlConverter = new HashMap<>();

            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "eq"), new GumgaHqlElement("='", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "ne"), new GumgaHqlElement("!='", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "ge"), new GumgaHqlElement(">='", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "le"), new GumgaHqlElement("<='", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "gt"), new GumgaHqlElement(">'", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "lt"), new GumgaHqlElement("<'", "'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "contains"), new GumgaHqlElement(" like '%", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "not_contains"), new GumgaHqlElement(" not like '%", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "starts_with"), new GumgaHqlElement(" like '", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.DEFAULT, "ends_with"), new GumgaHqlElement(" like '%", "'"));

            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.TEXT, "contains"), new GumgaHqlElement(" like '%", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.TEXT, "not_contains"), new GumgaHqlElement(" not like '%", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.TEXT, "starts_with"), new GumgaHqlElement(" like '", "%'"));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.TEXT, "ends_with"), new GumgaHqlElement(" like '%", "'"));

            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "eq"), new GumgaHqlElement("=", ""));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "ne"), new GumgaHqlElement("!=", ""));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "ge"), new GumgaHqlElement(">=", ""));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "le"), new GumgaHqlElement("<=", ""));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "gt"), new GumgaHqlElement(">", ""));
            hqlConverter.put(new GumgaHqlEntry(GumgaFieldStereotype.NUMBER, "lt"), new GumgaHqlElement("<", ""));

        }
        return hqlConverter;
    }

    public static GumgaFieldStereotype getFieldStereotype(Class type) {
        if (java.lang.String.class.equals(type)) {
            return GumgaFieldStereotype.TEXT;
        }
        if (java.lang.Byte.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Double.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Float.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Integer.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Long.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Number.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (java.lang.Short.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }
        if (GumgaMoney.class.equals(type)) {
            return GumgaFieldStereotype.NUMBER;
        }

        return GumgaFieldStereotype.DEFAULT;
    }

    public static List<QueryObjectElement> qoeFromString(String s) {
        List<QueryObjectElement> aRetornar = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode mainNode = mapper.readTree(s);
            Iterator<JsonNode> elements = mainNode.elements();
            while (elements.hasNext()) {
                QueryObjectElement qoe = new QueryObjectElement();
                aRetornar.add(qoe);
                JsonNode node = elements.next();
                if (node.has("attribute")) {
                    qoe.setAttribute(node.get("attribute").get("name").asText());
                }
                if (node.has("hql")) {
                    qoe.setHql(node.get("hql").get("hql").asText());
                }
                qoe.setValue(node.get("value").asText());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Problem with Jackson "+ex.toString());
        }
        return aRetornar;

    }

    public static String hqlFromQoes(JpaEntityInformation entityInformation, List<QueryObjectElement> qoes) {
        String aRetornar = "";
        try {
            for (QueryObjectElement qoe : qoes) {
                if ("NO_ATTRIBUTE".equals(qoe.getAttribute())) {
                    aRetornar += " " + qoe.getValue() + " ";
                } else {
                    Class type = String.class;
                    Field field = ReflectionUtils.findField(entityInformation.getJavaType(), qoe.getAttribute());
                    if (field != null) {
                        type = field.getType();

                    }
                    GumgaFieldStereotype fieldStereotype = getFieldStereotype(type);
                    if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getOracleLikeMap()) && fieldStereotype == GumgaFieldStereotype.TEXT) {
                        aRetornar += "upper(translate(obj." + qoe.getAttribute()
                                + ",'" + AbstractStringCriterionParser.SOURCE_CHARS + "','" + AbstractStringCriterionParser.TARGET_CHARS + "'"
                                + ")" + ")";
                    } else {
                        aRetornar += "obj." + qoe.getAttribute();
                    }

                    GumgaHqlEntry het = new GumgaHqlEntry(fieldStereotype, qoe.getHql());
                    GumgaHqlElement hel = getHqlConverter().get(het);
                    aRetornar += hel.before + removeAcentos(qoe.getValue()).toUpperCase() + hel.after;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aRetornar;
    }

    public static String removeAcentos(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        return str;

    }
    
}
