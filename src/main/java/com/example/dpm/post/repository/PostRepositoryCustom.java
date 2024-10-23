package com.example.dpm.post.repository;

import java.util.List;

import com.example.dpm.post.model.PostEntity;

public interface PostRepositoryCustom {
	List<PostEntity> findPostsWithPagination(int offset, int size);
}
