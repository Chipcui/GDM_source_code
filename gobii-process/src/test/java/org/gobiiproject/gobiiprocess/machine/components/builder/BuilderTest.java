package org.gobiiproject.gobiiprocess.machine.components.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.builder.Builder;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class BuilderTest {

	@Test
	public void testBuilder() throws Exception {

		String json = slurpResource("builder_test.json");
		JsonNode jsonNode = new ObjectMapper().readTree(json);

		Builder<TestState> builder = new Builder<>();

		Map<String, Pipeline<TestState>> pipelines = builder.build(jsonNode);

		Pipeline<TestState> pipeline = pipelines.get("test");

		assertNotNull(pipeline);
		assertFalse(pipeline.getSteps().isEmpty());

		Step<TestState> step0 = pipeline.getSteps().get(0);

		assertNotNull(step0.getTransition());
		assertTrue(step0.getTransition() instanceof TestTransition);

		assertNotNull(step0.getSideEffects());
		assertFalse(step0.getSideEffects().isEmpty());
		assertEquals(1, step0.getSideEffects().size());
		assertNotNull(step0.getSideEffects().get(0));
		assertTrue(step0.getSideEffects().get(0) instanceof TestSideEffect);
		assertEquals("test", ((TestSideEffect) step0.getSideEffects().get(0)).value);

		assertNotNull(step0.getValidation());
		assertTrue(step0.getValidation() instanceof TestValidator);

		assertNotNull(step0.getFailure());
		assertTrue(step0.getFailure() instanceof TestFailure);

		assertNotNull(step0.getPrototypes());
		assertFalse(step0.getPrototypes().isEmpty());
		assertEquals(1, step0.getPrototypes().size());
		assertNotNull(step0.getPrototypes().get(0));
		assertNotNull(step0.getPrototypes().get(0).getSideEffects());
		assertEquals(step0.getPrototypes().get(0).getSideEffects().size(), 2);

		assertNotNull(step0.getPrototypes().get(0).getSideEffects().get(0));
		assertTrue(step0.getPrototypes().get(0).getSideEffects().get(0) instanceof TestSideEffect);
		assertEquals("", ((TestSideEffect) step0.getPrototypes().get(0).getSideEffects().get(0)).value);

		assertNotNull(step0.getPrototypes().get(0).getSideEffects().get(1));
		assertTrue(step0.getPrototypes().get(0).getSideEffects().get(1) instanceof TestSideEffect);
		assertEquals("test", ((TestSideEffect) step0.getPrototypes().get(0).getSideEffects().get(1)).value);

		assertNotNull(pipeline.getPrototypes());
		assertFalse(pipeline.getPrototypes().isEmpty());
		assertEquals(1, pipeline.getPrototypes().size());
		assertNotNull(pipeline.getPrototypes().get(0));
		assertNotNull(pipeline.getPrototypes().get(0).getSideEffects());
		assertEquals(pipeline.getPrototypes().get(0).getSideEffects().size(), 2);

		assertNotNull(pipeline.getPrototypes().get(0).getSideEffects().get(0));
		assertTrue(pipeline.getPrototypes().get(0).getSideEffects().get(0) instanceof TestSideEffect);
		assertEquals("", ((TestSideEffect) pipeline.getPrototypes().get(0).getSideEffects().get(0)).value);

		assertNotNull(pipeline.getPrototypes().get(0).getSideEffects().get(1));
		assertTrue(pipeline.getPrototypes().get(0).getSideEffects().get(1) instanceof TestSideEffect);
		assertEquals("test", ((TestSideEffect) pipeline.getPrototypes().get(0).getSideEffects().get(1)).value);
	}

	private String slurpResource(String path) throws Exception {
		URL url = Thread.currentThread().getContextClassLoader().getResource("builder_test.json");
		File file = new File(url.getPath());

		InputStream is = new FileInputStream(file.getAbsolutePath());
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));

		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();

		while(line != null){
			sb.append(line).append("\n");
			line = buf.readLine();
		}

		return sb.toString();
	}

	public static class TestState {

	}

	@Component("transition")
	public static class TestTransition implements Transition<TestState> {

		@Override
		public void accept(TestState s0) {

		}
	}

	@Component("validate")
	public static class TestValidator implements Validation<TestState> {

		@Override
		public Predicate<TestState> apply(TestState s0) {
			return s1 -> true;
		}
	}

	@Data
	@Component("sideEffect")
	public static class TestSideEffect implements SideEffect<TestState> {

		private String value = "";

		@Override
		public Consumer<TestState> apply(TestState s0) {
			return s1 -> {};
		}
	}

	@Component("failure")
	public static class TestFailure implements Failure<TestState> {

		@Override
		public Consumer<TestState> apply(TestState s0) {
			return s1 -> {};
		}
	}

}
