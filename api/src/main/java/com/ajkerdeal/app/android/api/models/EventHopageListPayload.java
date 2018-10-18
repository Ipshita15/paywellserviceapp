package com.ajkerdeal.app.android.api.models;

/**
 * Created by Rasel on 1/9/2017.
 */

public class EventHopageListPayload {
    private int EventId;
    private String PercentageBng;
    private int OrderBy;
    private String Link;
    private int IsActive;
    private int PostedBy;
    private int Percentage;
    private String ActionImageLinkSquare;
    private String ActionImageLinkCicle;
    private int TotalDeals;

    public EventHopageListPayload() {
    }

    public EventHopageListPayload(int eventId, String percentageBng, int orderBy, String link, int isActive, int postedBy, int percentage, String actionImageLinkSquare, String actionImageLinkCicle, int totalDeals) {
        EventId = eventId;
        PercentageBng = percentageBng;
        OrderBy = orderBy;
        Link = link;
        IsActive = isActive;
        PostedBy = postedBy;
        Percentage = percentage;
        ActionImageLinkSquare = actionImageLinkSquare;
        ActionImageLinkCicle = actionImageLinkCicle;
        TotalDeals = totalDeals;
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public String getPercentageBng() {
        return PercentageBng;
    }

    public void setPercentageBng(String percentageBng) {
        PercentageBng = percentageBng;
    }

    public int getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(int orderBy) {
        OrderBy = orderBy;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(int postedBy) {
        PostedBy = postedBy;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }

    public String getActionImageLinkSquare() {
        return ActionImageLinkSquare;
    }

    public void setActionImageLinkSquare(String actionImageLinkSquare) {
        ActionImageLinkSquare = actionImageLinkSquare;
    }

    public String getActionImageLinkCicle() {
        return ActionImageLinkCicle;
    }

    public void setActionImageLinkCicle(String actionImageLinkCicle) {
        ActionImageLinkCicle = actionImageLinkCicle;
    }

    public int getTotalDeals() {
        return TotalDeals;
    }

    public void setTotalDeals(int totalDeals) {
        TotalDeals = totalDeals;
    }

    @Override
    public String toString() {
        return "EventHopageListPayload{" +
                "EventId=" + EventId +
                ", PercentageBng='" + PercentageBng + '\'' +
                ", OrderBy=" + OrderBy +
                ", Link='" + Link + '\'' +
                ", IsActive=" + IsActive +
                ", PostedBy=" + PostedBy +
                ", Percentage=" + Percentage +
                ", ActionImageLinkSquare='" + ActionImageLinkSquare + '\'' +
                ", ActionImageLinkCicle='" + ActionImageLinkCicle + '\'' +
                ", TotalDeals=" + TotalDeals +
                '}';
    }
}
