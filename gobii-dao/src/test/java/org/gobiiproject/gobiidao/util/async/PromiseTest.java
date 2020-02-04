package org.gobiiproject.gobiidao.util.async;


import org.junit.Test;
import static junit.framework.TestCase.*;

public class PromiseTest {


	@Test
	public void testPromise() throws InterruptedException {
		Promise<Integer> promise = new Promise<>();

		Thread settingThread = new Thread(() -> promise.set(1));
		Thread readingThread0 = new Thread(promise::get);
		Thread readingThread1 = new Thread(promise::get);

		readingThread0.start();
		readingThread1.start();
		Thread.sleep(1000);
		assertTrue(readingThread0.isAlive());
		assertTrue(readingThread1.isAlive());
		assertFalse(promise.isSet());

		settingThread.start();
		Thread.sleep(1000);

		assertFalse(readingThread0.isAlive());
		assertFalse(readingThread1.isAlive());
		assertFalse(settingThread.isAlive());
		assertTrue(promise.isSet());

		assertEquals(Integer.valueOf(1), promise.get());
	}
}