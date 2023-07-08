package com.mountblue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mountblue.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String tag);
    @Query("SELECT new map(t.name as name, t.id as id) FROM Tag t")
    List<Map<String, Integer>> getAllNamesWithIds();
}
