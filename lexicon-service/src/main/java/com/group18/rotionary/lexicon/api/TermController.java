package com.group18.rotionary.lexicon.api;

import com.group18.rotionary.lexicon.LexiconDomainService;
import com.group18.rotionary.lexicon.domain.aggregates.Term;
import com.group18.rotionary.lexicon.domain.entities.Definition;
import com.group18.rotionary.lexicon.domain.valueobjects.Tag;
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
    private final TagRepository tagRepository;
    private final DefinitionRepository definitionRepository;
    private final LexiconDomainService domainService;
    private final com.group18.rotionary.lexicon.infrastructure.TermEventPublisher eventPublisher;

    public TermController(TermRepository termRepository,
                          TagRepository tagRepository,
                          DefinitionRepository definitionRepository,
                          LexiconDomainService domainService,
                          com.group18.rotionary.lexicon.infrastructure.TermEventPublisher eventPublisher) {
        this.termRepository = termRepository;
        this.tagRepository = tagRepository;
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
            List<Term> results = termRepository.findByTagName(tag.toLowerCase());
            results.forEach(t -> eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                    t.getId(), t.getWord(), "BY_TAG", null, tag)));
            return ResponseEntity.ok(results);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/random")
    public ResponseEntity<Term> random() {
        List<Term> all = termRepository.findAll();
        if (all.isEmpty()) return ResponseEntity.notFound().build();
        Term rnd = domainService.selectRandomTerm(all);
        eventPublisher.publishTermQueried(new com.group18.rotionary.shared.domain.events.TermQueriedEvent(
                rnd.getId(), rnd.getWord(), "RANDOM_WORD", null, null));
        return ResponseEntity.ok(rnd);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Term> create(@RequestBody CreateTermRequest request) {
        if (!domainService.isValidTerm(request.word())) {
            return ResponseEntity.badRequest().build();
        }
        Term term = new Term(request.word(), request.description(), request.createdBy());
        if (request.tags() != null) {
            for (String tagName : request.tags()) {
                Tag tag = tagRepository.findByName(tagName.toLowerCase())
                        .orElseGet(() -> tagRepository.save(new Tag(tagName, null, request.createdBy())));
                term.addTag(tag);
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
                    if (request.description() != null) {
                        term.updateDescription(request.description());
                    }
                    if (request.tags() != null) {
                        term.getTags().forEach(term::removeTag);
                        for (String tagName : request.tags()) {
                            Tag tag = tagRepository.findByName(tagName.toLowerCase())
                                    .orElseGet(() -> tagRepository.save(new Tag(tagName, null, null)));
                            term.addTag(tag);
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
                    Definition def = new Definition(request.meaning(), request.example(), request.createdBy());
                    term.addDefinition(def);
                    definitionRepository.save(def);
                    return ResponseEntity.ok(def);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}


