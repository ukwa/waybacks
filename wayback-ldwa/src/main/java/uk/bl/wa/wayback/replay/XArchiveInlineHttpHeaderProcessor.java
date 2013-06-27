package uk.bl.wa.wayback.replay;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.archive.wayback.ResultURIConverter;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.replay.HttpHeaderProcessor;

public class XArchiveInlineHttpHeaderProcessor implements HttpHeaderProcessor {

	private static String DEFAULT_PREFIX = "X-Wayback-Orig-";
	private String prefix = DEFAULT_PREFIX; 
	private Set<String> passThrough = null;
	
	public XArchiveInlineHttpHeaderProcessor() {
		passThrough = new HashSet<String>();
		passThrough.add(HTTP_CONTENT_TYPE_HEADER_UP);
		// Disable passthrough of Content-Disposition header:
		//passThrough.add(HTTP_CONTENT_DISP_HEADER_UP);
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void filter(Map<String, String> output, String key, String value,
			ResultURIConverter uriConverter, CaptureSearchResult result) {
		String keyUp = key.toUpperCase();

		output.put(prefix + key,value);
		if (passThrough.contains(keyUp)) {
//			if (keyUp.startsWith(HTTP_CONTENT_TYPE_HEADER_UP)) {
			// add this one as-is, too.
			output.put(key, value);
		}
	}
}
