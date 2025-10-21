package com.group18.rotionary.lexicon.api;

import com.group18.rotionary.lexicon.LexiconDomainService;
import com.group18.rotionary.lexicon.domain.aggregates.Term;
import com.group18.rotionary.lexicon.domain.entities.Definition;
import com.group18.rotionary.lexicon.api.dto.TermDtos.*;
import com.group18.rotionary.lexicon.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermRepository termRepository;
    private final DefinitionRepository definitionRepository;
    private final LexiconDomainService domainService;
    private final com.group18.rotionary.lexicon.infrastructure.TermEventPublisher eventPublisher;

    public TermController(TermRepository termRepository,
                          DefinitionRepository definitionRepository,
                          LexiconDomainService domainService,
                          com.group18.rotionary.lexicon.infrastructure.TermEventPublisher eventPublisher) {
        this.termRepository = termRepository;
        this.definitionRepository = definitionRepository;
        this.domainService = domainService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public List<Term> list() {
        return termRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Term> getById(@PathVariable Long id) {
        return termRepository.findById(id)
                .map(term -> {
                    eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                            term.getId(), term.getWord(), "BY_ID", null, String.valueOf(id)));
                    return ResponseEntity.ok(term);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Term>> search(@RequestParam(required = false) String word,
                                             @RequestParam(required = false) String tag) {
        if (word != null) {
            return termRepository.findByWord(domainService.normaliseWord(word))
                    .map(found -> {
                        eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                                found.getId(), found.getWord(), "BY_WORD", null, word));
                        return List.of(found);
                    })
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (tag != null) {
            // filter by tag
            List<Term> results = termRepository.findAll().stream()
                    .filter(t -> t.getTags().contains(tag.toLowerCase()))
                    .toList();
            results.forEach(t -> eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                    t.getId(), t.getWord(), "BY_TAG", null, tag)));
            return ResponseEntity.ok(results);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/random")
    public ResponseEntity<Term> random() {
        List<Term> allTerms = termRepository.findAll();
        if (allTerms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Term randomTerm = domainService.selectRandomTerm(allTerms);
        eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
            randomTerm.getId(), randomTerm.getWord(), "RANDOM", null, null));
        return ResponseEntity.ok(randomTerm);
    }

    @GetMapping("/random-five")
    public ResponseEntity<Term> randomFive() {
        List<Term> allTerms = termRepository.findAll();
        List<Term> fiveLetterTerms = allTerms.stream()
            .filter(t -> t.getWord().length() == 5)
            .toList();
        if (fiveLetterTerms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Term randomTerm = domainService.selectRandomTerm(fiveLetterTerms);
        eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
            randomTerm.getId(), randomTerm.getWord(), "RANDOM_FIVE", null, null));
        return ResponseEntity.ok(randomTerm);
    }
    @PostMapping
    @Transactional
    public ResponseEntity<Term> create(@RequestBody CreateTermRequest request) {
        if (!domainService.isValidTerm(request.word())) {
            return ResponseEntity.badRequest().build();
        }
        
        // check if term already exists
        String normalisedWord = domainService.normaliseWord(request.word());
        if (termRepository.findByWord(normalisedWord).isPresent()) {
            return ResponseEntity.status(409).build();
        }
        
        Term term = new Term(request.word(), request.createdBy());
        if (request.tags() != null) {
            for (String tagName : request.tags()) {
                term.addTag(tagName);
            }
        }
        Term saved = termRepository.save(term);
        return ResponseEntity.created(URI.create("/api/terms/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Term> update(@PathVariable Long id, @RequestBody UpdateTermRequest request) {
        return termRepository.findById(id)
                .map(term -> {
                    if (request.tags() != null) {
                        term.getTags().forEach(t -> {}); // no operation to avoid concurrent modification exception
                        List<String> newTags = request.tags();
                        term.getTags().clear();
                        for (String tagName : newTags) {
                            term.addTag(tagName);
                        }
                    }
                    return ResponseEntity.ok(term);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!termRepository.existsById(id)) return ResponseEntity.notFound().build();
        termRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deleteAll() {
        termRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/definitions")
    public ResponseEntity<List<Definition>> getDefinitions(@PathVariable Long id) {
        return termRepository.findById(id)
            .map(term -> {
                List<Definition> definitions = term.getDefinition() != null ? 
                    List.of(term.getDefinition()) : List.of();
                return ResponseEntity.ok(definitions);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/definitions")
    @Transactional
    public ResponseEntity<Definition> addDefinition(@PathVariable Long id, @RequestBody DefinitionRequest request) {
        if (!domainService.isValidDefinition(request.meaning())) {
            return ResponseEntity.badRequest().build();
        }
        return termRepository.findById(id)
                .map(term -> {
                    Definition def = new Definition(request.meaning(), request.createdBy());
                    term.setDefinition(def);
                    definitionRepository.save(def);
                    return ResponseEntity.ok(def);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}


