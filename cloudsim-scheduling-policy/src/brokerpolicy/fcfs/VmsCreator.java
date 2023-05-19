package brokerpolicy.fcfs;
import java.util.ArrayList;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Vm;;


/**
 * VmsCreator Creates VM Lists as per the User Requirements.
 * @author Linda J
 *
 */
public class VmsCreator {
	
	//vmlist creator function
	public ArrayList<Vm> createRequiredVms(int reqVms, int brokerId){
		
		ArrayList<Vm> vmlist = new ArrayList<Vm>();
		
    	//VM description
    	int vmid = 0;
    	int mips = 1000;
    	long size = 1000; //image size (MB)
    	int ram = 512; //vm memory (MB)
    	long bw = 1000;
    	int pesNumber = 1; //number of cpus
    	String vmm = "Xen"; //VMM name

    	
    	
    	for(vmid=0;vmid<reqVms;vmid++){
    		//add the VMs to the vmList
    		vmlist.add(new Vm(vmid, brokerId, mips, pesNumber, ram, bw, 
    				size, vmm, new CloudletSchedulerSpaceShared()));
    	}

    	System.out.println("VmsCreator function Executed... SUCCESS:)");
		return vmlist;
		
	}

}
