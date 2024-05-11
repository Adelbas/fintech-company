package com.academy.fintech.exporter.exporters;

import com.academy.fintech.exporter.enums.TaskType;

public interface Exporter {
    void export();

    TaskType getType();
}
