package com.mountblue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mountblue.repository.TagRepository;
import com.mountblue.model.Tag;

@Service
public class TagService {

	@Autowired
	private TagRepository tagRepository;

	public TagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public void saveTag(Tag tag) {
		tagRepository.save(tag);
	}

	public void updateTag(Tag tags) {
		Tag existingTag = tagRepository.findById(tags.getId()).get();
		existingTag.setId(tags.getId());
		existingTag.setName(tags.getName());
		tagRepository.save(existingTag);
	}
}
