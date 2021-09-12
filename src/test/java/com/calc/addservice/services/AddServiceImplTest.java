package com.calc.addservice.services;

import static org.junit.Assert.assertEquals;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class AddServiceImplTest {

	private AddServiceImpl addServiceImpl;

	@BeforeEach
	public void setup() {
		this.addServiceImpl = new AddServiceImpl();
	}

	public static Stream<Arguments> data() {
		return Stream.of(
            Arguments.of(5,2,7),
            Arguments.of(1,6,7),
            Arguments.of(2,3,5),
            Arguments.of(1,2,3)
        );
	}

	@ParameterizedTest
	@MethodSource("data")
	public void shouldAdd(double x, double y, double expected) {
		assertEquals("message", expected, addServiceImpl.add(x, y), 0);
	}
}
