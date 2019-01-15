# CloudSim Simulation Modeling Project #

1 Cloud Service Provider / 1 Datacenter.
  - 2 hosts/servers created on each CSP.
	config: RAM = 44.5 GB,
		storage = 10,000 GB
		bandwidth = 100 Gbps.
  - VM characteristics:
	RAM = 512 MB
	processing power = 250 MIPS
	bandwidth = 1 Gbps
	image size = 256 MB
  - Cloudlets characteristics:
	cloudle length = 40,000 MIPS
	file size = 300 MB
	output file = 300 MB
	
Condition:
- No. of cloudlets and VMs are varied from 68-178, to evaluate the effect on time and cost
- VMAllocationPolicySimple is used
- VmSchedulerSpaceShared or VmSchedulerTimeShared
- BwProvisioningSimple
- CloudletSchedulerTimeShared or CloudletSchedulerSpaceShared