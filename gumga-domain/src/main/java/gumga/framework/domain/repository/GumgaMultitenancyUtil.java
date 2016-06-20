package gumga.framework.domain.repository;

import gumga.framework.core.GumgaThreadScope;
import gumga.framework.domain.GumgaMultitenancy;
import gumga.framework.domain.GumgaMultitenancyPolicy;

public class GumgaMultitenancyUtil {

    public static String getMultitenancyPattern(GumgaMultitenancy tenacy) {
        GumgaMultitenancyPolicy policy = tenacy.policy();
        String oiPattern = GumgaThreadScope.organizationCode.get();
        if (oiPattern == null) {
            oiPattern = "";
        }
        switch (policy) {
            case ORGANIZATIONAL:
                int firstPointPosition = oiPattern.indexOf('.');
                oiPattern = oiPattern.substring(0, firstPointPosition + 1);
                break;
            case TOP_DOWN:
            default:
            //Usa o OI como está no threadscope

        }
        System.out.println("-----Util---GumgaMultitenancyPattern----->" + oiPattern);
        return oiPattern;
    }

}
