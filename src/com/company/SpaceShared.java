package com.company;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;

public class SpaceShared extends Function{
    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmList;

    public static void main(String[] args) {
        Log.printLine("Starting Smart Grid simulation...");

        try {
            // 1.0: Initialize the CloudSim package. It should be called before creating any entities.
            int num_user = 1; // number of cloud users
            Calendar calendar = Calendar.getInstance(); // Calendar whose fields have been initialized with the current date and time.
            boolean trace_flag = false; // trace events
            CloudSim.init(num_user, calendar, trace_flag);

            // 2.0: Create 1 Datacenter
            // Responsible for creating the core infrastructure services required for Smart Grid
            Datacenter datacenter0 = createDatacenter("Datacenter_0");

            // 3.0: Create Broker
            DatacenterBroker broker = createBroker();
            int brokerId = broker.getId();

            // 4.0: Create VMs
            vmList = new ArrayList<Vm>();
            int no_of_VM = 68;

            // VM characteristics
            int mips = 250; // processing power (250 MIPS)
            long size = 256; // image size (256MB)
            int ram = 512; // vm memory (512MB)
            long bw = 1000000; // bandwidth (1Gbps = 1000000Kbps)
            int pesNumber = 1; // number of cpus
            String vmm = "Xen"; // VMM name

            for (int vmId = 1; vmId <= no_of_VM; vmId++) {
                Vm virtualMachine = new Vm(vmId, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
                vmList.add(virtualMachine);
            }

            // submit vm list to the broker
            broker.submitVmList(vmList);

            // 5.0: Create Cloudlets
            cloudletList = new ArrayList<Cloudlet>();
            int no_of_cloudlet = 68;

            // Cloudlet characteristics
            long length = 40000; // cloudlet length (40,000 MIPS)
            long fileSize = 300; // file size (300MB)
            long outputSize = 300; // output size (300MB)
            UtilizationModel utilizationModel = new UtilizationModelFull();

            for (int cloudletId = 1; cloudletId <= no_of_cloudlet; cloudletId++) {
                Cloudlet cloudlet = new Cloudlet(cloudletId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                cloudlet.setUserId(brokerId);
                cloudletList.add(cloudlet);
            }

            // submit cloudlet list to the broker
            broker.submitCloudletList(cloudletList);

            // 6.0: Starts the simulation
            CloudSim.startSimulation();

            CloudSim.stopSimulation();

            //Final step: Print results when simulation is over
            List<Cloudlet> finalCloudletExecutionResults = broker.getCloudletReceivedList();
            printCloudletList(finalCloudletExecutionResults);

            Log.printLine("Smart Grid simulation finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }
}