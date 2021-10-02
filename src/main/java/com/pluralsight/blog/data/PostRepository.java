package com.pluralsight.blog.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import com.pluralsight.blog.model.Post;

@Component
@RepositoryRestResource
public interface PostRepository extends JpaRepository<Post, Long> {

	@RestResource(rel = "contains-title", path = "containsTitle")
	List<Post> findByTitleContaining(@Param("title") String title);

	@RestResource(rel = "author-lastname", path = "byAuthorLastname")
	List<Post> findByAuthor_Lastname(@Param("lastname") String lastname);
}
