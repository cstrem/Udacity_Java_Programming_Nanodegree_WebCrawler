package com.udacity.webcrawler.main;

import com.google.inject.Guice;
import com.udacity.webcrawler.WebCrawler;
import com.udacity.webcrawler.WebCrawlerModule;
import com.udacity.webcrawler.json.ConfigurationLoader;
import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.json.CrawlResultWriter;
import com.udacity.webcrawler.json.CrawlerConfiguration;
import com.udacity.webcrawler.profiler.Profiler;
import com.udacity.webcrawler.profiler.ProfilerModule;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Objects;

public final class WebCrawlerMain {

  private final CrawlerConfiguration config;

  private WebCrawlerMain(CrawlerConfiguration config) {
    this.config = Objects.requireNonNull(config);
  }

  @Inject
  private WebCrawler crawler;

  @Inject
  private Profiler profiler;

  private void run() throws Exception {
    System.out.println("Starting web crawler run...");
    Guice.createInjector(new WebCrawlerModule(config), new ProfilerModule()).injectMembers(this);

    CrawlResult result = crawler.crawl(config.getStartPages());
    CrawlResultWriter resultWriter = new CrawlResultWriter(result);
    // TODO: Write the crawl results to a JSON file (or System.out if the file name is empty)
    if (this.config.getResultPath() != null) {
      System.out.println("Result path found, writing to file");
      resultWriter.write(Path.of(this.config.getResultPath()));
      System.out.println("File written to path");
    }
    else{
      System.out.println("Printing to terminal...");
      Writer out = new BufferedWriter(new OutputStreamWriter(System.out));
      resultWriter.write(out);
    }
    // TODO: Write the profile data to a text file (or System.out if the file name is empty)
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Starting main file...");
    if (args.length != 1) {
      System.out.println("Usage: WebCrawlerMain [starting-url]");
      return;
    }

    System.out.println("Initiating Config setup");
    CrawlerConfiguration config = new ConfigurationLoader(Path.of(args[0])).load();
    System.out.println("Configuration successfully built");
    new WebCrawlerMain(config).run();
  }
}
