package org.archive.wayback.resourceindex.filterfactory;

import java.util.List;

import org.archive.util.SurtPrefixSet;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.SearchResults;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.AccessControlException;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.exception.ResourceNotInArchiveException;
import org.archive.wayback.resourceindex.filters.SURTFilter;
import org.archive.wayback.util.ObjectFilter;
import org.archive.wayback.util.ObjectFilterChain;

public class SURTCaptureFilterGroup implements CaptureFilterGroup {
    private ObjectFilterChain<CaptureSearchResult> chain = null;

    public SURTCaptureFilterGroup(WaybackRequest request, SurtPrefixSet permittedSurts) {
	chain = new ObjectFilterChain<CaptureSearchResult>();
	chain.addFilter(new SURTFilter(request, permittedSurts));
    }

    @Override
    public void annotateResults(SearchResults results)
	    throws ResourceNotInArchiveException, BadQueryException,
	    AccessControlException {
    }

    @Override
    public List<ObjectFilter<CaptureSearchResult>> getFilters() {
	return chain.getFilters();
    }

}
