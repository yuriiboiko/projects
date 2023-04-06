import { Question } from "./Question";

export class Answer{
    id!:number;
    description_answer!:string;
    img_src!:string;
    status!:string;
    approved_by!:string;
    created_by!:string;
    datetime!:string;
    question!: Question;
  }