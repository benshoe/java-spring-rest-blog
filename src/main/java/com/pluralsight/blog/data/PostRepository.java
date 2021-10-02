package com.pluralsight.blog.data;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.*;

import com.pluralsight.blog.model.*;

@Component
@RestResource
public interface PostRepository extends JpaRepository<Post, Long> {

	@RestResource(rel = "contains-title", path = "containsTitle")
	List<Post> findByTitleContaining(@Param("title") String title);
}
