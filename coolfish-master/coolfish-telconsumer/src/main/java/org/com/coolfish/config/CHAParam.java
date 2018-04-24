package org.com.coolfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CHAParam {
    @Value("${coolfish.cha.url}")
    private String url;

    @Value("${coolfish.cha.user_id}")
    private String user_id;

    @Value("${coolfish.cha.password}")
    private String password;

    @Value("${coolfish.cha.keys}")
    private String keys;

    public String getUrl() {
        return url;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPassword() {
        return password;
    }

    public String getkey1() {
        return keys.substring(0, 3);
    }

    public String getkey2() {
        return keys.substring(3, 6);
    }

    public String getkey3() {
        return keys.substring(6, 9);
    }
}
