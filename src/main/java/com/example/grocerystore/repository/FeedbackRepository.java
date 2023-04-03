package com.example.grocerystore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.grocerystore.domain.entities.Feedback;



@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String>
{

}
