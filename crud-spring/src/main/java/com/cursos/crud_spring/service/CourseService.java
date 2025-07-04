package com.cursos.crud_spring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.cursos.crud_spring.dto.CourseDTO;
import com.cursos.crud_spring.dto.CoursePageDTO;
import com.cursos.crud_spring.dto.mapper.CourseMapper;
import com.cursos.crud_spring.exception.RecordNotFoundException;
import com.cursos.crud_spring.model.Course;
import com.cursos.crud_spring.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
@Validated
@Service
public class CourseService {


    private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;


    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
		this.courseMapper =  courseMapper;
    }

	//Metodo de Listagem sem a Paginação
	/*public List<CourseDTO> list() {
		return courseRepository.findAll()
		.stream()
		.map(courseMapper::toDTO)
		.collect(Collectors.toList());
	}*/

	//http://localhost:8080/api/courses?page=0&pageSize=10 - URL para utilizar a paginação na API
	//Metodo de Listagem com a Paginação
	public CoursePageDTO list(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<Course> pageCourse = courseRepository.findAll(PageRequest.of(page, pageSize));
        List<CourseDTO> courses = pageCourse.get().map(courseMapper::toDTO).collect(Collectors.toList());
        return new CoursePageDTO(courses, pageCourse.getTotalElements(), pageCourse.getTotalPages());
    }


    public CourseDTO findById(@NotNull @Positive Long id){
		return courseRepository.findById(id).map(courseMapper::toDTO)
		.orElseThrow(() -> new RecordNotFoundException(id));
	}
    

	public CourseDTO create(@Valid @NotNull CourseDTO course) {
		return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(course)));
	}

    public CourseDTO update(@NotNull @Positive Long id, @Valid CourseDTO courseDTO) {
		return courseRepository.findById(id)
			.map(recordFound -> {
				Course course = courseMapper.toEntity(courseDTO);
				recordFound.setName(courseDTO.name());
				recordFound.setCategory(courseMapper.convertCategoryValue(courseDTO.category()));
				//recordFound.setLessons(course.getLessons());
				recordFound.getLessons().clear();
				course.getLessons().forEach(recordFound.getLessons()::add);
				return courseMapper.toDTO(courseRepository.save(recordFound));
			}).orElseThrow(() -> new RecordNotFoundException(id));
	}

    public void delete(@NotNull @Positive Long id) {

        courseRepository.delete(courseRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id)));

        //Esse codigo faz a mesma coisa que o código acima, porem retorna um true q não possui serventia para nada
	     /*courseRepository.findById(id)
			.map(recordFound -> {
				courseRepository.deleteById(id);
				return true;
			})
			.orElseThrow(() -> new RecordNotFoundException(id));*/
	}

}
