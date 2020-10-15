package com.todobackend.rest.webservices.restfulwebservices.todo;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TodoResource {

    @Autowired
    private TodoJpaRepository todoJpaRepository;

    @Autowired
    private TodoModelAssembler todoModelAssembler;

    TodoResource(TodoJpaRepository todoJpaRepository, TodoModelAssembler todoModelAssembler) {
        this.todoJpaRepository = todoJpaRepository;
        this.todoModelAssembler = todoModelAssembler;
    }

    @GetMapping("/jpa/users/{username}/todos")
    public CollectionModel<EntityModel<Todo>> getAllTodos(@PathVariable String username) {
        List<EntityModel<Todo>> todos = todoJpaRepository.findByUsername(username).stream()
                .map(todoModelAssembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(todos, linkTo(methodOn(TodoResource.class).getAllTodos(username)).withSelfRel());

    }

//    @GetMapping("/employees")
//    CollectionModel<EntityModel<Employee>> all() {
//
//        List<EntityModel<Employee>> employees = repository.findAll().stream() //
//                .map(assembler::toModel) //
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//    }

    @GetMapping("/jpa/users/{username}/todos/{id}")
    public EntityModel<Todo> getTodo(@PathVariable String username, @PathVariable Long id) {
        Todo todo = todoJpaRepository.findById(id).get();
        return todoModelAssembler.toModel(todo);
    }

    @PostMapping("/jpa/users/{username}/todos")
    public ResponseEntity<?> createTodo (@PathVariable String username, @RequestBody Todo todo) {
        todo.setUsername(username);
        EntityModel<Todo> entityModel = todoModelAssembler.toModel(todoJpaRepository.save(todo));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
//        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
//        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/jpa/users/{username}/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable String username, @PathVariable Long id, @RequestBody Todo todoRequest) {
        Todo updatedTodo = todoJpaRepository.findById(id)
                .map(todo -> {
                    todo.setDescription(todoRequest.getDescription());
                    todo.setTargetDate(todoRequest.getTargetDate());
                    todo.setDone(todoRequest.isDone());
                    return todoJpaRepository.save(todo);
                })
                .orElseGet(() -> {
                    todoRequest.setId(id);
                    return todoJpaRepository.save(todoRequest);
                });
        EntityModel<Todo> entityModel = todoModelAssembler.toModel(updatedTodo);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("jpa/users/{username}/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable String username, @PathVariable Long id) {
        todoJpaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
