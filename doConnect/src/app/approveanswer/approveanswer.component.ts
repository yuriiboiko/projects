import { Component ,OnInit  } from '@angular/core';
import { Answer } from '../model/Answer';
import { AnswerService } from '../service/answer.service';

@Component({
  selector: 'app-approveanswer',
  templateUrl: './approveanswer.component.html',
  styleUrls: ['./approveanswer.component.css']
})
export class ApproveanswerComponent implements OnInit {

  answers: Answer[] = [];

  constructor(private answerService: AnswerService) {}


  ngOnInit() {
    this.getAnswers();

  } 

  getAnswers(){
    this.answerService.getAllAnswers().subscribe({
      next:data=>{
        this.answers=data;
      },error: e=>{
        alert("issue retrieving data")
      }
    });
}

  approveAnswer(answer: Answer): void {
    if(answer.status=="false"){
      answer.status = 'true';
      answer.approved_by= sessionStorage.getItem("username")??'';
    }else{
      answer.status = 'false';
      answer.approved_by= '';
    }
    
    this.answerService.updateAnswer(answer).subscribe({
      next: updatedAnswer => {
        this.getAnswers();
      },
      error: error => {
        console.error(`Error updating answer ${answer.id}: `, error);
      }
    });
  }

  deleteAnswer(id: any): void {
    this.answerService.deleteAnswer(id).subscribe({
      next: () => {
        this.getAnswers();
      },
      error: error => {
        console.error(`Error deleting answer ${id}: `, error);
      }
    });
  }

}
