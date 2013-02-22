package uk.bl.bspa.webarchive.prototype.lockingFilter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class FilterTest {

	/**
	 * Check opening of same page multiple times
	 */
	@Test
	public void testOpenMutltiPages() {

		/* NEED TO HAVE INSTANCE RUNNING
		DefaultHttpClient httpclient1 = new DefaultHttpClient();
		HttpGet httpGet1 = new HttpGet("http://localhost:8080/wayback/http://www.bl.uk");
		HttpGet httpGet2 = new HttpGet("http://localhost:8080/wayback/http://www.bl.uk");
		String cookieName = "www.bbc.co.uk/news/uk-16896731";
		
		removeAllCookies(httpclient1);
		removeAllPageLocks();
		
		try {
			System.out.println("Initial set of cookies:");
	        List<Cookie> cookies = httpclient1.getCookieStore().getCookies();
	        if (cookies.isEmpty()) {
	            System.out.println("None");
	        } else {
	            for (int i = 0; i < cookies.size(); i++) {
	                System.out.println("- " + cookies.get(i).toString());
	            }
	        }
			
			HttpResponse response1 = httpclient1.execute(httpGet1);
			System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            
           
            System.out.println(response1.getProtocolVersion());
            System.out.println(response1.getStatusLine().getStatusCode());
            System.out.println(response1.getStatusLine().getReasonPhrase());
            System.out.println(response1.getStatusLine().toString());
            
            
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
            
            HttpResponse response2 = httpclient1.execute(httpGet2);
			System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            
            System.out.println(response2.getProtocolVersion());
            System.out.println(response2.getStatusLine().getStatusCode());
            System.out.println(response2.getStatusLine().getReasonPhrase());
            System.out.println(response2.getStatusLine().toString());
            EntityUtils.consume(entity2);
            
            
            System.out.println("Final set of cookies:");
            cookies = httpclient1.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                    if(cookies.get(i).getName().equals(cookieName)){
                    	assertNotNull(cookies.get(i).getName());
                    }
                    	
                }
            }
                        
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity2);
            
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	@Test
	public void testQuartzJob(){
		AccessList.getInstance().removeAllLocks();
	}
	
	@Test
	public void testCookieSpec() throws ClientProtocolException, IOException {
		
		/* NEED TO HAVE INSTANCE RUNNING
		HttpClient httpclient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpget = new HttpGet("http://localhost:8080/wayback/http://www.bl.uk"); 
		@SuppressWarnings("unused")
		HttpResponse response = httpclient.execute(httpget, localContext);
		CookieOrigin cookieOrigin = (CookieOrigin) localContext.getAttribute(
		        ClientContext.COOKIE_ORIGIN);
		System.out.println("Cookie origin: " + cookieOrigin);
		CookieSpec cookieSpec = (CookieSpec) localContext.getAttribute(
		        ClientContext.COOKIE_SPEC);
		System.out.println("Cookie spec used: " + cookieSpec);
		*/
	}
	
	/*
	 * Remove all Cookies
	 */
	public void removeAllCookies(DefaultHttpClient httpClient){
		 List<Cookie>cookies = httpClient.getCookieStore().getCookies();
         if (cookies.isEmpty()) {
             System.out.println("None");
         } else {
             for (int i = 0; i < cookies.size(); i++) {
                 System.out.println("- " + cookies.get(i).toString());
                 cookies.remove(i);
             }
         }
	}
	
	/*
	 * Remove All Locks
	 */
	@Test
	public void removeAllPageLocks(){
		AccessList accessList = AccessList.getInstance();
		accessList.removeAllLocks();
	}
	
	/**
	 * Test Memento Call
	 */
	@Test
	public void testMementoCall() {

		/* NEED TO HAVE INSTANCE RUNNING
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://localhost:8080/memento/20100514031252/http://www.democracyclub.org.uk/aboutus/");
		
		removeAllPageLocks();
		
		try {
			
			// Dummy Chrome Header
			httpGet.setHeader("User-Agent", "Chrome");
			
			HttpResponse response = httpclient.execute(httpGet);
			System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            
           
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            
            
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity);
       
            
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		//
	}

}
