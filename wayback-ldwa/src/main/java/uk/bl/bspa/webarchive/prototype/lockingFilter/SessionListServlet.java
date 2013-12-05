package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.bl.bspa.webarchive.prototype.lockingFilter.VO.AccessDetailVO;

/**
 * 
 * Servlet for Removal of Page Locks by Session 
 * 
 * @author JoeObrien
 *
 */
public class SessionListServlet extends HttpServlet {
	private static final long serialVersionUID = 7742890416006752069L;
	private static final String PAGE_REQUEST_PREFIX = "pageRequest=";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		if(request.getParameter("sessionId") != null){
			removeBySession(request, response);
			
		}else if(request.getParameter("pageRequest") != null){
			removeByPage(request, response);
		}
		
	}
		
	/* 
	 * Remove Session by Session Id
	 */
	private void removeBySession(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		
		AccessDetailVO accessDetail = new AccessDetailVO();
		accessDetail.setSessionId(request.getParameter("sessionId"));
		
	    AccessList accessList = (AccessList) request.getSession().getServletContext().getAttribute("lockList");
	    accessList.removeSessionPages(accessDetail);
	    
	    URL url = new URL(request.getRequestURL().toString());
	    
	    String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort(); 
	    
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Session Removal";
		out.println(
        "<BODY>\n" +
        "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
        "<UL>\n" +
        "  <LI>Removed Session: "
        + request.getParameter("sessionId") + "\n" +       
        "</UL>\n" +
        "<a href='" + urlPrefix + "/" + WaybackLockFilter.FILTER_DOMAIN + WaybackLockFilter.SESSION_PAGE + 
        "'><button type='button'>Maintain Sessions</button></a>" +
        "</BODY></HTML>");
	}
	
	/* 
	 * Remove Session by Page Id
	 */
	private void removeByPage(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
		String queryString = request.getQueryString();
		String page = queryString.substring( queryString.indexOf( PAGE_REQUEST_PREFIX ) + PAGE_REQUEST_PREFIX.length() );
	    AccessList accessList = (AccessList) request.getSession().getServletContext().getAttribute("lockList");
	    accessList.removeSessionByPage(page);
	    
	    URL url = new URL(request.getRequestURL().toString());
	    
	    String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort(); 
	    
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Session Removal";
		out.println(
        "<BODY>\n" +
        "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
        "<UL>\n" +
        "  <LI>Removed Session for Page: "
        + request.getParameter("pageRequest") + "\n" +       
        "</UL>\n" +
        "<a href='" + urlPrefix + "/" + WaybackLockFilter.FILTER_DOMAIN + WaybackLockFilter.SESSION_PAGE + 
        "'><button type='button'>Maintain Sessions</button></a>" +
        "</BODY></HTML>");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doGet(request, response);
	}

}
