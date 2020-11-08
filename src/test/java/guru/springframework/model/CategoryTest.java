package guru.springframework.model;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest extends TestCase {

    Category category;

    @Before
    public void setUp(){
        category=new Category();
    }

    @Test
    public void testGetId() throws Exception {
        Integer id=15;
        category.setId(id);
        assertEquals(id,category.getId());
    }

    public void testGetDescription() {
    }

    public void testGetRecipes() {
    }

    public void testSetId() {
    }

    public void testSetDescription() {
    }

    public void testSetRecipes() {
    }
}