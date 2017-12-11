/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.bl.bspa.webarchive.prototype.lockingFilter.VO.AccessDetailVO;


/**
 * @author JoeObrien
 *
 *	Web Archive Abstract Locking Filter Prototype
 */
public abstract class BaseLockFilter implements Filter {
	
	// Do not change relative paths
    public static final String ERROR_PAGE = "/pages/status/SystemProblem.jsp"; // Relative
                                                                               // path
    public static final String LOCK_PAGE = "/pages/status/ItemAlreadyInUse.jsp"; // Relative
                                                                                 // path
	public static final String LOGOFF_PAGE = "/logoff"; // Relative path
	public static final String UNSUPPORTED_BROWSER_PAGE = "/pages/unsupportedBrowser.jsp"; //Relative path
	public static final String SESSION_PAGE = "/pages/maintainSessions.jsp";
	public static final String COOKIE_PREFIX = "sessionid-";
	
	public static final String LOGOFF_JSP_PAGE = "/pages/logoff.jsp";
	public static final String SESSION_LIST = "maintainSessions.jsp/pages/sessionList";
	public static final String FILTER_URL_IM = "im_/http://";
	public static final String FILTER_URL_CS = "cs_/http://";
	public static final String FILTER_URL_JS = "js_/http://";
	public static final String HTTP = "http://";
	public static final String HTTP_GET = "GET";
	
	public static final String FIREFOX = "Firefox";
	public static final String CHROME = "Chrome";
	public static final String VALID_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	protected static Logger logger = LoggerFactory.getLogger(BaseLockFilter.class);
	
	public Properties propertiesConfig;
	public List<String> excludeSuffixes = new ArrayList<String>();
	
	private String filterDomain = "";
	
	/**
	 * 
	 */
	public BaseLockFilter() {
	}
	
	protected void setFilterDomain(String filterDomain) {
		this.filterDomain = filterDomain;
	}
		protected String getFilterDomain() {
		return filterDomain;
	}
	
	private String getMessagePrefix() {
		return getFilterDomain() + "/pages/";
	}
	
	private String getWaybackQuery() {
		return getFilterDomain() + "/query";
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
 
		HttpServletRequest httpRequest = (HttpServletRequest) req;
	    HttpServletResponse httpResponse = (HttpServletResponse) res;
	    
	    // Check if the page is locked
	    AccessList accessList = (AccessList) httpRequest.getSession().getServletContext().getAttribute("lockList");

	    StringBuffer sb = httpRequest.getRequestURL();
	    String queryString = httpRequest.getQueryString();
	    if( queryString != null ) {
	    	sb.append( "?" );
	    	sb.append( queryString );
	    }
	    URL url = new URL( sb.toString() );
	    
	    /* DEBUG - View Headers
	    @SuppressWarnings("unchecked")
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			logger.debug("Header Name: " + headerName);
			String headerValue = httpRequest.getHeader(headerName);
			logger.debug(", Header Value:" + headerValue);
		}
	    */
     
        // Don't filter Message Pages
        if(StringUtils.containsIgnoreCase(url.getPath(), getMessagePrefix())){

        	// Set a session list
        	if(StringUtils.containsIgnoreCase(url.getPath(), SESSION_PAGE)){
        		httpRequest.getSession().setAttribute("sessionDetails", accessList.getSessionDetail());
        		RequestDispatcher rd = httpRequest.getRequestDispatcher(SESSION_PAGE);
        		rd.forward(httpRequest, httpResponse);
        		return;
        	}
        	
        	
        	// Use Request Dispatcher for a Session Removal Call
        	if(StringUtils.containsIgnoreCase(url.getPath(), SESSION_LIST)){
   	     		RequestDispatcher rd = httpRequest.getSession().getServletContext().getNamedDispatcher("sessionList");
        		rd.forward(httpRequest, httpResponse);
        		return;
        	}
        	
            logger.info("Message page passes through without locking...");
        	chain.doFilter(req, res);
	
        	return;
        }
        
        logger.warn("Looking at " + url);
        
        /*
         * Simulate Logoff
         */
        if (url.getPath().endsWith(LOGOFF_PAGE)) {
        	logoffCleanUp(httpRequest, httpResponse);
        	httpRequest.getSession().invalidate();
        	httpRequest.getSession(true);
        	httpResponse.sendRedirect(this.getFilterDomain() + LOGOFF_JSP_PAGE);
        	return;
        }
        
        //logger.debug("HTTP METHOD: " + httpRequest.getMethod());
        // Filter out anything but HTTP Gets
        if(httpRequest.getMethod() == null ||!httpRequest.getMethod().equals(HTTP_GET)){
        	chain.doFilter(req, res);
            logger.info("HTTP " + httpRequest.getMethod()
                    + " passed through without locking...");
			return;
        }
        
   
        // Ignore widget (images, etc) - Done by checking the Accept header
        // matches that expected for normal browser requests.
        // Only partially successful in that it's hard-coded against the
        // behaviour of a single browser!
        /*
         * String reqAccept = httpRequest.getHeader("accept");
         * if(!StringUtils.containsIgnoreCase(reqAccept, VALID_ACCEPT)){
         * logger.info("Passed based on ACCEPT header... " + reqAccept + " " +
         * VALID_ACCEPT); chain.doFilter(req, res); return; }
         */
        
        
        /*
         * Filter Urls with valid accept headers that contain "<n>_im/http://"
         * These url sub-calls are generated by wayback - usually for css, icons
         * and images
         * 
         * e.g. we are relying on Wayback to spot embeds reliably.
         */
        if(StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_IM)||StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_CS) ||
        		StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_JS)){
            logger.info("Apparent embed URL " + url
                    + " passed through without locking...");
        	chain.doFilter(req, res);
        	return;
        }      
        
       /* 
        * Further filters on .png, jpg, .ico and other suffixes denoted in the filter.properties file
	    */
        Iterator<String> iter = excludeSuffixes.iterator();
        while(iter.hasNext()){
        	String suffix = iter.next();
        	if(url.getPath().endsWith(suffix)){
                logger.info(
                        "URL " + url
                                + " passed through without locking based on suffix...");
        		chain.doFilter(req, res);
            	return;
        	}
        	
        }
		
        String shortPath = null;
        shortPath = url.getFile().substring((getFilterDomain()).length());
        logger.info("Got short Path: " + shortPath);
        
        // Remove -xx (like -cy) from archive extension
        if(shortPath.startsWith("-")){
        	shortPath = shortPath.substring(3);
        }
        
        /*
         * Special Page - Query. Lock at parameter level
         */
       	if (StringUtils.contains(url.getPath(), getWaybackQuery())) {
        		
       		String paramUrl = httpRequest.getParameter("url");
       		String paramDate = httpRequest.getParameter("date");
       		
       		logger.debug("Filtering Query params url: " + paramUrl + " and date: " + paramDate);
        		
       		if(paramDate != null && paramUrl != null){
       			shortPath = paramDate + "/" + paramUrl;
       		}else{
                logger.info("Query page passes through without locking...");
       			chain.doFilter(req, res);
       			return;
       		}

    	}
        
        // Do not lock empty path, or path shorter that date-time stamp plus character for url 
        if(shortPath == null ||shortPath.isEmpty()||shortPath.equals("/")||shortPath.length() < 17){
            logger.info("Empty path page passes through without locking...");
        	chain.doFilter(req, res);
			return;
        }
        
        //Check that the URL has a valid 14 digit date stamp
        String dateStamp = shortPath.substring(1,15);
                
        if(!dateStamp.matches( "\\d{14}")){
            logger.info(
                    "Timestamp " + dateStamp
                            + " does not match expected form so passing through without locking...");
        	chain.doFilter(req, res);
			return;
        }
        
        // Check for Page Lock
        logger.info("Checking for page lock...");
	    synchronized (this) {
	    	String ip = getClientIpAddr(httpRequest);
	    	// Perform reverse DNS lookup of the client ID. Not informative and very slow, so removing it.
	    	//String hostName = getHostNameFromIP(ip);
	    	String hostName = null;
	    	AccessDetailVO accessPage = new AccessDetailVO(httpRequest.getSession().getId(), URLDecoder.decode( shortPath, "UTF-8" ), new Date(),ip, hostName);
	        
	    	// Attempt to add a lock page
		    logger.debug("Attempting to lock page...");
		    if(accessList.addPageLock(accessPage) == 0){

		    	// Wrap the response so we can access the status later:
		    	StatusExposingServletResponse response = new StatusExposingServletResponse((HttpServletResponse)res);
		    	// Set a cookie so the session ID can be picked up.
		    	String sessionId = httpRequest.getSession().getId();
		    	Cookie cookie = new Cookie( COOKIE_PREFIX + sessionId, sessionId );
		    	cookie.setMaxAge( Integer.MAX_VALUE );
		    	response.addCookie( cookie );
		    	// So the chain:
                logger.info("Locked, and now passing down the chain...");
		    	chain.doFilter(req, response);
		        logger.debug("Examining the response...");

		    	// Response is now available, so release the lock if the request failed:
		    	if( (response.getStatus()/100) != 2 ) {
                    logger.info(
                            "Releasing lock as this page raised an non 2xx status code: "
                                    + accessPage.getPage());
		    		accessList.removePageLock(accessPage);
		    	}

	        } else {
	        	// Page is already locked
	        	RequestDispatcher rd = httpRequest.getRequestDispatcher(LOCK_PAGE);
        		rd.forward(httpRequest, httpResponse);
	        }
	    }
	}


	@Override
	public void init(FilterConfig config) throws ServletException {
			
		logger.info("Loading Properties file: filter.properties" );	      
		        
		propertiesConfig = new Properties();
		try {
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("filter.properties");
			propertiesConfig.load(in);
		} catch (Exception e) {
			logger.error("Unable to load properties from file", e);

		}
        
		excludeSuffixes = new ArrayList<String>(Arrays.asList(propertiesConfig.getProperty("exclude.suffix").split(";")));
		
		// Use .getContextPath() and set up paths.
		setFilterDomain( config.getServletContext().getContextPath() + propertiesConfig.getProperty("wayback.relativePath"));
        logger.warn("Using filter domain: " + getFilterDomain());
    }
	
	@Override
	public void destroy(){}
    
    /*
     * Remove Corresponding Pages from Access List
	 *
	 * @param pHttpRequest
	 * @param pHttpResponse
	 */
	protected void logoffCleanUp(HttpServletRequest pHttpRequest, HttpServletResponse pHttpResponse) {
		
		AccessList accessList = (AccessList) pHttpRequest.getSession().getServletContext().getAttribute("lockList");
		AccessDetailVO accessPage = new AccessDetailVO(pHttpRequest.getSession().getId(), null, new Date(), null, null); 
		accessList.removeSessionPages(accessPage);
		
    }
	
    /**
     * Retrieve the Client IP Address
     * 
     * @param request
     * @return
     */
    public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }
        // Proxied IPs may be listed as a comma-space-separated list.
        if( ip.indexOf( "," ) != -1 )
        	ip = ip.split( "," )[ 0 ].trim();
        return ip;  
    }  

    /*
     * Retrieve the host name from the IP Address
     * 
     */
    private static String getHostNameFromIP(String ip){
    	
    	InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(ip);
			String host = inetAddress.getHostName(); 
			return host;
		} catch (UnknownHostException e) {
			logger.error("Unknown Host: " + ip);
		}  
    	
		return null;
    }
}
