package com.ajkerdeal.app.android.api.models;

import java.util.List;

/**
 * Created by Rasel on 1/9/2017.
 */

public class EventHomePagePayLoad {

    private String OfferImageIcon;

    private String EventTitleBanner;

    private String TitleBng;

    private List<EventHopageListPayload> EventPercentageData;

    public EventHomePagePayLoad() {
    }

    public String getEventTitleBanner() {
        return EventTitleBanner;
    }

    public void setEventTitleBanner(String eventTitleBanner) {
        EventTitleBanner = eventTitleBanner;
    }

    public String getTitleBng() {
        return TitleBng;
    }

    public void setTitleBng(String titleBng) {
        TitleBng = titleBng;
    }

    public String getOfferImageIcon() {
        return OfferImageIcon;
    }

    public void setOfferImageIcon(String offerImageIcon) {
        OfferImageIcon = offerImageIcon;
    }

    public List<EventHopageListPayload> getEventHopageListPayloads() {
        return EventPercentageData;
    }

    public void setEventHopageListPayloads(List<EventHopageListPayload> eventHopageListPayloads) {
        this.EventPercentageData = eventHopageListPayloads;
    }

    @Override
    public String toString() {
        return "EventHomePagePayLoad{" +
                "OfferImageIcon='" + OfferImageIcon + '\'' +
                ", eventHopageListPayloads=" + EventPercentageData +
                '}';
    }
}
