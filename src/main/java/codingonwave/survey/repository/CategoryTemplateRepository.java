package codingonwave.survey.repository;

import codingonwave.survey.domain.CategoryTemplate;
import codingonwave.survey.dto.CategoryTemplateDto;
import codingonwave.survey.dto.QCategoryTemplateDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static codingonwave.survey.domain.QCategoryTemplate.*;

@Repository
public class CategoryTemplateRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CategoryTemplateRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public void save(CategoryTemplateDto dto) {
        CategoryTemplate categoryTemplate = new CategoryTemplate(dto.getName());
        em.persist(categoryTemplate);
    }

    @Transactional
    public void removeAll() {
        List<CategoryTemplate> categoryTemplates = queryFactory.selectFrom(categoryTemplate).fetch();
        categoryTemplates.forEach(em::remove);
    }

    @Transactional
    public void remove(Long categoryTemplateId) {
        CategoryTemplate categoryTemplate = em.find(CategoryTemplate.class, categoryTemplateId);
        em.remove(categoryTemplate);
    }

    public List<CategoryTemplateDto> findAll() {
        return queryFactory
                .select(new QCategoryTemplateDto(categoryTemplate.id, categoryTemplate.name))
                .from(categoryTemplate)
                .fetch();
    }

    public CategoryTemplateDto findById(Long id) {
        CategoryTemplate categoryTemplate = em.find(CategoryTemplate.class, id);
        return new CategoryTemplateDto(categoryTemplate.getId(), categoryTemplate.getName());
    }

    public CategoryTemplateDto findByName(String name) {
        return queryFactory
                .select(new QCategoryTemplateDto(categoryTemplate.id, categoryTemplate.name))
                .from(categoryTemplate)
                .where(categoryTemplate.name.eq(name))
                .fetchOne();
    }

    @Transactional
    public void update(Long id, String newName) {
        CategoryTemplate categoryTemplate = em.find(CategoryTemplate.class, id);
        categoryTemplate.setName(newName);
    }
}
