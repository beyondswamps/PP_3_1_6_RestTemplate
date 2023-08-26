package net.problem.resttemplateproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.problem.resttemplateproject.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Application {
    public static void main(String[] args) throws Exception {
        String url = "http://94.198.50.185:7081/api/users";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String cookie = responseEntity.
                getHeaders().
                getFirst(HttpHeaders.SET_COOKIE);

        ObjectMapper objectMapper = new ObjectMapper();
        User[] users = objectMapper.readValue(responseEntity.getBody(), User[].class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        User user = new User(3L,
                "James",
                "Brown",
                Byte.parseByte("3"));

        HttpEntity<User> request = new HttpEntity<>(
                user,
                headers);

        String firstSecretPart = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        ).getBody();

        user.setName("Thomas");
        user.setLastName("Shelby");

        String secondSecretPart = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        ).getBody();

        String thirdSecretPart = restTemplate.exchange(
                String.format("%s/%s", url, user.getId()),
                HttpMethod.DELETE,
                request,
                String.class
        ).getBody();

        System.out.println(String.format("%s%s%s", firstSecretPart, secondSecretPart, thirdSecretPart));
    }
}
