package com.example.dpm.post.repository;

import java.util.List;

import com.example.dpm.post.model.PostEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class PostRepositoryImpl implements PostRepositoryCustom{

	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PostEntity> findPostsWithPagination(int offset, int size) {
        String query = "SELECT p FROM PostEntity p ORDER BY p.postId ASC";
        return entityManager.createQuery(query, PostEntity.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

}
