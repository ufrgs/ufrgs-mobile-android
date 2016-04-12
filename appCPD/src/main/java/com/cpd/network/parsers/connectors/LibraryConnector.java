/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.network.parsers.connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cpd.exceptions.ConnectionErrorExecption;
import com.cpd.exceptions.InvalidLoginInfoException;
import com.cpd.exceptions.OverloadedSystemException;
import com.cpd.ufrgsmobile.R;
import com.cpd.utils.AppTags;
import com.cpd.utils.DebugUtils;
import com.cpd.utils.TextUtils;
import com.cpd.vos.LibraryBookVo;
import com.cpd.vos.LibraryUserVo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Holds all the connection to SABi.
 * Yes, it is a parser.
 * Yes, it has many connections.
 * Yes, it is hard to understand.
 *
 * Good luck.
 *
 * @author Alan Wink
 * @author Theodoro Mota
 * @version 2.0
 */
public class
LibraryConnector {

    private static final String TAG = LibraryConnector.class.getSimpleName();

    private Context mContext;

    public LibraryConnector(Context context) {
        this.mContext = context;
    }

    /**
     * Gets the information about the user, stored in a LibraryUserVo.
     *
     * @return A LibraryUserVo with the user info. Null for errors.
     */
    public LibraryUserVo getUserInfo() throws InvalidLoginInfoException, ConnectionErrorExecption, OverloadedSystemException {
        LibraryUserVo user = getUser();
        if(user == null){
            return null;
        }

        try {
            // Connect user
            String session = getSession();
            Map<String, String> cookies = connectUser(session, user);

            Document doc = Jsoup.connect("http://sabi.ufrgs.br/F/" + session + "?func=bor-info")
            //Document doc = Jsoup.connect("ttps://dl.dropboxusercontent.com/s/0780pkua9bhy23v/bibliotecaSobrecarregada.html")
                    .cookies(cookies)
                    .get();

            /*
             * Get user name. In SABi, the name is in the title like this:
             *
             * "Informações do usuário USER NAME"
             *
             * We need to remove the first part, and format the name.
             */
            Element tempElement = doc.select("div.title").first();
            String name = tempElement.text().replace("Informações do usuário ", "");
            user.name = TextUtils.toTitleCase(name);

            /*
             * Get user values.
             */
            Elements links = doc.select("a[href*=http]");
            int empStartPos = 0;

            // Search where "Empréstimos" is
            for(Element x : links){
                if(x.text().contentEquals("Empréstimos"))
                    break;
                empStartPos++;
            }

            if(empStartPos != 14){
                if(DebugUtils.DEBUG) Log.d(TAG, "End of list, not a valid thing to parse");
                throw new InvalidLoginInfoException();
            }

            // We have the position, now we can get the numbers.
            user.loans = Integer.valueOf(links.get(empStartPos + 1).text());
            user.reservations = Integer.valueOf(links.get(empStartPos + 3).text());
            user.scheduled = Integer.valueOf(links.get(empStartPos + 5).text());
            user.debt = Float.valueOf(links.get(empStartPos + 7).text());

        } catch (RuntimeException | IOException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "getUserInfo error: " + e.getMessage());
            parseOverloadSystem();
            return null;
        }

        return user;
    }

    private void parseOverloadSystem() throws OverloadedSystemException {
        try {
            //Document doc = Jsoup.connect("https://dl.dropboxusercontent.com/s/0780pkua9bhy23v/bibliotecaSobrecarregada.html")
            Document doc = Jsoup.connect("http://sabi.ufrgs.br/F/")
                    .get();

            String body = doc.select("body").text();
            if(body.contains("sobrecarregado")){
                throw new OverloadedSystemException();
            }
        } catch (IOException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "parseOverloadSystem error: " + e.getMessage());
            return;
        }
    }

    /**
     * Fill the LibraryUserVo with the books list.
     * @param user LibraryUserVo to be filled.
     */
    public void getBooksList(LibraryUserVo user) throws ConnectionErrorExecption {
        try{
            String session =  getSession();
            Map<String, String> cookies = connectUser(session, user);

            Document doc = Jsoup
                    .connect("http://sabi.ufrgs.br/F/" + session + "?func=bor-loan&adm_library=URS50")
                    .cookies(cookies)
                    .get();

            Elements infoTables = doc.select("td.td1");
            int tableSize = infoTables.size();

            // Creates book list
            user.bookList = new ArrayList<>();

            if((tableSize % 7) == 0){
                for(int i=1; i<=((tableSize)/7) ; i++){
                    // New book
                    LibraryBookVo book = new LibraryBookVo();
                    book.name = infoTables.get((i*7)-5).text();
                    book.author = infoTables.get((i*7)-6).text();
                    book.libraryCode = infoTables.get((i*7)-2).text();

                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    book.returnDate = format.parse(infoTables.get((i*7)-3).text());

                    user.bookList.add(book);
                }
            }

        } catch (IOException | ParseException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "getBooksList error: " + e.getMessage());
        }
    }

    /**
     * Renew all books for a given user. This method does not update the LibraryUserVo, so you
     * need to recall getUser() and getBookList() to update this info.
     *
     * @param user User to renew books.
     */
    public String renewAll(LibraryUserVo user) throws ConnectionErrorExecption {
        String response = "";
        try {
            String session = getSession();
            Map<String, String> cookies = connectUser(session, user);

            Document doc = Jsoup
                    .connect("http://sabi.ufrgs.br/F/" + session + "?func=bor-loan-renew-all")
                    .cookies(cookies)
                    .get();

            Elements infoTables = doc.select("td.td1");
            int numeroDeTables = infoTables.size();

            if((numeroDeTables%6) == 0 && numeroDeTables != 0) {
                for(int k=1; k<=(numeroDeTables/6) ; k++) {

                    response += infoTables.get((k*6)-5).text();
                    response += " - " + infoTables.get((k*6)-3).text();
                    response += "\n" + infoTables.get((k*6)-1).text() +"\n\n";


                }

            } else {
                // It is a SABi error message
                Elements errorMessage = doc.select("p[style*=color: red]");
                for (int i = 0; i < errorMessage.size(); i++) {
                    response += errorMessage.get(i).text() + "\n";
                }
            }

        } catch (IOException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "renewAll error: " + e.getMessage());
            response = mContext.getString(R.string.connection_error_message);

        }

        return  response;
    }

    /**
     * Find user and password stored in user preferences and creates a LibraryUserVo.
     *
     * @return A LibraryUserVo with login and password. Need to check for null values.
     */
    private LibraryUserVo getUser(){
        LibraryUserVo user = new LibraryUserVo();

        SharedPreferences settings = mContext.getSharedPreferences(AppTags.LIBRARY_LOGIN_PREF, 0);
        user.userLogin = settings.getString(AppTags.LIBRARY_PARSER_USER, null);
        user.userPassword = settings.getString(AppTags.LIBRARY_PARSER_PASSWORD, null);

        return user;
    }

    /**
     * Gets the session used during SABi parsing.
     *
     * @return The string representing a session. Returns null in case of a problem.
     */
    private String getSession(){

        try{
            // Creates a random to fill the url
            long randomLong = Math.abs((new Random()).nextLong());

            String url = "http://sabi.ufrgs.br/F?RN=" + String.valueOf(randomLong);

            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .execute();

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .followRedirects(true)
                    .maxBodySize(0)
                    .timeout(600000)
                    .cookies(response.cookies())
                    .get();

            // The toolbar links have the session information. Get one to read the session.
            Elements links = doc.select("a");
            String session = null;


            for (Element src : links)
            {
                if(src.text().contentEquals("Agendamentos"))
                {
                    session = src.attr("href");
                }
            }

            // Remove what is not the session from the link.
            session = session.replace("http://sabi.ufrgs.br/F/", "");
            session = session.replace("?func=file&file_name=agendamento", "");

            if(DebugUtils.DEBUG) Log.d(TAG, "Session: " + session);

            return session;

        } catch (NullPointerException | IOException e) {
                if(DebugUtils.ERROR) Log.e(TAG, "getSession error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Using login and password, connects a user to SABi.
     *
     * @return Map of cookies. Used to all actions after login.
     */
    private Map<String, String> connectUser(String session, LibraryUserVo user) throws ConnectionErrorExecption {
        try {
            if(DebugUtils.DEBUG) Log.d(TAG, "Will connect: (" + user.userLogin + ", " + user.userPassword + ")");
            Connection.Response login = Jsoup.connect("http://sabi.ufrgs.br/F/" + session + "?func=file&file_name=login-session")
                    .data("ssl_flag","Y","func","login-session","login_source","bor-info","bor_library","URS50","bor_id",user.userLogin,"bor_verification",user.userPassword,"x","0","y","0")
                    .method(Connection.Method.POST)
                    .timeout(60000)
                    .execute();

            return login.cookies();

        } catch (IOException | NullPointerException e) {
            if(DebugUtils.ERROR) Log.e(TAG, "connectUser error: " + e.getMessage());
            throw new ConnectionErrorExecption();
        }
    }
}
