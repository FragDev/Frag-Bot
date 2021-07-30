package me.fragment.fragbot.managers.form;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import me.fragment.fragbot.managers.form.FormManager.FormInstance;
import net.dv8tion.jda.api.interactions.components.Button;

public class Form {

	private String message;
	private List<Question> questions = new ArrayList<>();
	private Consumer<FormInstance> consumer;
	private Consumer<FormInstance> cancelConsumer;

	public Form(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public void addQuestions(Question question) {
		this.questions.add(question);
	}

	public Consumer<FormInstance> getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer<FormInstance> consumer) {
		this.consumer = consumer;
	}
	
	public Consumer<FormInstance> getCancelConsumer() {
		return cancelConsumer;
	}
	
	public void setCancelConsumer(Consumer<FormInstance> cancelConsumer) {
		this.cancelConsumer = cancelConsumer;
	}

	public static class Question {

		private Type type;
		private String message;
		private List<Button> buttons = new ArrayList<>();
		private Predicate<String> inputPredicate;
		private Consumer<FormInstance> consumer;

		public Question(Type type, String message) {
			this.type = type;
			this.message = message;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public List<Button> getButtons() {
			return buttons;
		}

		public void setButtons(List<Button> buttons) {
			this.buttons = buttons;
		}

		public Question addButton(Button button) {
			this.buttons.add(button);
			return this;
		}

		public Predicate<String> getInputPredicate() {
			return inputPredicate;
		}

		public Question setInputPredicate(Predicate<String> inputPredicate) {
			this.inputPredicate = inputPredicate;
			return this;
		}

		public Consumer<FormInstance> getConsumer() {
			return consumer;
		}

		public Question setConsumer(Consumer<FormInstance> consumer) {
			this.consumer = consumer;
			return this;
		}

		public enum Type {
			BUTTONS, INPUT;
		}

	}

}
