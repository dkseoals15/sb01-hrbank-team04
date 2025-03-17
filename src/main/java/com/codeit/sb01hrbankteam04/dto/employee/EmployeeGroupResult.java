package com.codeit.sb01hrbankteam04.dto.employee;

public class EmployeeGroupResult {
  private String groupName;
  private long count;

  public EmployeeGroupResult(String groupName, long count) {
    this.groupName = groupName;
    this.count = count;
  }

  public String getGroupName() {
    return groupName;
  }

  public long getCount() {
    return count;
  }
}
