package WebProgramming;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginAppWebHandler extends AbstractHandler {
    private final static Logger logger = LoggerFactory.getLogger(LoginAppWebHandler.class);
    private final static Map<String, String> usernamePassword;

    private final String sessionCookieName = "SESSION_ID";
    private final Map<String, UserSession> userSessions;

    // Initialize username/password database
    static {
        usernamePassword = new TreeMap<>();
        usernamePassword.put("jdoe", "password");
        usernamePassword.put("ksmith", "qwerty");
    }
    
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
                <TITLE>Login App</TITLE>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" integrity="sha384-X38yfunGUhNzHpBaEBsWLO+A0HDYOQi8ufWDkZ0k9e0eXz/tH3II7uKZ9msv++Ls" crossorigin="anonymous">            </HEAD>
            <BODY>
                %s                          
            </BODY>
        </HTML>
                                 """;

    private static String formTemplate = """
                <FORM name="login" method="POST" action="/login">
                    <LABEL>Username:</LABEL>
                    <INPUT id="Username" name="Username" type="text" value=""><br/>
                    <LABEL>Password:</LABEL>
                    <INPUT id="Password" name="Password" type="password" value=""><br/>
                    <INPUT name="Login" type="submit" value="Login">
                </FORM>
                %s
                                                                                  """;
    private static String errorTemplate = "<p>%s</p>";
    
    private static String infoTemplate = """
                <FORM name="login" method="POST" action="/login">
                    <INPUT name="Logout" type="submit" value="Logout">
                </FORM>
                <TABLE>
                    <TR>
                         <TH>Session ID</TH>                                                  
                         <TH>Username</TH>                                                  
                         <TH>Login Date</TH>                                                  
                    </TR>                                              
                    <TR>
                        <TD>%s</TD>                                                  
                        <TD>%s</TD>                                                  
                        <TD>%s</TD>                                                  
                   </TR>                                              
                </TABLE>
                                                                                  """;
    
    public LoginAppWebHandler() {
        super();
        this.userSessions = new TreeMap<>();
    }
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if("GET".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod())) {
            
            // Check for session cookie and try to lookup session if present
            String sessionID = null;
            UserSession userSession = null;
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for(Cookie cookie : cookies) {
                    if(sessionCookieName.equalsIgnoreCase(cookie.getName())) {
                        sessionID = cookie.getValue();
                        if(userSessions.containsKey(sessionID)) {
                            userSession = userSessions.get(sessionID);
                        }
                        break;
                    }
                }
            }
            
            String errorMessage = "";
            if(userSession == null) {
                // not logged in
                
                if("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("Login") != null) {
                    try {
                        String userName = request.getParameter("Username");
                        String password = request.getParameter("Password");

                        if(userName == null || userName.isEmpty()) {
                            errorMessage = "Username is required.";
                        }
                        else if(password == null || password.isEmpty()) {
                            errorMessage = "Password is required.";
                        }
                        else if(usernamePassword.containsKey(userName.toLowerCase()) && usernamePassword.get(userName.toLowerCase()).equals(password)) {
                            // Login successful, create user session
                            sessionID = UUID.randomUUID().toString();
                            userSession = new UserSession(sessionID, userName);
                            userSessions.put(sessionID, userSession);
                            Cookie newCookie = new Cookie(sessionCookieName, sessionID);
                            newCookie.setPath("/login");
                            response.addCookie(newCookie);
                        }
                        else {
                            errorMessage = "Unknown username or password.";
                        }
                    }
                    catch(NumberFormatException n) {
                        errorMessage = "Age must be an integer between 1 and 120.";
                    }
                }
            }
            else {
                // logged in
                if("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("Logout") != null) {
                    // Logout
                    Cookie oldCookie = new Cookie(sessionCookieName, sessionID);
                    oldCookie.setPath("/login");
                    oldCookie.setMaxAge(0); // delete cookie
                    response.addCookie(oldCookie);
                    userSession = null;
                }
            }
            
            if(userSession != null) {
                String info = infoTemplate.formatted(userSession.getID(), userSession.getUsername(), userSession.getLoginTimestamp());
                String html = htmlTemplate.formatted(info);
                sendHTMLResponse(target, baseRequest, request, response, html);
            }
            else {
                // HTTP GET
                String form = null;
                if(errorMessage.isEmpty()) {
                    form = formTemplate.formatted(""); // no error
                }
                else {
                    String error = errorTemplate.formatted(errorMessage);
                    form = formTemplate.formatted(error); // error
                }
                String html = htmlTemplate.formatted(form);
                sendHTMLResponse(target, baseRequest, request, response, html);
            }
            
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
