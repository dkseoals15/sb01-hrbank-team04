package com.codeit.sb01hrbankteam04.domain.employeehistory.audit;

import com.codeit.sb01hrbankteam04.domain.employeehistory.entity.EmployeeHistory;
import org.hibernate.envers.RevisionListener;

public class EmployeeRevisionListener implements RevisionListener {

  private static final ThreadLocal<String> memoHolder = new ThreadLocal<>();
  private static final ThreadLocal<String> modifiedHolder = new ThreadLocal<>();

  public static void setMemo(String memo) {
    memoHolder.set(memo);
  }

  public static void setModified(String modified) {
//    modified = "127.0.0.1"; // TODO: 아이피 임시로 넣어두기
    modifiedHolder.set(modified);
  }

  public static String getMemo() {
    return memoHolder.get();
  }

  public static String getModified() {
    return modifiedHolder.get();
  }

  @Override
  public void newRevision(Object revisionEntity) {
    EmployeeHistory employeeHistory = (EmployeeHistory) revisionEntity;
    employeeHistory.setMemo(getMemo());
    employeeHistory.setIpAddress(getModified());
  }

}
