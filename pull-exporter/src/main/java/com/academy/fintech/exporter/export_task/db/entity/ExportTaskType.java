package com.academy.fintech.exporter.export_task.db.entity;

import com.academy.fintech.exporter.enums.TaskType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ExportTaskType {

    @Id
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;
}
