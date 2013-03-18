/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author JoeObrien
 *
 *	Web Archive Page Locking Filter Prototype
 */
public class ListLockFilter extends BaseLockFilter {
	
	public static String FILTER_DOMAIN = "/list";
		
	protected static Logger logger = LoggerFactory.getLogger(ListLockFilter.class);
	
	/**
	 * 
	 */
	public ListLockFilter() {
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		BaseLockFilter.FILTER_DOMAIN = ListLockFilter.FILTER_DOMAIN;
		super.doFilter(req, res, chain);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	    // Use .getContextPath() and set up paths.
		FILTER_DOMAIN = config.getServletContext().getContextPath() + propertiesConfig.getProperty("list.relativePath");
		MSG_PAGE = FILTER_DOMAIN + "/pages/";
		WAYBACK_QUERY =  FILTER_DOMAIN + "/query";
		
    }


}
