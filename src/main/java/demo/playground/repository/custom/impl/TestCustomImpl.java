package demo.playground.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import demo.playground.jpa.entity.QTestEntity;
import demo.playground.jpa.entity.TestEntity;
import demo.playground.repository.custom.TestCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestCustomImpl implements TestCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public void findSomething() {
        List<TestEntity> fetch = queryFactory.from(QTestEntity.testEntity).select(QTestEntity.testEntity).fetch();
        log.info("fetch = {}", fetch);
    }
}
