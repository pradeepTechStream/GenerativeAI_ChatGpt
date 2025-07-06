package robot.ai.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import robot.ai.dto.request.ChatRequest;
import robot.ai.dto.response.ChatResponse;
import robot.ai.dto.response.ChatRoomResponse;
import robot.ai.service.ChatRoomService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;

import java.util.*;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Value("${openai.key}")
    private String openaiApiKey;

    @Value("${openai.url}")
    private String openaiApiUrl;

    @Value("${openai.model}")
    private String model;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplateWithConfig;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatRoomResponse getResponseFromChatGpt(String question) {
        ChatRoomResponse chatRoomResponse=new ChatRoomResponse();
        chatRoomResponse.setQuestion(question);
        try {
            // Construct the request body
            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o");

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "user", "content", question)
            );
            body.put("messages", messages);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            // Send the request
            ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String ans=root.path("choices").get(0).path("message").path("content").asText();
                chatRoomResponse.setAns(ans);
                return  chatRoomResponse;
            } else {
                chatRoomResponse.setAns("Error: " + response.getStatusCode());
                return  chatRoomResponse;

            }
        } catch (Exception e) {
            chatRoomResponse.setAns("Exception: " + e.getMessage());
            return  chatRoomResponse;
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> describeImage(MultipartFile file, String question) {
        try {
            byte[] imageBytes = IOUtils.toByteArray(file.getInputStream());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");

            List<Map<String, Object>> content = new ArrayList<>();
            content.add(Map.of("type", "text", "text", question));
            content.add(Map.of("type", "image_url", "image_url", Map.of("url", "data:image/jpeg;base64," + base64Image)));

            requestBody.put("messages", List.of(
                    Map.of("role", "user", "content", content)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String description = root.path("choices").get(0).path("message").path("content").asText();
                return ResponseEntity.ok(Map.of("description", description));
            } else {
                return ResponseEntity.status(response.getStatusCode())
                        .body(Map.of("error", "OpenAI API error: " + response.getStatusCode()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Exception: " + e.getMessage()));
        }
    }

    @Override
    public String getPromptResponse(String prompt) {

        ChatRequest request = new ChatRequest(model, prompt);
        request.setTop_p(1);
        request.setTemperature(1);
        request.setMax_tokens(200);
        ChatResponse response = restTemplateWithConfig.postForObject(openaiApiUrl, request, ChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}
