package com.ramoncgusmao.admin.catalogo.application.category.create;

import com.ramoncgusmao.admin.catalogo.domain.category.Category;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryGateway;
import com.ramoncgusmao.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

	private final CategoryGateway categoryGateway;

	public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
		this.categoryGateway = Objects.requireNonNull(categoryGateway);
	}

	@Override
	public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
		final var aCategory = Category.newCategory(aCommand.name(), aCommand.description(), aCommand.isActive());
		final var notification = Notification.create();
		aCategory.validate(notification);


		return notification.hasError() ? Left(notification) : create(aCategory);
	}

	private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
		return Try(() -> this.categoryGateway.create(aCategory))
				.toEither()
				.bimap(Notification::create, CreateCategoryOutput::from);
	}
}
