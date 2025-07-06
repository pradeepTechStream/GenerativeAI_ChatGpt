package robot.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    private String model; // version of the model we’ll send requests to
    private List<Message> messages; //messages are the prompts to the model.
    private double temperature;//controls the randomness of the response.
    private int max_tokens; //decide the response length. less value gives small response and big value give big response.
    private double top_p;


    public ChatRequest(String model, String prompt) {
        this.model = model;

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}
