package com.academy.fintech.exporter.export_task.db;

import com.academy.fintech.exporter.enums.TaskStatus;
import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.entity.ExportTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportTaskService {

    private final ExportTaskRepository exportTaskRepository;

    @Value("${exporter.dispatcher.retry-stalled.passed-minutes-in-processing-to-retry}")
    private int passedMinutesInProcessingToRetry;

    public List<ExportTask> findByTypeAndStatusNew(TaskType type) {
        return exportTaskRepository.updateAndReturnTasksByTypeAndStatusNew(type);
    }

    public List<ExportTaskTypeToProcess> findExportTasks() {
        return exportTaskRepository.countByTypeAndStatus(TaskStatus.NEW).stream()
                .map(o->new ExportTaskTypeToProcess(TaskType.valueOf((String) o[0]), ((Long) o[1]).intValue()))
                .collect(Collectors.toList());
    }

    public void updateToSuccess(ExportTask task) {
        log.info("Task {} status updated to SUCCESS", task.getId());
        task.setStatus(TaskStatus.SUCCESS);
        exportTaskRepository.save(task);
    }

    public void updateStalledTasks() {
        LocalDateTime updatedBefore = LocalDateTime.now().minusMinutes(passedMinutesInProcessingToRetry);

        List<ExportTask> tasks = exportTaskRepository.findExportTasksByStatusAndUpdatedAtBefore(
                TaskStatus.PROCESSING,
                updatedBefore
        );

        for (ExportTask task : tasks) {
            if (task.getRetriesCount() < task.getType().getRetryCount()) {
                task.setStatus(TaskStatus.NEW);
                task.setRetriesCount(task.getRetriesCount() + 1);
            } else {
                task.setStatus(TaskStatus.ERROR);
            }

            task.setUpdatedAt(LocalDateTime.now());
            exportTaskRepository.save(task);
        }
    }
}
