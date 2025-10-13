package com.group18.rotionary.dictionaryanalytics.service;

import com.group18.rotionary.dictionaryanalytics.domain.entities.DailyWOTD;
import com.group18.rotionary.dictionaryanalytics.repository.DailyWotdRepository;
import com.group18.rotionary.dictionaryanalytics.repository.QueryEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WordOfTheDayService {

    private final DailyWotdRepository dailyWotdRepository;
    private final QueryEventRepository queryEventRepository;

    public WordOfTheDayService(DailyWotdRepository dailyWotdRepository, 
                              QueryEventRepository queryEventRepository) {
        this.dailyWotdRepository = dailyWotdRepository;
        this.queryEventRepository = queryEventRepository;
    }

    // updates word of the day every 5 seconds based on most queried term in last hour
    @Scheduled(fixedRate = 5000)
    public void calculateWordOfTheDay() {
        try {
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            
            List<QueryEventRepository.TopTermCount> topTerms = queryEventRepository.findTopSince(
                oneHourAgo, 
                org.springframework.data.domain.PageRequest.of(0, 1)
            );
            
            if (!topTerms.isEmpty()) {
                QueryEventRepository.TopTermCount topTerm = topTerms.get(0);
                LocalDate today = LocalDate.now();
                
                dailyWotdRepository.findByDate(today)
                    .ifPresentOrElse(
                        existing -> {
                            existing.updateTerm(topTerm.getTermId(), topTerm.getTermWord(), topTerm.getCnt());
                            dailyWotdRepository.save(existing);
                        },
                        () -> {
                            DailyWOTD newWotd = new DailyWOTD(today, topTerm.getTermId(), topTerm.getTermWord(), topTerm.getCnt());
                            dailyWotdRepository.save(newWotd);
                        }
                    );
                
                System.out.println("WOTD updated: " + topTerm.getTermWord() + " (queried " + topTerm.getCnt() + " times)");
            }
        } catch (Exception e) {
            System.err.println("Error calculating WOTD: " + e.getMessage());
        }
    }

    // gets current wotd
    public DailyWOTD getCurrentWordOfTheDay() {
        return dailyWotdRepository.findTopByOrderByDateDesc()
            .orElse(null);
    }
}
