package com.mountblue.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mountblue.repository.CommentRepository;
import com.mountblue.repository.PostRepository;
import com.mountblue.model.Comment;
import com.mountblue.model.Post;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public List<Comment> fetchComment(int postId) {
		Post post = postRepository.findById(postId).get();
		return post.getComments();
	}

	public void deleteComment(int id) {
		commentRepository.deleteById(id);
	}

	public void updateComment(int id, Comment updatedComment) {
		Comment comment = commentRepository.findById(id).get();
		comment.setComment(updatedComment.getComment());
		comment.setUpdatedAt(LocalDateTime.now());
		commentRepository.save(comment);
	}

	public Comment saveComment(int postId, String name, String email, String comment) {
		Comment newComment = new Comment();
		newComment.setName(name);
		newComment.setEmail(email);
		newComment.setComment(comment);
		newComment.setPost(postRepository.findById(postId).get());
		commentRepository.save(newComment);
		return newComment;
	}
}
