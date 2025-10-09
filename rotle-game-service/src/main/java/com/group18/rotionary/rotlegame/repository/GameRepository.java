package com.group18.rotionary.rotlegame.repository;

import com.group18.rotionary.rotlegame.domain.aggregates.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {}


