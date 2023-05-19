package brokerpolicy.genetic;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class CloudSimSimulation {
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;
	
	private static int reqTasks;
	private static int reqVms;
	private static List<Integer> cloudletVmMapping;
	
	static double totalExecTime = 0.0;
	
	public CloudSimSimulation(int reqTasks, int reqVms, List<Integer> cloudletVmMapping) {
		CloudSimSimulation.reqTasks = reqTasks;
		CloudSimSimulation.reqVms = reqVms;
		CloudSimSimulation.cloudletVmMapping = cloudletVmMapping;
	}
	
	public double runSimulation() {
		
		Log.printLine("Starting Simulation to calculate total execution time ...");

        try {
        	// First step: Initialize the CloudSim package. It should be called
            	// before creating any entities.
            	int num_user = 1;   // number of cloud users
            	Calendar calendar = Calendar.getInstance();
            	boolean trace_flag = false;  // mean trace events

            	// Initialize the CloudSim library
            	CloudSim.init(num_user, calendar, trace_flag);

            	// Second step: Create Datacenters
            	//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
            	@SuppressWarnings("unused")
				Datacenter datacenter0 = createDatacenter("Datacenter_0");

            	//Third step: Create Broker
            	GABroker broker = createBroker();
            	int brokerId = broker.getId();

            	//Fourth step: Create one virtual machine
            	vmlist = new VMCreator().createRequiredVms(reqVms, brokerId);


            	//submit vm list to the broker
            	broker.submitVmList(vmlist);


            	//Fifth step: Create two Cloudlets
            	cloudletList = new CloudletCreator().createUserCloudlet(reqTasks, brokerId);
  	
            	//submit cloudlet list to the broker
            	broker.submitCloudletList(cloudletList);
            	
	
            	System.out.println("submitted cloudlet list to broker. Now calling scheduleTaskstoVms");
            	
            	broker.scheduleTaskstoVms(cloudletVmMapping);
            	System.out.println("============== after the call ============");
	
        	
            	// Sixth step: Starts the simulation
            	CloudSim.startSimulation();


            	// Final step: Print results when simulation is over
            	List<Cloudlet> newList = broker.getCloudletReceivedList();

            	CloudSim.stopSimulation();
            	//List<Cloudlet> newList = broker.getCloudletReceivedList();

            	totalExecTime = calculateTotalTime(newList);
            	Log.printLine("Simulation finished finished!");
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        return totalExecTime;
	}
	
	private static Datacenter createDatacenter(String name){
		Datacenter datacenter=new DataCenterCreator().createUserDatacenter(name, reqVms);			

        return datacenter;
    }

    //We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
    //to the specific rules of the simulated scenario
    private static GABroker createBroker(){

    	GABroker broker = null;
        try {
		broker = new GABroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return broker;
    }
    
    double calculateTotalTime(List<Cloudlet> cloudletReceivedList) {
    	double execTime = 0.0;
    	
    	for (Cloudlet task : cloudletReceivedList) {
    		execTime += task.getActualCPUTime();
    	}
    	printCloudletList(cloudletReceivedList);
    	return execTime;
    }
    
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
                Log.print("SUCCESS");

            	Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                     indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
                         indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }

    }
}