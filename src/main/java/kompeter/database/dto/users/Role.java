/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class Role {
    final int id;
    String name;

    @Builder
    @JsonCreator
    public Role(@JsonProperty("_roleId") final int id, @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }
}
