/**
 * 
 */
package org.archive.wayback.exception;

/**
 * 
 * This exception implements the 'unavailable for legal reasons' (Status code
 * 451) case.
 * 
 * For us, this is due to Legal Deposit.
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class UnavailableForLegalReasonsException extends WaybackException {

    private static final long serialVersionUID = 128947144788923147L;
    protected static final String ID = "unavailableForLegalReasonsException";

    /**
     * Constructor
     * 
     * @param message
     */
    public UnavailableForLegalReasonsException(String message) {
        super(message, "Unavailable for legal reasons");
        id = ID;
    }

    /**
     * Constructor with message and details
     * 
     * @param message
     * @param details
     */
    public UnavailableForLegalReasonsException(String message, String details) {
        super(message, "Unavailable for legal reasons", details);
        id = ID;
    }

    /**
     * @return the HTTP status code appropriate to this exception class.
     */
    public int getStatus() {
        return 451;
    }
}
