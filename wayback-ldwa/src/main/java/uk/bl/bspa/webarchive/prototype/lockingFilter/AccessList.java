/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.bl.bspa.webarchive.prototype.lockingFilter.VO.AccessDetailVO;
import uk.bl.bspa.webarchive.prototype.lockingFilter.VO.SessionDetailVO;

/**
 * Singleton to monitor and control access to Web Archive Pages
 * 
 * @author JoeObrien
 *
 */
public class AccessList {
	
	// Private constructor prevents instantiation from other classes
    private AccessList() {

    }
    
    private static final AccessList INSTANCE = new AccessList();
    
    private ConcurrentMap<String, AccessDetailVO> pageAccess = new ConcurrentHashMap<String, AccessDetailVO>();
    private static final Logger logger = LoggerFactory.getLogger(AccessList.class);


    public static AccessList getInstance() {
        return INSTANCE;
    }

    /**
     * Check if a page is locked
     * 
     * @param page
     * @return
     */
    public boolean isPageLocked(AccessDetailVO requestPage){
    	
    	// Not locked
    	if(pageAccess.get(requestPage.getPage()) == null){
    		return false;
    	}
    	
    	// Locked by current session
    	if(pageAccess.get(requestPage.getPage()).equals(requestPage)){
    		return false;
    	}
    	
    	// Locked by another session
    	return true;
    }
    
    /*
     * Add page to lock map
     */
    public int addPageLock(AccessDetailVO requestPage){
    	
    	//Check is Page is already locked
    	synchronized(pageAccess){	// Synchronised to avoid race conditions  	
       		 if(!isPageLocked(requestPage)){ 
       			pageAccess.put(requestPage.getPage(), requestPage);
       			logger.info("Adding Lock Page: " + requestPage.toString());
       			return 0;
       		 }else{
       			 return -1;
       		 }
    	 }
    }
    	
    	
    /*
     * Remove page lock
     */
    public void removePageLock(AccessDetailVO requestPage){  	

    	logger.info("Removing Lock Page: " + requestPage.toString());
    	pageAccess.remove(requestPage.getPage());
    }
    
    /*
     * Remove all Page locks for a given session
     */
    public void removeSessionPages(AccessDetailVO requestPage){
    	
    	logger.info("Removing Lock Pages for Session: " + requestPage.getSessionId());
    	for (Entry<String, AccessDetailVO> curPage : pageAccess.entrySet()) {
    		AccessDetailVO currLock = curPage.getValue();
    		if(currLock.getSessionId().equals(requestPage.getSessionId())){
    			removePageLock(currLock);
    		}
    	}

    }
    
    /*
     * Check Page exists and remove
     */
    public boolean pageExistsAndRemoved(AccessDetailVO requestPage){
    	
    	//Check if Page is already locked
    	synchronized(pageAccess){	// Synchronised to avoid race conditions  	
       		 if(isPageLocked(requestPage)){ 
       			logger.info("Removing Lock Page: " + requestPage.toString());
       			pageAccess.remove(requestPage.getPage());
       			return true;
       		 }
    	 }
    	
    	return false;
    }
    
    /*
     * Remove a session for a given page
     */
    public void removeSessionByPage(String page){
    	
    	AccessDetailVO access = pageAccess.get(page);
    	
    	removeSessionPages(access);
    	
    }
    
    
    /*
     * Remove all Locks
     */
    void removeAllLocks(){
    	pageAccess = new ConcurrentHashMap<String, AccessDetailVO>();

    }
    
    /**
     * Get report List of all locks
     * 
     * @return
     */
    public List<AccessDetailVO> getLockList(){
    	
    	List<AccessDetailVO> pageLocks = new ArrayList<AccessDetailVO>();
    	for (Entry<String, AccessDetailVO> curPage : pageAccess.entrySet()) {
    		AccessDetailVO currLock = curPage.getValue();
    		pageLocks.add(currLock);
    	}
    	
    	Collections.sort(pageLocks);
    	return pageLocks;
    }
    
    /**
     * Get report List of all locks
     * 
     * @return
     */
    public List<String> getSessionList(){
    	
    	List<String> sessionIds = new ArrayList<String>();
    	for (Entry<String, AccessDetailVO> curPage : pageAccess.entrySet()) {
    		AccessDetailVO currLock = curPage.getValue();
    		if(!sessionIds.contains(currLock.getSessionId())){
    			sessionIds.add(currLock.getSessionId());
    		}	
    	}
    	
    	return sessionIds;
    }
    
    /**
     * Get report List of all sessions and locks
     * 
     * @return
     */
	public List<SessionDetailVO> getSessionDetail(){
    	
    	List<AccessDetailVO> accessDetails = getLockList();
    	Iterator<AccessDetailVO> iter = accessDetails.iterator();
    	String prevSessionId = "";
    	List<SessionDetailVO> sessionList = new ArrayList<SessionDetailVO>();
    	
    	SessionDetailVO sessionTmp = new SessionDetailVO();
    	while(iter.hasNext()){
    		AccessDetailVO accessDetail = iter.next();
    		if(accessDetail.getSessionId() != prevSessionId){
    			sessionTmp = new SessionDetailVO();
    			sessionTmp.setSessionId(accessDetail.getSessionId());
    			sessionTmp.setSessionDetail(new ArrayList<AccessDetailVO>());
    			prevSessionId = accessDetail.getSessionId();
    			sessionList.add(sessionTmp);
    		}
    		sessionTmp.getSessionDetail().add(accessDetail);
    		
    	}
    	
    	return sessionList;
    }
    
}
