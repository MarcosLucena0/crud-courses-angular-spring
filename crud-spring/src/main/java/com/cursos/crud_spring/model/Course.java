package com.cursos.crud_spring.model;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import com.cursos.crud_spring.enums.Category;
import com.cursos.crud_spring.enums.Status;
import com.cursos.crud_spring.enums.converters.CategoryConverter;
import com.cursos.crud_spring.enums.converters.StatusConverter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data //Essa anotação substitui o getter e setter e diversos outras anotações
@Entity //Anotação que especifica a classe como uma entidade ou tabela no BD
//@Table(name ="cursos") //Anotação que atribui o nome entre "" para a tabela
@SQLDelete(sql = "UPDATE Course SET status = 'Inativo' WHERE id = ?")
@Where(clause = "status = 'Ativo'")
public class Course {

    @Id //Indica que o atributo id é um ID ou chave primaria
	@GeneratedValue(strategy = GenerationType.AUTO) //Indica que o valor do id seja gerado de forma automatica pelo BD
	@JsonProperty("_id") //serve para transformar o objeto para json ou vice versa, ou seja, o atributo id se tornará _json
	private Long id;
	
	@NotBlank
	@NotNull
	@Length(min = 5, max = 100)
	@Column(name = "nome", length = 100, nullable = false) //Indica que a coluna do BD terá o nome entre "", com o tamanho 200 e obrigatoriamente deverá ser registrado algo, n aceita valores nulos
	private String name;

	@NotNull
	//@Length(max = 10)      
	//@Pattern(regexp = "Back-end|Front-end")
	@Column(length = 10, name = "categoria", nullable = false)
	@Convert(converter = CategoryConverter.class)
	private Category category;

	@NotNull
	//@Length(max = 10)
	//@Pattern(regexp = "Ativo|Inativo")
	@Column(length = 10, nullable = false)
	@Convert(converter = StatusConverter.class)
	private Status status = Status.ACTIVE;


	@NotNull
	@NotEmpty
	@Valid
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "course")
	//@JoinColumn(name = "course_id")
	private List<Lesson> lessons = new ArrayList<>();
    
}
