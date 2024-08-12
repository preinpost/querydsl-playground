package demo.playground.repository;

import demo.playground.jpa.entity.TestEntity;
import demo.playground.repository.custom.TestCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long>, TestCustom {
}
