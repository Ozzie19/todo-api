package com.ozgur.ToDo.ToDo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Optional<ToDo> findByTitleAndCompleted(String title, boolean completed);

}
