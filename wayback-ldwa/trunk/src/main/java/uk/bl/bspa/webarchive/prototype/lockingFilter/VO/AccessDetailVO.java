/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter.VO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author JoeObrien
 *
 */
public class AccessDetailVO implements Serializable, Comparable<AccessDetailVO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9001572652355880162L;
	
	public AccessDetailVO() {

	}
	
	/**
	 * @param sessionId
	 * @param page
	 * @param dateInitiated
	 */
	public AccessDetailVO(String sessionId, String page, Date dateInitiated, String ip,String hostName) {
		this.sessionId = sessionId;
		this.dateInitiated = dateInitiated;
		this.page = page;
		this.ipAddress = ip;
		this.hostName = hostName;
	}
	
	private String sessionId;
	private Date dateInitiated;
	private String page;
	private String ipAddress;
	private String hostName;
	
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
	 * @return the dateInitiated
	 */
	public Date getDateInitiated() {
		return dateInitiated;
	}
	/**
	 * @param dateInitiated the dateInitiated to set
	 */
	public void setDateInitiated(Date dateInitiated) {
		this.dateInitiated = dateInitiated;
	}
	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccessDetailVO [sessionId=" + sessionId + ", dateInitiated="
				+ dateInitiated + ", page=" + page + ", ipAddress=" + ipAddress
				+ "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result
				+ ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessDetailVO other = (AccessDetailVO) obj;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}
	
	
	/* 
	 * Sort On Session Id
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AccessDetailVO access) {
		if(this.sessionId != null && access.sessionId != null){
			return this.sessionId.compareToIgnoreCase(access.sessionId);
		 }
		 return 0;
	}

}
