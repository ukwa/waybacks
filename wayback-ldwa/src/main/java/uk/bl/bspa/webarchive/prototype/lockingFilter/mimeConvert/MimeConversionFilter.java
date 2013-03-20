/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter.mimeConvert;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;

import uk.bl.bspa.webarchive.prototype.lockingFilter.BaseLockFilter;



/**
 * @author JoeObrien
 *
 *	Redirect specific Mime Types
 */
public class MimeConversionFilter implements Filter {
	
	protected static Logger logger = LoggerFactory.getLogger(MimeConversionFilter.class);
	public List<String> mimeTypes = new ArrayList<String>();
	public List<String> mimeRedirects = new ArrayList<String>();
	public Properties propertiesConfig;
	
	
	/**
	 * 
	 */
	public MimeConversionFilter() {
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
 
		HttpServletRequest httpRequest = (HttpServletRequest) req;
	    HttpServletResponse httpResponse = (HttpServletResponse) res;
	    
	    logger.debug("Checking MIME Type for: "+httpRequest.getRequestURL());
	    
	    // Ignore html pages
        String reqAccept = httpRequest.getHeader("accept");
        if(StringUtils.containsIgnoreCase(reqAccept, BaseLockFilter.VALID_ACCEPT)){
        	chain.doFilter(req, res);
        	return;
        }     
	    
	    Tika tika = new Tika();
	    String strMime = tika.detect(httpRequest.getRequestURL().toString());

		// Redirect if we have a suitable type:
	    if( !mimeTypes.contains(strMime) ) {
	    	chain.doFilter(req, res);
	    }
	    
	   // Redirect any filtered Mimetype
        for(int i = 0;i < mimeTypes.size();i++){
        	String mimeType = mimeTypes.get(i);
        	if(mimeType.equalsIgnoreCase(strMime)){
        		logger.info("Redirecting: "+httpRequest.getRequestURL() + " to: " +  mimeRedirects.get(i));
        		httpResponse.sendRedirect(httpResponse.encodeRedirectURL(mimeRedirects.get(i) + httpRequest.getRequestURL().toString()));
        		return;
        	}
        	
        }
		
	}

	@Override
	public void destroy(){}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		logger.info("Loading Properties file: filter.properties" );	      
        
		propertiesConfig = new Properties();
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("filter.properties");
			propertiesConfig.load(in);
		} catch (Exception e) {
			logger.error("Unable to load properties from file", e);

		}
        
		mimeTypes = new ArrayList<String>(Arrays.asList(propertiesConfig.getProperty("mimeType").split(";")));
		mimeRedirects = new ArrayList<String>(Arrays.asList(propertiesConfig.getProperty("mimeType.redirect").split(";")));
		
	}
    
}
