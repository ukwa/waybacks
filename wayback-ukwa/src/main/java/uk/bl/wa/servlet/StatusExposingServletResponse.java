/**
 * 
 */
package uk.bl.wa.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * To access the HTTP Status code in a filter, we need to do some extra work.
 * 
 * As per https://stackoverflow.com/questions/1302072/how-can-i-get-the-http-status-code-out-of-a-servletresponse-in-a-servletfilter
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class StatusExposingServletResponse extends HttpServletResponseWrapper {

    private int httpStatus;

    public StatusExposingServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void sendError(int sc) throws IOException {
        httpStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        httpStatus = sc;
        super.sendError(sc, msg);
    }


    @Override
    public void setStatus(int sc) {
        httpStatus = sc;
        super.setStatus(sc);
    }

    public int getStatus() {
        return httpStatus;
    }

    @Override
    public void reset() {
        super.reset();
        this.httpStatus = SC_OK;
    }
}
