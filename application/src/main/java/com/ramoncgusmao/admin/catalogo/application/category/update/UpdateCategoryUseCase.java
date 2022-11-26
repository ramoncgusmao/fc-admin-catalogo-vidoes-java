package com.ramoncgusmao.admin.catalogo.application.category.update;

import com.ramoncgusmao.admin.catalogo.application.Usecase;
import com.ramoncgusmao.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends Usecase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
