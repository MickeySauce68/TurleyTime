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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerAppWebHandler extends AbstractHandler {
    private final static Logger logger = LoggerFactory.getLogger(CustomerAppWebHandler.class);
    
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
                <TITLE>Customer App</TITLE>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/purecss@3.0.0/build/pure-min.css" integrity="sha384-X38yfunGUhNzHpBaEBsWLO+A0HDYOQi8ufWDkZ0k9e0eXz/tH3II7uKZ9msv++Ls" crossorigin="anonymous">            </HEAD>
            <BODY>
                <FORM name="newcustomer" method="POST" action="/customer">
                    <LABEL>First Name:</LABEL>
                    <INPUT id="FirstName" name="FirstName" type="text" value=""><br/>
                    <LABEL>Last Name:</LABEL>
                    <INPUT id="LastName" name="LastName" type="text" value=""><br/>
                    <LABEL>Age:</LABEL>
                    <INPUT id="Age" name="Age" type="text" value=""><br/>
                    <INPUT name="Create Customer" type="submit" value="Create Customer">
                </FORM>
                <br/>
                %s
                <TABLE>
                    <TR>
                         <TH>First Name</TH>                                                  
                         <TH>Last Name</TH>                                                  
                         <TH>Age</TH>                                                  
                    </TR>                                              
                    %s                                                                                                        
                </TABLE>
            </BODY>
        </HTML>
                                 """;
    
    private static String errorTemplate = "<p>%s</p>";
    
    private static String tableRowTemplate = """
                    <TR>
                        <TD>%s</TD>                                                  
                        <TD>%s</TD>                                                  
                        <TD>%s</TD>                                                  
                    </TR>                                              
                                                                      """;
    
    private final List<Customer> customers;
    
    public CustomerAppWebHandler() {
        super();
        this.customers = new LinkedList<>();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if("GET".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod())) {
            String errorMessage = "";
            if("POST".equalsIgnoreCase(request.getMethod())) {
                if(request.getParameter("Create Customer") != null) {
                    try {
                        String firstName = request.getParameter("FirstName");
                        String lastName = request.getParameter("LastName");
                        int age = Integer.parseInt(request.getParameter("Age"));

                        if(firstName == null || firstName.isEmpty()) {
                            errorMessage = "First Name is required.";
                        }
                        else if(lastName == null || lastName.isEmpty()) { 
                            errorMessage = "Last Name is required.";
                        }
                        else if(age < 1 || age > 120) {
                            errorMessage = "Age must be an integer between 1 and 120.";
                        }
                        else {
                            Customer newCustomer = new Customer(firstName, lastName, age);
                            this.customers.add(newCustomer);
                        }
                    }
                    catch(NumberFormatException n) {
                        errorMessage = "Age must be an integer between 1 and 120.";
                    }
                }
            }
            
            // build table row for each customer
            StringBuilder customerEntries = new StringBuilder();
            for(Customer c : this.customers) {
                customerEntries.append(tableRowTemplate.formatted(c.getFirstName(), c.getLastName(), c.getAge()));
            }
            
            String html = null;
            
            if(errorMessage.isEmpty()) {
                // no error
                html = htmlTemplate.formatted("", customerEntries.toString());
            }
            else {
                // show error
                String error = errorTemplate.formatted(errorMessage);
                html = htmlTemplate.formatted(error, customerEntries.toString());
            }
            
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
