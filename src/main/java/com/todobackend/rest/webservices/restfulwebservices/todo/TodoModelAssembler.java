package com.todobackend.rest.webservices.restfulwebservices.todo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class TodoModelAssembler implements RepresentationModelAssembler<Todo, EntityModel<Todo>> {
    @Override
    public EntityModel<Todo> toModel(Todo todo) {

        return EntityModel.of(todo, //
                linkTo(methodOn(TodoResource.class).getTodo(todo.getUsername(),todo.getId())).withSelfRel(),
                linkTo(methodOn(TodoResource.class).getAllTodos(todo.getUsername())).withRel("todos"));
    }
}
