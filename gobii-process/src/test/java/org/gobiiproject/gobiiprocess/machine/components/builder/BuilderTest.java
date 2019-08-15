package org.gobiiproject.gobiiprocess.machine.components.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.gobiiproject.gobiiprocess.machine.builder.*;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.*;
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

		assertEquals(3, pipeline.getPipes().size());

		assertNotNull(pipeline);
		assertFalse(pipeline.getPipes().isEmpty());

		assertTrue(pipeline.getPipes().get(0) instanceof Step);

		Step<TestState> step0 = (Step<TestState>) pipeline.getPipes().get(0);

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

		assertTrue(pipeline.getPipes().get(1) instanceof Fork);

		Fork<TestState> fork = (Fork<TestState>) pipeline.getPipes().get(1);

		assertEquals(2, fork.getBranches().size());

		List<Gate<TestState>> gates = new ArrayList<>();
		List<Pipe<TestState>> pipes = new ArrayList<>();
		fork.getBranches().forEach((gate, pipe) -> { gates.add((Gate<TestState>) gate); pipes.add(pipe);});

		assertEquals(2, gates.size());
		assertEquals(2, pipes.size());

		assertTrue(gates.get(0) instanceof Gate0);
		assertTrue(gates.get(1) instanceof Gate1);

		assertTrue(pipes.get(0) instanceof Pipeline);

		Pipeline<TestState> branch0Pipeline = (Pipeline<TestState>) pipes.get(0);

		assertNotNull(branch0Pipeline);
		assertNotNull(branch0Pipeline.getPipes());
		assertEquals(1, branch0Pipeline.getPipes().size());
		assertEquals(pipeline.getPipes().get(0), branch0Pipeline.getPipes().get(0));

		assertTrue(pipes.get(1) instanceof Pipeline);
		assertEquals(pipeline, pipes.get(1));

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

		assertTrue(pipeline.getPipes().get(2) instanceof Step);

		Step<TestState> step1 = (Step<TestState>) pipeline.getPipes().get(2);
		assertNotNull(step1);
		assertTrue(step1.getTransition() instanceof DependentTest);

		DependentTest dt = (DependentTest) step1.getTransition();

		assertNotNull(dt.getA());
		assertNotNull(dt.getB());
		assertEquals(dt.getA(), dt.getB());
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

	@Component("gate0")
	public static class Gate0 implements Gate<TestState> {

		@Override
		public boolean test(TestState testState) {
			return false;
		}
	}

	@Component("gate1")
	public static class Gate1 implements Gate<TestState> {

		@Override
		public boolean test(TestState testState) {
			return false;
		}
	}

	@Abstract("dependency/test")
	public interface DependencyAbstract extends Dependency {

	}

	public static class DependencyImpl implements @Implementation("dependency/test/impl") DependencyAbstract {

		private String testField;

		public boolean wasInitialized = false;
		public boolean wasValidated = false;
		public boolean wasReleased = false;

		public void setTestField(String s) {
			this.testField = s;
		}

		@Override
		public void initialize() {
			wasInitialized = true;
		}

		@Override
		public boolean isValid() {
			wasValidated = true;
			return true;
		}

		@Override
		public void release() {
			wasReleased = true;
		}
	}

	@Component("dependent")
	public static class DependentTest implements Transition<TestState> {

		@Dependent("dependency/test")
		DependencyImpl a;

		@Dependent("dependency/test/impl")
		DependencyImpl b;

		public DependencyImpl getA() {
			return a;
		}

		public void setA(DependencyImpl impl) {
			this.a = impl;
		}

		public DependencyImpl getB() {
			return b;
		}

		public void setB(DependencyImpl impl) {
			this.b = impl;
		}

		@Override
		public void accept(TestState testState) {

		}
	}
}
