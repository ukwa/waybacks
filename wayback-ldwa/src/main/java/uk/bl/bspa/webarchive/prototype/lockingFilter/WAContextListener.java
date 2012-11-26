/**
 * 
 */
package uk.bl.bspa.webarchive.prototype.lockingFilter;

import java.text.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author JoeObrien
 *
 */
public class WAContextListener implements ServletContextListener {
	
	private Scheduler scheduler = null;

	/**
	 * 
	 */
	public WAContextListener() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		try {
			scheduler.shutdown();
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        	System.out.println("Quartz failed to shutdown: " + ex);
        } catch (SchedulerException ex) {
        	System.out.println("Quartz failed to shutdown: " + ex);
        }

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		context.setAttribute("lockList", AccessList.getInstance());
		
		setQuartzSchedule();

	}

	/**
	 * Set the Quartz Scheduler
	 * 
	 * The AccessList Concurrent Hashmap is reset at midnight everyday, releasing all 
	 * page locks
	 */
	private void setQuartzSchedule(){
		
 
		try {
			
			JobDetail job = JobBuilder.newJob(ResetMap.class).withIdentity("MapResetJob","group1").build();
			
			// Trigger for midnight every night
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MapResetTrigger","group1").withSchedule(
    				CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
 
			//schedule it
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
	    	scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			System.out.println("SCHEDULEREXCEPTION: " + e);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("PARSEEXCEPTION: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("EXCEPTION: " + e);
			e.printStackTrace();
		}
	}

}
