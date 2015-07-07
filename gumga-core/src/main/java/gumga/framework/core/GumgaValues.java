package gumga.framework.core;

public interface GumgaValues {

    default String getGumgaSecurityUrl() {
        return "http://www.gumga.com.br/security/publicoperations";
    }

    default long getDefaultTokenDuration() {
        return 30 * 60 * 1000;
    }

    default boolean isLogActive() {
        return true;
    }

    default String getUploadTempDir() {
        return System.getProperty("user.home").concat("/gumgafiles/tempupload");

    }

    default String getLogDir() {
        return System.getProperty("user.home").concat("/gumgafiles/logs");
    }


}
