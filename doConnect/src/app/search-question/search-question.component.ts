import { Component, OnInit } from '@angular/core';
import { Router,NavigationExtras } from '@angular/router';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { QuestionService } from '../service/question.service';

interface Question {
  id: number;
  description_question: string;
  image_src: string;
  datetime: string;
  status: string;
  topic: string;
  title: string;
  qcreated_by: string;
  qapproved_by: string;
}

@Component({
  selector: 'app-search-question',
  templateUrl: './search-question.component.html',
  styleUrls: ['./search-question.component.css']
})
export class SearchQuestionComponent implements OnInit{
  selectedTopic: string = '';
  searchText: string= '';
  searchFilter=false;
  questions: Question[] = [];

  topicList = JSON.parse(sessionStorage.getItem("topicList") ?? "");

  ngOnInit() {
  }



  constructor(private questionService:QuestionService,private router: Router){}

  answerQuestion(question:Question){
    
    // Navigate to the '/my-component' route and pass the 'myObject' object as a parameter
    const navigationExtras: NavigationExtras = { queryParams: { myData: JSON.stringify(question) } };
    this.router.navigate(['answerQuestion'], navigationExtras);

  }

  clearResults(){
    this.searchFilter=false;
    this.selectedTopic='';
  }

  searchQuestions(){
    if (this.searchText === '') {
      this.questions = [];
      return;
    }

    this.questions = [];
    this.questionService.getAllAprovedQuestion().subscribe({
      next: (data) => {
        // Filter the list to only include questions that contain the phrase "certain phrase"
        const filteredQuestions = data.filter((q) => q.title.includes(this.searchText));
  
        // Do something with the filtered list of questions, for example, assign it to a variable
        this.questions = filteredQuestions;
        this.searchFilter=true;
      },
      error: (error) => {
        console.error(error);
      }
    });

  }
  
  onTopicSelected(){
    if (this.selectedTopic === '') {
      this.questions = [];
      return;
    }
    this.questionService.getAllAprovedQuestion()
      .pipe(
      catchError((error) => {
        console.error(error);
        return of([]);
        })
      )
      .subscribe({
        next: (data) => {
        const filteredQuestions = data.filter((q) => q.topic.includes((this.selectedTopic)));

        this.questions = filteredQuestions;
        },
      error: (error) => {
        console.error(error);
      }
    });
  }

}
