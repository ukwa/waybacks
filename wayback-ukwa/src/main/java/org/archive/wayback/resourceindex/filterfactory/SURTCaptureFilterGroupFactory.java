package org.archive.wayback.resourceindex.filterfactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.archive.util.SurtPrefixSet;
import org.archive.wayback.UrlCanonicalizer;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.core.WaybackRequest;
import org.archive.wayback.exception.BadQueryException;
import org.archive.wayback.resourceindex.LocalResourceIndex;
import org.archive.wayback.resourceindex.filters.SURTFilter;
import org.archive.wayback.util.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SURTCaptureFilterGroupFactory implements FilterGroupFactory {
    protected static Logger logger = LoggerFactory
            .getLogger(SURTCaptureFilterGroupFactory.class);
    private SurtPrefixSet permittedSurts;
    private Thread updateThread;
    private File includeFile;
    private int updateInterval = 1200;

    public SURTCaptureFilterGroupFactory(String path) {
        includeFile = new File(path);
        startUpdaterThread();
    }

    @Override
    public CaptureFilterGroup getGroup(WaybackRequest request,
            UrlCanonicalizer canonicalizer, LocalResourceIndex index)
            throws BadQueryException {
        return new SURTCaptureFilterGroup(request, this);
    }

    public CaptureFilterGroup getGroup(WaybackRequest request)
            throws BadQueryException {
        return new SURTCaptureFilterGroup(request, this);
    }
    
    public ObjectFilter<CaptureSearchResult> getFilter(WaybackRequest request) {
    	return new SURTFilter(request, this);
    }

    public SurtPrefixSet getPermittedSurts() {
		return permittedSurts;
	}

	private void loadFile() throws IOException {
		SurtPrefixSet newPermittedSurts = new SurtPrefixSet();
        FileReader fileReader = new FileReader(includeFile);
        newPermittedSurts.importFrom(fileReader);
        fileReader.close();
        permittedSurts = newPermittedSurts;
        logger.info("Added " + permittedSurts.size()
                + " SURTS to inclusion filter.");
    }

    private void startUpdaterThread() {
        if (updateThread == null) {
            updateThread = new UpdaterThread(this, updateInterval);
            updateThread.start();
        }
    }

    private class UpdaterThread extends Thread {
        private SURTCaptureFilterGroupFactory service = null;
        private int runInterval;

        public UpdaterThread(SURTCaptureFilterGroupFactory service,
                int runInterval) {
            super("UpdaterThread");
            super.setDaemon(true);
            this.service = service;
            this.runInterval = runInterval;
            logger.info("UpdaterThread is alive.");
        }

        public void run() {
            while (true) {
                try {
                    try {
                        service.loadFile();
                    } catch (IOException e) {
                        logger.warn(e.getMessage());
                    }
                    Thread.sleep(runInterval * 1000);
                } catch (InterruptedException i) {
                    logger.warn(i.getMessage());
                    return;
                }
            }
        }
    }
}
