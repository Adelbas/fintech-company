package com.academy.fintech.exporter.export_task.db;

import com.academy.fintech.exporter.enums.TaskStatus;
import com.academy.fintech.exporter.enums.TaskType;
import com.academy.fintech.exporter.export_task.db.entity.ExportTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExportTaskRepository extends JpaRepository<ExportTask, Long> {

    @Modifying
    @Query(value = "INSERT INTO export_task (type, status, key, payload) " +
                    "VALUES (:#{#type.name()}, :#{#status.name()}, :key, :payload)",
            nativeQuery = true)
    void saveTask(TaskType type, TaskStatus status, String key, String payload);

    @Modifying
    @Query(value =
            "UPDATE export_task " +
            "SET status = 'PROCESSING', " +
                "updated_at = clock_timestamp() " +
            "WHERE id IN (SELECT id " +
            "    FROM export_task et " +
            "    WHERE et.status = 'NEW' " +
            "    AND et.type = :#{#type.name()} " +
            "    FOR UPDATE SKIP LOCKED) " +
            "    RETURNING *",
            nativeQuery = true)
    List<ExportTask> updateAndReturnTasksByTypeAndStatusNew(TaskType type);

    @Query(value =
            "SELECT e.type as type, COUNT(e) as count " +
            "FROM export_task e " +
            "WHERE e.status = :#{#status.name()} " +
            "GROUP BY e.type",
            nativeQuery = true)
    List<Object[]> countByTypeAndStatus(TaskStatus status);

    List<ExportTask> findExportTasksByStatusAndUpdatedAtBefore(TaskStatus status, LocalDateTime updatedBefore);
}
