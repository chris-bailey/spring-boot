/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.test.json;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import org.springframework.core.ResolvableType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JacksonTester}.
 *
 * @author Phillip Webb
 */
public class JacksonTesterTests extends AbstractJsonMarshalTesterTests {

	@Test
	public void initFieldsWhenTestIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("TestInstance must not be null");
		JacksonTester.initFields(null, new ObjectMapper());
	}

	@Test
	public void initFieldsWhenObjectMapperIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Marshaller must not be null");
		JacksonTester.initFields(new InitFieldsTestClass(), (ObjectMapper) null);
	}

	@Test
	public void initFieldsShouldSetNullFields() {
		InitFieldsTestClass test = new InitFieldsTestClass();
		assertThat(test.test).isNull();
		assertThat(test.base).isNull();
		JacksonTester.initFields(test, new ObjectMapper());
		assertThat(test.test).isNotNull();
		assertThat(test.base).isNotNull();
		assertThat(test.test.getType().resolve()).isEqualTo(List.class);
		assertThat(test.test.getType().resolveGeneric()).isEqualTo(ExampleObject.class);
	}

	@Override
	protected AbstractJsonMarshalTester<Object> createTester(Class<?> resourceLoadClass,
			ResolvableType type) {
		return new JacksonTester<Object>(resourceLoadClass, type, new ObjectMapper());
	}

	static abstract class InitFieldsBaseClass {

		public JacksonTester<ExampleObject> base;

		public JacksonTester<ExampleObject> baseSet = new JacksonTester<ExampleObject>(
				InitFieldsBaseClass.class, ResolvableType.forClass(ExampleObject.class),
				new ObjectMapper());

	}

	static class InitFieldsTestClass extends InitFieldsBaseClass {

		public JacksonTester<List<ExampleObject>> test;

		public JacksonTester<ExampleObject> testSet = new JacksonTester<ExampleObject>(
				InitFieldsBaseClass.class, ResolvableType.forClass(ExampleObject.class),
				new ObjectMapper());

	}

}
