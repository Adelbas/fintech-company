package com.academy.fintech.exporter.exporters.agreement;

import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.ExportTaskService;
import com.academy.fintech.exporter.export_task.db.entity.ExportTask;
import com.academy.fintech.exporter.exporters.Exporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationStatusExporter implements Exporter {

    private final ExportTaskService exportTaskService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${exporter.application-status.topic}")
    private String applicationStatusTopicName;

    @Override
    public TaskType getType() {
        return TaskType.APPLICATION_STATUS_UPDATE;
    }

    @Override
    public void export() {
        List<ExportTask> tasks = exportTaskService.findByTypeAndStatusNew(getType());

        for (ExportTask task : tasks) {
            try {
                kafkaTemplate.send(applicationStatusTopicName, task.getKey(), task.getPayload())
                        .thenAccept(t->exportTaskService.updateToSuccess(task))
                        .exceptionally(exception -> {
                            processSendingError(task, exception);
                            return null;
                        });
            } catch (Exception e) {
                processSendingError(task, e);
            }
        }
    }

    private void processSendingError(ExportTask task, Throwable t) {
        //Simple error processing
        log.error("Error exporting application task {}: {}", task.getId(), t.getMessage());
    }
}
