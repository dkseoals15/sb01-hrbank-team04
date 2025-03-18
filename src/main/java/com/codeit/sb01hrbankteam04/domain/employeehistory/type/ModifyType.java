package com.codeit.sb01hrbankteam04.domain.employeehistory.type;

import org.hibernate.envers.RevisionType;

public enum ModifyType {
  CREATED, UPDATED, DELETED;

  public static int toRevisionType(ModifyType type) {
    return switch (type) {
      case CREATED -> 0;
      case UPDATED -> 1;
      case DELETED -> 2;
    };
  }

  public static ModifyType fromRevisionType(RevisionType revisionType) {
    return switch (revisionType) {
      case ADD -> CREATED;
      case MOD -> UPDATED;
      case DEL -> DELETED;
    };
  }
}
