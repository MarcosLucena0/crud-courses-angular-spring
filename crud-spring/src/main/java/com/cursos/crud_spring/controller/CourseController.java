package com.cursos.crud_spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.crud_spring.dto.CourseDTO;
import com.cursos.crud_spring.dto.CoursePageDTO;
import com.cursos.crud_spring.service.CourseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Validated
@RestController //Anotação que informa que essaclasse possui um endpoint, seja get, post, put...
@RequestMapping("/api/courses") //Anotação que Informa qual sera o endpoint
public class CourseController {

	private final CourseService courseService;


	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}


	/*Metodo controller de listagem sem paginação
	//@RequestMapping(method = RequestMethod.GET) essas sduas anotações possuem o mesmo papel
	@GetMapping //Anotação que Atribui o endpoint GET para o metodo
	public List<CourseDTO> list() {
		return courseService.list();
	}*/

	//Metodo controller de listagem com paginação
	@GetMapping
    public CoursePageDTO list(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
        @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return courseService.list(page, pageSize);
    }

	@GetMapping("/{id}")
	public CourseDTO findById(@PathVariable @NotNull @Positive Long id){
		return courseService.findById(id);
	}
	

	@PostMapping //Anotação que Atribui o endpoint POST para o metodo
	@ResponseStatus(code = HttpStatus.CREATED) //Serve para atribuir o numero 201 no cadastro
	public CourseDTO create(@RequestBody @NotNull @Valid CourseDTO course) {
		return courseService.create(course);
	}


	@PutMapping("/{id}")
	public CourseDTO update(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid CourseDTO course) {
		return courseService.update(id, course);
	}

	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable @NotNull @Positive Long id) {
		courseService.delete(id);
	}
    
}
