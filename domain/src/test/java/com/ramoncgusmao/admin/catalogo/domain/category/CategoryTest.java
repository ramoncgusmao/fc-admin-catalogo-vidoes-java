package com.ramoncgusmao.admin.catalogo.domain.category;

import com.ramoncgusmao.admin.catalogo.domain.exception.DomainException;
import com.ramoncgusmao.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

	@Test
	public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
		final var expectedName = "Filmes";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		Assertions.assertNotNull(actualCategory);
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());


	}

	@Test
	public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final String expectedName = null;
		final var expectedErrorMessage = "'name' should not be null";
		final var expectedErrorCount = 1;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var actualExcetion =
				Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertEquals(expectedErrorCount, actualExcetion.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, actualExcetion.getErrors().get(0).message());

	}

	@Test
	public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final String expectedName = " ";
		final var expectedErrorMessage = "'name' should not be empty";
		final var expectedErrorCount = 1;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var actualExcetion =
				Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertEquals(expectedErrorCount, actualExcetion.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, actualExcetion.getErrors().get(0).message());

	}

	@Test
	public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final String expectedName = "FI    ";
		final var expectedErrorMessage = "'name' must be between 3 and 255 character";
		final var expectedErrorCount = 1;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var actualExcetion =
				Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertEquals(expectedErrorCount, actualExcetion.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, actualExcetion.getErrors().get(0).message());

	}

	@Test
	public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final var expectedName = """
				Cada inconsciente relativo confunde-se, por meio de uma redução operacional,
				com a onipresença das fantasias subjacentes realiza a 
				travessia da fantasia que termina por transformar o objeto 
				em uma instância paranóica.Nesse pull request, a normalização da data superou 
				o desempenho na interpolação dinâmica de strings.
				""";
		final var expectedErrorMessage = "'name' must be between 3 and 255 character";
		final var expectedErrorCount = 1;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

		final var actualExcetion =
				Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertEquals(expectedErrorCount, actualExcetion.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, actualExcetion.getErrors().get(0).message());

	}

	@Test
	public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final String expectedName = "Ramon";
		final var expectedDescription = " ";
		final var expectedIsActive = true;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


		Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertNotNull(actualCategory);
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNull(actualCategory.getDeletedAt());

	}

	@Test
	public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenInstantiateACategory() {
		final String expectedName = "Ramon";
		final var expectedDescription = " description ";
		final var expectedIsActive = false;

		final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


		Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertNotNull(actualCategory);
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertNotNull(actualCategory.getCreatedAt());
		Assertions.assertNotNull(actualCategory.getUpdatedAt());
		Assertions.assertNotNull(actualCategory.getDeletedAt());

	}

	@Test
	public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivate() {
		final String expectedName = "Ramon";
		final var expectedDescription = " description ";
		final var expectedIsActive = false;

		final var aCategoy = Category.newCategory(expectedName, expectedDescription, true);
		Assertions.assertDoesNotThrow(() -> aCategoy.validate(new ThrowsValidationHandler()));

		final var createdAt = aCategoy.getUpdatedAt();
		final var updatedAt = aCategoy.getUpdatedAt();
		Assertions.assertNull(aCategoy.getDeletedAt());
		Assertions.assertTrue(aCategoy.isActive());

		final var actualCategory = aCategoy.deactivate();

		Assertions.assertEquals(aCategoy.getId(), actualCategory.getId());
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
		Assertions.assertNotNull(actualCategory.getDeletedAt());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
	}

	@Test
	public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
		final String expectedName = "Ramon";
		final var expectedDescription = " description ";
		final var expectedIsActive = true;

		final var aCategoy = Category.newCategory(expectedName, expectedDescription, false);
		Assertions.assertDoesNotThrow(() -> aCategoy.validate(new ThrowsValidationHandler()));

		final var createdAt = aCategoy.getUpdatedAt();
		final var updatedAt = aCategoy.getUpdatedAt();
		Assertions.assertNotNull(aCategoy.getDeletedAt());
		Assertions.assertFalse(aCategoy.isActive());

		final var actualCategory = aCategoy.activate();

		Assertions.assertEquals(aCategoy.getId(), actualCategory.getId());
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
		Assertions.assertNull(actualCategory.getDeletedAt());
		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
	}

	@Test
	public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
		final String expectedName = "Ramon";
		final var expectedDescription = "categoria assistida";
		final var expectedIsActive = false;

		final var aCategory = Category.newCategory("filme", "descricao alguma ai", true);
		Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

		final var createdAt = aCategory.getUpdatedAt();
		final var updatedAt = aCategory.getUpdatedAt();
		Assertions.assertNull(aCategory.getDeletedAt());
		Assertions.assertTrue(aCategory.isActive());

		final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

		Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));

		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

		Assertions.assertNotNull(actualCategory.getDeletedAt());
		Assertions.assertFalse(actualCategory.isActive());


	}

	@Test
	public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
		final String expectedName = null;
		final var expectedDescription = "categoria assistida";
		final var expectedIsActive = false;

		final var aCategory = Category.newCategory("filme", "descricao alguma ai", true);
		Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

		final var createdAt = aCategory.getUpdatedAt();
		final var updatedAt = aCategory.getUpdatedAt();
		Assertions.assertNull(aCategory.getDeletedAt());
		Assertions.assertTrue(aCategory.isActive());

		final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);


		Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
		Assertions.assertNotNull(actualCategory.getId());
		Assertions.assertEquals(expectedName, actualCategory.getName());
		Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
		Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
		Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));

		Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

		Assertions.assertNotNull(actualCategory.getDeletedAt());
		Assertions.assertFalse(actualCategory.isActive());


	}
}
