package com.ramoncgusmao.admin.catalogo.application.category.update;

import com.ramoncgusmao.admin.catalogo.domain.category.Category;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryGateway;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryID;
import com.ramoncgusmao.admin.catalogo.domain.exception.DomainException;
import com.ramoncgusmao.admin.catalogo.domain.validation.Error;
import com.ramoncgusmao.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

	private final CategoryGateway categoryGateway;

	public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
		this.categoryGateway = Objects.requireNonNull(categoryGateway);
	}

	@Override
	public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand anCategory) {

		final var anId = CategoryID.from(anCategory.id());

		final var notification = Notification.create();
		final var aCategory = this.categoryGateway.findById(anId).orElseThrow(notFoundCategoryID(anId));
		aCategory.update(anCategory.name(), anCategory.description(), anCategory.isActive()).validate(notification);

		return notification.hasError() ? Left(notification) : update(aCategory);
	}

	private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
		return Try(() -> this.categoryGateway.update(aCategory))
				.toEither()
				.bimap(Notification::create, UpdateCategoryOutput::from);

	}

	private static Supplier<DomainException> notFoundCategoryID(final CategoryID anId) {
		return () -> DomainException.with(new Error("Category with %s was not found".formatted(anId.getValue())));
	}
}
