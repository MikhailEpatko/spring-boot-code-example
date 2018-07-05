package ru.emi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.emi.domain.IdPostgres;
import ru.emi.domain.UserActivity;
import ru.emi.domain.MyHtml;
import ru.emi.domain.MyRule;
import ru.emi.repository.HtmlPostgresRepository;
import ru.emi.repository.IdPostgresRepository;
import ru.emi.repository.RulesPostgresRepository;
import ru.emi.repository.UserActivityTarantoolRepository;
import ru.emi.util.MyDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class MyService {

    private UserActivityTarantoolRepository userActivityRepository;
    private RulesPostgresRepository rulesRepository;
    private IdPostgresRepository idRepository;
    private HtmlPostgresRepository htmlRepository;

    @Autowired
    public MyService(UserActivityTarantoolRepository userActivityRepository, RulesPostgresRepository rulesRepository,
                     IdPostgresRepository IdRepository, HtmlPostgresRepository htmlRepository) {
        this.userActivityRepository = userActivityRepository;
        this.rulesRepository = rulesRepository;
        this.idRepository = IdRepository;
        this.htmlRepository = htmlRepository;
    }

    public String getHtmlByActivity(String userAgent, String referer, String id, String ntSessionId,
                                    String ntFingerPrint, String ip) {
        UserActivity lastActivity = userActivityRepository.getUserActivity(id);
        if (lastActivity == null) {
            lastActivity = new UserActivity(userAgent, referer, id, ntSessionId, ntFingerPrint, ip);
            lastActivity.incrementSessionsCount();
            lastActivity.incrementViewsPerSession();
            lastActivity.setCreated(MyDate.now());
            updateUserActivity(lastActivity);
        } else {
            lastActivity.setUserAgent(userAgent);
            lastActivity.setReferer(referer);
            lastActivity.setNtFingerPrint(ntFingerPrint);
            lastActivity.setIp(ip);
            lastActivity.update(MyDate.now());
            if (ntSessionId.equals(lastActivity.getNtSessionId())) {
                lastActivity.incrementViewsPerSession();
            } else {
                lastActivity.setNtSessionId(ntSessionId);
                lastActivity.incrementSessionsCount();
                lastActivity.setViewsPerSession(1);
            }
        }
        String result = getHtml(lastActivity);
        userActivityRepository.save(lastActivity);
        return result;
    }

    private String getHtml(UserActivity lastActivity) {
        List<MyRule> rules = rulesRepository.findRules(lastActivity.getB2b(), lastActivity.getHaveEmail(),
                lastActivity.getDaysAfterCreate(), lastActivity.getDaysAfterPreviousUpdate(),
                lastActivity.getSessionsCount(), lastActivity.getViewsPerSession());
        if (rules != null && !rules.isEmpty()) {
            Collections.sort(rules);
            for (MyRule rule : rules) {
                if (rule.getMyHtml() != null) {
                    MyHtml myHtml = rule.getMyHtml();
                    if (!lastActivity.getHtmlViewsCounters().containsKey(myHtml.getId()) ||
                            myHtml.getMayBeShown() > lastActivity.getHtmlViewsCounters().get(myHtml.getId())) {
                        lastActivity.incrementHtmlViews(myHtml.getId());
                        return myHtml.getHtml();
                    }
                }
            }
        }
        return null;
    }

    public MyRule saveRule(MyRule rule) {
        rule.setMyHtml(htmlRepository.save(rule.getMyHtml()));
        return rulesRepository.save(rule);
    }

    public List<MyRule> getAllRules() {
        List<MyRule> listResult = new LinkedList<>();
        Iterable<MyRule> iterableResult = rulesRepository.findAll();
        iterableResult.forEach(listResult::add);
        return listResult;
    }

    public MyRule updateRule(MyRule rule) {
        return rulesRepository.save(rule);
    }

    public void updateTuple(String id) {
        UserActivity userActivity = userActivityRepository.getUserActivity(id);
        if (userActivity != null) {
            updateUserActivity(userActivity);
            userActivityRepository.save(userActivity);
        }
    }

    private void updateUserActivity(UserActivity userActivity) {
        Optional<IdPostgres> idPostgres = idRepository.findById(userActivity.getId());
        userActivity.setHaveEmail(idPostgres.isPresent() &&
                                  idPostgres.get().getEmail() != null &&
                                  !idPostgres.get().getEmail().isEmpty() ?
                                  1 : 0);
        userActivity.setB2b(idPostgres.isPresent() && idPostgres.get().isB2b() ? 1 : 0);
    }

    public Optional<MyRule> getRuleById(int id) {
        return rulesRepository.findById(id);
    }
}