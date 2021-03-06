package nl.qsight.utils;

import nl.qsight.links.fields.RenderLink;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static nl.qsight.common.Constants.INPUT_MARKER;
import static org.junit.Assert.assertEquals;

public class TestConfigUtils {

    @Test
    public void testUnfoldInput() throws ParseException, IOException {
        String strConfig = "{\"chain\": [\"example\"], \"parsers\": {\"example\": {\"input\": \"{{var1}}\"}}}";
        JSONParser parseJSON = new JSONParser();
        JSONObject configJSON = (JSONObject) parseJSON.parse(strConfig);
        Map<String, Object> config = JSONUtils.JSONToMap(configJSON);
        Map<String, Object> parserConfig = ConfigUtils.unfoldInput(config);

        assertTrue(parserConfig.containsKey("chain"));
        assertTrue(parserConfig.get("chain") instanceof List);
        List chain = (List) parserConfig.get("chain");
        assertTrue(chain.size() == 2);
        assertTrue(parserConfig.get("parsers") instanceof Map);
        Map linksConfig = (Map) parserConfig.get("parsers");

        assertTrue(chain.get(0) instanceof String);
        String autolink = (String) chain.get(0);
        assertTrue(linksConfig.containsKey(autolink));
        assertTrue(linksConfig.get(autolink) instanceof Map);
        Map configAutolink = (Map) linksConfig.get(autolink);

        assertTrue(configAutolink.containsKey("class"));
        assertTrue(configAutolink.containsKey("template"));
        assertTrue(configAutolink.containsKey("output"));

        assertTrue(configAutolink.get("class") instanceof String);
        assertTrue(configAutolink.get("template") instanceof String);
        assertTrue(configAutolink.get("output") instanceof String);

        assertEquals(RenderLink.class.getName(), configAutolink.get("class"));
        assertEquals("{{var1}}", configAutolink.get("template"));
        assertEquals(INPUT_MARKER, configAutolink.get("output"));
    }

}
