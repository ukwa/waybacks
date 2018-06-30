/**
 * 
 */
package uk.bl.wa.wayback;

import java.io.IOException;
import java.util.logging.Logger;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.Resource;
import org.archive.wayback.exception.ResourceNotAvailableException;
import org.archive.wayback.resourcestore.SimpleResourceStore;
import org.archive.wayback.resourcestore.resourcefile.ArcWarcFilenameFilter;
import org.archive.wayback.resourcestore.resourcefile.ResourceFactory;

/**
 * 
 * WebHDFS does not support Range requests (see
 * https://issues.apache.org/jira/browse/HDFS-2316?focusedCommentId=13138726&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-13138726)
 * 
 * "> 2. ranges Since it is optional in the http spec, let's use offset and
 * length query parameters."
 * 
 * So this class maps a vanilla request into a WebHDFS-style offset request.
 * 
 * Use the 'prefix' parameter from SimpleResourceStore to set the host:port/path
 * of the WebHDFS service,
 * 
 * e.g. http://hadoop:50070/webhdfs/v1
 * 
 * @author AnJackson
 *
 */

public class WebHDFSResourceStore extends SimpleResourceStore {

	private final static Logger LOGGER = Logger.getLogger(
			WebHDFSResourceStore.class.getName());

	private static String HTTP_ERROR = "HTTP";
	private static String HTTP_502 = "502";

	private String hdfsUser = "hdfs";

	public String getHdfsUser() {
		return hdfsUser;
	}

	public void setHdfsUser(String hdfsUser) {
		this.hdfsUser = hdfsUser;
	}

	/* (non-Javadoc)
	 * @see org.archive.wayback.resourcestore.SimpleResourceStore#retrieveResource(org.archive.wayback.core.CaptureSearchResult)
	 */
	@Override
	public Resource retrieveResource(CaptureSearchResult result)
			throws ResourceNotAvailableException {
		LOGGER.fine("Following-up Capture Search Result: " + result.getCaptureTimestamp()+ " of " + result.getOriginalUrl());
		LOGGER.fine("Looking up "+result.getFile()+"@"+result.getOffset()+".."+result.getCompressedLength());

		// Move offset into a query parameter:
		String fileName = result.getFile();
		// Remove any existing fragment identifiers:
		fileName = fileName.replaceFirst("#.*$", "");
		
		// Add WebHDFS parameters:
		fileName = fileName + "?user.name="+hdfsUser+"&op=OPEN&offset="+result.getOffset();
		// Add the compressed length, if we know it:
		if (result.getCompressedLength() > 0) {
			fileName = fileName + "&length=" + result.getCompressedLength();
		}
		fileName = fileName + "#.warc.gz";
		
		// Update the search result object:
		result.setFile(fileName);
        result.setOffset(-1);

		LOGGER.fine("Resolving as "+result.getFile()+"@"+result.getOffset());

		return super.retrieveResource(result);
	}

	/**
	 * Experimental re-implementation of superclass logic (not in use).
	 * 
	 * @param result
	 * @return
	 * @throws ResourceNotAvailableException
	 */
	private Resource _retrieveResource(CaptureSearchResult result)
			throws ResourceNotAvailableException {

		LOGGER.fine("Looking up "+result.getFile()+result.getOffset());

		// Move offset into a query parameter:
		String fileName = result.getFile();
		// Remove any existing fragment identifier:s
		fileName = fileName.replaceFirst("#.*$", "");
		// Add WebHDFS parameters:
		fileName = "/webhdfs/v1/0_original/crawler05/heritrix/output/warcs/frequent/" + fileName + "?user.name="+hdfsUser+"&op=OPEN&offset="+result.getOffset()+"#.warc.gz";
		final long offset = 0;


		// If includeFilter is provided, filter out paths that don't contain the include filter
		if (getIncludeFilter() != null) {
			if (!fileName.contains(getIncludeFilter())) {
				throw new ResourceNotAvailableException("Resource " + fileName + " not found in this store", fileName);
			}
		}

		if(!fileName.endsWith(ArcWarcFilenameFilter.ARC_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.ARC_GZ_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.WARC_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.WARC_GZ_SUFFIX)) {
			fileName = fileName + ArcWarcFilenameFilter.ARC_GZ_SUFFIX;
		}

		String fileUrl;
		if ( getRegex() != null && getReplace() != null )
		{
			fileUrl = fileName.replaceAll( getRegex(), getReplace() );
		}
		else
		{
			fileUrl = getPrefix() + fileName;
		}

		Resource r = null;
		try {
			int attempts = getRetries();
			while(attempts-- > 0) {
				try {
					r = ResourceFactory.getResource(fileUrl, offset);
					break;
				} catch (IOException e) {
					String message = e.getMessage();
					if(attempts > 0 
							&& message.contains(HTTP_ERROR) 
							&& message.contains(HTTP_502)) {

						LOGGER.info(String.format(
								"Failed attempt for (%s) retrying with" +
										" (%d) attempts left",fileUrl,attempts));
					} else {
						throw e;
					}
				}
			}

		} catch (IOException e) {
			String msg = fileUrl + " - " + e;
			LOGGER.info("Unable to retrieve:" + msg);

			throw new ResourceNotAvailableException(msg, fileUrl, e);
		}
		return r;
	}

}
