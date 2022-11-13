package com.ramoncgusmao.admin.catalogo.application.category.create;

import com.ramoncgusmao.admin.catalogo.application.Usecase;
import com.ramoncgusmao.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends Usecase<CreateCategoryCommand, Either<Notification,  CreateCategoryOutput>> {

}
