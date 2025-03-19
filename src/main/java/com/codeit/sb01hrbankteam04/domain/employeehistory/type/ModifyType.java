package com.codeit.sb01hrbankteam04.domain.employeehistory.type;

import org.hibernate.envers.RevisionType;

public enum ModifyType {
  CREATED, UPDATED, DELETED;

  public static RevisionType toRevisionType(ModifyType type) {
    return switch (type) {
      case CREATED -> RevisionType.ADD;
      case UPDATED -> RevisionType.MOD;
      case DELETED -> RevisionType.DEL;
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
