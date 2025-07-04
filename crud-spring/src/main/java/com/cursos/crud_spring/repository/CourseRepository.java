package com.cursos.crud_spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cursos.crud_spring.model.Course;

@Repository //Anotação que libera os metodos do JPARepository para realizar os metodos dos CRUD
public interface CourseRepository extends JpaRepository<Course, Long>{

}
