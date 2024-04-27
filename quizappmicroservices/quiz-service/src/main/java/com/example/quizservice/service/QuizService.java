package com.example.quizservice.service;

import com.example.quizservice.fignConfig.QuizFiegnInterface;
import com.example.quizservice.model.QuestionWrapper;
import com.example.quizservice.model.Quiz;
import com.example.quizservice.model.Response;
import com.example.quizservice.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {


    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizFiegnInterface quizFiegnInterface;


    public ResponseEntity<Quiz> createQuiz(String category, int numQuestion, String title) {
//here use feignClient
        List<Integer> questions = quizFiegnInterface.getQuestionsForQuiz(category, numQuestion).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizRepository.save(quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz=quizRepository.findById(id).get();
        List<Integer> questionIds=quiz.getQuestionIds();
      // quizFiegnInterface.getQuestionsFromId(questionIds);
        ResponseEntity<List<QuestionWrapper>> questions=quizFiegnInterface.getQuestionsFromId(questionIds);
        return ResponseEntity.status(HttpStatus.OK).body(questions.getBody());
    }



    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
      ResponseEntity<Integer> score=quizFiegnInterface.getScore(responses);
      return ResponseEntity.status(HttpStatus.OK).body(score).getBody();
    }
}
