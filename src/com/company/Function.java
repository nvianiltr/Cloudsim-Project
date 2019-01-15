package com.company;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Function {
    /**
     * Creates the datacenter.
     */
    public static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<Host>();
        List<Pe> peList = new ArrayList<Pe>(); // PE == CPU

        int mips = 10000;
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        // Create Hosts with its id and list of PEs and add them to the list of machines
        int ram = 44500; //host memory (44.5 GB = 44,500 MB)
        long storage = 10000000; // host storage (10,000 GB = 10,000,000 MB)
        int bw = 100000000; // bandwidth (100 Gbps = 100,000,000 Kbps)

        for (int hostId = 0; hostId < 2; hostId++) {
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList,
                            new VmSchedulerTimeShared(peList)
                    )
            );
        }

        // Create a DatacenterCharacteristics object that stores the
        // properties of a data center: architecture, OS, list of
        // Machines, allocation policy: time- or space-shared, time zone
        // and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 5.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource - compute cost per second
        double costPerMem = 1.0;        // the cost of using memory in this resource
        double costPerStorage = 0.05;    // the cost of using storage in this resource
        double costPerBw = 0.10;            // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();    //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // Create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    /**
     * Creates the broker.
     */
    public static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker("Broker");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects.
     */
    public static void printCloudletList(List<Cloudlet> list, int no_of_cloudlets, long bw, int ram, long length, int mips) {
        int size = list.size();
        Cloudlet cloudlet;
        int cloudlets_160 = 0;
        int cloudlets_320 = 0;
        int cloudlets_480 = 0;
        int cloudlets_640 = 0;
        int cloudlets_800 = 0;

        String indent = "    ";
        Log.printLine();
        Log.printLine("========================= OUTPUT =========================");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + String.format("%02d", cloudlet.getCloudletId()) + indent + indent);

            long finishTime = Math.round(cloudlet.getFinishTime());
            if (finishTime == 160) {
                cloudlets_160 += 1;
            } else if (finishTime == 320) {
                cloudlets_320 += 1;
            } else if (finishTime == 480) {
                cloudlets_480 += 1;
            } else if (finishTime == 640) {
                cloudlets_640 += 1;
            } else if (finishTime == 800) {
                cloudlets_800 += 1;
            }

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId()
                        + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent
                        + dft.format(cloudlet.getActualCPUTime()) + indent
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent
                        + dft.format(cloudlet.getFinishTime()));
            }
        }
        // --- Fig. 3 & 4 ---
        Log.printLine("\nCloudlet & VM Configuration");
        Log.printLine("Total Cloudlets          : " + no_of_cloudlets);
        Log.printLine("Total Cloudlet length    : " + length);
        Log.printLine("Total VM bandwidth       : " + bw);
        Log.printLine("Total VM MIPS            : " + mips);
        Log.printLine("Total VM RAM             : " + ram);

        Log.printLine("\nCloudlet Completion Time");
        Log.printLine(cloudlets_160 + " cloudlets completed at 160s.");
        Log.printLine(cloudlets_320 + " cloudlets completed at 320s.");
        Log.printLine(cloudlets_480 + " cloudlets completed at 480s.");
        Log.printLine(cloudlets_640 + " cloudlets completed at 640s.");
        Log.printLine(cloudlets_800 + " cloudlets completed at 800s.");
    }
}
