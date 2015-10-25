package domain;

import play.data.validation.Constraints;

import java.io.Serializable;

public class User implements Serializable {
    @Constraints.Required
    private Long id;
    @Constraints.Required
    @Constraints.Min(0)
    private String name;

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{")
                .append(this.id)
                .append(", ")
                .append(this.name)
                .append("}" )
                .toString();
    }
}
