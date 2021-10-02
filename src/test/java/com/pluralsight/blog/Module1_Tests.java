package com.pluralsight.blog;

import static org.junit.Assert.*;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.powermock.core.classloader.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.test.context.junit4.*;

import com.pluralsight.blog.data.*;

@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureMockMvc
@PrepareForTest(DatabaseLoader.class)
public class Module1_Tests {

	@Autowired
	private DatabaseLoader databaseLoader;

	@Autowired
	private PostRepository postRepository;

	private PostRepository spyRepository;

	@Before
	public void setup() {
		Constructor<DatabaseLoader> constructor = null;
		try {
			constructor = DatabaseLoader.class.getDeclaredConstructor(PostRepository.class);
		} catch(NoSuchMethodException e) {
			//e.printStackTrace();
		}

		spyRepository = Mockito.spy(postRepository);
		try {
			databaseLoader = constructor.newInstance(spyRepository); //new BlogController(spyRepository);
		} catch(Exception e) {
			//e.printStackTrace();
		}
	}


	@Test
	public void task_1() {
		Class c = PostRepository.class;
		Class[] interfaces = c.getInterfaces();

		assertEquals("Task 1: `PostRepository` should extend 1 interface - `JpaRepository`.",
			1, interfaces.length);

		assertEquals("Task 1: `PostRepository` should be an `interface` that extends `JpaRepository<Post, Long>`.",
			JpaRepository.class, interfaces[0]);
	}

	@Test
	public void task_2() {
		// Task 1 - Add field PostRepository postRepository; to DatabaseLoader
		Field[] fields = DatabaseLoader.class.getDeclaredFields();

		boolean postRepositoryExists = false;
		boolean annotationExists = false;
		for(Field field : fields) {
			if(field.getName().equals("postRepository") && field.getType().equals(PostRepository.class)) {
				postRepositoryExists = true;
			}
		}

		String message = "Task 2: A field called `postRepository` of type `PostRepository` does not exist in `DatabaseLoader`.";
		assertTrue(message, postRepositoryExists);

		// Check for DatabaseLoader constructor with PostRepository parameter
		Constructor<DatabaseLoader> constructor = null;
		try {
			constructor = DatabaseLoader.class.getDeclaredConstructor(PostRepository.class);
		} catch(NoSuchMethodException e) {
			//e.printStackTrace();
		}

		message = "Task 2: A `DatabaseLoader` constructor with a `PostRepository` parameter does not exist.";
		assertNotNull(message, constructor);

		Annotation[] annotations = {};
		// Check for @Autowired
		try {
			annotations =
				DatabaseLoader.class.getDeclaredConstructor(PostRepository.class).getDeclaredAnnotations();
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		}

		assertTrue("Task 2: There should be 1 annotation, `@Autowired`, on the `DatabaseLoader` constructor.", annotations.length == 1);

		assertEquals("Task 2: The annotation on the `DatabaseLoader` constructor is not of type `@Autowired.", Autowired.class, annotations[0].annotationType());
	}

	@Test
	public void task_3() {

		Mockito.when(spyRepository.saveAll(databaseLoader.randomPosts)).thenReturn(null);
		try {
			databaseLoader.run(new DefaultApplicationArguments(new String[]{}));
		} catch(Exception e) {
			e.printStackTrace();
		}

		boolean calledSaveAll = false;
		try {
			Mockito.verify(spyRepository).saveAll(databaseLoader.randomPosts);
			calledSaveAll = true;
		} catch(Error e) {
			//e.printStackTrace();
		}

		String message = "Task 3: Did not call the `PostRepository` `saveAll()` in the `DatabaseLoader` `run()` method.";
		assertTrue(message, calledSaveAll);
	}

	@Test
	public void task_4() {
		// Replace data-categories.sql file to add Categories
		// Open data-categories.sql file and check contents
		Path path = Paths.get("src/main/resources/application.properties");
		String result = "";
		try {
			final String output = "";
			List<String> allLines = Files.readAllLines(path);
			result = String.join("\n", allLines);
		} catch(IOException e) {
			//e.printStackTrace();
		}

		assertTrue("Task 4: The `application.properties` file doesn't contain `spring.data.rest.default-page-size=8`.", result.contains("spring.data.rest.default-page-size=8"));
	}

	@Test
	public void task_5() {
		Method method = null;
		try {
			method = PostRepository.class.getMethod("findByTitleContaining", String.class);
		} catch(Exception e) {
			////e.printStackTrace();
		}

		assertNotNull("Task 5: The method `findByTitleContaining()` doesn't exist in the `PostRepository` class.", method);
	}

	@Test
	public void task_6() {
		Method method = null;
		try {
			method = PostRepository.class.getMethod("findByTitleContaining", String.class);
		} catch(Exception e) {
			////e.printStackTrace();
		}

		assertNotNull("Task 6: The method `findByTitleContaining()` doesn't exist in the `PostRepository` class.", method);

		Annotation[] annotations = method.getDeclaredAnnotations();
		assertTrue("Task 6: There should be 1 annotation, `@RestResource`, on the `findByTitleContaining` method.", annotations.length == 1);

		assertEquals("Task 6: The method `findByTitleContaining()` doesn't have the `@RestResource` annotation.", RestResource.class, annotations[0].annotationType());

		boolean contains_title_present = annotations[0].toString().replaceAll("/s", "").contains("rel=\"contains-title\"") ||
			annotations[0].toString().replaceAll("/s", "").contains("rel=contains-title");
		boolean containsTitle_present = annotations[0].toString().replaceAll("/s", "").contains("path=\"containsTitle\"") ||
			annotations[0].toString().replaceAll("/s", "").contains("path=containsTitle");
		String message = "Task 6: The `@RestResource` annotation is " + annotations[0].toString().replaceAll("/s", "") + " and doesn't have `(rel = \"contains-title\", path = \"containsTitle\")`.";
		assertTrue(message, contains_title_present && containsTitle_present);
	}
}
