package com.mountblue.restcontroller;

import com.mountblue.model.Post;
import com.mountblue.model.Tag;
import com.mountblue.service.CommentService;
import com.mountblue.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PostRestController {


    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts")
    public ResponseEntity home(Model model, @RequestParam(value="order", defaultValue="") String order,
                               @RequestParam(value="search", defaultValue="") String search,
                               @RequestParam(value="author", defaultValue="") String author,
                               @RequestParam(value="dateTime", defaultValue="") String dateTime,
                               @RequestParam(value="tag", defaultValue="") String tag,
                               @RequestParam(value = "start", defaultValue = "0", required = false) int pageNumber,
                               @RequestParam(value = "limit", defaultValue = "10", required = false) int pageSize) {
        pageNumber/=10;
        Page<Post> posts = postService.searchFilterSort(order, search, author, dateTime, tag, pageNumber, pageSize, model);
        model.addAttribute("order", order);
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", posts.getTotalPages());
        return ResponseEntity.ok(posts.getContent());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable("postId") int postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping("/post")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    public ResponseEntity<String> createPost(@RequestBody Post postRequest, Authentication authentication) {
        String tagsString = postRequest.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));
        postService.createPost(postRequest.getTitle(), tagsString, postRequest.getAuthor(), postRequest.getContent());
        return ResponseEntity.ok("Post created successfully");
    }

    @PutMapping("/post/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isValidUserForPost(#authentication, #postId)")
    public ResponseEntity<String> updatePost(@PathVariable("postId") int postId,
                                             @RequestBody Post postRequest,
                                             Authentication authentication) {
        String tagsString = postRequest.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(","));
        boolean updated = postService.updatePost(postId, postRequest.getTitle(), tagsString, postRequest.getAuthor(), postRequest.getContent());
        if (updated) {
            return ResponseEntity.ok("Post updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/post/{postId}")
    @PreAuthorize("hasAuthority('ADMIN') or @securityServiceRest.isValidUserForPost(#authentication, #postId)")
    public ResponseEntity<String> deletePost(@PathVariable("postId") int postId, Authentication authentication) {
        boolean deleted = postService.deletePost(postId);
        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}

