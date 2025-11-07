package com.taskflow.comment;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;

@Repository
>>>>>>> b26b43c (fix: ajusta comunicação entre o backend-frontend)
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
