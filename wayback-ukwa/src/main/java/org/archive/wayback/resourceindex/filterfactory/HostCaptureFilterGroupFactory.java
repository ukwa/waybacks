package org.archive.wayback.resourceindex.filterfactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.archive.wayback.UrlCanonicalizer;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.resourceindex.LocalResourceIndex;

public class HostCaptureFilterGroupFactory implements FilterGroupFactory {
    List<String> permittedHosts = new ArrayList<String>();

    public HostCaptureFilterGroupFactory(String path) {
	String line;
	try {
	    FileReader fileReader = new FileReader(path);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    while ((line = bufferedReader.readLine()) != null) {
		permittedHosts.add(line.trim());
	    }
	    bufferedReader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public CaptureFilterGroup getGroup(WaybackRequest request,
	    UrlCanonicalizer canonicalizer, LocalResourceIndex index)
	    throws BadQueryException {
	return new HostCaptureFilterGroup(request, permittedHosts);
    }

}
