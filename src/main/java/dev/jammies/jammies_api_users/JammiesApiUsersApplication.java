package dev.jammies.jammies_api_users;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JammiesApiUsersApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
                System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
        SpringApplication.run(JammiesApiUsersApplication.class, args);

    }
}
