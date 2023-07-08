package com.mountblue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mountblue.model.Comment;
import com.mountblue.model.Post;
import com.mountblue.service.CommentService;
import com.mountblue.service.PostService;

@Controller
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/post/{id}")
	public String getPost(@PathVariable("id") int postId, Model model) {
		Post post = postService.getPostById(postId);
		List<Comment> commentList = commentService.fetchComment(postId);
		model.addAttribute("post", post);
		model.addAttribute("commentList", commentList);
		return "view-post";
	}

	@GetMapping("/newPost")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
	public String newPost() {
		return "new-post";
	}

	@PostMapping("/savePost")
	public String createPost(@RequestParam("title") String title,
							 @RequestParam("tags") String tags,
							 @RequestParam("author") String author,
							 @RequestParam("content") String content) {
		postService.createPost(title, tags, author, content);
		return "redirect:/";
	}

	@GetMapping("/updatePost/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or @postService.getPostById(#postId).getAuthor().equals(authentication.principal.username)")
	public String fetchPost(@PathVariable("id") int postId, Model model) {
		postService.fetchPost(postId, model);
		return "update-post";
	}

	@PostMapping("/updatePost/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or @postService.getPostById(#postId).getAuthor().equals(authentication.principal.username)")
	public String updatePost(@PathVariable("id") int postId,
							 @RequestParam("title") String title,
							 @RequestParam("tags") String tags,
							 @RequestParam("author") String author,
							 @RequestParam("content") String content) {
		postService.updatePost(postId, title, tags, author, content);
		return "redirect:/";
	}

	@GetMapping("/deletePost/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or @postService.getPostById(#postId).getAuthor().equals(authentication.principal.username)")
	public String deletePost(@PathVariable("id") int postId) {
		postService.deletePost(postId);
		return "redirect:/";
	}
}
