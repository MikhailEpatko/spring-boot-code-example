package ru.emi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity implements Serializable {

    private String userAgent;
    private String referer;
    private String id;
    private String ntSessionId;
    private String ntFingerPrint;
    private String ip;
    private Integer b2b;
    private Integer haveEmail;
    private int viewsPerSession;
    private int sessionsCount;
    private LocalDate created;
    private LocalDate updated;
    private long daysAfterCreate;
    private long daysAfterPreviousUpdate;
    private Map<Integer, Integer> htmlViewsCounters = new HashMap<>();

    public UserActivity(String userAgent, String referer, String id, String ntSessionId, String ntFingerPrint, String ip) {
        this.userAgent = userAgent;
        this.referer = referer;
        this.id = id;
        this.ntSessionId = ntSessionId;
        this.ntFingerPrint = ntFingerPrint;
        this.ip = ip;
    }

    public void incrementViewsPerSession() {
        this.viewsPerSession++;
    }

    public void incrementSessionsCount() {
        this.sessionsCount++;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
        this.updated = created;
    }

    public void update(LocalDate newUpdated) {
        if (this.updated != null) {
            this.daysAfterPreviousUpdate = ChronoUnit.DAYS.between(this.updated, newUpdated);
        }
        this.updated = newUpdated;
        if (this.created != null) {
            this.daysAfterCreate = ChronoUnit.DAYS.between(this.created, newUpdated);
        }
    }

    public void incrementHtmlViews(Integer htmId) {
        if (this.htmlViewsCounters.containsKey(htmId)) {
            this.htmlViewsCounters.replace(htmId, this.htmlViewsCounters.get(htmId) + 1);
        } else {
            this.htmlViewsCounters.put(htmId, 1);
        }
    }
}
