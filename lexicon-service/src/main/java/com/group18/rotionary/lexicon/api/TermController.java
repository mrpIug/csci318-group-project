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
import java.util.Map;

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
            return termRepository.findByWord(domainService.normalizeWord(word))
                    .map(found -> {
                        eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                                found.getId(), found.getWord(), "BY_WORD", null, word));
                        return List.of(found);
                    })
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(List.of()));
        }
        if (tag != null) {
            // simple filter by element collection tag
            List<Term> results = termRepository.findAll().stream()
                    .filter(t -> t.getTags().contains(tag.toLowerCase()))
                    .toList();
            results.forEach(t -> eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                    t.getId(), t.getWord(), "BY_TAG", null, tag)));
            return ResponseEntity.ok(results);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> exists(@RequestParam String word) {
        boolean exists = termRepository.findByWord(domainService.normalizeWord(word)).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    @PostMapping
    @Transactional
    public ResponseEntity<Term> create(@RequestBody CreateTermRequest request) {
        if (!domainService.isValidTerm(request.word())) {
            return ResponseEntity.badRequest().build();
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
                        // replace tag list
                        term.getTags().forEach(t -> {}); // no-op read to avoid removal during iteration
                        List<String> newTags = request.tags();
                        // clear and re-add
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


