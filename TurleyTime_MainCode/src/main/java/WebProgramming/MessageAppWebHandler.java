package WebProgramming;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageAppWebHandler extends AbstractHandler {
    private final static Logger logger = LoggerFactory.getLogger(MessageAppWebHandler.class);
    
    private static String unsupportedMethodTemplate = """
        <!doctype html>
        <HTML>
            <HEAD>
                <TITLE>Unsupported HTTP method</TITLE>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" integrity="sha384-X38yfunGUhNzHpBaEBsWLO+A0HDYOQi8ufWDkZ0k9e0eXz/tH3II7uKZ9msv++Ls" crossorigin="anonymous">            </HEAD>
            <BODY>
                <p>Unsupported HTTP method</p>
            </BODY>
        </HTML>
                                                                                                            """;
    
    private static String htmlTemplate = """
        <!doctype html>
        <HTML>
            <HEAD>
                <TITLE>Message App</TITLE>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" integrity="sha384-X38yfunGUhNzHpBaEBsWLO+A0HDYOQi8ufWDkZ0k9e0eXz/tH3II7uKZ9msv++Ls" crossorigin="anonymous">            </HEAD>
            <BODY>
                <FORM name="message" method="POST" action="/message">
                    <LABEL>Message:</LABEL>
                    <INPUT id="Message" name="Message" type="text" value=""><br/>
                    <INPUT name="Submit" type="submit" value="Submit">
                </FORM>
                <br/>
                <p>%s</p>
            </BODY>
        </HTML>
                                 """;
    
    public MessageAppWebHandler() {
        super();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if("GET".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod())) {
            String html = null;
            String message = null;

            if("POST".equalsIgnoreCase(request.getMethod())) {
                if(request.getParameter("Submit") != null) {
                    message = request.getParameter("Message");
                }
            }
            
            if(message == null) message = "";
            
            /*
                The Encode.forHtml() method of the OWasp Encode library makes the input
                safe to inject into the HTML by applying the required HTML escape sequences
                to insecure characters.
                For example, put <h1>Hello</h1> in the message box and submit.  Then view the
                source and you will see the escaped characters (greater than and less than).
            */
            html = htmlTemplate.formatted(Encode.forHtml(message));
            sendHTMLResponse(target, baseRequest, request, response, html);
        }
        else {
            // Unsupported HTTP method
            sendHTMLResponse(target, baseRequest, request, response, unsupportedMethodTemplate);
        }
    }
    
    public void sendHTMLResponse(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response, String html) throws IOException {
        // set response parameters
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(html.length());
        PrintWriter t_PrintWriter = response.getWriter();
        t_PrintWriter.print(html);
        t_PrintWriter.flush();
        baseRequest.setHandled(true);
    }
}
