package robot.ai.service;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import robot.ai.dto.response.ChatRoomResponse;

import java.util.Map;

public interface ChatRoomService {

    ChatRoomResponse getResponseFromChatGpt(String question);

    ResponseEntity<Map<String, String>> describeImage(MultipartFile file, String question);

    String getPromptResponse(String prompt);
}
