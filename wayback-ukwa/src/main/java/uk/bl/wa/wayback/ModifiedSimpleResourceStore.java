/**
 * 
 */
package uk.bl.wa.wayback;

import java.util.logging.Logger;

import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.Resource;
import org.archive.wayback.exception.ResourceNotAvailableException;
import org.archive.wayback.resourcestore.SimpleResourceStore;
import org.archive.wayback.resourcestore.resourcefile.ArcWarcFilenameFilter;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class ModifiedSimpleResourceStore extends SimpleResourceStore {
	
	private final static Logger LOGGER = Logger.getLogger(
			ModifiedSimpleResourceStore.class.getName());

	/* (non-Javadoc)
	 * @see org.archive.wayback.resourcestore.SimpleResourceStore#retrieveResource(org.archive.wayback.core.CaptureSearchResult)
	 */
	@Override
	public Resource retrieveResource(CaptureSearchResult result)
			throws ResourceNotAvailableException {
		
		String fileName = result.getFile();
		
		LOGGER.fine("Looking up "+fileName + " @" + result.getOffset());
		
		if(!fileName.endsWith(ArcWarcFilenameFilter.ARC_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.ARC_GZ_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.WARC_SUFFIX)
				&& !fileName.endsWith(ArcWarcFilenameFilter.WARC_GZ_SUFFIX)) {
			fileName = fileName + "#.warc.gz";
		}

		LOGGER.fine("Resolving as "+fileName);

		result.setFile(fileName);

		return super.retrieveResource(result);
	}

}
