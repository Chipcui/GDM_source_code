package org.gobiiproject.gobiidao.util.async;

/***
 * Promise provides a thread safe box to place a single value into
 * Can only be set once
 * Calling get causes calling thread to wait for result to be delivered
 *
 * @author ljc237
 * @param <T> The type to box
 */
public class Promise<T> {

	private T t = null;

	private boolean isSet = false;

	/***
	 Sets the value of the promise. If a value has already been delivered, nothing will happen
	 @param t the value to set the promise
	 */
	public synchronized void set(T t) {
		if (! isSet) {
			this.t = t;
			isSet = true;
			notifyAll();
		}
	}

	/***
	 * Returns if the promise has been set or not
	 * @return the set statek
	 */
	public synchronized boolean isSet() {
		return isSet;
	}

	/***
	 * Returns the value of the promise, if it has been set, otherwise waits for result
	 * @return The value stored in the promise
	 */
	public synchronized T get() {
		while (! isSet) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return t;
	}
}
