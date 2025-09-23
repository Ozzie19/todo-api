package com.ozgur.ToDo.ToDo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ToDoConfig {

    @Bean
    CommandLineRunner commandLineRunner(ToDoRepository toDoRepository) {
        return args -> {
            ToDo example1 = new ToDo(
                    "Example 1",
                    "This is an example completed task",
                    true
            );

            ToDo example2 = new ToDo(
                    "Example 2",
                    "This is an example of an uncompleted task",
                    false
            );

            toDoRepository.saveAll(
                    List.of(example1, example2)
            );
        };
    }
}
