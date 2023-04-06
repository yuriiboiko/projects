import { Component, EventEmitter, Output } from '@angular/core';

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
  selector: 'app-answerform',
  templateUrl: './answerform.component.html',
  styleUrls: ['./answerform.component.css']
})
export class AnswerformComponent {

  maxAnswerLength=575;
  @Output() answerSubmitted = new EventEmitter<Answer>();

  answer:Answer;

  constructor() {
    this.answer = {
      description_answer: '',
      img_src: '',
      status: "false",
      approved_by: '',
      created_by: '',
      question: {
        id: 0,
        description_question: '',
        image_src: '',
        datetime: '',
        status: "false",
        topic: '',
        title: '',
        qcreated_by: '',
        qapproved_by: ''
      }
    };
  }

  sumbitForm(){
    if(this.answer.description_answer===null||this.answer.description_answer===''){
      alert("cant leave description empty");
      return;
    }
    this.answer.created_by=sessionStorage.getItem("username")??"";
    this.answer.status="false";
    this.answerSubmitted.emit(this.answer);
  }
}
