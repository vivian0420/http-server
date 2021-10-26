package cs601.project3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestTest {

    @Test
    public void testEmptyLine() {
        ServerRequest req = new ServerRequest("", null, "");
        assertTrue(req.is400());
    }

    @Test
    public void testMissingPartRequestLine() {
        ServerRequest req = new ServerRequest("GET HTTP", null, "");
        assertTrue(req.is400());
    }

    @Test
    public void testValidRequestLine() {
        ServerRequest req = new ServerRequest("GET / HTTP", null, "");
        assertFalse(req.is400());

    }

    @Test
    public void testLowercaseMethod() {
        ServerRequest req = new ServerRequest("get / HTTP", null, "");
        assertTrue(req.is400());
    }

    @Test
    public void testIs405() {
        ServerRequest req  = new ServerRequest("PUT / HTTP", null, "");
        assertTrue(req.is405());
    }

    @Test
    public void testValidRequestMethod() {
        ServerRequest req  = new ServerRequest("POST / HTTP", null, "");
        assertFalse(req.is405());
    }



}
