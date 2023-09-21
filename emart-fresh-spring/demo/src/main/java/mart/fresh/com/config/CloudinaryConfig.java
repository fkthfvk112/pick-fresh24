package mart.fresh.com.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dczgjybvl",
            "api_key", "281892245863443",
            "api_secret", "3IcdwlxaxGEIcOvYWNE5q5wvSq8"
        ));
        return cloudinary;
    }
}