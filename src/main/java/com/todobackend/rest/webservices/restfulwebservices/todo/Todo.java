package com.todobackend.rest.webservices.restfulwebservices.todo;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Todo {
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String description;
    private LocalDate targetDate;
    private boolean isDone;

}
