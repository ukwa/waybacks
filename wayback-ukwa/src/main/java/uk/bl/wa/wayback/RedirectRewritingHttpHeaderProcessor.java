/**
 * 
 */
package uk.bl.wa.wayback;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.archive.wayback.ResultURIConverter;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.replay.HttpHeaderProcessor;
import org.archive.wayback.util.url.UrlOperations;

/**
 * 
 * A very minor modification to disable the CONTENT_DISPOSITION header being passed through.
 * This was because, in the LD playback environment, attachment=download causes playback problems.
 * 
 * @see org.archive.wayback.replay.RedirectRewritingHttpHeaderProcessor
 * 
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */

public class RedirectRewritingHttpHeaderProcessor 
	implements HttpHeaderProcessor {

	private static String DEFAULT_PREFIX = null;
	private String prefix = DEFAULT_PREFIX; 
	private Set<String> passThroughHeaders = null;
	private Set<String> rewriteHeaders = null;
	private boolean passThroughContentDisposition = true;
	
	public RedirectRewritingHttpHeaderProcessor() {
		passThroughHeaders = new HashSet<String>();
		passThroughHeaders.add(HTTP_CONTENT_TYPE_HEADER_UP);
		// Conditionally pass-through the content disposition header.
		if( this.passThroughContentDisposition ) {
			passThroughHeaders.add(HTTP_CONTENT_DISP_HEADER_UP);
		}
		
		rewriteHeaders = new HashSet<String>();
		rewriteHeaders.add(HTTP_LOCATION_HEADER_UP);
		rewriteHeaders.add(HTTP_CONTENT_LOCATION_HEADER_UP);
		rewriteHeaders.add(HTTP_CONTENT_BASE_HEADER_UP);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	/**
	 * @return the passThroughContentDisposition
	 */
	public boolean isPassThroughContentDisposition() {
		return passThroughContentDisposition;
	}

	/**
	 * @param passThroughContentDisposition the passThroughContentDisposition to set
	 */
	public void setPassThroughContentDisposition(
			boolean passThroughContentDisposition) {
		this.passThroughContentDisposition = passThroughContentDisposition;
		if( passThroughContentDisposition ) {
			passThroughHeaders.add( HTTP_CONTENT_DISP_HEADER_UP );
		} else {
			passThroughHeaders.remove( HTTP_CONTENT_DISP_HEADER_UP );
		}
	}

	/* (non-Javadoc)
	 * @see org.archive.wayback.replay.HttpHeaderProcessor#filter(java.util.Map, java.lang.String, java.lang.String, org.archive.wayback.ResultURIConverter, org.archive.wayback.core.CaptureSearchResult)
	 */
	public void filter(Map<String, String> output, String key, String value,
			ResultURIConverter uriConverter, CaptureSearchResult result) {

		String keyUp = key.toUpperCase();

		// first stick it in as-is, or with prefix, then maybe we'll overwrite
		// with the later logic.
		if(prefix == null) {
			if(!keyUp.equals(HTTP_LENGTH_HEADER_UP)) {
				output.put(key, value);
			}
		} else {
			output.put(prefix + key, value);
		}

		// rewrite Location header URLs
		if(rewriteHeaders.contains(keyUp)) {
			String baseUrl = result.getOriginalUrl();
			String cd = result.getCaptureTimestamp();
			// by the spec, these should be absolute already, but just in case:
			String u = UrlOperations.resolveUrl(baseUrl, value);

			output.put(key, uriConverter.makeReplayURI(cd,u));

		} else if(passThroughHeaders.contains(keyUp)) {
			// let's leave this one as-is:
			output.put(key,value);
		}
	}
}