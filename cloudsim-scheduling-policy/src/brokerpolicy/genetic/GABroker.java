package brokerpolicy.genetic;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;

/**
 * A Broker that schedules Tasks to the VMs 
 * as per FCFS Scheduling Policy
 * @author Linda J
 *
 */
public class GABroker extends DatacenterBroker {

	public GABroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	 public void scheduleTaskstoVms(List<Integer> genome){
		int reqTasks=cloudletList.size();
		
		// genome: create ArrayList for reqTasks
		// population(size): generate genomes to generate population of size
		// fitness function
		// selection: selection two high fitness score parents to next gen
		// cross-over : for next gen
		// mutation : for next gen
		
		System.out.println("\n\tGenetic Broker bind cloudlet to vms \n");
    	for(int i = 0; i < reqTasks; i++) {
    		bindCloudletToVm(i, (genome.get(i)));
    		System.out.println("Task" + cloudletList.get(i).getCloudletId()+
    				" is bound with VM" + vmList.get(genome.get(i)).getId());
    	}
    	System.out.println("\n");
	} 
}