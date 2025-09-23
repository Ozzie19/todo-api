package com.ozgur.ToDo.ToDo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;

    @Autowired
    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ToDo getToDoById(Long id) {
        return toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ToDo with id " + id + " does not exist"));
    }

    public ToDo addToDo (ToDo toDo) {
        Optional<ToDo> toDoOptional = toDoRepository
                .findByTitleAndCompleted(toDo.getTitle(), false);
        if (toDoOptional.isPresent()) {
            throw new IllegalStateException("Already a pending task with this title");
        }
        toDoRepository.save(toDo);
        return toDoRepository.save(toDo); // returns the saved entity, including its ID
    }

    public void deleteToDo (Long id) {
        boolean exists = toDoRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Task with id " + id + " does not exist");
        }
        toDoRepository.deleteById(id);
    }

    @Transactional
    public ToDo updateToDo(Long id, ToDo toDo) {
        ToDo existingToDo = toDoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Task with id " + id + " does not exist"));

        String newTitle = toDo.getTitle();
        if (newTitle != null && !newTitle.isBlank() && !newTitle.equals(existingToDo.getTitle())) {
            Optional<ToDo> toDoOptional = toDoRepository
                    .findByTitleAndCompleted(newTitle, false);
            if (toDoOptional.isPresent()) {
                throw new IllegalStateException("Already a pending task with this title");
            }
            existingToDo.setTitle(newTitle);
        }

        String newDescription = toDo.getDescription();
        if (newDescription != null && !newDescription.isBlank() && !newDescription.equals(existingToDo.getDescription())) {
            existingToDo.setDescription(newDescription);
        }

        Boolean newCompleted = toDo.getCompleted();
        if (newCompleted != null && !newCompleted.equals(existingToDo.getCompleted())) {
            existingToDo.setCompleted(newCompleted);
        }

        return toDoRepository.save(existingToDo); // returns the saved entity, including its ID

    }

}
