package com.example.UserService.service;

import com.example.UserService.config.Keycloaks;

import com.example.UserService.constant.RoleConstant;
import com.example.UserService.dto.request.RegisterReq;
import com.example.UserService.dto.request.RoleReq;
import com.example.UserService.entity.Role;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.Time;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakService {
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private  String serverUrl;

    @Value("${keycloak.username}")
    private  String username;
    @Value("${keycloak.password}")
    private  String password;
    @Value("${keycloak.resource}")
    private  String clientId;

    @Value("${keycloak.credentials.secret}")
    private  String clientSecret;

    public List<UserRepresentation> searchUserByEmail(String email) {
        Keycloak keycloak = Keycloaks.getKeycloakInstance();
        try {

            List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
            UsersResource usersResource = keycloak.realm(realm).users();
            return  usersResource.search(email);




        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (keycloak != null) {
                keycloak.close();
            }
        }
        return  null;
    }

    public  void  createUser(RegisterReq registerReq){

        String accessToken = null;
        String email = registerReq.getEmail();

        if (!ObjectUtils.isEmpty(searchUserByEmail(email))) {
            throw  new RuntimeException("Email của bạn đã được sử dụng");
        }

        Keycloak keycloak = Keycloaks.getKeycloakInstance();
        UsersResource userResource = keycloak.realm(realm).users();
        CredentialRepresentation passwordCredential = new CredentialRepresentation();
        passwordCredential.setTemporary(false);
        passwordCredential.setType(CredentialRepresentation.PASSWORD);
        passwordCredential.setValue(registerReq.getPassword());
        passwordCredential.setCreatedDate((long) Time.currentTime());

        UserRepresentation user = new UserRepresentation();
        user.setEmail(email);
        user.setUsername(email);
        user.setCredentials(Collections.singletonList(passwordCredential));
        user.setEnabled(true);
        Response res = userResource.create(user);
        int statusCode = res.getStatus();

        if (statusCode!= 201) {
            throw new BadRequestException("Không thể đăng kí được tài khoản. Vui lòng thử lại!");
        }
        if (statusCode == 201) {
            // Gán role cho người dùng
            String userId = res.getLocation().getPath().replaceAll(".*/([^/]+)", "$1");
            UserRepresentation createdUser = userResource.get(userId).toRepresentation();

            RoleMappingResource roleMappingResource = userResource.get(userId).roles();
            RoleRepresentation roleToAdd = keycloak.realm(realm).roles().get(registerReq.getRole()).toRepresentation();
            roleMappingResource.realmLevel().add(Collections.singletonList(roleToAdd));

        }

    }
    public void createRole(Role roleReq) {


            Keycloak keycloak = Keycloaks.getKeycloakInstance();


            RealmResource realmResource = keycloak.realm(realm);

            // Tạo một đối tượng RoleRepresentation và đặt tên và mô tả cho role
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleReq.getName());
            role.setDescription(roleReq.getDescription());

            realmResource.roles().create(role);


    }

    public  void  deleteUserByEmail(String email) {
        Keycloak keycloak = Keycloaks.getKeycloakInstance();
        UsersResource userResource = keycloak.realm(realm).users();
        UserRepresentation user = new UserRepresentation();
        userResource.get(email).remove();
    }

    public  String  getAccessToken(String username, String password) {
      Keycloak keycloak =  Keycloak.getInstance(
                serverUrl,
                realm,
              username,
              password,
                clientId,
                clientSecret);
        String accessToken = keycloak.tokenManager().getAccessTokenString();
        return accessToken;
    }

}

