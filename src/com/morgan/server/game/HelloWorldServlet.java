package com.morgan.server.game;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.HtmlEmail;
import org.joda.time.Duration;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.morgan.server.alarm.AlarmCallback;
import com.morgan.server.alarm.AlarmHandle;
import com.morgan.server.alarm.AlarmManager;
import com.morgan.server.security.SecurityInformationHelper;
import com.morgan.server.security.UserIdObfuscator;
import com.morgan.server.util.log.AdvancedLogger;

/**
 * Simple "hello world" servlet to prove the server is running.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class HelloWorldServlet extends HttpServlet {

  static final long serialVersionUID = 0L;

  private static final AdvancedLogger log = new AdvancedLogger(HelloWorldServlet.class);

  private final AlarmManager alarms;
  private final SecurityInformationHelper helper;
  private final UserIdObfuscator obfuscator;
  private final Provider<HtmlEmail> htmlEmailProvider;

  @Inject HelloWorldServlet(AlarmManager alarms, SecurityInformationHelper helper, UserIdObfuscator obfuscator, Provider<HtmlEmail> htmlEmailProvider) {
    this.alarms = alarms;
    this.helper = helper;
    this.obfuscator = obfuscator;
    this.htmlEmailProvider = htmlEmailProvider;
  }

  @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try (ServletOutputStream out = resp.getOutputStream()) {
      PrintStream pOut = new PrintStream(out);
      pOut.println("<html><body><h1>Hello, World!</h1>\n");
      pOut.println("<ul>");
      for (Map.Entry<String, String> entry : helper.getInformation().entrySet()) {
        pOut.format("<li><b>%s</b>: %s\n", entry.getKey(), entry.getValue());
      }
      pOut.println("</ul>");
      String encrypted = obfuscator.obfuscateId(7L);
      pOut.format("<b>Obfuscating 7L:</b> %s<br>\n", encrypted);
      pOut.format("<b>Deobfuscating:</b> %d<br>\n", obfuscator.deobfuscateId(encrypted));
      pOut.println("</body></html>");
      pOut.flush();
    }

    resp.setStatus(HttpServletResponse.SC_OK);

    /*
    HtmlEmail email = htmlEmailProvider.get();
    try {
      email.setHtmlMsg("<html><div><h1>Hello</h1></div><div>This is an <i>HTML</i> message!</div></html>");
      email.setTextMsg("The text version");
      email.setSubject("A subject");
      email.addTo("mark@mark-morgan.net");
      email.send();
    } catch (EmailException e) {
      Throwables.propagate(e);
    }
    */

    log.info("Scheduling a transient alarm to repeat every 5 seconds");
    alarms.createTransientAlarm(new AlarmCallback() {
      @Override public void handleAlarm(AlarmHandle alarm, Optional<Object> alarmData) {
        log.info("Alarm went off with data %s", alarmData.get());
      }
    }).setAlarmData("Howdy!")
        .scheduleToRepeatEvery(Duration.standardSeconds(5));

    log.info("Scheduling a persistent alarm to repeat every 7 seconds");
    alarms.createPersistentAlarm(PersistentAlarmCallback.class)
        .setAlarmData("Hola")
        .scheduleToRepeatEvery(Duration.standardSeconds(7));
  }

  static class PersistentAlarmCallback implements AlarmCallback {
    @Inject PersistentAlarmCallback() {
    }

    @Override public void handleAlarm(AlarmHandle alarm, Optional<Object> alarmData) {
      log.info("Persistent alarm callback called with data %s", alarmData.get());
    }
  }
}
