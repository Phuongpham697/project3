package junit.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.NoResultException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jmaster.io.project3.DemoApplication;
import jmaster.io.project3.dto.CategoryDTO;
import jmaster.io.project3.service.CategoryService;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
//@DataJpaTest
//@WebMvcTest(CategoryController.class)
public class CategoryIntegrationTest {

	@Autowired
	CategoryService categoryService;

	@Test
	public void createCategory() {
		// given
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setName("Test");

		// when
		categoryService.create(categoryDTO);

		CategoryDTO saveCategory = categoryService.getById(categoryDTO.getId());

		// then
//		System.out.println(saveCategory.getName());
//		System.out.println(categoryDTO.getName());
		assertThat(categoryDTO.getName()).isEqualTo(saveCategory.getName());

		categoryService.delete(categoryDTO.getId());

		assertThrows(NoResultException.class, () -> {
			categoryService.getById(categoryDTO.getId());
		});
	}
}
