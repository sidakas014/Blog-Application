package com.mountblue.controller;

import com.mountblue.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mountblue.service.CommentService;

@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	@GetMapping("/deleteComment/{commentId}/{postId}")
	public String deleteComment(@PathVariable("commentId") int commentId, @PathVariable("postId") int postId) {
		commentService.deleteComment(commentId);
		return "redirect:/post/{postId}";
	}

	@PostMapping("/updateComment/{commentId}/{postId}")
	public String updateComment(@PathVariable("commentId") int commentId, @RequestParam("updatedComment") Comment updatedComment) {
		commentService.updateComment(commentId, updatedComment);
		return "redirect:/post/{postId}";
	}

	@PostMapping("/saveComment/{postId}")
	public String saveComment(@PathVariable("postId") int postId, @RequestParam("name") String name,
			@RequestParam("email") String email, @RequestParam("comment") String comment) {
		commentService.saveComment(postId, name, email, comment);
		return "redirect:/post/{postId}";
	}
}
