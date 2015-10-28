package gumga.framework.core;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptEngine {

    private final static ScriptEngineManager engineManager= new ScriptEngineManager();


    public static Object eval(String script, Map<String, Object> objects) {
        try {
            ScriptEngine engine = engineManager.getEngineByName("JavaScript");
            if (objects != null) {
                for (String key : objects.keySet()) {
                    engine.put(key, objects.get(key));
                }
            }
            return engine.eval(script);
        } catch (ScriptException ex) {
            Logger.getLogger(JavaScriptEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
