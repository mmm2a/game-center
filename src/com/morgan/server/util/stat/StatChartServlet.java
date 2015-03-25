package com.morgan.server.util.stat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Servlet for serving statistics charts.
 *
 * @author mark@mark-morgan.net (Mark Morgan)
 */
@Singleton
class StatChartServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  @Inject StatChartServlet() {
  }

  @Override protected void doGet(
      HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HistogramDataset dataset = new HistogramDataset();
    dataset.setType(HistogramType.FREQUENCY);


    double[] value = new double[100];
    Random generator = new Random();
    for (int i=1; i < 100; i++) {
      value[i] = generator.nextDouble();
    }
    int number = 10;
    dataset.addSeries("Histogram",value,number);

    JFreeChart chart = ChartFactory.createHistogram("Histogram", "X axis", "Y axis", dataset, PlotOrientation.VERTICAL, true, true, false);

    try (OutputStream out = resp.getOutputStream()) {
      resp.setContentType("image/png");
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
      resp.setHeader("Cache-Control", "max-age=0"); //HTTP 1.1
      resp.setHeader("Cache-Control", "no-store"); //HTTP 1.1
      resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
      resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
      ChartUtilities.writeChartAsPNG(out, chart, 1000, 800);
    }
  }
}
