/**
 * 
 */
package org.archive.wayback.resourceindex;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.CaptureSearchResults;
import org.archive.wayback.core.SearchResults;
import org.archive.wayback.core.UrlSearchResults;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.AccessControlException;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.exception.ResourceIndexNotAvailableException;
import org.archive.wayback.exception.ResourceNotInArchiveException;
import org.archive.wayback.exception.UnavailableForLegalReasonsException;
import org.archive.wayback.resourceindex.filterfactory.ClosestTrackingCaptureFilterGroup;
import org.archive.wayback.resourceindex.filterfactory.SURTCaptureFilterGroupFactory;
import org.archive.wayback.util.ObjectFilter;
import org.archive.wayback.util.ObjectFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author anj
 *
 */
public class SURTFilteringRemoteResourceIndex extends RemoteResourceIndex {

	protected static Logger logger = LoggerFactory.getLogger(SURTFilteringRemoteResourceIndex.class);

	private String surtFile;
	private SURTCaptureFilterGroupFactory surtFilterFactory;
	
    // Show a 451 error when content is unavailable, rather than silently
    // suppressing the results.
    private boolean declareLegalRestriction = true;

    private boolean useWhitelist = true;

	public void setSurtFile(String surtFile) {
		this.surtFile = surtFile;
		if( this.surtFile == null || "".equals(this.surtFile)) {
			// Do no filtering at all if this is unset:
			logger.warn("No CDX whitelist set - assuming all content is whitelisted.");
            this.useWhitelist = false;
			return;
		}
		logger.info("Firing up CDX whitelist...");
		surtFilterFactory = new SURTCaptureFilterGroupFactory(surtFile);
	}

	public String getSurtFile() {
		return this.surtFile;
	}

	/*
	 * (non-Javadoc)
	 * @see org.archive.wayback.resourceindex.RemoteResourceIndex#query(org.archive.wayback.core.WaybackRequest)
	 */
	@Override
	public SearchResults query(WaybackRequest wbRequest) throws ResourceIndexNotAvailableException,
			ResourceNotInArchiveException, BadQueryException, AccessControlException {
		SearchResults results = super.query(wbRequest);
        if (!useWhitelist) {
			logger.debug("Not filtering query results...");
			return results;
		}
		logger.debug("Filtering query results...");
		// Here, rather than swallowing them silently, we make our holdings known, but raise a 451 on access:
		if( results instanceof UrlSearchResults ) {
			UrlSearchResults urs = (UrlSearchResults) results;
			logger.debug("IS UrlSearchResults... " + urs);
		} else if( results instanceof CaptureSearchResults ) {
			CaptureSearchResults crs = (CaptureSearchResults) results;
			for( CaptureSearchResult r : crs ) {
				ObjectFilter<CaptureSearchResult> filter = surtFilterFactory.getFilter(r.getOriginalUrl());
				if( filter.filterObject(r) == ObjectFilter.FILTER_EXCLUDE ) {
			        // If a result was found, but has been excluded here, then it's because
			        // of the legal deposit restrictions.
			        // Therefore, attempt to raise a clear HTTP 451 error in this case:
                    throw new UnavailableForLegalReasonsException(
                            "This Legal Deposit resource can only be accessed on site in a Legal Deposit Library reading room.");
				}
			}
		}
		
		return results;
	}

	/* (non-Javadoc)
	 * @see org.archive.wayback.resourceindex.RemoteResourceIndex#getSearchResultFilters(org.archive.wayback.core.WaybackRequest, org.archive.wayback.resourceindex.filterfactory.ClosestTrackingCaptureFilterGroup)
	 */
	@Override
	protected ObjectFilter<CaptureSearchResult> getSearchResultFilters(
			WaybackRequest wbRequest,
			ClosestTrackingCaptureFilterGroup closestGroup) {
		ObjectFilterChain<CaptureSearchResult> filters = (ObjectFilterChain<CaptureSearchResult>) super.getSearchResultFilters(wbRequest, closestGroup);
        if (filters == null) {
            filters = new ObjectFilterChain<CaptureSearchResult>();
        }
        // Use the exclude file to drop unwanted results:
        filters.addFilter(wbRequest.getExclusionFilter());
	// Use embargo filter to drop unwanted results:       
        filters.addFilter(new DateEmbargoFilter(wbRequest.getAccessPoint().getEmbargoMS()));

		// Optionally, silently filter using the white list:
        if (useWhitelist && !declareLegalRestriction) {
			filters.addFilter(surtFilterFactory.getFilter(wbRequest));
		}
		return filters;
	}

}
