/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter.exceptions;

/**
 * @author JoeObrien
 *
 */
public class LockFilterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3058590033249159082L;

	/**
	 * 
	 */
	public LockFilterException() {
	}

	/**
	 * @param arg0
	 */
	public LockFilterException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public LockFilterException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public LockFilterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
