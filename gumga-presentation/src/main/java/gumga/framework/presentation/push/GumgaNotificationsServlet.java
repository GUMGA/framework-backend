/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.push;

import gumga.framework.application.GumgaMessageService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaMessage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GumgaNotificationsServlet extends HttpServlet {

    private static final long cycleTime = 5 * 60 * 1000l;
    private static final long intervalTime = 1000l;
    private static final long cycles = cycleTime / intervalTime;

    @Autowired
    private GumgaMessageService messageService;

    @RequestMapping("/notifications/viewed")
    protected void viewd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long l=new Long(request.getParameter("id"));
        GumgaMessage gm=new GumgaMessage(l);
        messageService.delete(gm);
    
    }

    
    @RequestMapping("/notifications/source")
    protected void notifications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        QueryObject qo = new QueryObject();
        qo.setAq("destinationLogin='"+GumgaThreadScope.login.get()+"' and viewedIn is null ");
        qo.setAq("viewedIn is null ");
        qo.setPageSize(1000);

        for (int i = 0; i < cycles; i++) {
            SearchResult<GumgaMessage> pesquisa = messageService.pesquisa(qo);
            String message = "data: {"
                    + " \"newMessagesCount\":" + pesquisa.getCount()
                    + ",\"newMessages\":" + pesquisa.getValues()
                    + "}";
            writer.write(message + "\n\n");
            writer.flush();
            try {
                Thread.sleep(intervalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.close();
    }

    @RequestMapping("/notifications/dummy")
    protected void dummyHtml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(""
                + "<!DOCTYPE HTML>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Server-Sent Events Servlet example</title>\n"
                + "        <meta charset=\"utf-8\"/>\n"
                + "        <style>\n"
                + "            body {\n"
                + "                font-family: sans-serif;\n"
                + "            }\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "\n"
                + "        New messages: <span id=\"foo\"></span>\n"
                + "        <br><br>\n"
                + "        Messages:<br> \n"
                + "        <pre id=\"messages\"></pre>\n"
                + "\n"
                + "\n"
                + "        <script>\n"
                + "            \n"
                + "            var params = getSearchParameters();\n"
                + "\n"
                + "            var eventSource = new EventSource(\"source?gumgaToken=\"+params['gumgaToken']);\n"
                + "            eventSource.onmessage = function (event) {\n"
                + "                //  console.log(event.data);\n"
                + "                var notifications = JSON.parse(event.data);\n"
                + "                document.getElementById('foo').innerHTML = notifications.newMessagesCount;\n"
                + "                \n"
                + "                var s='';\n"
                + "                \n"
                + "                for (i=0;i<notifications.newMessages.length;i++){\n"
                + "                    m=notifications.newMessages[i];\n"
                + "                    s+=m.senderLogin+\" \"+m.message+\"\\n\";\n"
                + "                }\n"
                + "                \n"
                + "                document.getElementById('messages').innerHTML = s;\n"
                + "                \n"
                + "            }\n"
                + "\n"
                + "            function getSearchParameters() {\n"
                + "                var prmstr = window.location.search.substr(1);\n"
                + "                return prmstr != null && prmstr != \"\" ? transformToAssocArray(prmstr) : {};\n"
                + "            }\n"
                + "\n"
                + "            function transformToAssocArray(prmstr) {\n"
                + "                var params = {};\n"
                + "                var prmarr = prmstr.split(\"&\");\n"
                + "                for (var i = 0; i < prmarr.length; i++) {\n"
                + "                    var tmparr = prmarr[i].split(\"=\");\n"
                + "                    params[tmparr[0]] = tmparr[1];\n"
                + "                }\n"
                + "                return params;\n"
                + "            }\n"
                + "\n"
                + "            \n"
                + "\n"
                + "        </script>\n"
                + "    </body>\n"
                + "</html> "
                + "");
        writer.close();
    }

}
