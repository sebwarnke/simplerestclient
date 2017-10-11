package com.camunda.consulting.simplerestclient.testpojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "project")
public class Project implements Serializable {

	private static final long serialVersionUID = 1293584948462516896L;
	
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("client_id")
	private Integer clientId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("code")
	private String code;
	@JsonProperty("active")
	private Boolean active;
	@JsonProperty("billable")
	private Boolean billable;
	@JsonProperty("is_fixed_fee")
	private Boolean isFixedFee;
	@JsonProperty("fee")
	private Integer fee;
	@JsonProperty("bill_by")
	private String billBy;
	@JsonProperty("hourly_rate")
	private Integer hourlyRate;
	@JsonProperty("budget")
	private Integer budget;
	@JsonProperty("budget_by")
	private String budgetBy;
	@JsonProperty("notify_when_over_budget")
	private Boolean notifyWhenOverBudget;
	@JsonProperty("over_budget_notification_percentage")
	private Integer overBudgetNotificationPercentage;
	@JsonProperty("over_budget_notified_at")
	private Object overBudgetNotifiedAt;
	@JsonProperty("show_budget_to_all")
	private Boolean showBudgetToAll;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("updated_at")
	private String updatedAt;
	@JsonProperty("starts_on")
	private String startsOn;
	@JsonProperty("ends_on")
	private String endsOn;
	@JsonProperty("estimate")
	private Integer estimate;
	@JsonProperty("estimate_by")
	private String estimateBy;
	@JsonProperty("hint_earliest_record_at")
	private String hintEarliestRecordAt;
	@JsonProperty("hint_latest_record_at")
	private String hintLatestRecordAt;
	@JsonProperty("notes")
	private String notes;
	@JsonProperty("cost_budget")
	private Object costBudget;
	@JsonProperty("cost_budget_include_expenses")
	private Boolean costBudgetIncludeExpenses;

	public Project() {
		// left blank by intention
	}
	
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("client_id")
	public Integer getClientId() {
		return clientId;
	}

	@JsonProperty("client_id")
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("active")
	public Boolean getActive() {
		return active;
	}

	@JsonProperty("active")
	public void setActive(Boolean active) {
		this.active = active;
	}

	@JsonProperty("billable")
	public Boolean getBillable() {
		return billable;
	}

	@JsonProperty("billable")
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	@JsonProperty("is_fixed_fee")
	public Boolean getIsFixedFee() {
		return isFixedFee;
	}

	@JsonProperty("is_fixed_fee")
	public void setIsFixedFee(Boolean isFixedFee) {
		this.isFixedFee = isFixedFee;
	}

	@JsonProperty("fee")
	public Integer getFee() {
		return fee;
	}

	@JsonProperty("fee")
	public void setFee(Integer fee) {
		this.fee = fee;
	}

	@JsonProperty("bill_by")
	public String getBillBy() {
		return billBy;
	}

	@JsonProperty("bill_by")
	public void setBillBy(String billBy) {
		this.billBy = billBy;
	}

	@JsonProperty("hourly_rate")
	public Integer getHourlyRate() {
		return hourlyRate;
	}

	@JsonProperty("hourly_rate")
	public void setHourlyRate(Integer hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	@JsonProperty("budget")
	public Integer getBudget() {
		return budget;
	}

	@JsonProperty("budget")
	public void setBudget(Integer budget) {
		this.budget = budget;
	}

	@JsonProperty("budget_by")
	public String getBudgetBy() {
		return budgetBy;
	}

	@JsonProperty("budget_by")
	public void setBudgetBy(String budgetBy) {
		this.budgetBy = budgetBy;
	}

	@JsonProperty("notify_when_over_budget")
	public Boolean getNotifyWhenOverBudget() {
		return notifyWhenOverBudget;
	}

	@JsonProperty("notify_when_over_budget")
	public void setNotifyWhenOverBudget(Boolean notifyWhenOverBudget) {
		this.notifyWhenOverBudget = notifyWhenOverBudget;
	}

	@JsonProperty("over_budget_notification_percentage")
	public Integer getOverBudgetNotificationPercentage() {
		return overBudgetNotificationPercentage;
	}

	@JsonProperty("over_budget_notification_percentage")
	public void setOverBudgetNotificationPercentage(Integer overBudgetNotificationPercentage) {
		this.overBudgetNotificationPercentage = overBudgetNotificationPercentage;
	}

	@JsonProperty("over_budget_notified_at")
	public Object getOverBudgetNotifiedAt() {
		return overBudgetNotifiedAt;
	}

	@JsonProperty("over_budget_notified_at")
	public void setOverBudgetNotifiedAt(Object overBudgetNotifiedAt) {
		this.overBudgetNotifiedAt = overBudgetNotifiedAt;
	}

	@JsonProperty("show_budget_to_all")
	public Boolean getShowBudgetToAll() {
		return showBudgetToAll;
	}

	@JsonProperty("show_budget_to_all")
	public void setShowBudgetToAll(Boolean showBudgetToAll) {
		this.showBudgetToAll = showBudgetToAll;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("updated_at")
	public String getUpdatedAt() {
		return updatedAt;
	}

	@JsonProperty("updated_at")
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@JsonProperty("starts_on")
	public String getStartsOn() {
		return startsOn;
	}

	@JsonProperty("starts_on")
	public void setStartsOn(String startsOn) {
		this.startsOn = startsOn;
	}

	@JsonProperty("ends_on")
	public String getEndsOn() {
		return endsOn;
	}

	@JsonProperty("ends_on")
	public void setEndsOn(String endsOn) {
		this.endsOn = endsOn;
	}

	@JsonProperty("estimate")
	public Integer getEstimate() {
		return estimate;
	}

	@JsonProperty("estimate")
	public void setEstimate(Integer estimate) {
		this.estimate = estimate;
	}

	@JsonProperty("estimate_by")
	public String getEstimateBy() {
		return estimateBy;
	}

	@JsonProperty("estimate_by")
	public void setEstimateBy(String estimateBy) {
		this.estimateBy = estimateBy;
	}

	@JsonProperty("hint_earliest_record_at")
	public String getHintEarliestRecordAt() {
		return hintEarliestRecordAt;
	}

	@JsonProperty("hint_earliest_record_at")
	public void setHintEarliestRecordAt(String hintEarliestRecordAt) {
		this.hintEarliestRecordAt = hintEarliestRecordAt;
	}

	@JsonProperty("hint_latest_record_at")
	public String getHintLatestRecordAt() {
		return hintLatestRecordAt;
	}

	@JsonProperty("hint_latest_record_at")
	public void setHintLatestRecordAt(String hintLatestRecordAt) {
		this.hintLatestRecordAt = hintLatestRecordAt;
	}

	@JsonProperty("notes")
	public String getNotes() {
		return notes;
	}

	@JsonProperty("notes")
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@JsonProperty("cost_budget")
	public Object getCostBudget() {
		return costBudget;
	}

	@JsonProperty("cost_budget")
	public void setCostBudget(Object costBudget) {
		this.costBudget = costBudget;
	}

	@JsonProperty("cost_budget_include_expenses")
	public Boolean getCostBudgetIncludeExpenses() {
		return costBudgetIncludeExpenses;
	}

	@JsonProperty("cost_budget_include_expenses")
	public void setCostBudgetIncludeExpenses(Boolean costBudgetIncludeExpenses) {
		this.costBudgetIncludeExpenses = costBudgetIncludeExpenses;
	}
}