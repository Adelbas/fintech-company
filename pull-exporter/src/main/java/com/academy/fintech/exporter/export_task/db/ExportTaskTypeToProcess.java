package com.academy.fintech.exporter.export_task.db;

import com.academy.fintech.exporter.enums.TaskType;

public record ExportTaskTypeToProcess(
    TaskType type,
    int count
) { }
