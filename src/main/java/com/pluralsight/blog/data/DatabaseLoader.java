package com.pluralsight.blog.data;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.stereotype.*;

import com.pluralsight.blog.model.*;

@Component
public class DatabaseLoader implements ApplicationRunner {
	private final String[] templates = {
		"Smart Home %s", "Mobile %s - For When You're On he Go", "The %s - Your New Favorite Accessory"};

	private final String[] gadgets = {
		"Earbuds", "Speakers", "Tripod", "Instant Pot", "Coffee Cup", "Keyboard", "Sunglasses"};

	public List<Post> randomPosts = new ArrayList<>();

	public List<Author> authors = new ArrayList<>();

	private final PostRepository postRepository;

	@Autowired
	public DatabaseLoader(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		IntStream.range(0, 40).forEach(i -> {
			String template = templates[i % templates.length];
			String gadget = gadgets[i % gadgets.length];

			String title = String.format(template, gadget);
			Post post = new Post(title, "Lorem ipsum dolor sit amet, consectetur adipiscing elit… ");
			randomPosts.add(post);
		});
		postRepository.saveAll(randomPosts);
	}
}
