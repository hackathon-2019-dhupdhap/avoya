package com.mlbd.avoya.schemas;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

  private static final long serialVersionUID = -6323300045962916950L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  
  @Column(name = "created_at", columnDefinition = "DATETIME")
  @CreatedDate
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by")
  private int createdBy;

  @Column(name = "updated_at", columnDefinition = "DATETIME")
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private int updatedBy; 

  @Override
  public String toString() {
    return String.format("id=%s, createdAt=%s, createdBy=%s, updatedAt=%s, updatedBy=%s",
        super.toString(), id, createdAt, createdBy, updatedAt, updatedBy);
  }

}
