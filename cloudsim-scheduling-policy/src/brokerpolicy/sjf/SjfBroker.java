package brokerpolicy.sjf;
import java.util.Collections;
import java.util.Comparator;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class SjfBroker extends DatacenterBroker {

	public SjfBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	//scheduling function
	public void scheduleTaskstoVms(){
		int reqTasks=cloudletList.size();
		int reqVms=vmList.size();
		
		// sort the tasks in ascending order based on cloudlet length (unit in - million instructions)
		Collections.sort(cloudletList, new Comparator<Cloudlet>() {
			@Override
			public int compare(Cloudlet task1, Cloudlet task2) {
				return Long.compare(task1.getCloudletLength(), task2.getCloudletLength());
			}
		});
		
		System.out.println("\n\tSJF Broker Schedules\n");
    	for(int i=0;i<reqTasks;i++){
    		// execute shortest job first
    		int taskId = cloudletList.get(i).getCloudletId();
    		bindCloudletToVm(taskId, (i%reqVms));
    		System.out.println("Task"+cloudletList.get(i).getCloudletId()+" is bound with VM"+vmList.get(i%reqVms).getId());
    	}
    	
    	System.out.println("\n");
	}
}
