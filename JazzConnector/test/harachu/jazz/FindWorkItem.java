package harachu.jazz;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.foundation.common.text.XMLString;
import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IContributorManager;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.ITeamRepository.ILoginHandler;
import com.ibm.team.repository.client.ITeamRepository.ILoginHandler.ILoginInfo;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.IChangeEvent;
import com.ibm.team.repository.common.IContributor;
import com.ibm.team.repository.common.IContributorHandle;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemOperation;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.Expression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.SelectClause;
import com.ibm.team.workitem.common.expression.SortCriteria;
import com.ibm.team.workitem.common.expression.Statement;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.expression.VariableAttributeExpression;
import com.ibm.team.workitem.common.expression.variables.CurrentUserVariable;
import com.ibm.team.workitem.common.expression.variables.StatusVariable;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.ICategoryHandle;
import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.workflow.ICombinedWorkflowInfos;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

public class FindWorkItem {
	private static class LoginHandler implements ILoginHandler, ILoginInfo {

		private String fUserId;
		private String fPassword;

		private LoginHandler(String userId, String password) {
			fUserId = userId;
			fPassword = password;
		}

		public String getUserId() {
			return fUserId;
		}

		public String getPassword() {
			return fPassword;
		}

		public ILoginInfo challenge(ITeamRepository repository) {
			return this;
		}

	}

	private static class WorkItemInitialization extends WorkItemOperation {

		private String fSummary;
		private ICategoryHandle fCategory;

		public WorkItemInitialization(String summary, ICategoryHandle category) {
			super("Initializing Work Item");
			fSummary = summary;
			fCategory = category;
		}

		@Override
		protected void execute(WorkItemWorkingCopy workingCopy,
				IProgressMonitor monitor) throws TeamRepositoryException {
			IWorkItem workItem = workingCopy.getWorkItem();
			workItem.setHTMLSummary(XMLString.createFromPlainText(fSummary));
			workItem.setCategory(fCategory);
		}
	}

	public static void main(String[] args) {
		info("first");
		boolean result;
		TeamPlatform.startup();
		info("afterStartup");
		try {
			result = run(args);
			info("afterRun");
		} catch (TeamRepositoryException x) {
			x.printStackTrace();
			result = false;
		} finally {
			TeamPlatform.shutdown();
		}

		if (!result)
			System.exit(1);

	}

	private static boolean run(String[] args) throws TeamRepositoryException {

		// if (args.length != 7) {
		// System.out.println("Usage: CreateWorkItem <repositoryURI> <userId> <password> <projectArea> <workItemType> <summary> <category>");
		// return false;
		// }

		String repositoryURI = "https://chm01.dv.jp.honda.com:9443/jazz/";
		String userId = "JP203998";
		String password = "honda1";
		String projectAreaName = "JTST";
		String typeIdentifier = "task";
		String summary = "test";
		String categoryName = "JTST";

		ITeamRepository teamRepository = TeamPlatform
				.getTeamRepositoryService().getTeamRepository(repositoryURI);
		teamRepository.registerLoginHandler(new LoginHandler(userId, password));
		teamRepository.login(null);

		IProcessClientService processClient = (IProcessClientService) teamRepository
				.getClientLibrary(IProcessClientService.class);
		IAuditableClient auditableClient = (IAuditableClient) teamRepository
				.getClientLibrary(IAuditableClient.class);
		IWorkItemClient workItemClient = (IWorkItemClient) teamRepository
				.getClientLibrary(IWorkItemClient.class);

		URI uri = URI.create(projectAreaName.replaceAll(" ", "%20"));
		IProjectArea projectArea = (IProjectArea) processClient
				.findProcessArea(uri, null, null);
		if (projectArea == null) {
			System.out.println("Project area not found.");
			return false;
		}

		IWorkItem workItem = workItemClient.findWorkItemById(6225,
				IWorkItem.FULL_PROFILE, null);
		IContributorHandle iCon = workItem.getOwner();
		IContributor contributor = (IContributor) teamRepository.itemManager()
				.fetchCompleteItem(iCon, IItemManager.REFRESH, null);
		System.out.println(contributor.getName());

		List<IWorkItemType> workItemTypeList = workItemClient
				.findWorkItemTypes(projectArea, null);
		Map<String, String> workItemMap = new HashMap();
		for (IWorkItemType type : workItemTypeList) {
			System.out.println(type.getIdentifier());
			workItemMap.put(type.getIdentifier(), type.getDisplayName());
		}

		ICombinedWorkflowInfos wfInfo = workItemClient
				.findCombinedWorkflowInfos(projectArea, null);
		// IWorkflowInfo wfInfo = new
		// WorkflowUtilities().findWorkflowInfo(workItem, monitor);

		Identifier<IState>[] stateIds = wfInfo.getAllStateIds();

		Identifier<IState> selectedState = null;

		for (Identifier<IState> s : stateIds) {

			System.out.println("getStateName:" + wfInfo.getStateName(s));
			System.out
					.println("getStringIdentifier:" + s.getStringIdentifier());
			System.out.println("getStateGroup:" + wfInfo.getStateGroup(s));
			System.out.println("getIconName:" + wfInfo.getStateIconName(s));
			System.out.println("getToString:" + s.toString());
			System.out.println("--------------------------------------");

		}

		IContributorManager icm = teamRepository.contributorManager();
		IContributor jp203998 = icm.fetchContributorByUserId("JP203998", null);

		IQueryableAttributeFactory factory = QueryableAttributes
				.getFactory(IWorkItem.ITEM_TYPE);

		IQueryableAttribute queryableAttribute = factory.findAttribute(
				projectArea, IWorkItem.OWNER_PROPERTY, auditableClient, null);

		CurrentUserVariable currentUser = new CurrentUserVariable();
		// Expression queryExpression = new VariableAttributeExpression(
		// queryableAttribute, AttributeOperation.EQUALS, currentUser);
		AttributeExpression queryExpression = new AttributeExpression(
				queryableAttribute, AttributeOperation.EQUALS, iCon);
//		AttributeExpression queryExpression = new AttributeExpression(
//				queryableAttribute, AttributeOperation.EQUALS, jp203998);

		IQueryableAttribute projectAreaAttribute = factory.findAttribute(
				projectArea, IWorkItem.PROJECT_AREA_PROPERTY, auditableClient,
				null);
		AttributeExpression projectAreaExpression = new AttributeExpression(
				projectAreaAttribute, AttributeOperation.EQUALS, projectArea);

		// StatusVariable OpenAndInProgress = new
		// StatusVariable(StatusVariable.OPEN_AND_IN_PROGRESS_STATES);
		StatusVariable closedStateGroup = new StatusVariable(
				IWorkflowInfo.CLOSED_STATES_GROUP);
		IQueryableAttribute stateAttribute = factory.findAttribute(projectArea,
				IWorkItem.STATE_PROPERTY, auditableClient, null);
		Expression stateExpression = new VariableAttributeExpression(
				stateAttribute, AttributeOperation.NOT_EQUALS, closedStateGroup);

		Term term = new Term(Operator.AND);
		term.add(queryExpression);
		term.add(projectAreaExpression);
		term.add(stateExpression);



		SortCriteria[] sortCriteria = new SortCriteria[] { new SortCriteria(
				IWorkItem.MODIFIED_PROPERTY, SortCriteria.ASCENDING) };
		Statement statement = new Statement(new SelectClause(),
				(Expression) term, sortCriteria);

		IQueryClient queryClient = (IQueryClient) teamRepository
				.getClientLibrary(IQueryClient.class);

		IQueryResult<IResolvedResult<IWorkItem>> result = queryClient
				.getResolvedExpressionResults(projectArea, statement.getConditions(),
						IWorkItem.FULL_PROFILE);

		while (result.hasNext(null)) {
			IResolvedResult<IWorkItem> resolved = result.next(null);
			IWorkItem item = resolved.getItem();
			String id = String.valueOf(item.getId());
			String title = item.getHTMLSummary().getPlainText();
			String type = workItemMap.get(item.getWorkItemType());
			IWorkflowInfo info = workItemClient.findWorkflowInfo(item, null);
			String status = info.getStateName(item.getState2());
			System.out.println(id + ":" + title + ":" + type + ":" + status);
		}
		
		IChangeEvent event = IChangeEvent.FACTORY.newInstance(null);

		teamRepository.logout();

		return true;
	}

	static void info(String where) {
		System.out.println("----" + new Date() + "----" + where);
	}

}
