package com.ozgur.ToDo.ToDo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/todo")
public class ToDoController {

    private final ToDoService toDoService;

    @Autowired
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public List<ToDo> getAllToDos() {return toDoService.getAllToDos();}

    @GetMapping(path="{id}")
    public ToDo getToDoById(@PathVariable("id") Long id) {return toDoService.getToDoById(id);}

    @PostMapping
    public ToDo addToDo(@RequestBody ToDo toDo) {return toDoService.addToDo(toDo);
    }

    @DeleteMapping(path = "{id}")
    public void deleteToDo(@PathVariable("id") Long id) {
        toDoService.deleteToDo(id);
    }

    @PutMapping(path = "{id}")
    public ToDo updateToDo(@PathVariable("id") Long id,
                           @RequestBody ToDo toDo) {
        return toDoService.updateToDo(id, toDo);
    }
}
