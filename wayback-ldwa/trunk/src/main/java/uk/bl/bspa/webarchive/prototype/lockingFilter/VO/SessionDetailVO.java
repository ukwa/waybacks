/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter.VO;

import java.io.Serializable;
import java.util.List;

/**
 * @author JoeObrien
 *
 */
public class SessionDetailVO implements Serializable {

	private static final long serialVersionUID = -5767861670867520105L;
	private String sessionId;
	private List<AccessDetailVO> sessionDetail;
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @return the sessionDetail
	 */
	public List<AccessDetailVO> getSessionDetail() {
		return sessionDetail;
	}
	/**
	 * @param sessionDetail the sessionDetail to set
	 */
	public void setSessionDetail(List<AccessDetailVO> sessionDetail) {
		this.sessionDetail = sessionDetail;
	}

	
}
