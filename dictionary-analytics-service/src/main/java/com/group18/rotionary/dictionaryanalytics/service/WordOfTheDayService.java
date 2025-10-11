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

    /**
     * Calculate and update Word of the Day every 5 seconds
     * Based on the most queried term in the last hour
     */
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void calculateWordOfTheDay() {
        try {
            // Get query events from the last hour
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            
            // Find the most queried term in the last hour
            List<QueryEventRepository.TopTermCount> topTerms = queryEventRepository.findTopSince(
                oneHourAgo, 
                org.springframework.data.domain.PageRequest.of(0, 1)
            );
            
            if (!topTerms.isEmpty()) {
                QueryEventRepository.TopTermCount topTerm = topTerms.get(0);
                LocalDate today = LocalDate.now();
                
                // Update or create the WOTD for today
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

    /**
     * Get the current Word of the Day
     */
    public DailyWOTD getCurrentWordOfTheDay() {
        return dailyWotdRepository.findTopByOrderByDateDesc()
            .orElse(null);
    }
}
