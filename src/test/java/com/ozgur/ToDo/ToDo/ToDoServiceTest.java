package com.ozgur.ToDo.ToDo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    @Mock private ToDoRepository toDoRepository;
    private ToDoService underTest;

    @BeforeEach
    void setUp() {underTest = new ToDoService(toDoRepository);}

    @Test
    void canGetAllToDos() {
        // when
        underTest.getAllToDos();
        // then
        verify(toDoRepository).findAll();
    }

    @Test
    void canGetToDoById() {
        // given
        Long id = 1L;
        ToDo toDo = new ToDo(id, "title", "desc", false);
        given(toDoRepository.findById(id)).willReturn(Optional.of(toDo));

        // when
        ToDo result = underTest.getToDoById(id);

        // then
        assertThat(result).isEqualTo(toDo);
    }

    @Test
    void cannotGetToDoByIdIfNotExist() {
        // given
        Long id = 1L;
        given(toDoRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            underTest.getToDoById(id);
        });

        assertThat(exception.getMessage()).isEqualTo("ToDo with id 1 does not exist");
    }


    @Test
    void canAddToDo() {
        // given
        ToDo toDo = new ToDo("title", "desc", false);

        // when
        underTest.addToDo(toDo);

        // then
        ArgumentCaptor<ToDo> toDoArgumentCaptor = ArgumentCaptor.forClass(ToDo.class);
        verify(toDoRepository).save(toDoArgumentCaptor.capture());

        ToDo capturedToDo = toDoArgumentCaptor.getValue();

        assertThat(capturedToDo).isEqualTo(toDo);
    }

    @Test
    void canAddToDoWhenNoPendingTaskExists() {
        // given
        ToDo toDo = new ToDo("title", "desc", false);

        // Mock repository: no existing pending task
        given(toDoRepository.findByTitleAndCompleted(toDo.getTitle(), toDo.getCompleted()))
                .willReturn(Optional.empty());

        // Mock repository save to return the same ToDo (with an ID, typically)
        ToDo savedToDo = new ToDo(1L, toDo.getTitle(), toDo.getDescription(), toDo.getCompleted());
        given(toDoRepository.save(toDo)).willReturn(savedToDo);

        // when
        ToDo result = underTest.addToDo(toDo);

        // then
        verify(toDoRepository).save(toDo);
        assertThat(result).isEqualTo(savedToDo);
    }

    @Test
    void cannotAddToDoIfPendingTaskWithSameTitleExists() {
        // given
        ToDo toDo = new ToDo("title", "desc", false);

        // Mock repository to return an existing pending task
        given(toDoRepository.findByTitleAndCompleted(toDo.getTitle(), toDo.getCompleted()))
                .willReturn(Optional.of(new ToDo(1L, "title", "desc", false)));

        // when & then
        assertThatThrownBy(() -> underTest.addToDo(toDo))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Already a pending task with this title");

        // Verify that save was never called
        verify(toDoRepository, never()).save(toDo);
    }

    @Test
    void canDeleteToDo() {
        // given
        Long id = 1L;
        given(toDoRepository.existsById(id)).willReturn(true);

        // when
        underTest.deleteToDo(id);

        // then
        verify(toDoRepository).deleteById(id);

    }

    @Test
    void cantDeleteToDo() {
        // given
        Long id = 1L;
        given(toDoRepository.existsById(id)).willReturn(false);

        // when & then
        assertThrows(IllegalStateException.class, () -> underTest.deleteToDo(id));
        verify(toDoRepository, never()).deleteById(id);

    }

    @Test
    void canUpdateToDoSuccessfully() {
        // given
        Long id = 1L;
        ToDo existing = new ToDo(id, "oldTitle", "oldDesc", false);
        ToDo update = new ToDo("newTitle", "newDesc", true);

        given(toDoRepository.findById(id)).willReturn(Optional.of(existing));
        given(toDoRepository.findByTitleAndCompleted("newTitle", false)).willReturn(Optional.empty());
        given(toDoRepository.save(existing)).willReturn(existing);

        // when
        ToDo updated = underTest.updateToDo(id, update);

        // then
        assertThat(updated.getTitle()).isEqualTo("newTitle");
        assertThat(updated.getDescription()).isEqualTo("newDesc");
        assertThat(updated.getCompleted()).isTrue();
        verify(toDoRepository).save(existing);
    }

    @Test
    void cannotUpdateToDoWithDuplicateTitle() {
        // given

        Long id = 1L;
        ToDo existing = new ToDo(id, "oldTitle", "oldDesc", false);
        ToDo update = new ToDo("duplicateTitle", "newDesc", true);

        given(toDoRepository.findById(id)).willReturn(Optional.of(existing));
        given(toDoRepository.findByTitleAndCompleted("duplicateTitle", false))
                .willReturn(Optional.of(new ToDo(2L, "duplicateTitle", "otherDesc", false)));

        // when & then
        assertThatThrownBy(() -> underTest.updateToDo(id, update))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Already a pending task with this title");

        verify(toDoRepository, never()).save(existing);
    }

    @Test
    void cannotUpdateNonExistentToDo() {
        //given
        Long id = 1L;
        ToDo update = new ToDo("newTitle", "newDesc", true);

        given(toDoRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> underTest.updateToDo(id, update))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Task with id " + id + " does not exist");
    }

    @Test
    void updatesOnlyDescriptionIfTitleAndCompletedAreUnchanged() {
        // given
        Long id = 1L;
        ToDo existing = new ToDo(id, "oldTitle", "oldDesc", false);
        ToDo update = new ToDo(null, "newDesc", false); // title unchanged, completed unchanged

        given(toDoRepository.findById(id)).willReturn(Optional.of(existing));
        given(toDoRepository.save(existing)).willReturn(existing);

        // when
        ToDo updated = underTest.updateToDo(id, update);

        // then
        assertThat(updated.getTitle()).isEqualTo("oldTitle");
        assertThat(updated.getDescription()).isEqualTo("newDesc");
        assertThat(updated.getCompleted()).isFalse();
        verify(toDoRepository).save(existing);
    }


    @Test
    void updatesOnlyCompletedStatusIfTitleAndDescriptionAreNullOrBlank() {
        // given
        Long id = 1L;
        ToDo existing = new ToDo(id, "oldTitle", "oldDesc", false);
        ToDo update = new ToDo(null, null, true); // only completed updated

        given(toDoRepository.findById(id)).willReturn(Optional.of(existing));
        given(toDoRepository.save(existing)).willReturn(existing);

        // when
        ToDo updated = underTest.updateToDo(id, update);

        // then
        assertThat(updated.getTitle()).isEqualTo("oldTitle");
        assertThat(updated.getDescription()).isEqualTo("oldDesc");
        assertThat(updated.getCompleted()).isTrue();
        verify(toDoRepository).save(existing);
    }

    @Test
    void doesNotChangeFieldsIfUpdateContainsBlankOrSameValues() {
        // given
        Long id = 1L;
        ToDo existing = new ToDo(id, "title", "desc", false);
        ToDo update = new ToDo("", "   ", false); // blank title & description, same completed

        given(toDoRepository.findById(id)).willReturn(Optional.of(existing));
        given(toDoRepository.save(existing)).willReturn(existing);

        // when
        ToDo updated = underTest.updateToDo(id, update);

        // then
        assertThat(updated.getTitle()).isEqualTo("title");
        assertThat(updated.getDescription()).isEqualTo("desc");
        assertThat(updated.getCompleted()).isFalse();
        verify(toDoRepository).save(existing);
    }


}
