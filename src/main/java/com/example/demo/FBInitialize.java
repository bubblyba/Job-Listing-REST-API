package com.example.demo;

import com.google.auth.oauth2.GoogleCredentials;
        import com.google.firebase.FirebaseApp;
        import com.google.firebase.FirebaseOptions;
        import org.springframework.stereotype.Service;

        import javax.annotation.PostConstruct;
        import java.io.FileInputStream;

@Service
public class FBInitialize {

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/joblisting-f64bd-firebase-adminsdk-rhkbz-b580299dc0.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://chatapp-e6e15.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}