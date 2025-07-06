package robot.ai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String role; //specifies the sender of the message. It will be “user” in requests and “assistant” in the response.
    private String content; //actual message

}
