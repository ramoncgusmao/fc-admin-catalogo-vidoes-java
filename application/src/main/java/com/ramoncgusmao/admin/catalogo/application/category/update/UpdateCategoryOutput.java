package com.ramoncgusmao.admin.catalogo.application.category.update;

import com.ramoncgusmao.admin.catalogo.domain.category.Category;
import com.ramoncgusmao.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
		CategoryID id
) {

	public static UpdateCategoryOutput from(final Category aCategory) {
		return new UpdateCategoryOutput(aCategory.getId());
	}
}
