/**
 * 
 */
package uk.bl.wa.wayback;

import java.util.logging.Logger;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.Resource;
import org.archive.wayback.exception.ResourceNotAvailableException;
import org.archive.wayback.resourcestore.SimpleResourceStore;

/**
 * 
 * WebHDFS does not support Range requests (see https://issues.apache.org/jira/browse/HDFS-2316?focusedCommentId=13138726&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-13138726)
 * 
 *     "> 2. ranges
 *      Since it is optional in the http spec, let's use offset and length query parameters."
 * 
 * So this class maps a vanilla request into a WebHDFS-style offset request.
 * 
 * Use the 'prefix' parameter from SimpleResourceStore to set the host and port of the WebHDFS service.
 * 
 * @author AnJackson
 *
 */
public class WebHDFSResourceStore extends SimpleResourceStore {

	private final static Logger LOGGER = Logger.getLogger(
			WebHDFSResourceStore.class.getName());

	/* (non-Javadoc)
	 * @see org.archive.wayback.resourcestore.SimpleResourceStore#retrieveResource(org.archive.wayback.core.CaptureSearchResult)
	 */
	@Override
	public Resource retrieveResource(CaptureSearchResult result)
			throws ResourceNotAvailableException {
		
		LOGGER.fine("Looking up "+result.getFile()+result.getOffset());

		// Move offset into a query parameter:
		String fileName = result.getFile();
		// Remove any existing fragment identifier:s
		fileName = fileName.replaceFirst("#.*$", "");
		// Add WebHDFS parameters:
		fileName = "/webhdfs/v1" + fileName + "?user.name=hdfs&op=OPEN&offset="+result.getOffset()+"#.warc.gz";
		// Update the search result object:
		result.setFile(fileName);
		result.setOffset(0);

		LOGGER.fine("Resolving as "+result.getFile()+result.getOffset());
		
		return super.retrieveResource(result);
	}
	
}
