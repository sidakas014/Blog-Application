package com.mountblue.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.mountblue.repository.PostRepository;
import com.mountblue.repository.TagRepository;
import com.mountblue.model.Post;
import com.mountblue.model.Tag;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TagRepository tagRepository;

	public PostService(PostRepository postRepository, TagRepository tagRepository) {
		this.postRepository = postRepository;
		this.tagRepository = tagRepository;
	}

	public Post getPostById(int postId) {
		return postRepository.findById(postId).get();
	}

	public void createPost(String title, String tags, String author, String content) {
		Post post = new Post();
		post.setTitle(title);
		post.setAuthor(author);
		post.setContent(content);
		String[] tagsList = tags.split(",");
		for (int i = 0; i < tagsList.length; i++) {
		    tagsList[i] = tagsList[i].trim();
		}
		List<Tag> finalTags = new ArrayList<>();
		for (String tag : tagsList) {
			Tag newTag = new Tag();
			Tag existingTag = tagRepository.findByName(tag);
			if(existingTag == null){
				newTag.setName(tag);
				finalTags.add(newTag);
			}else {
				finalTags.add(existingTag);
			}
		}
		post.setTags(finalTags);
		if (post.getContent().length() < 120) {
			post.setExcerpt(content + ".....");
		} else {
			post.setExcerpt(content.substring(0, 120) + ".....");
		}
		postRepository.save(post);
	}

	public void fetchPost(int postId, Model model) {
		Post post = postRepository.findById(postId).get();
		List<Tag> tags = post.getTags();
		String tagName = "";
		for (Tag tag : tags) {
			tagName += tag.getName() + ",";
		}
		model.addAttribute("tags", tagName);
		model.addAttribute("post", post);
	}

	public boolean updatePost(int postId, String title, String tags, String author, String content) {
		String[] tagsList = tags.split(",");
		for (int i = 0; i < tagsList.length; i++) {
			tagsList[i] = tagsList[i].trim();
		}
		List<Tag> finalTags = new ArrayList<>();
		for (String tag : tagsList) {
			Tag newTag = new Tag();
			Tag existingTag = tagRepository.findByName(tag);
			if(existingTag == null){
				newTag.setName(tag);
				finalTags.add(newTag);
			}else {
				finalTags.add(existingTag);
			}
		}
		Post existingPost = postRepository.findById(postId).get();
		existingPost.setId(postId);
		if(title != null) {
			existingPost.setTitle(title);
		}
		if(tags != null) {
			existingPost.setTags(finalTags);
		}
		if(author != null) {
			existingPost.setAuthor(author);
		}
		if(content != null) {
			existingPost.setContent(content);
			if (content.length() < 120) {
				existingPost.setExcerpt(content + ".....");
			} else {
				existingPost.setExcerpt(content.substring(0, 120) + ".....");
			}
		}
		existingPost.setUpdatedAt(LocalDateTime.now());
		postRepository.save(existingPost);
		return true;
	}

	public boolean deletePost(int postId) {
		postRepository.deleteById(postId);
		return true;
	}
	
	public Page<Post> searchFilterSort(String order, String search, String author, String dateTime, String tag, int pageNumber, int pageSize, Model model) {
		if(!(search.equals(""))) {
			Page<Post> posts = searchedPosts(search, 0 ,100);
			model.addAttribute("posts", posts);
			return posts;
		}else if(!(order.equals(""))){
			Page<Post> posts = sortedPosts(order, 0, 100);
			model.addAttribute("posts", posts.getContent());
			return posts;
		}else if(!(author.equals("")) || !(dateTime.equals("")) || !(tag.equals(""))){
			Page<Post> posts = filteredPosts(author, dateTime, tag, 0, 100);
			model.addAttribute("posts", posts);
			return posts;
		} else {
			Page<Post> posts = getPosts(pageNumber, pageSize, model);
			model.addAttribute("posts", posts.getContent());
			model.addAttribute("currentPage", pageNumber);
			model.addAttribute("totalPages", posts.getTotalPages());
			return posts;
		}
	}

	public Page<Post> getPosts(int pageNumber, int pageSize, Model model) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> pagePost = postRepository.findAll(pageable);
		List<Post> allPost = postRepository.findAll();
		List<Tag> tags = tagRepository.findAll();
		List<Map<String, Integer>> tagName = tagRepository.getAllNamesWithIds();
		Set<String> postAuthor = new HashSet<>();
		Set<LocalDateTime> postPublishedAt = new HashSet<>();
		for (Post post : allPost) {
			postAuthor.add(post.getAuthor());
			postPublishedAt.add(post.getPublishedAt());
		}
		model.addAttribute("postAuthor", postAuthor);
		model.addAttribute("postPublishedAt", postPublishedAt);
		model.addAttribute("tags", tagName);
		return pagePost;
	}

	public Page<Post> searchedPosts(String search, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Post> post = postRepository
				.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrTagsNameContainingIgnoreCase(search,
						search, search, search, pageable);
		return post;
	}

	public Page<Post> sortedPosts(String order, int pageNumber, int pageSize) {
		Pageable pageable;
		if (order.equals("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publishedAt").ascending());
		} else {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publishedAt").descending());
		}
		Page<Post> page = postRepository.findAll(pageable);
		return page;
	}

	public Page<Post> filteredPosts(String authors, String dateTimes, String tags, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<String> authorFilters = Arrays.asList(authors.split(","));
		List<String> dateTimeList = Arrays.asList(dateTimes.split(","));
		List<String> tagsList = Arrays.asList(tags.split(","));
		List<Integer> tagFilters = new ArrayList<>();
		List<LocalDateTime> publishedAtFilters = new ArrayList<>();
		for(String tag : tagsList){
			if (!tag.isEmpty()) {
				tagFilters.add(Integer.parseInt(tag));
			}
		}
		for(String dateTime : dateTimeList){
			if (!dateTime.isEmpty()) {
				LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
				publishedAtFilters.add(localDateTime);
			}
		}
		return postRepository.findByAuthorInOrPublishedAtInOrTagsIn(authorFilters, publishedAtFilters, tagFilters, pageable);
	}
}
