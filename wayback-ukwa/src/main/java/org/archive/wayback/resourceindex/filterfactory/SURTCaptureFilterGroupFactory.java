package org.archive.wayback.resourceindex.filterfactory;

import java.io.FileReader;
import java.io.IOException;

import org.archive.util.SurtPrefixSet;
import org.archive.wayback.UrlCanonicalizer;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.resourceindex.LocalResourceIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SURTCaptureFilterGroupFactory implements FilterGroupFactory {
    protected static Logger logger = LoggerFactory
	    .getLogger(SURTCaptureFilterGroupFactory.class);
    SurtPrefixSet permittedSurts = new SurtPrefixSet();

    public SURTCaptureFilterGroupFactory(String path) {
	try {
	    FileReader fileReader = new FileReader(path);
	    permittedSurts.importFrom(fileReader);
	    fileReader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	logger.debug("Added " + permittedSurts.size() + " SURTS to inclusion filter.");
    }

    @Override
    public CaptureFilterGroup getGroup(WaybackRequest request,
	    UrlCanonicalizer canonicalizer, LocalResourceIndex index)
	    throws BadQueryException {
	return new SURTCaptureFilterGroup(request, permittedSurts);
    }

}
