import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Question } from '../model/Question';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private apiUrl = 'http://localhost:8080';

  constructor(private httpClient: HttpClient) { }

  addQuestion(question: any) {
    return this.httpClient.post(`${this.apiUrl}/question/addquestion`, question);
  }

  getQuestionsByTopic(topic: string): Observable<Question[]> {
    const url = `${this.apiUrl}/question/getQuestionbytopic/${topic}`;
    return this.httpClient.get<Question[]>(url);
  }

  getAllQuestions():Observable<Question[]> {
    const url = `${this.apiUrl}/question/getallquestion`;
    return this.httpClient.get<Question[]>(url); 
  }

  getAllUnaprovedQuestion():Observable<Question[]> {
    const url = `${this.apiUrl}/question/getallquestionFalse`;
    return this.httpClient.get<Question[]>(url); 
  }

  getAllAprovedQuestion():Observable<Question[]> {
    const url = `${this.apiUrl}/question/getallquestionTrue`;
    return this.httpClient.get<Question[]>(url); 
  }

  updateQuestion(answer:Question):Observable<Question>{
    const url = `${this.apiUrl}/question/updatequestion`;
    return this.httpClient.put(url,answer) as Observable<Question>;
  }

  deleteQuestion(id:number): Observable<any>{
    return this.httpClient.delete(`${this.apiUrl}/question/deleteQuestionById/${id}`)
  }

  deleteAnswersFromQuestion(id:number):Observable<any>{
    return this.httpClient.delete(`${this.apiUrl}/answer/deleteAnswerByQuestionId/${id}`)
  }

  

}
