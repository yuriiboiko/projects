import { Component ,OnInit} from '@angular/core';
import { Router } from '@angular/router';
import { QuestionService } from '../service/question.service';

@Component({
  selector: 'app-ask-aquestion',
  templateUrl: './ask-aquestion.component.html',
  styleUrls: ['./ask-aquestion.component.css']
})
export class AskAQuestionComponent implements OnInit {

  topicList = JSON.parse(sessionStorage.getItem("topicList")??'');

  constructor(private questionService: QuestionService, private route:Router) {}
  maxDescriptionLength = 575;
  maxTitleLength = 255;

  title: string='';
  topic: string ='';
  description: string='';
  imageFile!: File;
  imageSrc!: string;
  qcreated_by:string='';

  ngOnInit(): void {
    this.qcreated_by=sessionStorage.getItem('username')??'';
  }

  submit() {
   
    if(this.title===null || this.title===''){
      alert("Title field cannot be empty")
      return;
    }else if(this.topic===null || this.topic===''){
      alert("Topic field cannot be empty")
      return;
    }
    if(this.description===null || this.description===''){
      alert("Description field cannot be empty")
      return;
    }

    const question = {
      description_question: this.description,
      imageSrc: this.imageSrc,
      topic: this.topic,
      title: this.title,
      qcreated_by: this.qcreated_by
    };


    this.questionService.addQuestion(question).subscribe({
      next: () => {
        alert('Question submitted');
        this.description='';
        this.topic='';
        this.title='';

        this.route.navigate(['askQuestion']);
      },
      error: (error) => {
        alert('Error submitting question:');
      },
    });


    
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.imageSrc = reader.result as string;
      };
    }
  }


}
