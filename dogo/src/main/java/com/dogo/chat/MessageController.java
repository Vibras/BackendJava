package com.dogo.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	@Autowired
	MessageRepository dao;

	@GetMapping("/chat")
	public List<Message> getMessages() {
		List<Message> foundMessages = dao.findAll();
		return foundMessages;
	}

	@PostMapping("/chat")
	public ResponseEntity<Message> postMessage(@RequestBody Message message) {

		// Saving to DB using an instance of the repo interface.
		Message createdMessage = dao.save(message);

		// RespEntity crafts response to include correct status codes.
		return ResponseEntity.ok(createdMessage);
	}

	@DeleteMapping("/chat/{id}")
	public ResponseEntity<Message> deleteMessage(@PathVariable(value = "id") Integer id) {
		Message foundMessage = dao.findById(id).orElse(null);

		if (foundMessage == null) {
			return ResponseEntity.notFound().header("Message", "Nothing found with that id").build();
		} else {
			dao.delete(foundMessage);
		}
		return ResponseEntity.ok().build();
	}

	@PutMapping("/chat/{id}")
	public ResponseEntity<Message> putMessage(@PathVariable Integer id, @RequestBody Message message) {
		// find the message to update
		Message foundMessage = dao.findById(id).orElse(null);
		// did we find something?
		if (foundMessage == null) {
			// nothing found by that id
			return ResponseEntity.notFound().header("Message", "Nothing found with that id").build();
		} else {
			// check the properties to update!
			if (message.getName() != null) {
				// update the name
				foundMessage.setName(message.getName());
			}
			if (message.getContent() != null) {
				// update the content
				foundMessage.setContent(message.getContent());
			}
			dao.save(foundMessage);
		}
		return ResponseEntity.ok(foundMessage);
	}

}