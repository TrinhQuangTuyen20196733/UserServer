package com.example.UserService.config;


import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;

public class Keycloaks {
    @Value("${keycloak.realm}")
    private static String realm;
    @Value("${keycloak.auth-server-url}")
    private static String serverUrl;

    @Value("${keycloak.username}")
    private static String username;
    @Value("${keycloak.password}")
    private static String password;
    @Value("${keycloak.resource}")
    private static String clientId;

    @Value("${keycloak.credentials.secret}")
    private static String clientSecret;
    public static Keycloak getKeycloakInstance() {
        return Keycloak.getInstance(
                "http://localhost:7080",
                "master",
                "admin",
                "admin",
                "HustApp",
                "LGeqHvV7k08hF69m2aRgpe16XF1cvkgJ");
    }
}
