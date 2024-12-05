package WebProgramming;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortenerAppWebHandler extends AbstractHandler {
    private final static Logger logger = LoggerFactory.getLogger(ShortenerAppWebHandler.class);
    public ShortenerAppWebHandler() {
        super();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info(target); // log target so we can see it in the output
        /*
            Target is a relative path to the base URL assigned to the handler.
            For example, http://localhost:8080/short causes target to be "/".
            Why "/"?  Because nothing comes after /short.
            Another example, http://localhost:8080/short/abc causes target to be "/abc".
            Why?  Because it comes after /short => /short/abc => "/abc"
        */
          
        if("/best".equalsIgnoreCase(target)) {
            response.sendRedirect("https://malonepioneers.com/");
            baseRequest.setHandled(true);
        }
        else if("/worst".equalsIgnoreCase(target)) {
            response.sendRedirect("https://athletics.walsh.edu/");
            baseRequest.setHandled(true);
        }
        else {
            response.sendRedirect("https://www.google.com");
            baseRequest.setHandled(true);
        }
    }
}
