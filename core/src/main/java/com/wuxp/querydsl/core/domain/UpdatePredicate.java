package com.wuxp.querydsl.core.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * 更新 {@link com.querydsl.core.types.Predicate}
 * 用于多字段，多条件的情况下的更新
 * <p>
 * 实现类可以使用{@link com.wuxp.querydsl.annoations.map.Update}标记哪些字段用于更新，
 * 则其他字段可以使用{@link com.wuxp.querydsl.annoations.op.Op} 表明更新条件
 * id字段永远只会表示更新条件
 * updateById {@link CrudRepository#save(Persistable))}
 * </p>
 *
 * @author wuxp
 * @see com.wuxp.querydsl.annoations.map.Update
 */
public interface UpdatePredicate {
}
