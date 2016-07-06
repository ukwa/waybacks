package org.archive.wayback.resourceindex.filters;

import org.apache.commons.httpclient.URIException;
import org.archive.url.UsableURI;
import org.archive.url.UsableURIFactory;
import org.archive.util.SurtPrefixSet;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.resourceindex.filterfactory.SURTCaptureFilterGroupFactory;
import org.archive.wayback.util.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SURTFilter implements ObjectFilter<CaptureSearchResult> {
	public final static char LEGAL_DEPOSIT_FLAG = 'L';
	public final static char OPEN_LICENCE_FLAG = 'O';
	public final static char EXCLUDE_FLAG = 'X';
	public final static char EMPTY_FLAG = '-';

	protected static Logger logger = LoggerFactory.getLogger(SURTFilter.class);
	private int filter;

	public SURTFilter(WaybackRequest request, SURTCaptureFilterGroupFactory surtCaptureFilterGroupFactory) {
		try {
			UsableURI uuri = UsableURIFactory.getInstance(request
					.getRequestUrl());
			String surt = SurtPrefixSet.getCandidateSurt(uuri);
			for( String s : surtCaptureFilterGroupFactory.getPermittedSurts()) {
				logger.info("SURT: "+s);
			}
			if (surtCaptureFilterGroupFactory.getPermittedSurts().containsPrefixOf(surt)) {
				filter = FILTER_INCLUDE;
				logger.info("Allowing " + uuri.toString() + " (" + surt + ")");
			} else {
				filter = FILTER_EXCLUDE;
				logger.info("Denying " + uuri.toString() + " (" + surt + ")");
			}
		} catch (URIException e) {
			logger.warn(e.getMessage());
			filter = FILTER_EXCLUDE;
		}
	}

	@Override
	public int filterObject(CaptureSearchResult r) {
		char flag = r.getRobotFlags().charAt(0);
		switch (flag) {
		case OPEN_LICENCE_FLAG:
			return FILTER_INCLUDE;
		case EXCLUDE_FLAG:
			return FILTER_EXCLUDE;
		case LEGAL_DEPOSIT_FLAG:
			return filter;
		default:
			return filter;
		}
	}
}
