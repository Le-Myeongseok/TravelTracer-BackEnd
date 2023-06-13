package com.project.travelTracer.Post.repository;

import com.project.travelTracer.Post.condition.PostSearchCondition;
import com.project.travelTracer.Post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import static com.project.travelTracer.member.entity.QMember.member;
import static com.project.travelTracer.Post.entity.QPost.post;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final JPAQueryFactory query;

    public CustomPostRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }


    @Override
    public Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable) {
        List<Post> content = query.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle()),
                        addressHasStr(postSearchCondition.getAddress())
                )
                .leftJoin(post.writer, member)
                .fetchJoin()
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = query.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle()),
                        addressHasStr(postSearchCondition.getAddress())
                );
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }
    private BooleanExpression contentHasStr(String content) {
        return StringUtils.hasLength(content) ? post.content.contains(content) : null;
    }

    private BooleanExpression titleHasStr(String title) {
        return StringUtils.hasLength(title) ? post.title.contains(title) : null;
    }
    private BooleanExpression addressHasStr(String address) {
        return StringUtils.hasLength(address) ? post.address.contains(address) : null;
    }

}
