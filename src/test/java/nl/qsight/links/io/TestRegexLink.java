package nl.qsight.links.io;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRegexLink {

    private RegexLink link;

    @Before
    public void setUp() {
        this.link = new RegexLink();
    }

    @After
    public void tearDown() {
        this.link = null;
    }

    @Test
    public void testRegexLink() {
        String input = "01-01-1970";
        Map<String, Object> selector = new HashMap<>();
        selector.put("day", "1");
        selector.put("month", "2");
        selector.put("year", "3");
        selector.put("all", "0");

        this.link.setSelector(selector);
        this.link.setPattern("([0-9]{1,2})-([0-9]{1,2})-([0-9]{4})");

        Object outputObject = this.link.parseInputField(input);
        assertTrue(outputObject instanceof JSONObject);
        JSONObject output = (JSONObject) outputObject;
        assertEquals(4, output.size());
        assertTrue(output.containsKey("all"));
        assertTrue(output.containsKey("month"));
        assertTrue(output.containsKey("year"));
        assertTrue(output.containsKey("day"));
        assertEquals("01", output.get("day"));
        assertEquals("01", output.get("month"));
        assertEquals("1970", output.get("year"));
        assertEquals("01-01-1970", output.get("all"));
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalIndex() {
        String input = "test_input";
        Map<String, Object> selector = new HashMap<>();
        selector.put("test_key", "illegal_index");
        this.link.setSelector(selector);
        this.link.setPattern("(.*)");
        this.link.parseInputField(input);
    }

}
