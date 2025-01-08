package com.polarbookshop.dispatcherservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;


@Configuration
public class DispatchingFunctions {

    private static final Logger log = LoggerFactory.getLogger(DispatchingFunctions.class);

    /**
     * 주문 포장
     * @return
     */
    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {    // 주문을 포장하는 비즈니스 로직을 구현하는 함수
        return orderAcceptedMessage -> {    // OrderAcceptedMessage 객체를 입력으로 받는다.
            log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId();  // 주문의 식별자(Long 타입)을 출력으로 반환한다.
        };
    }

    /**
     * 주문 레이블링(배송)
     * @return
     */
    @Bean
    public Function<Flux<Long>, Flux<OrderDispatcherMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            log.info("The order with id {} is labeled.", orderId);
            return new OrderDispatcherMessage(orderId);
        });
    }

}
