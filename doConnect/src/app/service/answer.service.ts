import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Answer } from '../model/Answer';

@Injectable({
  providedIn: 'root'
})
export class AnswerService {

  constructor(private httpClient: HttpClient) { }

  private apiUrl = 'http://localhost:8080/answer';


  addAnswer(answer:any){  
    return this.httpClient.post(`${this.apiUrl}/addanswer`,answer);
  }

  getAnswerForQuestion(id: any):Observable<Answer[]>{
    return this.httpClient.get(`${this.apiUrl}/getAnswersbyQuestionID/${id}`) as Observable<Answer[]>
  }

  getValidAnswerForQuestion(id: any):Observable<Answer[]>{
    return this.httpClient.get(`${this.apiUrl}/getValidAnswerById/${id}`) as Observable<Answer[]>
  } 

  getAllAnswers():Observable<Answer[]>{
      return this.httpClient.get(`${this.apiUrl}/getallanswers`) as Observable<Answer[]>
  }

  updateAnswer(answer:Answer): Observable<Answer>{
    return this.httpClient.put(`${this.apiUrl}/updateanswer`,answer)as Observable<Answer>;
  }

  getAllUnapprovedAnswers():Observable<Answer[]>{
      return this.httpClient.get(`${this.apiUrl}/getAllAnswerFalse}`) as Observable<Answer[]>
  }
  getAllApprovedAnswers():Observable<Answer[]>{
    return this.httpClient.get(`${this.apiUrl}/getAllAprovedAnswers}`) as Observable<Answer[]>
  }

  deleteAnswer(id:number):Observable<any>{
    return this.httpClient.delete(`${this.apiUrl}/deleteAnswerById/${id}`)
  }

  
}


