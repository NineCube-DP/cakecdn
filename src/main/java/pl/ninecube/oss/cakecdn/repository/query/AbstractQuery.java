package pl.ninecube.oss.cakecdn.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.ninecube.oss.cakecdn.model.entity.QItemEntity;

@Transactional
public abstract class AbstractQuery {
    @Autowired
    private EntityManager entityManager;

    protected QItemEntity itemEntity = QItemEntity.itemEntity;

    protected JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
