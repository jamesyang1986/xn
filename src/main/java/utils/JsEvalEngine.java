package utils;

import java.util.Iterator;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsEvalEngine {
    private static ScriptEngine engine;
    static {
        engine = new ScriptEngineManager().getEngineByName("JavaScript");
    }

    public static String eval(String script, Map<String, String> paramMap)
            throws ScriptException {
        if (paramMap != null) {
            Iterator<String> keyIt = paramMap.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                script = script.replaceAll(key, paramMap.get(key));
            }
        }

        try {
            return engine.eval(script).toString();
        } catch (ScriptException e) {
            throw e;
        }
    }

}
