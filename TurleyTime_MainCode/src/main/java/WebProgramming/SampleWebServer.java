package WebProgramming;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleWebServer {
    private final static Logger logger = LoggerFactory.getLogger(SampleWebServer.class);
    private boolean init;
    String resourcePath;
    private Server server;
    
    public SampleWebServer(String resourcePath) {
        super();
        this.init = false;
        this.resourcePath = resourcePath;
    }
    
    public void init() {
        if(this.init) return;
        
        this.server = new Server();
        
        // Create connectors for server (listen on which port)
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] {connector});
        
        // create collection of context handlers (map URLs to servlets)
        ContextHandlerCollection httpContexts = new ContextHandlerCollection();
        server.setHandler(httpContexts);

        // Web Root Handler
        // Redirect Handler (/ -> /resources)
        {
            ContextHandler t_ContextHandler = new ContextHandler();
            t_ContextHandler.setAllowNullPathInfo(true); // prevents 302 redirect (/bob => /bob/)
            t_ContextHandler.setContextPath("/");
            t_ContextHandler.setHandler(new RedirectHandler("/customer"));
            httpContexts.addHandler(t_ContextHandler);
        }
        
        // ResourceHandler - static files (html, js, css, jpg/png)
        {
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setWelcomeFiles(new String[] { "index.html" });
            resourceHandler.setResourceBase(this.resourcePath);
            
            ContextHandler t_ContextHandler = new ContextHandler();
            t_ContextHandler.setAllowNullPathInfo(true);
            t_ContextHandler.setContextPath("/resources");
            t_ContextHandler.setHandler(resourceHandler);
            httpContexts.addHandler(t_ContextHandler);
        }

        // Customer App
        {
            ContextHandler contextHandler = new ContextHandler();
            contextHandler.setAllowNullPathInfo(true); // prevents 302 redirect (/bob => /bob/)
            contextHandler.setContextPath("/customer");
            contextHandler.setHandler(new CustomerAppWebHandler());
            
            httpContexts.addHandler(contextHandler);
        }

        // Login App
        {
            ContextHandler contextHandler = new ContextHandler();
            contextHandler.setAllowNullPathInfo(true); // prevents 302 redirect (/bob => /bob/)
            contextHandler.setContextPath("/login");
            contextHandler.setHandler(new LoginAppWebHandler());
            
            httpContexts.addHandler(contextHandler);
        }

        // Shortener App
        {
            ContextHandler contextHandler = new ContextHandler();
            contextHandler.setAllowNullPathInfo(true); // prevents 302 redirect (/bob => /bob/)
            contextHandler.setContextPath("/short");
            contextHandler.setHandler(new ShortenerAppWebHandler());
            
            httpContexts.addHandler(contextHandler);
        }

        // Reflective App
        {
            ContextHandler contextHandler = new ContextHandler();
            contextHandler.setAllowNullPathInfo(true); // prevents 302 redirect (/bob => /bob/)
            contextHandler.setContextPath("/message");
            contextHandler.setHandler(new MessageAppWebHandler());
            
            httpContexts.addHandler(contextHandler);
        }

        try {
            server.start();

            // Add a hook to shutdown web server when Java application is terminated
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try { server.stop(); } catch(Exception e) { logger.error("Shutdown Hook - Http Server Shutdown", e); }
                    System.out.println("Shutdown Hook Completed.");
                    System.out.flush();
                }
            }));
        }
        catch(Exception e) {
            Throwable t_Throwable = e;
            do { logger.error(t_Throwable.toString()); } while ((t_Throwable = t_Throwable.getCause()) != null);
        }
    }
}
