package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record ItemDto(
        int _itemId, @NotNull Timestamp _createdAt, @NotNull String name, String description) {
	
	public static class Builder {
		private int itemId;
		private Timestamp createdAt;
		private String name;
		private String description;
		
		public @NotNull ItemDto build() throws IllegalStateException{
			if (itemId <= 0) {
				throw new IllegalStateException("Item ID must be greater than 0");
			}
			if (createdAt == null) {
				throw new IllegalStateException("Item creation time cannot be null");
			}
			if (name == null || name.isBlank()) {
				throw new IllegalStateException("Display name cannot be null or empty");
			}
			return new ItemDto(itemId, createdAt, name, description);
		}
		
		public Builder setCreatedAt(Timestamp createdAt) {
			this.createdAt = createdAt;
			return this;
		}
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}
		public Builder setItemId(int itemId) {
			this.itemId = itemId;
			return this;
		}
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
	}
	
	
}
