package com.ramoncgusmao.admin.catalogo.application.category.create;

import com.ramoncgusmao.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

	@InjectMocks
	private DefaultCreateCategoryUseCase useCase;

	@Mock
	private CategoryGateway categoryGateway;

	@Test
	public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {

		final var expectedName = "Filmes";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;

		final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

		final var actualOutput = useCase.execute(aCommand).get();

		assertNotNull(actualOutput);
		assertNotNull(actualOutput.id());

		verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
				Objects.equals(expectedName, aCategory.getName())
						&& Objects.equals(expectedDescription, aCategory.getDescription())
						&& Objects.equals(expectedIsActive, aCategory.isActive())
						&& Objects.nonNull(aCategory.getId())
						&& Objects.nonNull(aCategory.getCreatedAt())
						&& Objects.nonNull(aCategory.getUpdatedAt())
						&& Objects.isNull(aCategory.getDeletedAt())
		));

	}

	@Test
	public void givenAInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException() {
		final String expectedName = null;
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = true;
		final var expectedErrorMessage = "'name' should not be null";
		final var expectedErrorCount = 1;

		final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
		final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);

		final var notification = useCase.execute(aCommand).getLeft();

		Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
		Mockito.verify(categoryGateway, times(0)).create(any());
	}

	@Test
	public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
		final String expectedName = "categoria";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = false;

		final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

		final var notification = useCase.execute(aCommand).get();

		assertNotNull(notification);
		assertNotNull(notification.id());

		verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
				Objects.equals(expectedName, aCategory.getName())
						&& Objects.equals(expectedDescription, aCategory.getDescription())
						&& Objects.equals(expectedIsActive, aCategory.isActive())
						&& Objects.nonNull(aCategory.getId())
						&& Objects.nonNull(aCategory.getCreatedAt())
						&& Objects.nonNull(aCategory.getUpdatedAt())
						&& Objects.nonNull(aCategory.getDeletedAt())

		));
	}

	@Test
	public void givenAValidCommand_whenCallsThrowsRandomException_shouldReturnException() {
		final String expectedName = "categoria";
		final var expectedDescription = "A categoria mais assistida";
		final var expectedIsActive = false;

		final var expectedErrorMessage = "Gateway Error";
		final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

		when(categoryGateway.create(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

		final var notification = useCase.execute(aCommand).getLeft();

		Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
		verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
				Objects.equals(expectedName, aCategory.getName())
						&& Objects.equals(expectedDescription, aCategory.getDescription())
						&& Objects.equals(expectedIsActive, aCategory.isActive())
						&& Objects.nonNull(aCategory.getId())
						&& Objects.nonNull(aCategory.getCreatedAt())
						&& Objects.nonNull(aCategory.getUpdatedAt())
						&& Objects.nonNull(aCategory.getDeletedAt())
		));
	}
}
