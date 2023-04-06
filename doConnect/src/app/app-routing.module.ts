import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnswerQuestionComponent } from './answerquestion/answerquestion.component';
import { ApproveanswerComponent } from './approveanswer/approveanswer.component';
import { ApprovequestionComponent } from './approvequestion/approvequestion.component';
import { AskAQuestionComponent } from './ask-aquestion/ask-aquestion.component';
import { UserListComponent } from './user-list/user-list.component';
import { ConversationComponent } from './conversation/conversation.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { RegisterComponent } from './register/register.component';
import { SearchQuestionComponent } from './search-question/search-question.component';
import { AuthGuard } from './service/auth-guard.service';
 

const routes: Routes = [
  
    {path:"login", component:LoginComponent},
    {path:"", component:HomeComponent},
    {path:"register", component:RegisterComponent},
    {path:"forbidden", component:ForbiddenComponent},
    {path: "main", component:MainComponent, canActivate: [AuthGuard]},
    {path: "chat", component:UserListComponent, canActivate: [AuthGuard]},
    {path: "askQuestion", component:AskAQuestionComponent, canActivate: [AuthGuard]},
    {path: "searchQuestion", component:SearchQuestionComponent, canActivate: [AuthGuard]},
    {path: "conversation", component:ConversationComponent, canActivate: [AuthGuard]},
    {path: "answerQuestion", component:AnswerQuestionComponent, canActivate: [AuthGuard]},
    {path: "approveAnswer", component:ApproveanswerComponent, canActivate: [AuthGuard]},
    {path: "approveQuestion", component:ApprovequestionComponent, canActivate: [AuthGuard]},
    {path: '**', redirectTo: 'forbidden' } // Redirect all other invalid URLs to the forbidde page


  
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
