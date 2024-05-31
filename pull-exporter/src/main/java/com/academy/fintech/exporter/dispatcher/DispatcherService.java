package com.academy.fintech.exporter.dispatcher;

import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.ExportTaskTypeToProcess;
import com.academy.fintech.exporter.exporters.Exporter;
import com.academy.fintech.exporter.export_task.db.ExportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class DispatcherService {

    private final ExportTaskService exportTaskService;

    private final Map<TaskType, Exporter> exporters;

    public DispatcherService(ExportTaskService exportTaskService, List<Exporter> exporters) {
        this.exportTaskService = exportTaskService;

        this.exporters = exporters.stream()
                .collect(Collectors.toMap(Exporter::getType, Function.identity()));
    }

    @Scheduled(fixedRateString = "${exporter.dispatcher.stalled-seconds-rate}", timeUnit = SECONDS, zone = "${exporter.dispatcher.tz}")
    public void processStalled() {
        log.info("Processing stalled tasks");
        exportTaskService.updateStalledTasks();
    }

    @Scheduled(fixedRateString = "${exporter.dispatcher.export-seconds-rate}", timeUnit = SECONDS, zone = "${exporter.dispatcher.tz}")
    public void exporter() {
        log.info("Exporting new tasks");
        List<ExportTaskTypeToProcess> exportTasksToProcess = exportTaskService.findExportTasks();

        exportTasksToProcess.stream()
                .filter(task -> task.count() > 0)
                .forEach(task -> executeExporter(task.type()));
    }

    private void executeExporter(TaskType type) {
        exporters.get(type).export();
    }
}
