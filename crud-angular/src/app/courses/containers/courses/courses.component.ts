import { CoursesService } from '../../services/courses.service';
import { Component, ViewChild } from '@angular/core';
import { Course } from '../../model/course';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import {MatToolbarModule} from '@angular/material/toolbar';
import { catchError, Observable, of, tap } from 'rxjs';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import { CategoryPipe } from "../../../shared/pipes/category.pipe";
import { ActivatedRoute, Router } from '@angular/router';
import { CoursesListComponent } from '../../components/courses-list/courses-list.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../../shared/components/error-dialog/error-dialog.component';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { CoursePage } from '../../model/course-page';
import {MatPaginator, MatPaginatorModule, PageEvent} from '@angular/material/paginator';




@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [MatTableModule, MatCardModule, MatToolbarModule, MatProgressSpinnerModule, CommonModule, MatIconModule, CategoryPipe, CoursesListComponent, MatPaginatorModule],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent {

  courses$: Observable<CoursePage> | null = null;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  pageIndex = 0;
  pageSize = 10;

  constructor(private coursesService: CoursesService, public dialog: MatDialog, private router: Router, private route: ActivatedRoute, private snackBar: MatSnackBar) {
    this.refresh();
  }

  refresh(pageEvent: PageEvent = {length: 0, pageIndex: 0, pageSize: 10 }) {
    this.courses$ = this.coursesService.list(pageEvent.pageIndex, pageEvent.pageSize)
    .pipe(
      tap( () => {
        this.pageIndex = pageEvent.pageIndex;
        this.pageSize = pageEvent.pageSize;
      }),
      catchError(error => {
        this.onError('erro ao carregar cursos');
        return of({courses: [], totalElements: 0, totalPages: 0 })
      })
    );
  }

  onError(errorMsg: string){
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });
  }

  onAdd(){
    this.router.navigate(['new'], {relativeTo: this.route});
  }

  onEdit(course: Course) {
    this.router.navigate(['edit', course._id], {relativeTo: this.route});
  }

  onRemove(course: Course) {

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Tem certeza que deseja remover esse curso?',
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if(result) {
        this.coursesService.remove(course._id).subscribe(
        () => {
        this.refresh();
        this.snackBar.open('Curso removido com sucesso!', 'X', {duration: 3000, verticalPosition: 'top', horizontalPosition: 'center'});
        },
        () => this.onError('Erro ao tentar remover curso.')
        );
      }
    });
  }

}
