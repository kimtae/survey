package codingonwave.survey.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class CategoryTemplate {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public CategoryTemplate(Long id) {
        this.id = id;
    }

    public CategoryTemplate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
