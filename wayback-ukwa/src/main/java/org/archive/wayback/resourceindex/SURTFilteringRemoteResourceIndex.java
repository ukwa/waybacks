/**
 * 
 */
package org.archive.wayback.resourceindex;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.resourceindex.filterfactory.ClosestTrackingCaptureFilterGroup;
import org.archive.wayback.resourceindex.filterfactory.SURTCaptureFilterGroupFactory;
import org.archive.wayback.util.ObjectFilter;
import org.archive.wayback.util.ObjectFilterChain;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author anj
 *
 */
public class SURTFilteringRemoteResourceIndex extends RemoteResourceIndex {

	private String surtFile;
	private SURTCaptureFilterGroupFactory surtFilterFactory;

	public void setSurtFile(String surtFile) {
	    this.surtFile = surtFile;
	    if( this.surtFile == null || "".equals(this.surtFile)) {
	    	// Do no filtering at all if this is unset:
	    	return;
	    }
	    surtFilterFactory = new SURTCaptureFilterGroupFactory(surtFile);
	}

	public String getSurtFile() {
	    return this.surtFile;
	}
	

	
	/* (non-Javadoc)
	 * @see org.archive.wayback.resourceindex.RemoteResourceIndex#getSearchResultFilters(org.archive.wayback.core.WaybackRequest, org.archive.wayback.resourceindex.filterfactory.ClosestTrackingCaptureFilterGroup)
	 */
	@Override
	protected ObjectFilter<CaptureSearchResult> getSearchResultFilters(
			WaybackRequest wbRequest,
			ClosestTrackingCaptureFilterGroup closestGroup) {
		ObjectFilterChain<CaptureSearchResult> filters = (ObjectFilterChain<CaptureSearchResult>) super.getSearchResultFilters(wbRequest, closestGroup);
		if( filters == null ) {
			filters = new ObjectFilterChain<CaptureSearchResult>();
		}
		filters.addFilter(surtFilterFactory.getFilter(wbRequest));
		return filters;
	}


}
