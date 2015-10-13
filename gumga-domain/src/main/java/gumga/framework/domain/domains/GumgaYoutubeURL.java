package gumga.framework.domain.domains;

import com.google.common.base.Preconditions;
import gumga.framework.domain.domains.youtube.GumgaShortYoutubeURL;
import gumga.framework.domain.domains.youtube.GumgaWebsiteYoutubeURL;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL do Youtube
 */
public interface GumgaYoutubeURL {

    /**
     * Recupera o id do video presente na URL
     *
     * @return O identificador do video
     */
    String getVideoId();


    /**
     * Cria um YoutubeURL de uma String
     *
     * @param urlString A url do video
     * @return A url do video no formato GumgaYoutubeURL
     */
    public static GumgaYoutubeURL fromString(String urlString) {
        Preconditions.checkNotNull(urlString, "É necessário informar uma URL.");
        try {
            URL url = new URL(urlString);

            if (GumgaWebsiteYoutubeURL.accept(url)) {
                return new GumgaWebsiteYoutubeURL(url);

            } else if (GumgaShortYoutubeURL.accept(url)){
                return new GumgaShortYoutubeURL(url);

            } else {
                throw new IllegalArgumentException("A URL informada não é uma URL do Youtube válida.");
            }

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL Inválida.", e);
        }
    }

}
