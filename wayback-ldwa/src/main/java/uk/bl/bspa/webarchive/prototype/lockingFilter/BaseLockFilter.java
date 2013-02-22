/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
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
	
	public static String FILTER_DOMAIN = "/";
	// Do not change relative paths
	public static final String ERROR_PAGE ="/pages/error.jsp"; 	// Relative path
	public static final String LOCK_PAGE = "/pages/pageLocked.jsp";	// Relative path
	public static final String LOGOFF_PAGE = "/logoff"; // Relative path
	public static final String UNSUPPORTED_BROWSER_PAGE = "/pages/unsupportedBrowser.jsp"; //Relative path
	public static final String SESSION_PAGE = "/pages/maintainSessions.jsp";
	
	public static String MSG_PAGE = FILTER_DOMAIN+"/pages/";
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
	public static String WAYBACK_QUERY = "/wayback/query";
	protected static Logger logger = LoggerFactory.getLogger(BaseLockFilter.class);
	
	public Properties propertiesConfig;
	public List<String> excludeSuffixes = new ArrayList<String>();
	
	/**
	 * 
	 */
	public BaseLockFilter() {
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
 
		HttpServletRequest httpRequest = (HttpServletRequest) req;
	    HttpServletResponse httpResponse = (HttpServletResponse) res;
	    
	    // Check if the page is locked
	    AccessList accessList = (AccessList) httpRequest.getSession().getServletContext().getAttribute("lockList");
	    URL url = new URL(httpRequest.getRequestURL().toString());
	    
	    logger.debug("Host: " + url.getHost()+" Path: " + url.getPath());
	    
	    // DEBUG - View Headers
	    @SuppressWarnings("unchecked")
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			logger.debug("Header Name: " + headerName);
			String headerValue = httpRequest.getHeader(headerName);
			logger.debug(", Header Value:" + headerValue);
		}
	    
	    
        // Filter unsupported browsers
        String userAgent = httpRequest.getHeader("User-Agent");
        if (!userAgent.contains(FIREFOX)&&!userAgent.contains(CHROME)){
        	if( url.getPath().endsWith(UNSUPPORTED_BROWSER_PAGE) != true ) {
        		logger.debug("Redirecting from "+httpRequest.getRequestURI() + " to "+ UNSUPPORTED_BROWSER_PAGE);
        		httpResponse.sendRedirect(UNSUPPORTED_BROWSER_PAGE);
        	} else {
            	chain.doFilter(req, res);
        	}
        	return;
        }
     
        
        // Don't filter Message Pages
        if(StringUtils.containsIgnoreCase(url.getPath(), MSG_PAGE)){

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
        	
        	chain.doFilter(req, res);
	
        	return;
        }
        
        /*
         * Simulate Logoff
         */
        if (url.getPath().endsWith(LOGOFF_PAGE)) {
        	logoffCleanUp(httpRequest, httpResponse);
        	httpRequest.getSession().invalidate();
        	httpRequest.getSession(true);
        	httpResponse.sendRedirect(LOGOFF_JSP_PAGE);
        	return;
        }
        
        logger.debug("HTTP METHOD: " + httpRequest.getMethod());
        // Filter out anything but HTTP Gets
        if(httpRequest.getMethod() == null ||!httpRequest.getMethod().equals(HTTP_GET)){
        	chain.doFilter(req, res);
			return;
        }
        
   
        // Ignore widget (images, etc) - Done by checking the Accept header (only partially successful)
        String reqAccept = httpRequest.getHeader("accept");
        if(!StringUtils.containsIgnoreCase(reqAccept, VALID_ACCEPT)){
        	chain.doFilter(req, res);
        	return;
        }     
        
        
        /* 
         * Filter Urls with valid accept headers that contain "<n>_im/http://" 
         * These url sub-calls are generated by wayback - usually for css, icons and images
         */
        if(StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_IM)||StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_CS) ||
        		StringUtils.containsIgnoreCase(url.getPath(), FILTER_URL_JS)){
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
        		chain.doFilter(req, res);
            	return;
        	}
        	
        }
		
        String shortPath = null;
        shortPath = url.getPath().substring((FILTER_DOMAIN + "/").length());
        logger.debug("Short Path: " + shortPath);
        
        /*
         * Special Page - Query. Lock at parameter level
         */
       	if (StringUtils.contains(url.getPath(), WAYBACK_QUERY)) {
        		
       		String paramUrl = httpRequest.getParameter("url");
       		String paramDate = httpRequest.getParameter("date");
       		
       		logger.debug("Filtering Query params url: " + paramUrl + " and date: " + paramDate);
        		
       		if(paramDate != null && paramUrl != null){
       			shortPath = paramDate + "/" + paramUrl;
       		}else{
       			chain.doFilter(req, res);
       			return;
       		}

    	}
        
        // Do not lock empty path, or path shorter that date-time stamp plus character for url 
        if(shortPath == null ||shortPath.isEmpty()||shortPath.equals("/")||shortPath.length() < 17){
        	chain.doFilter(req, res);
			return;
        }
        
        //Check that the URL has a valid 14 digit date stamp
        String dateStamp = shortPath.substring(1,15);
                
        if(!dateStamp.matches( "\\d{14}")){
        	chain.doFilter(req, res);
			return;
        }
                
        // Check for Page Lock
	    synchronized (this) {
	    	String ip = getClientIpAddr(httpRequest);
	    	String hostName = getHostNameFromIP(ip);
	    	AccessDetailVO accessPage = new AccessDetailVO(httpRequest.getSession().getId(), shortPath, new Date(),ip, hostName);
	        
	    	// Attempt to add a lock page
		    if(accessList.addPageLock(accessPage) == 0){
		    	chain.doFilter(req, res);

	        }else{
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
		FILTER_DOMAIN = config.getServletContext().getContextPath() + propertiesConfig.getProperty("wayback.relativePath");
		MSG_PAGE = FILTER_DOMAIN + "/pages/";
		WAYBACK_QUERY =  FILTER_DOMAIN + "/query";
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
        return ip;  
    }  

    /*
     * Retrieve the host name from the IP Address
     * 
     */
    public static String getHostNameFromIP(String ip){
    	
    	InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(ip);
			String host = inetAddress.getHostName(); 
			return host;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	
		return null;
    }
}
