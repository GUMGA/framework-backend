package gumga.framework.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        return "http://www.gumga.com.br/security-api/publicoperations";
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
     * Para não logar no banco
     *
     * @return
     */
    default boolean isLogRequestOnConsole() {
        return false;
    }

    /**
     * Lista de paths de urls que serão ignoradas do log
     *
     * @return
     */
    default Set<String> getUrlsToNotLog() {
        return new HashSet<String>();
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
