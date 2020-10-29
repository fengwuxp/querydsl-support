package com.wuxp.querydsl.repository.auditing;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * JPA entity listener to capture auditing information on persiting and updating entities. To get this one flying be
 * sure you configure it as entity listener in your {@code orm.xml} as follows:
 *
 * <pre>
 * &lt;persistence-unit-metadata&gt;
 *     &lt;persistence-unit-defaults&gt;
 *         &lt;entity-listeners&gt;
 *             &lt;entity-listener class="org.springframework.data.jpa.domain.support.AuditingEntityListener" /&gt;
 *         &lt;/entity-listeners&gt;
 *     &lt;/persistence-unit-defaults&gt;
 * &lt;/persistence-unit-metadata&gt;
 * </pre>
 *
 * After that it's just a matter of activating auditing in your Spring config:
 *
 * <pre>
 * &#064;Configuration
 * &#064;EnableJpaAuditing
 * class ApplicationConfig {
 *
 * }
 * </pre>
 *
 * <pre>
 * &lt;jpa:auditing auditor-aware-ref="yourAuditorAwarebean" /&gt;
 * </pre>
 *
 * @author wuxp
 */
@Configurable
public class AuditingEntityListener {


    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * persist events.
     *
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

//        if (handler != null) {
//
//            AuditingHandler object = handler.getObject();
//            if (object != null) {
//                object.markCreated(target);
//            }
//        }
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link Auditable} on
     * update events.
     *
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {

        Assert.notNull(target, "Entity must not be null!");

//        if (handler != null) {
//
//            AuditingHandler object = handler.getObject();
//            if (object != null) {
//                object.markModified(target);
//            }
//        }
    }
}
