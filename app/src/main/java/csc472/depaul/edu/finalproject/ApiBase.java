package csc472.depaul.edu.finalproject;

public class ApiBase {
    protected static String clientId;
    protected static String secret;


    ApiBase(String clientId, String secret) {
        if (this.clientId == null || this.secret == null) {
            this.clientId = clientId;
            this.secret = secret;
        }
    }
}
