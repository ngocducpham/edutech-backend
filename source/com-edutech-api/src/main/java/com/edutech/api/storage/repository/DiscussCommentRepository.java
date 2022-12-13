package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.DiscussComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscussCommentRepository extends JpaRepository<DiscussComment, Long> {
    Page<DiscussComment> findAll(Specification<DiscussComment> specification, Pageable pageable);

    @Query("SELECT dc " +
            "FROM DiscussComment dc join dc.discuss d " +
            "WHERE d.id = ?1 order by dc.posted desc")
    Page<DiscussComment> findAllByDiscussId(Long discussId, Pageable pageable);

    @Query("SELECT dc " +
            "FROM DiscussComment dc join dc.discuss d " +
            "WHERE d.id = ?1 AND dc.parentComment.id is null order by dc.posted desc")
    List<DiscussComment> listAllByDiscussId(Long discussId);

    @Query("SELECT dc " +
            "FROM DiscussComment dc " +
            "WHERE dc.parentComment.id = ?1 order by dc.posted desc")
    Page<DiscussComment> findAllByParentId(Long parentId, Pageable pageable);
}
