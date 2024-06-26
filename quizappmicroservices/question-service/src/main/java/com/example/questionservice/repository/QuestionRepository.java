package com.example.questionservice.repository;

import com.example.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findAllByCategory(String category);

//    @Query(value = "Select q.id from question q where q.category=:category ORDER By RANDOM() LIMIT :numQuestions")
//    List<Integer> findRandomQuestionsByCategory(String category, Integer numQuestions);

    @Query(value = "SELECT q.id FROM Question q WHERE q.category = :category ORDER BY RAND() LIMIT :numQuestions")
    List<Integer> findRandomQuestionsByCategory(String category, Integer numQuestions);

}
