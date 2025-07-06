package robot.ai.controller;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import robot.ai.dto.response.ChatRoomResponse;
import robot.ai.service.ChatRoomService;

import java.io.IOException;
import java.util.*;
import com.google.gson.Gson;
import okhttp3.*;


@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @GetMapping("/chat-room")
    public ResponseEntity<ChatRoomResponse> connectWithChatGptAI(@RequestParam String question){
        ChatRoomResponse response=chatRoomService.getResponseFromChatGpt(question);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> describeImage(@RequestParam("file") MultipartFile file,@RequestParam("question") String question) {
        return  chatRoomService.describeImage(file,question);
    }

    @GetMapping("/prompt")
    public ResponseEntity<String> getPromptResponse(@RequestParam String prompt){
        String response=chatRoomService.getPromptResponse(prompt);
        return ResponseEntity.ok(response);
    }

}
