import { Component } from '@angular/core';
import { FormBuilder, FormGroup, NonNullableFormBuilder, UntypedFormArray, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { CoursesService } from '../../services/courses.service';
import { ActivatedRoute } from '@angular/router';
import { Course } from '../../model/course';
import { Lesson } from '../../model/Lesson';
import {MatIconModule} from '@angular/material/icon';
import { FormUtilsService } from '../../../shared/form/form-utils.service';

@Component({
  selector: 'app-course-form',
  standalone: true,
  imports: [CommonModule, MatFormFieldModule, MatInputModule, ReactiveFormsModule, MatCardModule, MatToolbarModule, MatSelectModule, MatSnackBarModule, MatIconModule],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent {

  form!: FormGroup;


  constructor(private formBuilder: NonNullableFormBuilder,
    private service: CoursesService,
    private snackBar: MatSnackBar,
    private location: Location,
    private route: ActivatedRoute,
    public formUtils: FormUtilsService){

  }

  ngOnInit():void {
    const course: Course = this.route.snapshot.data['course'];

    this.form = this.formBuilder.group({
      _id:[course._id],
      name: [course.name, [Validators.required,
      Validators.minLength(5),
      Validators.maxLength(100)]],
      category: [course.category, [Validators.required]],
      lessons: this.formBuilder.array(this.retrieveLessons(course), Validators.required)
    });

  }

  private retrieveLessons(course: Course) {
    const lessons = [];
    if (course?.lessons){
      course.lessons.forEach(lesson => lessons.push(this.createLesson(lesson)));
    } else{
      lessons.push(this.createLesson());
    }
    return lessons;
  }

  private createLesson(lesson: Lesson = {id: '', name: '', youtubeUrl: ''}) {
    return this.formBuilder.group({
      id: [lesson.id],
      name: [lesson.name, [Validators.required,
      Validators.minLength(5),
      Validators.maxLength(100)]],
      youtubeUrl: [lesson.youtubeUrl, [Validators.required,
      Validators.minLength(10),
      Validators.maxLength(20)]]
    });
  }

  getLessonsFormArray() {
    return (<UntypedFormArray>this.form.get('lessons')).controls;
  }

  addNewLesson() {
    const lessons = this.form.get('lessons') as UntypedFormArray;
    lessons.push(this.createLesson());
  }

  removeLesson(index: number) {
    const lessons = this.form.get('lessons') as UntypedFormArray;
    lessons.removeAt(index);
  }

  onSubmit(){
    if(this.form.valid) {
      this.service.save(this.form.value)
        .subscribe(result => this.onSuccess(), error => this.onError());
    }else {
      this.formUtils.validateAllFormFields(this.form);
    }
  }

  onCancel(){
    this.location.back();
  }

  private onSuccess() {
    this.snackBar.open('Curso salvo com sucesso!', '', {duration: 3000});
    this.onCancel();
  }


  private onError() {
    this.snackBar.open('Erro ao salvar curso.', '', {duration: 3000});
  }


}
