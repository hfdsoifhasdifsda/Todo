package example.todo.payload.request;

import java.util.List;

public class TodoDelRequest {
	private List<Message> message;

	public List<Message> getMessage() {
		return message;
	}

	public void setMessage(List<Message> message) {
		this.message = message;
	}

	public static class Message {
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}


	}
}
