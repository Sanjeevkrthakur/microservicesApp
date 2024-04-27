package com.example.questionservice.service;

import com.example.questionservice.model.Question;
import com.example.questionservice.model.QuestionWrapper;
import com.example.questionservice.model.Response;
import com.example.questionservice.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {


    @Autowired
    QuestionRepository questionRepository;

    public ResponseEntity<List<Question>> getAllQuestion() {
        List<Question> questions = questionRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(questions);

    }

    public ResponseEntity<List<Question>> category(String category) {
        List<Question> questions = questionRepository.findAllByCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    public ResponseEntity<Question> addQuestion(Question question) {
        questionRepository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    public ResponseEntity<List<Integer>> getquestionsForQuiz(String category, Integer numQuestions) {
        List<Integer> questions = questionRepository.findRandomQuestionsByCategory(category, numQuestions);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }


    public ResponseEntity<List<QuestionWrapper>> getquestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for (Integer id : questionIds) {
            questions.add(questionRepository.findById(id).get());
        }

        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }

        return ResponseEntity.status(HttpStatus.OK).body(wrappers);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right = 0;
        for (Response response : responses) {
            Question question = questionRepository.findById(response.getId()).get();
            if (response.getResponse().equals(question.getCorrectAnswer()))
                right++;
        }
        return ResponseEntity.status(HttpStatus.OK).body(right);
    }
}
