package org.archive.wayback.resourceindex.filters;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.util.ObjectFilter;

public class HostFilter implements ObjectFilter<CaptureSearchResult> {
    public final static char LEGAL_DEPOSIT_FLAG = 'L';
    public final static char OPEN_LICENCE_FLAG = 'O';
    public final static char EMPTY_FLAG = '-';
    private int filter;

    public HostFilter(WaybackRequest request, List<String> permittedHosts) {
	URL url;
	try {
	    url = new URL(request.getRequestUrl());
	    String host = url.getHost();
	    for (String permitted : permittedHosts) {
		if (permitted.equals(host)) {
		    filter = FILTER_INCLUDE;
		    return;
		}
	    }
	    if (request.isAnyEmbeddedContext()) {
		URL referrerUrl = new URL(request.getRefererUrl());
		String referrerHost = referrerUrl.getHost();
		for (String permitted : permittedHosts) {
		    if (permitted.equals(referrerHost)) {
			filter = FILTER_INCLUDE;
			return;
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    filter = FILTER_EXCLUDE;
	}
	filter = FILTER_EXCLUDE;
    }

    @Override
    public int filterObject(CaptureSearchResult r) {
	char flag = r.getRobotFlags().charAt(0);
	switch (flag) {
	case OPEN_LICENCE_FLAG:
	    return FILTER_INCLUDE;
	case EMPTY_FLAG:
	    return FILTER_EXCLUDE;
	case LEGAL_DEPOSIT_FLAG:
	    return filter;
	default:
	    return FILTER_EXCLUDE;
	}
    }
}
