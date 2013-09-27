package harachu.mhr.jazz;

import java.util.List;
import java.util.logging.Logger;

import harachu.jazz.WorkItemOverview;
import harachu.mhr.model.ActivityList;

public class ActivityTranslator {
	public static int refillSize = 5;
	private static Logger logger = Logger.getLogger("mhr");

	public static ActivityList refill(ActivityList activityList,
			List<WorkItemOverview> workitemList) {
		int size = (workitemList.size() < refillSize ? workitemList.size()
				: refillSize);
		logger.info("size is "+size);
		
		for (int i = 0; i < size; i++) {
			WorkItemOverview workitem = workitemList.get(i);
			String activity = workitem.getId() + ":" + workitem.getTask() + ","
					+ workitem.getType() + "," + workitem.getCategory();
			activityList.pushAndStore(activity);
		}
		return activityList;
	}

}
