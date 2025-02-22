package com.udacity.webcrawler.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.io.Reader;
import java.lang.module.Configuration;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A static utility class that loads a JSON configuration file.
 */

public final class ConfigurationLoader {

  private final Path path;
  private ObjectMapper objectMapper;

  /**
   * Create a {@link ConfigurationLoader} that loads configuration from the given {@link Path}.
   */
  public ConfigurationLoader(Path path) {
    this.path = Objects.requireNonNull(path);
  }

  /**
   * Loads configuration from this {@link ConfigurationLoader}'s path
   *
   * @return the loaded {@link CrawlerConfiguration}.
   */
  public CrawlerConfiguration load() throws IOException {
    // TODO: Fill in this method.
    System.out.println("Starting to load crawler config...");
    Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
    System.out.println("Created reading object in loader");
    this.read(reader);
    reader.close();
    System.out.println("Closing the stream...");
    return new CrawlerConfiguration.Builder().build();
  }

  /**
   * Loads crawler configuration from the given reader.
   *
   * @param reader a Reader pointing to a JSON string that contains crawler configuration.
   * @return a crawler configuration
   */
  public static CrawlerConfiguration read(Reader reader) throws IOException{
    // This is here to get rid of the unused variable warning.
    Objects.requireNonNull(reader);
    ObjectMapper objectMapper = new ObjectMapper();

    objectMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);

    return objectMapper.readValue(reader,CrawlerConfiguration.Builder.class).build();
  }
}
