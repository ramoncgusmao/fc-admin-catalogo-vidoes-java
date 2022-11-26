package com.ramoncgusmao.admin.catalogo.domain.category;

import com.ramoncgusmao.admin.catalogo.domain.AggregateRoot;
import com.ramoncgusmao.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

	private String name;
	private String description;
	private boolean active;
	private Instant createdAt;
	private Instant updatedAt;
	private Instant deletedAt;

	private Category(
			final CategoryID anId,
			final String name,
			final String description,
			final boolean active,
			final Instant createdAt,
			final Instant updatedAt,
			final Instant deletedAt) {
		super(anId);
		this.name = name;
		this.description = description;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
	}

	@Override
	public void validate(final ValidationHandler handler) {
		new CategoryValidator(this, handler).validate();
	}

	public static Category newCategory(String name, String description, boolean isActive) {
		final var id = CategoryID.unique();
		final var now = Instant.now();
		final var deletedAt = isActive ? null : now;
		return new Category(id, name, description, isActive, now, now, deletedAt);
	}

	public CategoryID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isActive() {
		return active;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	public Category deactivate() {
		if (getDeletedAt() == null) {
			this.deletedAt = Instant.now();
		}
		this.active = false;
		this.updatedAt = Instant.now();
		return this;
	}

	public Category activate() {

		this.deletedAt = null;
		this.active = true;
		this.updatedAt = Instant.now();
		return this;
	}

	public Category update(
			String aName,
			String aDescription,
			boolean isActive
	) {

		if (isActive) {
			activate();
		} else {
			deactivate();
		}
		this.name = aName;
		this.description = aDescription;
		this.updatedAt = Instant.now();
		return this;
	}

	@Override
	public Category clone() {
		try {
			return (Category) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
