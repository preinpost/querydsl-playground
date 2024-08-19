package demo.playground.service;

import demo.playground.jpa.entity.TestEntity;
import demo.playground.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {


    private final TestRepository testRepository;

    public String doService() {

        innerService();

        TestEntity entity = TestEntity.of("name1", "email1");
        testRepository.save(entity);



        return "Service result";
    }

    private String innerService() {
        log.debug("inner");
        return "inner service";
    }
}
