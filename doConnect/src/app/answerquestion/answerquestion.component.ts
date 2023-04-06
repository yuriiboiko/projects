import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnswerService } from '../service/answer.service';


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

interface Answer{
  description_answer:string;
  img_src:string;
  status:string;
  approved_by:string;
  created_by:string;
  question: Question;
}

@Component({
  selector: 'app-answerquestion',
  templateUrl: './answerquestion.component.html',
  styleUrls: ['./answerquestion.component.css']
})
export class AnswerQuestionComponent implements OnInit{
  question: any;
  answer:any;
  answers: Answer[] = [];
  displayForm:boolean= false;
  constructor(private route:ActivatedRoute, private answerService:AnswerService){ }

  ngOnInit(): void {
    const myData:string|null = this.route.snapshot.queryParamMap.get('myData');
    if(myData!==null){
       this.question = JSON.parse(myData);
    }
    this.loadAnswers();
  }
  answerQuestion(id:string){
    this.displayForm=true;
  }

  cancelAnswerForm(){
    this.displayForm=false;
  }
  onAnswerSubmitted(answer: Answer) {
    // Do something with the answer object, such as adding it to a list of answers
    this.answer=answer;
    this.answer.question=this.question;
    this.displayForm=false;
    this.answerService.addAnswer(answer).subscribe({
      next: response=>{
        alert("answer added successfully");
        this.loadAnswers();

      },
      error: e=>{
        alert("failed to add answer to database")
      }
    });

  }

  loadAnswers(){
    this.answerService.getValidAnswerForQuestion(this.question.id).subscribe({
      next: (response: Answer[]) => {
        this.answers = response;
      },
      error: e => {
        console.log('Failed to retrieve answers', e);
      }
    });
  }

}
