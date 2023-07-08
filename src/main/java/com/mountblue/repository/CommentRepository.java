package com.mountblue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mountblue.model.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
