package harachu.jazz;

public class WorkItemOverview {
	String id;
	String task;
	String type;
	String category;
	String status;

	public WorkItemOverview(String id, String task, String type, String category,
			String status) {
		super();
		this.id = id;
		this.task = task;
		this.type = type;
		this.category = category;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "JazzActivity [id=" + id + ", task=" + task + ", type=" + type
				+ ", category=" + category + ", status=" + status + "]";
	}

}
