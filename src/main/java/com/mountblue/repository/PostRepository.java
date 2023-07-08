package com.mountblue.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mountblue.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrTagsNameContainingIgnoreCase(String search1, String search2, String search3, String search4, Pageable pageable);

    Page<Post> findByAuthorInOrPublishedAtInOrTagsIn(List<String> authorFilters, List<LocalDateTime> publishedAtFilters, List<Integer> tagFilters, Pageable pageable);
}
