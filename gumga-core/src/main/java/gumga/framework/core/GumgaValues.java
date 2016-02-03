package gumga.framework.core;

/**
 * Interface que deve ser implementada para alterar comportamentos padrão do
 * framework.
 *
 * @author munif
 */
public interface GumgaValues {

    /**
     *
     * @return Tempo padrão de expiração do ticket para troca de senha.
     */
    default long getDefaultExpirationForChangePassword() {
        return 3600l * 1000 * 6;
    }

    /**
     * @return URL do sistema de segurança
     */
    default String getGumgaSecurityPage() {
        return "http://www.gumga.com.br/security";
    }

    /**
     * @return URL da api do sistema de segurança
     */
    default String getGumgaSecurityUrl() {
        return "http://www.gumga.com.br/security/publicoperations";
    }

    /**
     * @return Tempo padrão de expiração do token curto neste software em
     * segundos.
     */
    default long getDefaultTokenDuration() {
        return 30 * 60 * 1000;
    }

    /**
     * @return se o log está ativo
     */
    default boolean isLogActive() {
        return true;
    }

    /**
     * @return diretório para armazenar aquivos vindos do upload
     */
    default String getUploadTempDir() {
        return System.getProperty("user.home").concat("/gumgafiles/tempupload");

    }

    /**
     * @return diretório para armazenar aquivos de log
     */
    default String getLogDir() {
        return System.getProperty("user.home").concat("/gumgafiles/logs");
    }

    /**
     * @return diretório para armazenar templates como email
     */
    default String getTemplatesFolder() {
        return System.getProperty("user.home").concat("/gumgafiles/templates");
    }
}
