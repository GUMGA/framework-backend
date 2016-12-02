/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api.voice;

import gumga.framework.application.nlp.GumgaNLP;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.lucene.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/api")
public class VoiceReceiverAPI {

    @Autowired(required = false)
    private GumgaNLP gumgaNLP;

    private final String[] CONTEXT = {"Gumga", "Munif"};

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @RequestMapping(value = "/voice", method = RequestMethod.POST)
    public Map recebeSom(HttpServletRequest httpRequest) throws IOException {
        String som = convertStreamToString(httpRequest.getInputStream());
        System.out.println("----->" + som);
        Map<String, Object> problemas = new HashMap<>();
        try {
            som = som.replaceFirst("data:audio/wav;base64,", "");
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> config = new HashMap<>();

            config.put("encoding", "LINEAR16");
            config.put("sampleRate", "44000");
            config.put("languageCode", "pt-BR");
            config.put("maxAlternatives", "1");
            config.put("profanityFilter", "false");
            Map<String, Object> context = new HashMap<>();
            context.put("phrases", CONTEXT);
            config.put("speechContext", context); //EXPLORAR DEPOIS COM FRASES PARA "AJUDAR" o reconhecedor
            Map<String, Object> audio = new HashMap<>();
            audio.put("content", som);
            Map<String, Object> request = new HashMap<>();
            request.put("config", config);
            request.put("audio", audio);
            Map resposta = restTemplate.postForObject("https://speech.googleapis.com/v1beta1/speech:syncrecognize?key=AIzaSyC7E4dZ4EneRmSzVMs2qhyJYGoTK49FCYM", request, Map.class);
            analiseSintatica(resposta);
            return resposta;
        } catch (Exception ex) {
            problemas.put("exception", ex);
        }
        return problemas;
    }

    private void analiseSintatica(Map resposta) {
        try {
            List<Map> resultados = (List<Map>) resposta.get("results");
            for (Map resultado : resultados) {
                List<Map> alternativas = (List<Map>) resultado.get("alternatives");
                for (Map alternativa : alternativas) {
                    String texto = alternativa.get("transcript").toString();
                    System.out.println("--------->" + texto);
                    System.out.println("--------->" + gumgaNLP.createObjectsFromDocument(texto, "criar"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
