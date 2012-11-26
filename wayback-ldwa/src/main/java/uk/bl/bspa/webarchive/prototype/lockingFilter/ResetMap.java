package uk.bl.bspa.webarchive.prototype.lockingFilter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quartz Job to Reset Concurrent HashMap
 * 
 * @author JoeObrien
 *
 */
    public class ResetMap implements Job {		
    	
    	private static final Logger logger = LoggerFactory.getLogger(ResetMap.class);
    	
    	@Override
		public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
    		logger.info("Resetting all Page Locks via Quartz Job");
    		AccessList.getInstance().removeAllLocks();
    						
		}
    }
