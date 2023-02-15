package me.zhengjie;

import me.zhengjie.domain.Hint;
import me.zhengjie.domain.Problem;
import me.zhengjie.repository.ProblemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EladminSystemApplicationTests {

    @Autowired
    ProblemRepository repository;
    @Test
    public void testJpa() {
        Problem problem = new Problem();
        problem.setTitle("test1");
        problem.setDescription("good1");
        Hint hint1 = new Hint();
        hint1.setDescription("123");
        Hint hint2 = new Hint();
        hint2.setDescription("456");
        ArrayList<Hint> list = new ArrayList<>();
        list.add(hint1);
        list.add(hint2);
        problem.setHints(list);
        repository.save(problem);

    }

    public static void main(String[] args) {
    }
}

