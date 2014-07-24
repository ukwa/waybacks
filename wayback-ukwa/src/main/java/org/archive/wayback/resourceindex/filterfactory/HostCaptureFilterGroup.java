package org.archive.wayback.resourceindex.filterfactory;

import java.util.List;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.SearchResults;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.AccessControlException;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.exception.ResourceNotInArchiveException;
import org.archive.wayback.resourceindex.filters.HostFilter;
import org.archive.wayback.util.ObjectFilter;
import org.archive.wayback.util.ObjectFilterChain;

public class HostCaptureFilterGroup implements CaptureFilterGroup {
    private ObjectFilterChain<CaptureSearchResult> chain = null;

    public HostCaptureFilterGroup(WaybackRequest request, List<String> permittedHosts) {
	chain = new ObjectFilterChain<CaptureSearchResult>();
	chain.addFilter(new HostFilter(request, permittedHosts));
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
