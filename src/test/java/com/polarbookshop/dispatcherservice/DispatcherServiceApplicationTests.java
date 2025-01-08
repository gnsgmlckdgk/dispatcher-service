package com.polarbookshop.dispatcherservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.function.Function;


@FunctionalSpringBootTest
class DispatcherServiceApplicationTests {

	@Autowired
	private FunctionCatalog catalog;

	@Test
	void packAndLabelOrder() {
		Function<OrderAcceptedMessage, Flux<OrderDispatcherMessage>>
				packAndLabel = catalog.lookup(
						Function.class,
				"pack|label");	// FunctionCatalog 로 부터 합성 함수를 가져온다.
		long orderId = 121;

		StepVerifier.create(packAndLabel.apply(
				new OrderAcceptedMessage(orderId)	// 함수에 대한 입력인 OrderAcceptedMessage 를 정의한다.
		)).expectNextMatches(dispatchedOrder ->		// 함수의 출력이 OrderDispatcherMessage 임을 확인한다.
				dispatchedOrder.equals(new OrderDispatcherMessage(orderId))).verifyComplete();
	}


	@Test
	void contextLoads() {
	}

}
