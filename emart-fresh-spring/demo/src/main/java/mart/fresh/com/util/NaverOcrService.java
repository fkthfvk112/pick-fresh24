package mart.fresh.com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class NaverOcrService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NaverOcrService.class);
	private static final String API_URL = "https://kg12yznacy.apigw.ntruss.com/custom/v1/25346/6cbeb9687f5478931aba3f93a7a1a5131cc8d77795494fd09b8908abeb2880b0/infer";
	private static final int SUCCESS_RESPONSE = 200; // HTTP OK

	@Value("${naver.ocr.client.secret}")
	private String clientSecret;

	public boolean callNaverCloudOcr(MultipartFile file) {

		try {
			URL url = new URL(API_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setReadTimeout(30000);
			con.setRequestMethod("POST");
			String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			con.setRequestProperty("X-OCR-SECRET", clientSecret);

			JSONObject json = new JSONObject();
			json.put("version", "V1");
			json.put("requestId", UUID.randomUUID().toString());
			json.put("timestamp", System.currentTimeMillis());

			// 변경: images 배열에 JSON 데이터를 추가
			JSONArray images = new JSONArray();
			JSONObject image = new JSONObject();
			image.put("format", "png");
			image.put("name", "demo");
			images.put(image);
			json.put("images", images);

			String postParams = json.toString();

			con.connect();
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			byte[] fileBytes = file.getBytes();
			writeMultiPart(wr, postParams, fileBytes, boundary);
			wr.close();
			if (con.getResponseCode() == SUCCESS_RESPONSE) {
				String response = readResponse(con.getInputStream());
				System.out.println("response : " + response + "response.toString" + response.toString());
				ObjectMapper mapper = new ObjectMapper();
				try {
					JsonNode rootNode = mapper.readTree(response);
				} catch (IOException e) {
					LOGGER.error("Error parsing OCR response", e);
				}
				LOGGER.info("OCR Response for file {}: {}", file.getOriginalFilename(), response);

				return response.contains("사업자등록증");
			} else {
				String errorResponse = readResponse(con.getErrorStream());
				LOGGER.error("Error during OCR call for file {}: {}", file.getOriginalFilename(), errorResponse);
				return false;
			}

		} catch (IOException e) {
			LOGGER.error("Exception during Naver Cloud OCR call for file " + file.getOriginalFilename(), e);
			return false;
		} finally {
			closeResources(null, null);
		}
	}

	private String readResponse(InputStream is) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	private void closeResources(DataOutputStream wr, HttpURLConnection con) {
		try {
			if (wr != null)
				wr.close();
			if (con != null)
				con.disconnect();
		} catch (IOException e) {
			LOGGER.error("Error closing resources", e);
		}
	}

	private void writeMultiPart(DataOutputStream wr, String postParams, byte[] fileBytes, String boundary)
			throws IOException {
		// 텍스트 파트 추가
		wr.writeBytes("--" + boundary + "\r\n");
		wr.writeBytes("Content-Disposition: form-data; name=\"message\"\r\n\r\n");
		wr.writeBytes(postParams + "\r\n");

		// 파일 파트 추가
		wr.writeBytes("--" + boundary + "\r\n");
		wr.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\r\n");
		wr.writeBytes("Content-Type: image/png\r\n\r\n");
		wr.write(fileBytes);

		// 마지막 바운더리 추가
		wr.writeBytes("\r\n--" + boundary + "--\r\n");
		wr.flush();
	}

}