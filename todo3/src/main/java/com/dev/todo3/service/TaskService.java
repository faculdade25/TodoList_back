package com.dev.todo3.service;

import com.dev.todo3.entity.Task;
import com.dev.todo3.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Injeção do repositório via construtor
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        if (task != null) {
            try {
                return taskRepository.save(task);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao salvar a tarefa", e);
            }
        }
        throw new IllegalArgumentException("A tarefa não pode ser nula");
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);  // Retorna a tarefa se encontrada
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();  // Retorna todas as tarefas no banco
    }

    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> existingTaskOpt = taskRepository.findById(id);

        if (existingTaskOpt.isPresent()) {
            Task existingTask = existingTaskOpt.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setStatus(updatedTask.getStatus());

            return taskRepository.save(existingTask);  // Atualiza a tarefa no banco
        } else {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
    }

    public void deleteTask(Long id) {
        Optional<Task> existingTaskOpt = taskRepository.findById(id);

        if (existingTaskOpt.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
    }
}