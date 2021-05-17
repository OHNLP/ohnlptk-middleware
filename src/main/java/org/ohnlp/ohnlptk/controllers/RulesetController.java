package org.ohnlp.ohnlptk.controllers;

import org.ohnlp.ohnlptk.repositories.RuleSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contains NLP ruleset management functions
 */
@Controller
@RequestMapping("/_ruleset")
public class RulesetController {
    @Autowired
    public RuleSetRepository ruleSetRepository;
}
