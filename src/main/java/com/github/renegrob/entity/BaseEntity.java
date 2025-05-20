package com.github.renegrob.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@MappedSuperclass
public abstract class BaseEntity extends PanacheEntityBase {

        /**
         * The auto-generated ID field. This field is set by Hibernate ORM when this entity
         * is persisted.
         *
         * @see #persist()
         */
        @Id
        @GeneratedValue
        public Long id;

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "<" + id + ">";
        }
    }