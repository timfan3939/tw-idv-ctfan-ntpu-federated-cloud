package tw.idv.ctfan.cloud.middleware.MPI;

import tw.idv.ctfan.cloud.middleware.Cluster.AdminAgent;
import tw.idv.ctfan.cloud.middleware.policy.data.JobNode;

public class MPIAdminAgent extends AdminAgent {
	private static final long serialVersionUID = -6002055057210057475L;

	public MPIAdminAgent() {
		super(new MPIJobType());
	}

	@Override
	public String GetJobAgentClassName() {
		return tw.idv.ctfan.cloud.middleware.MPI.MPIJobAgent.class.getName();
	}

	@Override
	public boolean InitilizeCluster() {
		return true;
	}

	@Override
	protected String OnEncodeClusterLoadInfo() {
		return (super.m_jobList.size()>0?"Busy":"Free");
	}

	@Override
	protected String OnEncodeJobInfo(JobNode jn) {
		return "Nothing to tell";
	}

	@Override
	public String OnEncodeNewJobAgent(JobNode jn) {
		String result = "";
			if(jn.GetContinuousAttribute("Thread")!=-1) {
				result += jn.GetDiscreteAttribute("Thread");
			}
			result += "\t";
			if(jn.GetDiscreteAttribute("Command")!=null) {
				result += jn.GetDiscreteAttribute("Command");
			}
		return result;
	}

	@Override
	public void OnTerminateCluster() {
		System.out.println("I'm going to be terminated.");
	}

}
