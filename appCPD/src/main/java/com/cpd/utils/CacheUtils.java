package com.cpd.utils;

import com.cpd.network.models.News;
import com.cpd.network.models.Ru;
import com.cpd.network.models.Tickets;
import com.google.gson.internal.LinkedTreeMap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import st.lowlevel.storo.Storo;

/**
 * Created by theolm on 23/11/16.
 */

public class CacheUtils {
    private static final String TAG = "CacheUtils";
    private final static String NEWS_LIST = "NewsList";
    private final static String RU_LIST = "RuList";
    private final static String TICKETS_LIST = "TicketsList";
    private final static String TICKETS_DATE = "TicketsDate";

    public static void cacheTickets(List<Tickets> tickets) {
        Storo.delete(TICKETS_LIST);
        Storo.put(TICKETS_LIST, tickets).execute();
        cacheTicketsDate();
    }

    private static void cacheTicketsDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM - hh:mm");
        String sDate = sdf.format(date);
        Storo.delete(TICKETS_DATE);
        Storo.put(TICKETS_DATE, sDate).execute();
    }

    public static void cacheNews(List<News> newsList){
        Storo.delete(NEWS_LIST);
        Storo.put(NEWS_LIST, newsList).execute();
    }

    public static void cacheRu(List<Ru> ruList){
        Storo.delete(RU_LIST);
        Storo.put(RU_LIST, ruList).setExpiry(6, TimeUnit.HOURS).execute();
    }


    public static String getTicketsDate(){
        String date = "";
        if(Storo.contains(TICKETS_DATE))
            date = Storo.get(TICKETS_DATE, date.getClass()).execute();

        return date;
    }

    public static List<Tickets> getTickets(){
        List<Tickets> ticketsList = new ArrayList<>();
        if(Storo.contains(TICKETS_LIST)){
            List<LinkedTreeMap> tempList = Storo.get(TICKETS_LIST, ticketsList.getClass()).execute();
            if(tempList != null) {
                for (LinkedTreeMap x : tempList) {
                    Tickets tickets = new Tickets();
                    tickets.nrrefeicoestotal = (String) x.get("nrrefeicoestotal");
                    tickets.nrrefeicoesresta = (String) x.get("nrrefeicoesresta");
                    tickets.nrtiquete = (String) x.get("nrtiquete");

                    ticketsList.add(tickets);
                }
            }
        } else {
            return null;
        }

        return ticketsList;
    }

    public static List<News> getNewsList(){
        List<News> newsList = new ArrayList<>();

        if(Storo.contains(NEWS_LIST)){
            List<LinkedTreeMap> tempList = Storo.get(NEWS_LIST, newsList.getClass()).execute();
            if(tempList != null) {
                for (LinkedTreeMap x : tempList) {
                    News vo = new News(x);
                    newsList.add(vo);
                }
            }
        }

        return newsList;
    }

    public static List<Ru> getRuList(){
        List<Ru> ruList = new ArrayList<>();
        System.out.println("getRuList()");
        if(Storo.contains(RU_LIST)){
            List<LinkedTreeMap> tempList = Storo.get(RU_LIST, ruList.getClass()).execute();
            if(tempList != null) {
                for (LinkedTreeMap x : tempList) {
                    Ru vo = new Ru();
                    vo.nome = (String) x.get("nome");
                    vo.cardapio = (String) x.get("cardapio");

                    Double num = ((Double) x.get("number"));
                    if (num != null) {
                        vo.number = num.intValue();
                    } else {
                        vo.number = 42;
                    }

                    System.out.println(vo.nome);
                    ruList.add(vo);
                }
            }
        }

        return ruList;
    }
}
