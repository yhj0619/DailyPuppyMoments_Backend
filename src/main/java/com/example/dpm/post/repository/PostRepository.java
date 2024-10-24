package com.example.dpm.post.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dpm.post.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>, PostRepositoryCustom{

	List<PostEntity> findPostsWithPagination(int offset, int size);

    @Query("SELECT p FROM PostEntity p WHERE p.member.member_id = :member_id ORDER BY p.postDate DESC")
    Page<PostEntity> findByMember_MemberId(@Param("member_id") Long member_id, Pageable pageable);
    
	 // 제목 검색 및 날짜 순 정렬, 페이지네이션 처리
    @Query("SELECT p FROM PostEntity p WHERE p.title LIKE %:keyword% ORDER BY p.postDate ASC")
    Page<PostEntity> searchByTitle(@Param("keyword")String keyword, Pageable pageable);
    
    // 최신순 정렬된 게시물 페이지네이션
    @Query("SELECT p FROM PostEntity p ORDER BY p.postDate DESC")
    Page<PostEntity> findAllOrderByPostDateLatest(Pageable pageable);
    
    // 오래된순 정렬된 게시물 페이지네이션
    @Query("SELECT p FROM PostEntity p ORDER BY p.postDate ASC")
    Page<PostEntity> findAllOrderByPostDateEarliest(Pageable pageable);

    // 좋아요 많이 받은 순 정렬된 게시물 페이지네이션
    @Query("SELECT p FROM PostEntity p ORDER BY p.totalLikeHeart DESC")
    Page<PostEntity> findAllOrderByLikes(Pageable pageable);

    // tag로 검색하여 정렬된 게시물 페이지네이션
    @Query("SELECT p FROM PostEntity p JOIN p.tags t WHERE t.tagName = :tagName")
    Page<PostEntity> findPostsByTagName(@Param("tagName") String tagName, Pageable pageable);
}
