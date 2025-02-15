package com.dev.todo3.service;

import com.dev.todo3.entity.Task;
import com.dev.todo3.entity.TodoList;
import com.dev.todo3.entity.User;
import com.dev.todo3.repository.ListRepository;
import com.dev.todo3.repository.TaskRepository;
import com.dev.todo3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoListService {

    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TodoListService(ListRepository listRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.listRepository = listRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public TodoList createTodoList(Long userId, TodoList todoList) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            todoList.setUser(user.get()); // Associa a lista ao usuário
            return listRepository.save(todoList); // Salva a lista no banco
        }

        throw new IllegalArgumentException("Usuário não encontrado com o ID: " + userId);
    }

    public List<TodoList> getAllTodoListsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            return listRepository.findByUser(user.get()); // Obtém todas as listas associadas ao usuário
        }

        throw new IllegalArgumentException("Usuário não encontrado com o ID: " + userId);
    }

    public Optional<TodoList> getTodoListById(Long listId) {
        return listRepository.findById(listId); // Retorna a lista se encontrada
    }

    public TodoList updateTodoList(Long listId, TodoList updatedList) {
        Optional<TodoList> existingListOpt = listRepository.findById(listId);

        if (existingListOpt.isPresent()) {
            TodoList existingList = existingListOpt.get();
            existingList.setName(updatedList.getName()); // Atualiza o nome da lista
            existingList.setTasks(updatedList.getTasks());
            return listRepository.save(existingList); // Salva as alterações
        } else {
            throw new IllegalArgumentException("Lista não encontrada com o ID: " + listId);
        }
    }

    public TodoList addTaskToList(Long listId, Task task){
        Optional<TodoList> existingListOpt = listRepository.findById(listId);

        if (existingListOpt.isPresent()) {
            TodoList existingList = existingListOpt.get();
            Task newTask = new Task();
            newTask.setTitle(task.getTitle());
            newTask.setPriority(task.getPriority());
            newTask.setStatus(task.getStatus());
            newTask.setTodoList(existingList);
            existingList.getTasks().add(newTask);
            System.out.println(existingList.getTasks());
            return listRepository.save(existingList);
        }else {
            throw new IllegalArgumentException("Lista não encontrada com o ID: " + listId);
        }
    }

    public TodoList updateTaskInList(Long listId, Long taskId, Task updatedTask) {
        Optional<TodoList> existingListOpt = listRepository.findById(listId);

        if (existingListOpt.isPresent()) {
            TodoList existingList = existingListOpt.get();
            Optional<Task> taskToUpdateOpt = existingList.getTasks().stream()
                    .filter(task -> task.getId().equals(taskId))
                    .findFirst();
            if (taskToUpdateOpt.isPresent()) {
                Task taskToUpdate = taskToUpdateOpt.get();
                taskToUpdate.setTitle(updatedTask.getTitle());
                taskToUpdate.setPriority(updatedTask.getPriority());
                taskToUpdate.setStatus(updatedTask.getStatus());
                return listRepository.save(existingList);
            } else {
                throw new IllegalArgumentException("Tarefa não encontrada com o ID: " + taskId);
            }
        } else {
            throw new IllegalArgumentException("Lista não encontrada com o ID: " + listId);
        }
    }

    public TodoList deleteTaskFromList(Long listId, Long taskId) {
        Optional<TodoList> existingListOpt = listRepository.findById(listId);
        if (existingListOpt.isPresent()) {
            TodoList existingList = existingListOpt.get();
            Optional<Task> taskToDeleteOpt = existingList.getTasks().stream()
                    .filter(task -> task.getId().equals(taskId))
                    .findFirst();

            if (taskToDeleteOpt.isPresent()) {
                Task taskToDelete = taskToDeleteOpt.get();
                existingList.getTasks().remove(taskToDelete);
                return listRepository.save(existingList);
            } else {
                throw new IllegalArgumentException("Tarefa não encontrada com o ID: " + taskId);
            }
        } else {
            throw new IllegalArgumentException("Lista não encontrada com o ID: " + listId);
        }
    }

    public void deleteTodoList(Long listId) {
        Optional<TodoList> existingListOpt = listRepository.findById(listId);

        if (existingListOpt.isPresent()) {
            listRepository.deleteById(listId); // Deleta a lista no banco
        } else {
            throw new IllegalArgumentException("Lista não encontrada com o ID: " + listId);
        }
    }

    public Task updateTaskPriority(Long taskId, Long priority) {
        Optional<Task> exisitingTask = taskRepository.findById(taskId);
        if (exisitingTask.isPresent()) {
            Task task = exisitingTask.get();
            task.setPriority(Math.toIntExact(priority));
            return taskRepository.save(task);
        }
        return null;
    }

    public Task updateTaskStatus(Long taskId, Long status) {
        Optional<Task> exisitingTask = taskRepository.findById(taskId);
        if (exisitingTask.isPresent()) {
            Task task = exisitingTask.get();
            task.setStatus(Math.toIntExact(status));
            return taskRepository.save(task);
        }
        return null;
    }
}