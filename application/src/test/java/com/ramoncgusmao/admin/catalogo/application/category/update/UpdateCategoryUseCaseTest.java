package com.ramoncgusmao.admin.catalogo.application.category.update;

import com.ramoncgusmao.admin.catalogo.domain.category.Category;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryGateway;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryID;
import com.ramoncgusmao.admin.catalogo.domain.exception.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

	@InjectMocks
	private DefaultUpdateCategoryUseCase useCase;

	@Mock
	private CategoryGateway categoryGateway;

	@Test
	public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {

		final var aCategory = Category.newCategory("Film", null, true);
		final var expectedName = "FIlmes";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var expectedId = aCategory.getId();

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
		when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

		final var actualOutput = useCase.execute(aCommand).get();
		Assertions.assertNotNull(actualOutput);
		Assertions.assertNotNull(actualOutput.id());

		verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
				aUpdateCategory ->
						Objects.equals(expectedName, aUpdateCategory.getName())
								&& Objects.equals(expectedDescription, aUpdateCategory.getDescription())
								&& Objects.equals(expectedIsActive, aUpdateCategory.isActive())
								&& Objects.equals(expectedId, aUpdateCategory.getId())
								&& Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
								&& aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
								&& Objects.isNull(aUpdateCategory.getDeletedAt())
		));
	}


	@Test
	public void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {

		final var aCategory = Category.newCategory("Film", null, true);
		final String expectedName = null;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;
		final var expectedErrorCount = 1;
		final var expectedId = aCategory.getId();
		final var expectedErrorMessage = "'name' should not be null";
		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

		final var notification = useCase.execute(aCommand).getLeft();
		Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

		verify(categoryGateway, Mockito.times(0)).update(any());
	}


	@Test
	public void givenAValidInactiveCommnad_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
		final var aCategory = Category.newCategory("Film", null, true);
		final var expectedId = aCategory.getId();
		final String expectedName = "categoria";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = false;

		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
		when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

		final var notification = useCase.execute(aCommand).get();

		assertNotNull(notification);
		assertNotNull(notification.id());

		verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(aUpdateCategory ->
				Objects.equals(expectedName, aUpdateCategory.getName())
						&& Objects.equals(expectedDescription, aUpdateCategory.getDescription())
						&& Objects.equals(expectedIsActive, aUpdateCategory.isActive())
						&& Objects.nonNull(aUpdateCategory.getId())
						&& Objects.nonNull(aUpdateCategory.getCreatedAt())
						&& Objects.nonNull(aUpdateCategory.getUpdatedAt())
						&& Objects.nonNull(aUpdateCategory.getDeletedAt())

		));
	}

	@Test
	public void givenAValidCommand_whenCallsThrowsRandomException_shouldReturnException() {
		final var aCategory = Category.newCategory("Film", null, true);
		final var expectedId = aCategory.getId();

		final String expectedName = "categoria";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var expectedErrorMessage = "Gateway Error";
		final var aCommand = UpdateCategoryCommand.with(
				expectedId.getValue(),
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
		when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));


		final var notification = useCase.execute(aCommand).getLeft();

		Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
		verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
				aUpdateCategory ->
						Objects.equals(expectedName, aUpdateCategory.getName())
								&& Objects.equals(expectedDescription, aUpdateCategory.getDescription())
								&& Objects.equals(expectedIsActive, aUpdateCategory.isActive())
								&& Objects.equals(expectedId, aUpdateCategory.getId())
								&& Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
								&& aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
								&& Objects.isNull(aUpdateCategory.getDeletedAt())
		));

	}

	@Test
	public void givenAValidCommand_whenCallsThrowsRandomException_shouldReturnNotFound() {
		final var expectedId = "123";

		final String expectedName = "categoria";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = false;

		final var expectedErrorMessage = "Category with 123 was not found";
		final var aCommand = UpdateCategoryCommand.with(
				expectedId,
				expectedName,
				expectedDescription,
				expectedIsActive
		);

		when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());


		final var exception = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

		Assertions.assertEquals(1, exception.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

		verify(categoryGateway, Mockito.times(0)).update(any());
	}
}
