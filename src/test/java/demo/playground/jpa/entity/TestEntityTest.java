package demo.playground.jpa.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.playground.repository.TestRepository;
import demo.playground.service.TestService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


//@DataJpaTest
//@TestPropertySource(locations = "classpath:application-test.yml")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@Rollback(false)
@ActiveProfiles("test")
class TestEntityTest {

//    @Autowired
//    EntityManager entityManager;

//    JPAQueryFactory queryFactory;
//
//    @BeforeEach
//    void init() {
//        queryFactory = new JPAQueryFactory(entityManager);
//    }

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestService testService;

    @Test
    void test() {
        System.out.println("run Test");

        TestEntity entity = new TestEntity();
        entity.setName("name1");
        entity.setEmail("email1");

        testRepository.save(entity);

        String result = testService.doService();

    }

//    @Test
//    void select() {
//
//        TestEntity entity = new TestEntity();
//        entity.setName("name2");
//        entity.setEmail("email2");
//
//        testRepository.save(entity);
//
//        testRepository.findSomething();
//    }

}
