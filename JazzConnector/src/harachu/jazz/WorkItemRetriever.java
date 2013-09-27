package harachu.jazz;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.process.client.IProcessClientService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IAuditableClient;
import com.ibm.team.workitem.client.IQueryClient;
import com.ibm.team.workitem.client.IWorkItemClient;
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
import com.ibm.team.workitem.common.model.ICategory;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

public class WorkItemRetriever {
	private static class LoginHandler implements ILoginHandler2 {
		String userId, password;

		LoginHandler(String userId, String password) {
			this.userId = userId;
			this.password = password;
		}

		public ILoginInfo2 challenge(ITeamRepository repository) {
			return new UsernameAndPasswordLoginInfo(userId, password);
		}
	}

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().readConfiguration(
				WorkItemRetriever.class
						.getResourceAsStream("logging.properties"));
		String repositoryURI = "https://chm01.dv.jp.honda.com:9443/jazz/";
		String projectName = "JTST";// "S.T.D. AreaPartner
		String userId = "JP203998";
		String password = "honda1";
		new WorkItemRetriever(repositoryURI, projectName, userId, password)
				.execute();
	}

	private Logger logger = Logger.getLogger("jazz");
	private String password;
	private String projectName;// "S.T.D. AreaPartner
	private String repositoryURI;

	private String userId;
	private ITeamRepository teamRepository;
	private IProgressMonitor monitor = new NoProgressMonitor();

	public WorkItemRetriever(String repositoryURI, String projectName,
			String userId, String password) {
		this.repositoryURI = repositoryURI;
		this.projectName = projectName;
		this.userId = userId;
		this.password = password;
	}

	public List<WorkItemOverview> execute() throws InvalidResultException{
		boolean result = true;
		TeamPlatform.startup();
		logger.info("startup now");
		try {
			login();
			logger.info("login now");
			return retrieveWorkItem();
		} catch (TeamRepositoryException x) {
			x.printStackTrace();
			throw new InvalidResultException(x);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			throw new InvalidResultException(e);
		} finally {
			TeamPlatform.shutdown();
			logger.info("shutdown now");
		}

	}

	private void login() throws TeamRepositoryException {
		teamRepository = TeamPlatform.getTeamRepositoryService()
				.getTeamRepository(repositoryURI);
		teamRepository.registerLoginHandler(new LoginHandler(userId, password));
		teamRepository.login(monitor);
	}

	private List<WorkItemOverview> retrieveWorkItem() throws TeamRepositoryException,
			InvalidArgumentException {

		IProcessClientService processClient = (IProcessClientService) teamRepository
				.getClientLibrary(IProcessClientService.class);
		IAuditableClient auditableClient = (IAuditableClient) teamRepository
				.getClientLibrary(IAuditableClient.class);
		IWorkItemClient workItemClient = (IWorkItemClient) teamRepository
				.getClientLibrary(IWorkItemClient.class);

		URI uri = URI.create(projectName.replaceAll(" ", "%20"));
		IProjectArea projectArea = (IProjectArea) processClient
				.findProcessArea(uri, null, monitor);
		if (projectArea == null) {
			throw new InvalidArgumentException("Project area not found.");
		}

		List<IWorkItemType> workItemTypeList = workItemClient
				.findWorkItemTypes(projectArea, monitor);
		Map<String, String> workItemMap = new HashMap<String, String>();
		for (IWorkItemType type : workItemTypeList) {
			workItemMap.put(type.getIdentifier(), type.getDisplayName());
		}

		List<ICategory> categoryList = workItemClient.findCategories(
				projectArea, ICategory.FULL_PROFILE, monitor);
		Map<String, String> categoryMap = new HashMap<String, String>();
		for (ICategory category : categoryList) {
			categoryMap
					.put(category.getItemId().toString(), category.getName());
		}

		IQueryableAttributeFactory factory = QueryableAttributes
				.getFactory(IWorkItem.ITEM_TYPE);

		IQueryableAttribute ownerAttribute = factory.findAttribute(
				projectArea, IWorkItem.OWNER_PROPERTY, auditableClient, monitor);


//		IContributorManager icm = teamRepository.contributorManager();
//		IContributor owner = icm.fetchContributorByUserId("JP203998", null);
//
//		AttributeExpression ownerExpression = new AttributeExpression(
//				ownerAttribute, AttributeOperation.EQUALS, owner);
		
		CurrentUserVariable currentUser = new CurrentUserVariable();
		Expression ownerExpression = new VariableAttributeExpression(
				ownerAttribute, AttributeOperation.EQUALS, currentUser);

		IQueryableAttribute projectAreaAttribute = factory.findAttribute(
				projectArea, IWorkItem.PROJECT_AREA_PROPERTY, auditableClient,
				monitor);

		AttributeExpression projectAreaExpression = new AttributeExpression(
				projectAreaAttribute, AttributeOperation.EQUALS, projectArea);

		StatusVariable closedStateGroup = new StatusVariable(
				IWorkflowInfo.CLOSED_STATES_GROUP);

		IQueryableAttribute stateAttribute = factory.findAttribute(projectArea,
				IWorkItem.STATE_PROPERTY, auditableClient, monitor);

		Expression stateExpression = new VariableAttributeExpression(
				stateAttribute, AttributeOperation.NOT_EQUALS, closedStateGroup);

		Term term = new Term(Operator.AND);
		term.add(ownerExpression);
		term.add(projectAreaExpression);
		term.add(stateExpression);

		SortCriteria[] sortCriteria = new SortCriteria[] { new SortCriteria(
				IWorkItem.MODIFIED_PROPERTY, SortCriteria.DESCENDING) };
		Statement statement = new Statement(new SelectClause(),
				(Expression) term, sortCriteria);

		IQueryClient queryClient = (IQueryClient) teamRepository
				.getClientLibrary(IQueryClient.class);

		IQueryResult<IResolvedResult<IWorkItem>> result = queryClient
				.getResolvedExpressionResults(projectArea, statement,
						IWorkItem.FULL_PROFILE);

		LinkedList<WorkItemOverview> list = new LinkedList<WorkItemOverview>();

		while (result.hasNext(monitor)) {
			IResolvedResult<IWorkItem> resolved = result.next(monitor);
			IWorkItem item = resolved.getItem();
			String id = String.valueOf(item.getId());
			String title = item.getHTMLSummary().getPlainText();
			String type = workItemMap.get(item.getWorkItemType());
			String category = categoryMap.get(item.getCategory().getItemId()
					.toString());
			IWorkflowInfo info = workItemClient.findWorkflowInfo(item, monitor);
			String status = info.getStateName(item.getState2());
			WorkItemOverview activity = new WorkItemOverview(id, title, type, category,
					status);
			list.addLast(activity);
			logger.info(activity.toString());
		}

		teamRepository.logout();

		return list;
	}
}
