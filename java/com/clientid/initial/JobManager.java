package com.clientid.initial;
import java.util.ArrayList;

public class JobManager extends ArrayList<IInitialized> implements IInitialized {

	
	private static final long serialVersionUID = 1L;

	@Override
	public String exec() {
		for(IInitialized job : this) {
			String msg = job.exec();
			System.out.println(String.format("%s\n%s", job.getClass().getName(), msg));
		}
		return "";
	}

}
