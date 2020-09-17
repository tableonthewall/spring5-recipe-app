package guru.springframework.model;

import javax.persistence.*;

@Entity
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Recipe recipe;

    @Lob
    private String rescipeNote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getRescipeNote() {
        return rescipeNote;
    }

    public void setRescipeNote(String rescipeNote) {
        this.rescipeNote = rescipeNote;
    }
}
