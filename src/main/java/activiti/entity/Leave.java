package activiti.entity;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Entity: Leave
 *
 * @author HenryYan
 */
@Entity
@Table(name = "business_leave")
public class Leave extends IdEntity<Leave> {

	private static final long serialVersionUID = 1L;

	private String processInstanceId;
	private String userId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date endTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date realityStartTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date realityEndTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date applyTime;
	private String leaveType;
	private String reason;

	// 流程任务
	private Task task;

	private Map<String, Object> variables;

	// 运行中的流程实例
	private ProcessInstance processInstance;

	// 流程定义
	private ProcessDefinition processDefinition;

	@Column(name = "PROCESS_INSTANCE_ID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	@Transient
	public String getStringStartDate() {
		if (startTime == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(startTime);
	}

	@Transient
	public String getStringStartTime() {
		if (startTime == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(startTime);
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	@Transient
	public String getStringEndDate() {
		if (endTime == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(endTime);
	}

	@Transient
	public String getStringEndTime() {
		if (endTime == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(endTime);
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLY_TIME")
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	@Column(name = "LEAVE_TYPE")
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	@Column(name = "REASON")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REALITY_START_TIME")
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}

	@Column(name = "REALITY_END_TIME")
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}

	@Transient
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Transient
	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	@Transient
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	@Transient
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

}