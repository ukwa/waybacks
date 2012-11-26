/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;


import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author JoeObrien
 *
 *	Web Archive Page Locking Filter Prototype
 */
public class WaybackLockFilter extends BaseLockFilter{
	
	public static String FILTER_DOMAIN = "/wayback";
	
	protected static Logger logger = LoggerFactory.getLogger(WaybackLockFilter.class);
	
	
	/**
	 * 
	 */
	public WaybackLockFilter() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	    // Use .getContextPath() and set up paths.	
		FILTER_DOMAIN = config.getServletContext().getContextPath() + propertiesConfig.getProperty("wayback.relativePath");
		MSG_PAGE = FILTER_DOMAIN + "/pages/";
		WAYBACK_QUERY =  FILTER_DOMAIN + "/query";
		
    }
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
