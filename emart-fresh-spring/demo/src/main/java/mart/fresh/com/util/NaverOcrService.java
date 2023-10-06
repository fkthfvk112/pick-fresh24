package mart.fresh.com.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NaverOcrService {

//    @Value("${naver.ocr.client.id}")
//    private String clientId;

    @Value("${naver.ocr.client.secret}")
    private String clientSecret;


    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String callNaverCloudOcr(MultipartFile file) {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.add("X-NCP-APIGW-API-KEY", clientSecret);
        
        HttpEntity<MultipartFile> entity = new HttpEntity<>(file, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://kg12yznacy.apigw.ntruss.com/custom/v1/25346/6cbeb9687f5478931aba3f93a7a1a5131cc8d77795494fd09b8908abeb2880b0/infer", HttpMethod.POST, entity, String.class);


        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to call OCR: " + response.getStatusCode());
        }
    }
}
