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
public class MementoLockFilter extends BaseLockFilter {
	
	public static String FILTER_DOMAIN = "/memento";
	protected static Logger logger = LoggerFactory.getLogger(MementoLockFilter.class);
	
	/**
	 * 
	 */
	public MementoLockFilter() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
	    // Use .getContextPath() and set up paths.
		FILTER_DOMAIN = config.getServletContext().getContextPath() + propertiesConfig.getProperty("memento.relativePath");
		setFilterDomain(FILTER_DOMAIN);
        logger.warn("Using filter domain: " + FILTER_DOMAIN);
    }


}
