package com.mountblue.restcontroller;

import com.mountblue.model.Comment;
import com.mountblue.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {

    @Autowired
    private CommentService commentService;
    @GetMapping("/post/{postId}/comments")
    //@PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isValidUser(authentication, #postId)")
    public ResponseEntity<List<Comment>> getPostComments(@PathVariable("postId") int postId) {
        List<Comment> commentList = commentService.fetchComment(postId);
        if(commentList != null) {
            return ResponseEntity.ok(commentList);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/post/{postId}/comments/{commentId}")
    @PreAuthorize("@securityServiceRest.isValidUserForPost(#authentication, #postId)")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") int commentId,
                                           @PathVariable("postId") int postId,
                                           Authentication authentication)  {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment successfully deleted.");
    }

    @PutMapping("/post/{postId}/comments/{commentId}")
    @PreAuthorize("@securityServiceRest.isValidUserForPost(#authentication, #postId)")
    public ResponseEntity<String> updateComment(@PathVariable("commentId") int commentId,
                                                @PathVariable("postId") int postId,
                                                Authentication authentication,
                                                @RequestBody Comment updatedComment) {
        commentService.updateComment(commentId, updatedComment);
        return ResponseEntity.ok("Comment Successfully updated");
    }

    @PostMapping("/post/{postId}/comments")
    //@PreAuthorize("@securityServiceRest.isValidUser(authentication, #postId)")
    public ResponseEntity<Comment> saveComment(@RequestBody Comment comment, @PathVariable("postId") int postId) {
        Comment savedComment = commentService.saveComment(postId, comment.getName(), comment.getEmail(), comment.getComment());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }
}
