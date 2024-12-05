package WebProgramming;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class RedirectHandler extends AbstractHandler {
    private final String path;
    
    public RedirectHandler(String path) {
        super();
        this.path = path;
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect(path);
        baseRequest.setHandled(true);
    }
    
}
