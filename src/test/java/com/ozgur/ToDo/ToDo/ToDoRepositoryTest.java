package com.ozgur.ToDo.ToDo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByTitleAndCompleted() {
        // given
        String title = "Test1";
        boolean completed = true;
        ToDo toDo = new ToDo(
                title,
                "Random description",
                completed
        );
        underTest.save(toDo);
        // when
        Optional<ToDo> result = underTest.findByTitleAndCompleted(title, completed);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(title);
        assertThat(result.get().getCompleted()).isEqualTo(completed);
    }

    @Test
    void itShouldNotFindWhenTitleDoesMatchButCompletedDoesNotMatch() {
        // given
        String title = "Test1";
        ToDo toDo = new ToDo(
                title,
                "Random description",
                true
        );
        underTest.save(toDo);

        // when
        Optional<ToDo> result = underTest.findByTitleAndCompleted(title, false);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShouldNotFindWhenTitleDoesNotMatchButCompletedDoesMatch() {
        // given
        String title = "Test1";
        boolean completed = true;
        ToDo toDo = new ToDo(
                title,
                "Random description",
                completed
        );
        underTest.save(toDo);

        // when
        Optional<ToDo> result = underTest.findByTitleAndCompleted("Wrong Title", completed);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShouldNotFindWhenTitleAndCompletedDoNotMatch() {
        // given
        String title = "Test1";
        String wrongTitle = "Wrong Title";
        ToDo toDo = new ToDo(
                title,
                "Random description",
                true
        );
        underTest.save(toDo);

        // when
        Optional<ToDo> result = underTest.findByTitleAndCompleted(wrongTitle, false);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    void itShouldNotFindWhenToDoDoesNotExist() {
        // given
        String title = "Test1";
        boolean completed = true;

        // when
        Optional<ToDo> result = underTest.findByTitleAndCompleted(title, completed);

        // then
        assertThat(result).isNotPresent();
    }


}
