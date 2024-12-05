package WebProgramming;

import java.util.Date;

public class UserSession {
    private final String ID;
    private final String username;
    private final Date loginTimestamp;
    
    public UserSession(String ID, String username) {
        super();
        this.ID = ID;
        this.username = username;
        this.loginTimestamp = new Date();
    }
    
    public String getID() { return this.ID; }
    public String getUsername() { return this.username; }
    public Date getLoginTimestamp() { return this.loginTimestamp; }
}
