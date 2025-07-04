package com.cursos.crud_spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.cursos.crud_spring.enums.Category;
import com.cursos.crud_spring.model.Course;
import com.cursos.crud_spring.model.Lesson;
import com.cursos.crud_spring.repository.CourseRepository;

@SpringBootApplication
public class CrudSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(CourseRepository courseRepository) {
		return args -> {
			courseRepository.deleteAll();

			/*for (int i = 0; i < 5; i++) {
			
				Course c = new Course();
				c.setName("Angular" + i);
				c.setCategory(Category.FULL_STACK);

				Lesson l = new Lesson();
				l.setName("Introdução");
				l.setYoutubeUrl("012345678901");
				l.setCourse(c);
				c.getLessons().add(l);

				Lesson l2 = new Lesson();
				l2.setName("Angular");
				l2.setYoutubeUrl("012345678902");
				l2.setCourse(c);
				c.getLessons().add(l2);
				
				courseRepository.save(c);

			}*/
		};
	}

}
