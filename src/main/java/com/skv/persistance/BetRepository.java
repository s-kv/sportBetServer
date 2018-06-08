package com.skv.persistance;

import com.skv.domain.Bet;
import com.skv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    List<Bet> findByUser(User user);
}
