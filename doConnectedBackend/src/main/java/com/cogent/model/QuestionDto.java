package com.cogent.model;

public class QuestionDto {
		private String description_question;
		private String image_src;
		private String topic;
		private String title;
		private String qcreated_by;
		
		
		public QuestionDto(String description_question, String image_src, String topic, String title,
				String qcreated_by) {
			super();
			this.description_question = description_question;
			this.image_src = image_src;
			this.topic = topic;
			this.title = title;
			this.qcreated_by = qcreated_by;
		}
		public QuestionDto() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getDescription_question() {
			return description_question;
		}
		public void setDescription_question(String description_question) {
			this.description_question = description_question;
		}
		public String getImage_src() {
			return image_src;
		}
		public void setImage_src(String image_src) {
			this.image_src = image_src;
		}
		public String getTopic() {
			return topic;
		}
		public void setTopic(String topic) {
			this.topic = topic;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getQcreated_by() {
			return qcreated_by;
		}
		public void setQcreated_by(String qcreated_by) {
			this.qcreated_by = qcreated_by;
		}


}
