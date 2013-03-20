/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter.resource;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.bl.bspa.webarchive.prototype.lockingFilter.StatusExposingServletResponse;


/**
 * @author JoeObrien
 *
 *	Web Archive 404 Filter
 */
public class MissingResourceLoggingFilter implements Filter {
	
	protected static Logger logger = LoggerFactory.getLogger(MissingResourceLoggingFilter.class);
	
	
	/**
	 * 
	 */
	public MissingResourceLoggingFilter() {
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
 
		HttpServletRequest httpRequest = (HttpServletRequest) req;
	    //HttpServletResponse httpResponse = (HttpServletResponse) res;
	    
	    logger.debug("Checking for 404: "+httpRequest.getRequestURL());
		
        
    	// Wrap the response so we can access the status later:
    	StatusExposingServletResponse response = new StatusExposingServletResponse((HttpServletResponse)res);
    	chain.doFilter(req, response);

    	// Response is now available - check for 404
		if( response.getStatus() == 404 ) {
			logger.error("Missing Resource: "+httpRequest.getRequestURL());
		}
        
	}

	@Override
	public void destroy(){}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
    
}
