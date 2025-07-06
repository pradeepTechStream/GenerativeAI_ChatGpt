package robot.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import robot.ai.dto.request.Message;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    private int index;
    private Message message;
}