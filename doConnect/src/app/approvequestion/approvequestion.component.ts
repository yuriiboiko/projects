import { Component, OnInit } from '@angular/core';
import { Question } from '../model/Question';
import { QuestionService } from '../service/question.service';

@Component({
  selector: 'app-approvequestion',
  templateUrl: './approvequestion.component.html',
  styleUrls: ['./approvequestion.component.css']
})
export class ApprovequestionComponent implements OnInit {

  questions!: Question[];

  constructor(private questionService: QuestionService) { }

  ngOnInit() {
    this.getAllQuestions();
  }

  getAllQuestions() {
    this.questionService.getAllQuestions().subscribe((data: Question[]) => {
      this.questions = data;
    });
  }

  approveQuestion(question: Question) {
    if(question.status=='false'){
      question.status = 'true';
      question.qapproved_by=sessionStorage.getItem("username")??'';
    }else{
      question.status = 'false';
    question.qapproved_by='';
    }

    this.questionService.updateQuestion(question).subscribe({
      next:response=>{
        this.getAllQuestions()
      },error:e=>{
        console.log(e);
      }
    });
  }


  deleteQuestion(id:number){
    this.questionService.deleteAnswersFromQuestion(id).subscribe({
      next:data=>{

        this.questionService.deleteQuestion(id).subscribe({
          next:data=>{
            this.getAllQuestions()
          }, error:error=>{
            alert("Error Deleting Question")
          }
        });
      },error:error=>{
        alert("Error Deleting Answers that are part of this Question")
      }
    });


  }

}
