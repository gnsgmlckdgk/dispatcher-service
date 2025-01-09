package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)   // 테스트 빌더 설정
public class FunctionStreamIntegrationTests {

    @Autowired
    private InputDestination input; // 입력 바인딩 packlabel-in-0 을 나타낸다.

    @Autowired
    private OutputDestination output;   // 출력 바인딩 packlabel-in-0 을 나타낸다.

    @Autowired
    private ObjectMapper objectMapper;  // JSON 메시지 페이로드를 자바 객체로 역직렬화하기 위해 젝슨을 사용한다.

    @Test
    void whenOrderAccecptedThenDispatched() throws IOException {
        long orderId = 121;
        Message<OrderAcceptedMessage> inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
        Message<OrderDispatcherMessage> expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatcherMessage(orderId)).build();

        this.input.send(inputMessage);  // 입력 채널로 메시지를 보낸다.
        assertThat(objectMapper.readValue(output.receive().getPayload(),
                OrderDispatcherMessage.class)).isEqualTo(expectedOutputMessage.getPayload());   // 출력 채널로부터 메시지를 받아서 확인한다.
    }

}
