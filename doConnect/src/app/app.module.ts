import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http'
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { MainComponent } from './main/main.component';

import { AskAQuestionComponent } from './ask-aquestion/ask-aquestion.component';
import { SearchQuestionComponent } from './search-question/search-question.component';
import { BasicAuthHtppInterceptorService } from './service/basicAuthHtppInterceptorService';
import { UserListComponent } from './user-list/user-list.component';
import { ConversationComponent } from './conversation/conversation.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { AnswerQuestionComponent } from './answerquestion/answerquestion.component';
import { AnswerformComponent } from './answerform/answerform.component';
import { ApproveanswerComponent } from './approveanswer/approveanswer.component';
import { ApprovequestionComponent } from './approvequestion/approvequestion.component';


 
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    MainComponent,
    AskAQuestionComponent,
    SearchQuestionComponent,
    UserListComponent,
    ConversationComponent,
    ForbiddenComponent,
    AnswerQuestionComponent,
    AnswerformComponent,
    ApproveanswerComponent,
    ApprovequestionComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [ {
    provide:HTTP_INTERCEPTORS, useClass:BasicAuthHtppInterceptorService, multi:true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
